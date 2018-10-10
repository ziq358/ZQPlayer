//
// Created by jj on 2018/10/8.
//
#include "PlayerCallJava.h"
#include "list"
#include "queue"
#include "common.h"
#include "AndroidLog.h"
#include <pthread.h>
#include "Clock.h"
#ifndef ZQPLAYER_VIDEO_H
#define ZQPLAYER_VIDEO_H

extern "C" {
#include <libavcodec/avcodec.h>
#include <libswresample/swresample.h>
#include <libavutil/time.h>
#include <libavformat/avformat.h>

}

class Video {
public:
    JavaVM *javaVM;
    PlayerCallJava *playerCallJava;
    std::list<int> streamList;
    AVCodecContext *videoCodecCtx;
    AVBitStreamFilterContext* mimType = NULL;
    int currentStreamIndex;
    std::queue<AVPacket*> queuePacket;
    pthread_t playThread;
    int status = STATUS_IDLE;
    int count = 0;
    int countSend = 0;
    int duration;
    Clock *clock;
    AVRational time_base;
public:
    Video(JavaVM *javaVM, PlayerCallJava *playerCallJava);

    void initAvCodecContext(AVCodecParameters *parameters);
    void init(AVStream *stream);
    void play();
    void pause();
    void stop();
    void playAction();
    void sendData(AVPacket *packet);
};
#endif //ZQPLAYER_VIDEO_H
