//
// Created by jj on 2018/10/8.
//



#ifndef ZQPLAYER_AUDIO_H
#define ZQPLAYER_AUDIO_H

#import "PlayerCallJava.h"
#import "list"
#import "queue"
#import "common.h"
#import "AndroidLog.h"
#import <pthread.h>
#import "Clock.h"
extern "C" {
#import <libavcodec/avcodec.h>
#import <libswresample/swresample.h>
#import <libavutil/time.h>
#import <libavformat/avformat.h>

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
    std::queue<AVPacket*> queuePacket;
    pthread_t playThread;
    int status = STATUS_IDLE;
    int count = 0;
    int countSend = 0;
    int duration;
    Clock *clock;
    AVRational time_base;
public:
    Audio(JavaVM *javaVM, PlayerCallJava *playerCallJava);
    void initAvCodecContext(AVCodecParameters *parameters);
    void init(AVStream *stream);
    void play();
    void pause();
    void stop();
    void playAction();
    void sendData(AVPacket *packet);
};

#endif //ZQPLAYER_AUDIO_H
