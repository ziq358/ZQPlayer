#import "LibInit.h"
#import <unistd.h> // sleep 的头文件
#import <string>
#import <iostream>
#import "AndroidLog.h"

#import "PlayerCallJava.h"

#import "Audio.h"
#import "Video.h"
#import "Decoder.h"

extern "C" {
//在c++代码中链接c语言的库，没有添加extern "C"； 会 undefined reference to 'av_frame_alloc'
//编码
#import "libavcodec/avcodec.h"
//封装格式处理
#import "libavformat/avformat.h"
//像素处理
#import "libswscale/swscale.h"
#import "libavutil/imgutils.h"
#import <android/native_window.h>
#import <android/native_window_jni.h>
//filter
#import <libavfilter/avfilter.h>
#import <libavfilter/buffersrc.h>
#import <libavfilter/buffersink.h>
#import "libswresample/swresample.h"
#import "libavutil/time.h"
#import "Clock.h"
#import "common.h"

class Player {
public:
    JavaVM *javaVM;
    ANativeWindow *nativeWindow = NULL;
    PlayerCallJava *playerCallJava;
    const char *url;
    Clock *clock;
    Decoder *decoder;
    double mMaxBufferDuration = 1;// 缓存多了 占内存
    double mBufferedDuration = 0;//当前缓存
    pthread_t initThread = NULL;
    pthread_t playFrameThread= NULL;
    pthread_t playAudioFrameThread= NULL;
    pthread_t playVideoFrameThread= NULL;
    pthread_t clearVideoFrameThread= NULL;
    std::list<PlayerFrame*> audioframes;
    std::list<PlayerFrame*> videoframes;
    std::list<PlayerFrame*> abandonVideoframes;
//    pthread_mutex_t videoFramesMutex;
    int status = STATUS_IDLE;
public:
    Player(JavaVM *javaVM, PlayerCallJava *playerCallJava);
    void init(const char *url);
    void setNativeWindow(ANativeWindow *nativeWindow);
    void play();
    void pause();
    void stop();
    void clear();
    bool isPlaying();
};


JNIEXPORT void JNICALL
Java_com_zq_playerlib_ZQPlayer_playdemo(JNIEnv *env, jobject, jobject, jobject, jstring, jint);

JNIEXPORT void JNICALL
Java_com_zq_playerlib_ZQPlayer_init(JNIEnv *env, jobject, jstring);

JNIEXPORT void JNICALL
Java_com_zq_playerlib_ZQPlayer_setSurface(JNIEnv *env, jobject, jobject);

JNIEXPORT void JNICALL
Java_com_zq_playerlib_ZQPlayer_play(JNIEnv *env, jobject);

JNIEXPORT void JNICALL
Java_com_zq_playerlib_ZQPlayer_pause(JNIEnv *env, jobject);

JNIEXPORT void JNICALL
Java_com_zq_playerlib_ZQPlayer_stop(JNIEnv *env, jobject);

JNIEXPORT jboolean JNICALL
Java_com_zq_playerlib_ZQPlayer_isPlaying(JNIEnv *env, jobject);
}

