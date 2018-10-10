//
// Created by jj on 2018/10/8.
//



#include "Audio.h"


Audio::Audio(JavaVM *javaVM, PlayerCallJava *playerCallJava) {
    Audio::javaVM = javaVM;
    Audio::playerCallJava = playerCallJava;
}

void Audio::initAvCodecContext(AVCodecParameters *parameters) {
    AVCodec *dec = avcodec_find_decoder(parameters->codec_id);
    if(!dec) {
        loge("获取编码器失败");
        return;
    }
    audioCodecCtx = avcodec_alloc_context3(dec);
    if(!audioCodecCtx) {
        loge("获取编码器 上下文 失败");
    }
    if(avcodec_parameters_to_context(audioCodecCtx, parameters) != 0)
    {
        loge("编码器 上下文 配置 失败");
    }
    if(avcodec_open2(audioCodecCtx, dec, 0) != 0)
    {
        loge("打开编码器上下文 失败");
    }
}


void Audio::init(AVStream *stream) {
    logd(" 开始　音频初始化");
    time_base = stream->time_base;
    initAvCodecContext(stream->codecpar);
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

    logd(" 结束　音频初始化");
}

void Audio::playAction() {
    loge("音频 playAction 启动");
    status = STATUS_PLAYING;
    while (status != STATUS_STOP){
        if(status == STATUS_PAUSE || queuePacket.empty()){
//            logd("音频 playAction 睡眠");
            av_usleep(1000 * 10);
            continue;
        }
        countSend++;
        AVPacket *packet = queuePacket.front();
        clock->time = packet->pts * av_q2d(time_base);
        loge("--- 音频 取数据  队列 %d 次数 %d 时间  %f ", queuePacket.size(), countSend, clock->time);
        queuePacket.pop();
        int frameFinished;
        AVFrame *pFrame = av_frame_alloc();
        avcodec_decode_audio4(audioCodecCtx, pFrame, &frameFinished, packet);
        if (frameFinished) {
            int out_channer_nb = av_get_channel_layout_nb_channels(AV_CH_LAYOUT_STEREO);//    获取通道数  2
            swr_convert(swrContext, &out_buffer, 44100 * 2, (const uint8_t **) pFrame->data, pFrame->nb_samples);
            int size = av_samples_get_buffer_size(NULL, out_channer_nb, pFrame->nb_samples, AV_SAMPLE_FMT_S16, 1);
            playerCallJava->sendDataToAudioTrack(out_buffer, size);
        }
        av_packet_free(&packet);
        av_free(packet);
        packet = NULL;
        av_frame_free(&pFrame);
        av_free(pFrame);
        pFrame = NULL;
    }
    loge("音频 playAction 结束");
}

void* audioPlayRunnable(void *data) {
    Audio* player = (Audio*)data;
    player->playAction();
    return NULL;
}

void Audio::play() {
    if(status == STATUS_PAUSE){
        status = STATUS_PLAYING;
    }else{
        pthread_create(&playThread, NULL, audioPlayRunnable, this);
    }
}

void Audio::pause() {
    status = STATUS_PAUSE;
}

void Audio::stop() {
    status = STATUS_STOP;
}

void Audio::sendData(AVPacket* packet) {
    count++;
    logd("+++ 音频 写数据  %d  次数 %d", queuePacket.size(), count);
    queuePacket.push(packet);
}

