#include <jni.h>
#include <unistd.h> // sleep 的头文件
#include <string>
#include <iostream>
#include "AndroidLog.h"
#include "PlayerCallJava.h"
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

#include <libavfilter/avfiltergraph.h>
#include <libavfilter/buffersrc.h>
#include <libavfilter/buffersink.h>
#include "libswresample/swresample.h"


JNIEXPORT void JNICALL
Java_com_zq_playerlib_ZQPlayer_play(JNIEnv *env, jobject, jobject, jobject, jstring, jint);
}

