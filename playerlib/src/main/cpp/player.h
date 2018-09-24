#include <jni.h>
#include <unistd.h> // sleep 的头文件
#include <string>
#include <iostream>
#include <android/log.h>

#define TAG    "jni-log" // 这个是自定义的LOG的标识
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,TAG, __VA_ARGS__) // 定义LOGD类型

extern "C"{
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

    #include <libavfilter/avfiltergraph.h>
    #include <libavfilter/buffersrc.h>
    #include <libavfilter/buffersink.h>
    #include "libswresample/swresample.h"

    JNIEXPORT void JNICALL Java_com_zq_playerlib_ZQPlayer_play(JNIEnv *env, jobject, jobject,jobject, jstring, jint);
}

