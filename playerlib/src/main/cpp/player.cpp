#include "player.h"


extern "C" {

JavaVM *javaVM;
PlayerCallJava* playerCallJava;
int ret;


jint JNI_OnLoad(JavaVM *vm, void *reserved) {

    jint result = -1;
    javaVM = vm;
    JNIEnv *env;

    logd("JNI_OnLoad");
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        loge("GetEnv 失败");
        return result;
    }
    return JNI_VERSION_1_6;
}

void Java_com_zq_playerlib_ZQPlayer_play(JNIEnv *env, jobject cls, jobject surface,
                                         jobject surfaceFilter, jstring path, jint type) {
    //        char* file_name = jstringToChar(env, path);



    jboolean isCopy = JNI_TRUE;
    const char *file_name = (env)->GetStringUTFChars(path, &isCopy);
    logd("播放 = %s, %d\n", file_name, isLogEnable());


    playerCallJava = new PlayerCallJava(javaVM, &cls);
    playerCallJava->onLoading();

    av_register_all();
    avfilter_register_all();//注册 filter

    AVFormatContext *pFormatCtx = avformat_alloc_context();
    // 打开文件
    logd("打开文件 %d ", isLogEnable());
    if (avformat_open_input(&pFormatCtx, file_name, NULL, NULL) != 0) {
        logd("无法打开文件:%s\n", file_name);
        return; // Couldn't open file
    }
    // 读 流信息
    logd("读 流信息");
    if (avformat_find_stream_info(pFormatCtx, NULL) < 0) {
        logd("没有找到流信息");
        return;
    }
    //寻找 视频流
    logd("寻找 视频流");
    int i, videoStream = -1, audioStream = -1;
    for (i = 0; i < pFormatCtx->nb_streams; i++) {
        if (pFormatCtx->streams[i]->codec->codec_type == AVMEDIA_TYPE_VIDEO && videoStream < 0) {
            videoStream = i;
        }
        if (pFormatCtx->streams[i]->codec->codec_type == AVMEDIA_TYPE_AUDIO && audioStream < 0) {
            audioStream = i;
        }
    }
    if (videoStream == -1) {
        logd("没有找到 视频流");
        return;
    }
    if (audioStream == -1) {
        logd("没有找到 频流");
    }

    AVCodecContext *videoCodecCtx = pFormatCtx->streams[videoStream]->codec;
    // 视频 解码器
    logd("找视频 解码器");
    AVCodec *pCodec = avcodec_find_decoder(videoCodecCtx->codec_id);
    if (pCodec == NULL) {
        logd("解码器 不存在");
        return;
    }
    logd("打开解码器");
    if (avcodec_open2(videoCodecCtx, pCodec, NULL) < 0) {
        logd("无法打开解码器");
        return;
    }
    // 获取视频宽高
    logd("获取视频宽高 width = %d, height = %d", videoCodecCtx->width, videoCodecCtx->height);
    int videoWidth = videoCodecCtx->width;
    int videoHeight = videoCodecCtx->height;
    // 获取native window
    logd("获取native window");
    ANativeWindow *nativeWindow = ANativeWindow_fromSurface(env,
                                                            surface);//cmake文件要添加 -landroid # 解决ANativeWindow_fromSurface 找不到问题
    // 设置native window的buffer大小,可自动拉伸
    ANativeWindow_setBuffersGeometry(nativeWindow, videoWidth, videoHeight,
                                     WINDOW_FORMAT_RGBA_8888);
    ANativeWindow_Buffer windowBuffer;
    logd("获取native window Filter");
    ANativeWindow *nativeWindowFilter = ANativeWindow_fromSurface(env, surfaceFilter);
    // 设置native window的buffer大小,可自动拉伸
    ANativeWindow_setBuffersGeometry(nativeWindowFilter, videoWidth, videoHeight,
                                     WINDOW_FORMAT_RGBA_8888);
    ANativeWindow_Buffer windowBufferFilter;
    logd("打开解码器");
    if (avcodec_open2(videoCodecCtx, pCodec, NULL) < 0) {
        logd("无法打开解码器");
        return;
    }
    logd("分配 原始 frame 对象");
    AVFrame *pFrame = av_frame_alloc();
    logd("用于格式转换后 frame 对象");
    AVFrame *pFrameRGBA = av_frame_alloc();
    if (pFrameRGBA == NULL || pFrame == NULL) {
        logd("不能 allocate video frame.");
        return;
    }
    // buffer中数据就是用于渲染的,且格式为RGBA
    int numBytes = av_image_get_buffer_size(AV_PIX_FMT_RGBA, videoCodecCtx->width,
                                            videoCodecCtx->height, 1);
    logd("分配 buffer %d", numBytes);//字节 unit8_t
    uint8_t *buffer = (uint8_t *) av_malloc(numBytes * sizeof(uint8_t));//字节 unit8_t 是一字节
    av_image_fill_arrays(pFrameRGBA->data, pFrameRGBA->linesize, buffer, AV_PIX_FMT_RGBA,
                         videoCodecCtx->width, videoCodecCtx->height, 1);

    // 由于解码出来的帧格式不是RGBA的,在渲染之前需要进行格式转换
    struct SwsContext *sws_ctx = sws_getContext(videoCodecCtx->width, videoCodecCtx->height,
                                                videoCodecCtx->pix_fmt,
                                                videoCodecCtx->width, videoCodecCtx->height,
                                                AV_PIX_FMT_RGBA,
                                                SWS_BILINEAR, NULL, NULL, NULL);


    logd("开始 filter初始化");
    AVFilterGraph *filter_graph = avfilter_graph_alloc();
    AVFilterContext *buffersink_ctx;
    AVFilterContext *buffersrc_ctx;
    AVFilter *buffersrc = avfilter_get_by_name("buffer");
    AVFilter *buffersink = avfilter_get_by_name("buffersink");//新版的ffmpeg库必须为buffersink
    AVFilterInOut *outputs = avfilter_inout_alloc();
    AVFilterInOut *inputs = avfilter_inout_alloc();
    /* buffer video source: the decoded frames from the decoder will be inserted here. */
    //snprintf 将可变个参数(...)按照format格式化成字符串，然后将其复制到str中
    char args[512];
    snprintf(args, sizeof(args),
             "video_size=%dx%d:pix_fmt=%d:time_base=%d/%d:pixel_aspect=%d/%d",
             videoCodecCtx->width, videoCodecCtx->height, videoCodecCtx->pix_fmt,
             videoCodecCtx->time_base.num, videoCodecCtx->time_base.den,
             videoCodecCtx->sample_aspect_ratio.num, videoCodecCtx->sample_aspect_ratio.den);
    ret = avfilter_graph_create_filter(&buffersrc_ctx, buffersrc, "in", args, NULL, filter_graph);
    if (ret < 0) {
        logd("无法创建 buffer source %d", ret);
        return;
    }

    AVBufferSinkParams *buffersink_params = av_buffersink_params_alloc();
    enum AVPixelFormat pix_fmts[] = {AV_PIX_FMT_YUV420P, AV_PIX_FMT_NONE};
    buffersink_params->pixel_fmts = pix_fmts;
    ret = avfilter_graph_create_filter(&buffersink_ctx, buffersink, "out", NULL, buffersink_params,
                                       filter_graph);
    av_free(buffersink_params);
    if (ret < 0) {
        logd("无法创建 buffer sink %d", ret);
        return;
    }
    //filter_graph 的 两端点
    outputs->name = av_strdup("in");
    outputs->filter_ctx = buffersrc_ctx;
    outputs->pad_idx = 0;
    outputs->next = NULL;
    inputs->name = av_strdup("out");
    inputs->filter_ctx = buffersink_ctx;
    inputs->pad_idx = 0;
    inputs->next = NULL;
    char *filters_descr = "lutyuv='u=128:v=128'";
    if (avfilter_graph_parse_ptr(filter_graph, filters_descr, &inputs, &outputs, NULL) < 0) {
        logd("Cannot avfilter_graph_parse_ptr");
        return;
    }
    if (avfilter_graph_config(filter_graph, NULL) < 0) {
        logd("Cannot avfilter_graph_config");
        return;
    }
    logd("结束 filter初始化");

    logd(" 开始　音频初始化");
    logd(" 找码器");
    AVCodecContext *audioCodecCtx = pFormatCtx->streams[audioStream]->codec;
    AVCodec *audioCodec = avcodec_find_decoder(audioCodecCtx->codec_id);
    if (audioCodec == NULL) {
        logd(" 音频  解码器 不存在");
        return;
    }
    logd("打开解码器");
    if (avcodec_open2(audioCodecCtx, audioCodec, NULL) < 0) {
        logd("无法打开解码器");
        return;
    }
    logd(" 音频 channel_layout %d", audioCodecCtx->channel_layout);
    logd(" 音频 sample_rate %d", audioCodecCtx->sample_rate);
    logd(" 音频 sample_fmt %d", audioCodecCtx->sample_fmt);
    logd(" 音频 channels %d", audioCodecCtx->channels);
    //得到SwrContext ，进行重采样，具体参考http://blog.csdn.net/jammg/article/details/52688506
    SwrContext *swrContext = swr_alloc();
    uint8_t *out_buffer = (uint8_t *) av_malloc(44100 * 2);//缓存区
    uint64_t out_ch_layout = AV_CH_LAYOUT_STEREO; //输出的声道布局（立体声）
    enum AVSampleFormat out_formart = AV_SAMPLE_FMT_S16;//输出采样位数  16位
    int out_sample_rate = audioCodecCtx->sample_rate;//输出的采样率必须与输入相同
    //swr_alloc_set_opts将PCM源文件的采样格式转换为自己希望的采样格式
    swr_alloc_set_opts(swrContext, out_ch_layout, out_formart, out_sample_rate,
                       audioCodecCtx->channel_layout, audioCodecCtx->sample_fmt,
                       audioCodecCtx->sample_rate,
                       0, NULL);
    swr_init(swrContext);
    int out_channer_nb = av_get_channel_layout_nb_channels(AV_CH_LAYOUT_STEREO);//    获取通道数  2

    playerCallJava->initAudioTrack(44100, out_channer_nb);
    logd(" 结束　音频初始化");

    int frameFinished;
    AVPacket packet;
    logd("开始读 数据");
    struct timeval startTv;
    gettimeofday(&startTv, NULL);    //该函数在sys/time.h头文件中
    while (av_read_frame(pFormatCtx, &packet) >= 0) {
        if (packet.stream_index == videoStream && (type == 0 || type == -1)) {
            // Decode video frame
            avcodec_decode_video2(videoCodecCtx, pFrame, &frameFinished, &packet);
            // 如果一帧太大, 并不是decode一次就可解码出一帧
            if (frameFinished) {
                pFrame->pts = av_frame_get_best_effort_timestamp(pFrame);

                logd("Framerate %d/%d", videoCodecCtx->framerate.num, videoCodecCtx->framerate.den);
                logd("Frame %lld %llu %llu", pFormatCtx->duration, packet.pts, pFrame->pts);
                //每帧 等待 时间

                struct timeval currentTv;
                gettimeofday(&currentTv, NULL);

                int targetTime = int((1000 / videoCodecCtx->framerate.num) * pFrame->pts);
                while (targetTime > int((currentTv.tv_sec * 1000 * 1000 + currentTv.tv_usec) -
                                        (startTv.tv_sec * 1000 * 1000 + startTv.tv_usec))) {
                    usleep(100);
                    gettimeofday(&currentTv, NULL);
                }
                logd("Frame++ %d %d",
                     int((currentTv.tv_sec * 1000 * 1000 + currentTv.tv_usec ) - (startTv.tv_sec * 1000 * 1000 + startTv.tv_usec )),
                     targetTime);

                // lock native window buffer
                if (ANativeWindow_lock(nativeWindow, &windowBuffer, 0)) {
                    logd("出错 退出 native window");
                    return;
                };
                // 格式转换
                sws_scale(sws_ctx, (uint8_t const *const *) pFrame->data,
                          pFrame->linesize, 0, videoCodecCtx->height,
                          pFrameRGBA->data, pFrameRGBA->linesize);
                // 获取stride
                uint8_t *dst = (uint8_t *) windowBuffer.bits;
                int dstStride = windowBuffer.stride * 4;
                uint8_t *src = (pFrameRGBA->data[0]);
                int srcStride = pFrameRGBA->linesize[0];
                // 由于window的stride和帧的stride不同,因此需要逐行复制
                int h;
                for (h = 0; h < videoHeight; h++) {
                    memcpy(dst + h * dstStride, src + h * srcStride, srcStride);
                }
                ANativeWindow_unlockAndPost(nativeWindow);

                // AVfilter start
                //* push the decoded frame into the filtergraph
                if (av_buffersrc_add_frame(buffersrc_ctx, pFrame) < 0) {
                    logd("Could not av_buffersrc_add_frame");
                    break;
                }
                if (av_buffersink_get_frame(buffersink_ctx, pFrame) < 0) {
                    logd("Could not av_buffersink_get_frame");
                    break;
                }
                //AVfilter end
                // lock native window buffer
                if (ANativeWindow_lock(nativeWindowFilter, &windowBufferFilter, 0)) {
                    logd("出错 退出 filter native window");
                    return;
                };
                // 格式转换
                sws_scale(sws_ctx, (uint8_t const *const *) pFrame->data,
                          pFrame->linesize, 0, videoCodecCtx->height,
                          pFrameRGBA->data, pFrameRGBA->linesize);
                // 获取stride
                uint8_t *dstFilter = (uint8_t *) windowBufferFilter.bits;
                int dstStrideFilter = windowBufferFilter.stride * 4;
                uint8_t *srcFilter = (pFrameRGBA->data[0]);
                int srcStrideFilter = pFrameRGBA->linesize[0];
                // 由于window的stride和帧的stride不同,因此需要逐行复制
                for (h = 0; h < videoHeight; h++) {
                    memcpy(dstFilter + h * dstStrideFilter, srcFilter + h * srcStrideFilter,
                           srcStrideFilter);
                }
                ANativeWindow_unlockAndPost(nativeWindowFilter);
            }
        }
        if (packet.stream_index == audioStream && (type == 1 || type == -1)) {
            //  解码  mp3   编码格式frame----pcm   frame
            avcodec_decode_audio4(audioCodecCtx, pFrame, &frameFinished, &packet);
            if (frameFinished) {
                swr_convert(swrContext, &out_buffer, 44100 * 2, (const uint8_t **) pFrame->data,
                            pFrame->nb_samples);
                int size = av_samples_get_buffer_size(NULL, out_channer_nb, pFrame->nb_samples,
                                                      AV_SAMPLE_FMT_S16, 1);
                jbyteArray audio_sample_array = env->NewByteArray(size);
                env->SetByteArrayRegion(audio_sample_array, 0, size,
                                        (const jbyte *) out_buffer);//该函数将本地的数组数据拷贝到了 Java 端的数组中
                playerCallJava->sendDataToAudioTrack(audio_sample_array, size);

                env->DeleteLocalRef(audio_sample_array);//删除 obj 所指向的局部引用。
            }
        }
        av_packet_unref(&packet);
    }
    logd("解码 播放结束 释放资源");
    av_free(buffer);
    av_free(pFrameRGBA);
    av_free(pFrame);
    swr_free(&swrContext);
    avcodec_close(audioCodecCtx);
    avfilter_graph_free(&filter_graph);
    avcodec_close(videoCodecCtx);
    avformat_close_input(&pFormatCtx);
}

}

