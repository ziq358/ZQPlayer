//
// Created by jj on 2018/10/8.
//

#include "PlayerCallJava.h"
#include "list"

#ifndef ZQPLAYER_AUDIO_H
#define ZQPLAYER_AUDIO_H

extern "C" {
#include <libavcodec/avcodec.h>
#include <libswresample/swresample.h>
}

class Audio {
public:
    JavaVM *javaVM;
    PlayerCallJava *playerCallJava;
    std::list<int> streamList;
    int currentStreamIndex;
    AVCodecContext *audioCodecCtx;
    SwrContext *swrContext;
    uint8_t *out_buffer;
    AVFrame *pFrame;
public:
    Audio(JavaVM *javaVM, PlayerCallJava *playerCallJava);

    void init(AVCodecParameters *codecpar);

    void sendData(AVPacket *packet);
};

#endif //ZQPLAYER_AUDIO_H
