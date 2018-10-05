//
// Created by johnwu on 2018/10/5.
//

#include "AndroidLog.h"

#define TAG    "ziq Jni" // 这个是自定义的LOG的标识
bool showLog = true;

void setLogEnable(bool isEnable) {
    showLog = isEnable;
} ;

bool isLogEnable() {
    return showLog;
}

void loge(const char *fmt, ...) {
    if (showLog) {
        va_list vl;         //va_list指针，用于va_start取可变参数
        va_start(vl, fmt);      //取得可变参数列表
        __android_log_vprint(ANDROID_LOG_ERROR, TAG, fmt, vl);
        va_end(vl);
    }
} ;

void logd(const char *fmt, ...) {
    if (showLog) {
        va_list vl;
        va_start(vl, fmt);
        __android_log_vprint(ANDROID_LOG_DEBUG, TAG, fmt, vl);
        va_end(vl);
    }
} ;
