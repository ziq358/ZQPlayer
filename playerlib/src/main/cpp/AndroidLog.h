//
// Created by johnwu on 2018/10/5.
//

#include <android/log.h>

#ifndef ZQPLAYER_ANDROIDLOG_H
#define ZQPLAYER_ANDROIDLOG_H
extern "C" { // C++ 不能用下面这种写法， 所以 用 extern "C" 包括
void setLogEnable(bool isEnable);
bool isLogEnable();
void loge(const char *fmt, ...);

void logd(const char *fmt, ...);
}

#endif //ZQPLAYER_ANDROIDLOG_H
