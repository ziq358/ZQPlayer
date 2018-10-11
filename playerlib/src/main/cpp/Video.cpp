//
// Created by jj on 2018/10/8.
//

#include "Video.h"


Video::Video(JavaVM *javaVM, PlayerCallJava *playerCallJava) {
    Video::javaVM = javaVM;
    Video::playerCallJava = playerCallJava;
}

void Video::initAvCodecContext(AVCodecParameters *parameters) {
    AVCodec *dec = avcodec_find_decoder(parameters->codec_id);
    if (!dec) {
        loge("获取编码器失败");
        return;
    }
    videoCodecCtx = avcodec_alloc_context3(dec);
    if (!videoCodecCtx) {
        loge("获取编码器 上下文 失败");
    }
    if (avcodec_parameters_to_context(videoCodecCtx, parameters) != 0) {
        loge("编码器 上下文 配置 失败");
    }
    if (avcodec_open2(videoCodecCtx, dec, 0) != 0) {
        loge("打开编码器上下文 失败");
    }
}


void Video::init(AVStream *stream) {
    time_base = stream->time_base;
    initAvCodecContext(stream->codecpar);
    loge("videoCodecCtx->codec->name = %s  ", videoCodecCtx->codec->name);
    loge("videoCodecCtx->width = %d  ", videoCodecCtx->width);
    loge("videoCodecCtx->height = %d  ", videoCodecCtx->height);
    loge("videoCodecCtx->extradata_size = %d  ", videoCodecCtx->extradata_size);
    loge("videoCodecCtx->extradata = %d  ", videoCodecCtx->extradata);
    const char *codecName = videoCodecCtx->codec->name;
    const char *mimetype = "";
    if (strcmp(codecName, "h264") == 0) {
        mimetype = "video/avc";
        mimType =  av_bitstream_filter_init("h264_mp4toannexb");
    }
    if (strcmp(codecName, "hevc") == 0) {
        mimetype = "video/hevc";
        mimType =  av_bitstream_filter_init("hevc_mp4toannexb");
    }
    if (strcmp(codecName, "mpeg4") == 0) {
        mimetype = "video/mp4v-es";
        mimType =  av_bitstream_filter_init("h264_mp4toannexb");
    }
    if (strcmp(codecName, "wmv3") == 0) {
        mimetype = "video/x-ms-wmv";
        mimType =  av_bitstream_filter_init("h264_mp4toannexb");
    }

    playerCallJava->initMediaCodec(mimetype, videoCodecCtx->width, videoCodecCtx->height,
                                   videoCodecCtx->extradata_size, videoCodecCtx->extradata_size,
                                   videoCodecCtx->extradata,videoCodecCtx->extradata);
}

void Video::playAction() {
    loge("视频 playAction 启动");
    status = STATUS_PLAYING;
    while (status != STATUS_STOP) {
        if (status == STATUS_PAUSE || queuePacket.empty()) {
//            logd("视频 playAction 睡眠");
            av_usleep(1000 * 10);
            continue;
        }
        countSend++;
        AVPacket *packet = queuePacket.front();
        queuePacket.pop();
        double time = packet->pts * av_q2d(time_base);
        loge("--- 视频 取数据  队列 %d 次数 %d 时间  %f packet->data = %d packet->size = %d", queuePacket.size(), countSend, time, packet->data, packet->size);
        if(time > clock->time){
            logd("--- 视频 取数据  sleep  %d", (unsigned int)(1000 * 1000 * (time - clock->time)));
            av_usleep((unsigned int)(1000 * 1000 * (time - clock->time)));
        }

        //todo  查询 这么写的原因
        uint8_t *data;
        av_bitstream_filter_filter(mimType, videoCodecCtx, NULL, &data, &packet->size, packet->data, packet->size, 0);
        uint8_t *tdata = NULL;
        tdata = packet->data;
        packet->data = data;
        if(tdata != NULL)
        {
            av_free(tdata);
        }

        playerCallJava->sendToMediaCodec(packet->data, packet->size, 0);

        av_free(packet->data);
        av_free(packet->buf);
        av_free(packet->side_data);
        packet = NULL;

    }
    if(mimType != NULL)
    {
        av_bitstream_filter_close(mimType);
    }
    loge("视频 playAction 结束");
}

void *videoPlayRunnable(void *data) {
    Video *player = (Video *) data;
    player->playAction();
    return NULL;
}

void Video::play() {
    if (status == STATUS_PAUSE) {
        status = STATUS_PLAYING;
    } else {
        pthread_create(&playThread, NULL, videoPlayRunnable, this);
    }
}

void Video::pause() {
    status = STATUS_PAUSE;
}

void Video::stop() {
    status = STATUS_STOP;
}

void Video::sendData(AVPacket *packet) {
    count++;
    logd("+++ 视频 写数据  %d  次数 %d", queuePacket.size(), count);
    queuePacket.push(packet);
}