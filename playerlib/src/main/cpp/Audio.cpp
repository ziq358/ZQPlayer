//
// Created by jj on 2018/10/8.
//


#include "Audio.h"
#include "AndroidLog.h"

AVCodecContext * getAvCodecContext(AVCodecParameters *parameters) {
    AVCodecContext *audioCodecCtx;
    AVCodec *dec = avcodec_find_decoder(parameters->codec_id);
    if(!dec) {
        loge("获取编码器失败");
        return NULL;
    }
    audioCodecCtx = avcodec_alloc_context3(dec);
    if(!audioCodecCtx) {
        loge("获取编码器 上下文 失败");
        return audioCodecCtx;
    }
    if(avcodec_parameters_to_context(audioCodecCtx, parameters) != 0)
    {
        loge("编码器 上下文 配置 失败");
    }
    if(avcodec_open2(audioCodecCtx, dec, 0) != 0)
    {
        loge("打开编码器上下文 失败");
    }
    return audioCodecCtx;
}


Audio::Audio(JavaVM *javaVM, PlayerCallJava *playerCallJava) {
    Audio::javaVM = javaVM;
    Audio::playerCallJava = playerCallJava;
}

void Audio::init(AVCodecParameters *codecpar) {
    logd(" 开始　音频初始化");
    audioCodecCtx = getAvCodecContext(codecpar);
    if(audioCodecCtx == NULL){
        loge(" 获取编码器上下文失败");
        return;
    }
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
    swrContext = swr_alloc();
    out_buffer = (uint8_t *) av_malloc(44100 * 2);//缓存区

    uint64_t out_ch_layout = AV_CH_LAYOUT_STEREO; //输出的声道布局（立体声）
    int out_channer_nb = av_get_channel_layout_nb_channels(AV_CH_LAYOUT_STEREO);//    获取通道数  2
    enum AVSampleFormat out_formart = AV_SAMPLE_FMT_S16;//输出采样位数  16位
    int out_sample_rate = audioCodecCtx->sample_rate;//输出的采样率必须与输入相同

    //swr_alloc_set_opts将PCM源文件的采样格式转换为自己希望的采样格式
    swr_alloc_set_opts(swrContext, out_ch_layout, out_formart, out_sample_rate,
                       audioCodecCtx->channel_layout, audioCodecCtx->sample_fmt,
                       audioCodecCtx->sample_rate,
                       0, NULL);
    swr_init(swrContext);

    playerCallJava->initAudioTrack(44100, out_channer_nb);

    pFrame = av_frame_alloc();
    logd(" 结束　音频初始化");
}

void Audio::sendData(AVPacket* packet) {
    int frameFinished;
    avcodec_decode_audio4(audioCodecCtx, pFrame, &frameFinished, packet);
    if (frameFinished) {
        int out_channer_nb = av_get_channel_layout_nb_channels(AV_CH_LAYOUT_STEREO);//    获取通道数  2
        swr_convert(swrContext, &out_buffer, 44100 * 2, (const uint8_t **) pFrame->data, pFrame->nb_samples);
        int size = av_samples_get_buffer_size(NULL, out_channer_nb, pFrame->nb_samples, AV_SAMPLE_FMT_S16, 1);


        JNIEnv *env;
        bool isAttached = false;
        jint status = javaVM->GetEnv((void **) &env, JNI_VERSION_1_6);
        if (status < 0) {
            status = javaVM->AttachCurrentThread(&env, NULL);//将当前线程注册到虚拟机中．为了获取JNIEnv， 子线程 需要
            if(status != JNI_OK){
                loge("获取JNIEnv 失败");
                return;
            }
            isAttached = true;
        }

        jbyteArray audio_sample_array = env->NewByteArray(size);
        env->SetByteArrayRegion(audio_sample_array, 0, size, (const jbyte *) out_buffer);//该函数将本地的数组数据拷贝到了 Java 端的数组中
        playerCallJava->sendDataToAudioTrack(audio_sample_array, size);
        env->DeleteLocalRef(audio_sample_array);//删除 obj 所指向的局部引用。

        if (isAttached) {
            javaVM->DetachCurrentThread();
        }
    }
    av_packet_free(&packet);
    av_free(packet);
    packet = NULL;
}

