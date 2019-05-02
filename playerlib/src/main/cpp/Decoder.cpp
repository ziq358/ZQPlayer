//
// Created by johnwu on 2019/4/27.
//


#include <sys/param.h>
#include <zconf.h>
#import "Decoder.h"
#import "AndroidLog.h"
#import "common.h"
#import "PlayerFrameVideo.h"
#import "PlayerFrameAudio.h"

#define IOTimeout 30
static time_t sIOStartTime = 0;
static int interruptCallback(void *context) {
    time_t dt = time(NULL) - sIOStartTime;
    if (sIOStartTime || dt > IOTimeout) return 1;
    return 0;
}

Decoder::Decoder(PlayerCallJava *playerCallJava){
    this->playerCallJava = playerCallJava;
}

bool Decoder::open(const char *url) {

    mIsEOF = NO;
    //1、Init
    logd("1、ffmpeg init 初始化 %s", url);
    av_register_all();
    //2、Open Input 获取输入的上下文
    logd("2、ffmpeg 获取输入的上下文");
    AVFormatContext *formatContext = NULL;
    int ret = avformat_open_input(&formatContext, url, NULL, NULL);
    if (ret != 0) {
        logd("2、ffmpeg 获取输入的上下文 失败");
        if (formatContext != NULL){
            avformat_free_context(formatContext);
        }
        return NO;
    }
    //打印信息
    av_dump_format(formatContext, 0, url, 0);
    // 3. Analyze Stream Info 流信息
    logd("3、ffmpeg 获取 流信息");
    ret = avformat_find_stream_info(formatContext, NULL);
    if (ret != 0) {
        logd("3、ffmpeg 获取 流信息 失败");
        if (formatContext != NULL){
            avformat_close_input(&formatContext);
        }
        return NO;
    }

    //4、遍历流信息, 初始化 video coder
    if(!initVideoCoder(formatContext)){
        return NO;
    }
    if(!initAudioCoder(formatContext)){
        return NO;
    }

    //变量赋值
    mFormatContext = formatContext;
    double duration = (double)formatContext->duration;
    mDuration = duration <= 0 ? (double)0 : (duration / AV_TIME_BASE);
    //FFmpeg长时间无响应的解决方法  中断
    AVIOInterruptCB icb = {interruptCallback, NULL};
    formatContext->interrupt_callback = icb;
    return YES;
}

