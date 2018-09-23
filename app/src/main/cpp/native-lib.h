#include <jni.h>
#include <unistd.h> // sleep 的头文件
#include <string>
#include <iostream>
#include <android/log.h>

#define TAG    "jni-log" // 这个是自定义的LOG的标识
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,TAG, __VA_ARGS__) // 定义LOGD类型

extern "C"{

    JNIEXPORT jstring JNICALL Java_com_zq_zqplayer_MainActivity_stringFromJNI(JNIEnv *env, jobject);
}

