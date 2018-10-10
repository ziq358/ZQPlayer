#include <jni.h>
#include <unistd.h> // sleep 的头文件
#include <string>
#include <iostream>
#include "AndroidLog.h"

#include "PlayerCallJava.h"

#include "Audio.h"
#include "Video.h"

extern "C" {
//在c++代码中链接c语言的库，没有添加extern "C"； 会 undefined reference to 'av_frame_alloc'
//编码
#include "libavcodec/avcodec.h"
//封装格式处理
#include "libavformat/avformat.h"
//像素处理
#include "libswscale/swscale.h"
#include "libavutil/imgutils.h"
#include <android/native_window.h>
#include <android/native_window_jni.h>
//filter
#include <libavfilter/avfiltergraph.h>
#include <libavfilter/buffersrc.h>
#include <libavfilter/buffersink.h>
#include "libswresample/swresample.h"
#include "libavutil/time.h"
#include "Clock.h"
#include "common.h"

class Player {
public:
    JavaVM *javaVM;
    PlayerCallJava *playerCallJava;
    const char *url;
    AVFormatContext *pFormatCtx;
    Clock *clock;
    Audio *audio;
    Video *video;
    pthread_t prepareThread;
    pthread_t startThread;
    int status = STATUS_IDLE;
public:
    Player(JavaVM *javaVM, PlayerCallJava *playerCallJava, const char *url);
    void prepare();
    void start();
    void pause();
    void stop();
    bool isPlaying();
};


JNIEXPORT void JNICALL
Java_com_zq_playerlib_ZQPlayer_play(JNIEnv *env, jobject, jobject, jobject, jstring, jint);

JNIEXPORT void JNICALL
Java_com_zq_playerlib_ZQPlayer_prepare(JNIEnv *env, jobject, jstring);

JNIEXPORT void JNICALL
Java_com_zq_playerlib_ZQPlayer_start(JNIEnv *env, jobject);

JNIEXPORT void JNICALL
Java_com_zq_playerlib_ZQPlayer_pause(JNIEnv *env, jobject);

JNIEXPORT void JNICALL
Java_com_zq_playerlib_ZQPlayer_stop(JNIEnv *env, jobject);

JNIEXPORT jboolean JNICALL
Java_com_zq_playerlib_ZQPlayer_isPlaying(JNIEnv *env, jobject);
}