bool Decoder::initVideoCoder(AVFormatContext *formatContext){
    logd("4、video遍历流信息");
    int videoStreamIndex = -1;
    AVCodecContext *videoCodeCtx = NULL;
    for (int i = 0; i < formatContext->nb_streams; ++i) {
        //视频
        if (formatContext->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
            videoStreamIndex = i;
            videoCodeCtx = openCodec(formatContext ,videoStreamIndex);
            break;
        }
    }

//    BOOL isYUV = NO;
    AVFrame *vframe = NULL;
    struct SwsContext *swsctx = NULL;
    if (videoStreamIndex >= 0 && videoCodeCtx != NULL) {
        if (videoCodeCtx->pix_fmt != AV_PIX_FMT_NONE) {
            vframe = av_frame_alloc();
//            isYUV = (videoCodeCtx->pix_fmt == AV_PIX_FMT_YUV420P || videoCodeCtx->pix_fmt == AV_PIX_FMT_YUVJ420P);
            //非YUV 数据要进行转换 (暂时全都转换吧)
            //            if (!isYUV) {
            logd("非YUV 数据要进行转换");
            swsctx = sws_getContext(videoCodeCtx->width, videoCodeCtx->height, videoCodeCtx->pix_fmt,
                                    videoCodeCtx->width, videoCodeCtx->height, AV_PIX_FMT_RGBA,
                                    SWS_BILINEAR, NULL, NULL, NULL);
            //            }
            //获得fps，时间基， 用于同步
            AVStream *stream = formatContext->streams[videoStreamIndex];
            double fps = 0, timeBase = 0.04;
            if (stream->time_base.den > 0 && stream->time_base.num > 0) {
                timeBase = av_q2d(stream->time_base);
            }
            if (stream->avg_frame_rate.den > 0 && stream->avg_frame_rate.num) {
                fps = av_q2d(stream->avg_frame_rate);
            } else if (stream->r_frame_rate.den > 0 && stream->r_frame_rate.num > 0) {
                fps = av_q2d(stream->r_frame_rate);
            } else {
                fps = 1 / timeBase;
            }
            mVideoFPS = fps;
            mVideoTimebase = timeBase;
            mRotation = rotationFromVideoStream(stream);
        }

        //        BOOL swsError = isYUV ? NO : (swsctx == NULL || vswsframe == NULL);
        //失败释放资源
        if (vframe == NULL) {
            logd("ffmpeg 获取编码器失败 释放资源");
            videoStreamIndex = -1;
            if (videoCodeCtx != NULL) avcodec_free_context(&videoCodeCtx);
            if (vframe != NULL) av_frame_free(&vframe);
            if (swsctx != NULL) { sws_freeContext(swsctx); swsctx = NULL; }
            return NO;
        }
    }else{
        logd("ffmpeg 获取编码器失败");
        if (formatContext != NULL){
            avformat_close_input(&formatContext);
        }
        return NO;
    }

    //变量赋值
    mVideoStreamIndex = videoStreamIndex;
    mVideoFrame = vframe;
    mVideoCodecContext = videoCodeCtx;
    mVideoSwsContext = swsctx;
    mHasVideo = TRUE;
    return YES;
}

bool Decoder::initAudioCoder(AVFormatContext *formatContext){
    logd("4、audio遍历流信息");
    int ret;
    int audioStreamIndex = -1;
    AVCodecContext *audioCodeCtx = NULL;
    for (int i = 0; i < formatContext->nb_streams; ++i) {
        //音频
        if (formatContext->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_AUDIO) {
            audioStreamIndex = i;
            audioCodeCtx = openCodec(formatContext ,audioStreamIndex);
            break;
        }
    }
    AVFrame *aframe = NULL;
    SwrContext *aswrctx = NULL;
    double timeBase = 0.025;
    if (audioStreamIndex >= 0 && audioCodeCtx != NULL) {
        aframe = av_frame_alloc();
        AVStream *stream = formatContext->streams[audioStreamIndex];

        if (stream->time_base.den > 0 && stream->time_base.num > 0) {
            timeBase = av_q2d(stream->time_base);
        }
        if (aframe == NULL) {
            audioStreamIndex = -1;
            if (audioCodeCtx != NULL){
                avcodec_free_context(&audioCodeCtx);
            }
            return NO;
        }


        src_ch_layout = audioCodeCtx->channel_layout;
        src_sample_fmt = audioCodeCtx->sample_fmt;
        src_rate = audioCodeCtx->sample_rate;
        //swr_alloc_set_opts将PCM源文件的采样格式转换为自己希望的采样格式
        logd("5、audio遍历流信息 out_sample_rate = %d, in_sample_rate = %d", dst_rate, src_rate);
        aswrctx = swr_alloc_set_opts(NULL,
                                     dst_ch_layout,
                                     dst_sample_fmt,
                                     dst_rate,
                                     src_ch_layout,
                                     src_sample_fmt,
                                     src_rate, 0, NULL);

        if (aswrctx == NULL) {
            audioStreamIndex = -1;
            if (audioCodeCtx != NULL) avcodec_free_context(&audioCodeCtx);
            if (aframe != NULL) av_frame_free(&aframe);
            return NO;
        }

        ret = swr_init(aswrctx);
        if (ret < 0) {
            audioStreamIndex = -1;
            if (aswrctx != NULL) swr_free(&aswrctx);
            if (audioCodeCtx != NULL) avcodec_free_context(&audioCodeCtx);
            if (aframe != NULL) av_frame_free(&aframe);
            return NO;
        }
        int out_channer_nb = av_get_channel_layout_nb_channels(dst_ch_layout);//获取通道数  2
        playerCallJava->initAudioTrack(this->dst_rate, out_channer_nb);
    }else{
        logd("ffmpeg 获取编码器失败");
        if (formatContext != NULL){
            avformat_close_input(&formatContext);
        }
        return NO;
    }

    //变量赋值
    mAudioStreamIndex = audioStreamIndex;
    mAudioFrame = aframe;
    mAudioTimebase = timeBase;
    mAudioCodecContext = audioCodeCtx;
    mAudioSwrContext = aswrctx;
    mHasAudio = TRUE;
    return YES;
}

//初始化 编码器上下文
AVCodecContext* Decoder::openCodec(AVFormatContext *formatContext, int stream){
    AVCodecParameters *params = formatContext->streams[stream]->codecpar;
    AVCodec *codec = avcodec_find_decoder(params->codec_id);
    if (codec == NULL) return NULL;
    AVCodecContext *context = avcodec_alloc_context3(codec);
    if (context == NULL) return NULL;
    int ret = avcodec_parameters_to_context(context, params);
    if (ret < 0) {
        avcodec_free_context(&context);
        return NULL;
    }
    ret = avcodec_open2(context, codec, NULL);
    if (ret < 0) {
        avcodec_free_context(&context);
        return NULL;
    }
    return context;
}

//获取旋转
double Decoder::rotationFromVideoStream(AVStream *stream) {
    double rotation = 0;
    AVDictionaryEntry *entry = av_dict_get(stream->metadata, "rotate", NULL, AV_DICT_MATCH_CASE);
    if (entry && entry->value) { rotation = av_strtod(entry->value, NULL); }
    //提取变换矩阵的旋转分量。
    uint8_t *display_matrix = av_stream_get_side_data(stream, AV_PKT_DATA_DISPLAYMATRIX, NULL);
    if (display_matrix) { rotation = -av_display_rotation_get((int32_t *)display_matrix); }
    return rotation;
}


std::list<PlayerFrame*> Decoder::readFrames() {
    AVPacket packet;
    std::list<PlayerFrame*> frames;
    BOOL reading = YES;
    while (reading) {
        sIOStartTime = time(NULL);
        int ret = av_read_frame(mFormatContext, &packet);
        if (ret < 0) {
            if (ret == AVERROR_EOF) mIsEOF = YES;
            char *e = av_err2str(ret);
            loge("read frame error: %s", e);
            break;
        }

        std::list<PlayerFrame*> tempFrames;
        if (packet.stream_index == mVideoStreamIndex) {
            tempFrames = handleVideoPacket(&packet ,mVideoCodecContext ,mVideoFrame ,mVideoSwsContext);
            reading = NO;
        }else if(packet.stream_index == mAudioStreamIndex){
            tempFrames = handleAudioPacket(&packet,mAudioCodecContext ,mAudioFrame ,mAudioSwrContext);
            reading = NO;
        }

        if (!tempFrames.empty()) {

//            std::list<PlayerFrame>::iterator iter;
//            for(iter = tempFrames.begin(); iter != tempFrames.end() ;iter++)
//            {
//                iter->data
//            }
            frames.splice(frames.end(), tempFrames);
        }
        av_packet_unref(&packet);
    }
    return frames;
}

std::list<PlayerFrame*> Decoder::handleVideoPacket(AVPacket *packet, AVCodecContext *context,
                                                  AVFrame *frame, struct SwsContext *swsctx) {

    int ret = avcodec_send_packet(context, packet);
    std::list<PlayerFrame*> frames;
    if (ret != 0) {
        logd("avcodec_send_packet: %d", ret);
        return frames;
    }

    const int width = context->width;
    const int height = context->height;
    do {
        ret = avcodec_receive_frame(context, frame);
        if (ret == AVERROR_EOF || ret == AVERROR(EAGAIN)){
            break;
        }else if (ret < 0) {
            logd("avcodec_receive_frame: %d", ret);
            break;
        }

        AVFrame *swsframe = av_frame_alloc();
        av_image_alloc(swsframe->data, swsframe->linesize, width, width, AV_PIX_FMT_RGBA, 1);
        sws_scale(swsctx,
                  (uint8_t const **)frame->data,
                  frame->linesize,
                  0,
                  context->height,
                  swsframe->data,
                  swsframe->linesize);

        auto *videoFrame = new PlayerFrameVideo();
        videoFrame->data = swsframe;
        videoFrame->width = width;
        videoFrame->height = height;
        double best_effort_timestamp = (double)frame->best_effort_timestamp;
        videoFrame->position = best_effort_timestamp * mVideoTimebase;
        double duration = (double)frame->pkt_duration;
        if (duration > 0) {
            videoFrame->duration = duration * mVideoTimebase;
            double repeat_pict = (double)frame->repeat_pict;
            videoFrame->duration += repeat_pict * mVideoTimebase * 0.5;
        } else {
            videoFrame->duration = 1 / mVideoFPS;
        }
        frames.push_back(videoFrame);

    } while(ret == 0);

//    if(!frames.empty()){
//        loge("======read frame size: %d", frames.size());
//        std::list<PlayerFrame*>::iterator iter;
//        for(iter = frames.begin(); iter != frames.end() ;iter++)
//        {
//            if(((*iter)->data) != NULL){
//                av_free((*iter)->data->data[0]);
//                av_frame_free(&(*iter)->data);
//            }
//        }
//        frames.clear();
//    }

    return frames;
}

std::list<PlayerFrame*> Decoder::handleAudioPacket(AVPacket *packet,AVCodecContext *context,
        AVFrame *frame ,SwrContext *swrContext){

    int ret = avcodec_send_packet(context, packet);
    std::list<PlayerFrame*> frames;
    if (ret != 0) {
        loge("avcodec_send_packet: %d", ret);
        return frames;
    }

    do {
        ret = avcodec_receive_frame(context, frame);
        if (ret == AVERROR_EOF || ret == AVERROR(EAGAIN)) {
            break;
        } else if (ret < 0) {
            loge("avcodec_receive_frame: %d", ret);
            break;
        }
        if (frame->data[0] == NULL) continue;

        AVFrame *pFrame = av_frame_alloc();
        int dst_nb_channels = av_get_channel_layout_nb_channels(dst_ch_layout);//    获取通道数  2
        //不同 采样率，一帧的时间应是相同，所以，按比例得出 新的采样率一帧的采样数， 同时考虑 延时问题。
        int64_t dst_nb_samples = av_rescale_rnd(swr_get_delay(swrContext, src_rate) +
                                                    frame->nb_samples, dst_rate, src_rate, AV_ROUND_UP);
        ret = av_samples_alloc(pFrame->data, &pFrame->linesize[0], dst_nb_channels, dst_nb_samples, dst_sample_fmt, 1);


        //这里的in_count,out_count是一帧的样本数
        int samplesPerChannel = swr_convert(swrContext, pFrame->data, dst_nb_samples, (const uint8_t **) frame->data, frame->nb_samples);
        int size = av_samples_get_buffer_size(&pFrame->linesize[0], dst_nb_channels, samplesPerChannel, dst_sample_fmt, 1);

        auto *audioFrame = new PlayerFrameAudio();
        audioFrame->data = pFrame;
        audioFrame->size = size;

        double best_effort_timestamp = (double)frame->best_effort_timestamp;
        double pkt_duration = (double)frame->pkt_duration;
        audioFrame->position = best_effort_timestamp * mAudioTimebase;
        audioFrame->duration = pkt_duration * mAudioTimebase;
        frames.push_back(audioFrame);
    } while(ret == 0);
    return frames;

}







