//
// Created by johnwu on 2018/10/5.
//

#include <jni.h>
#include "AndroidLog.h"

#ifndef ZQPLAYER_JAVACALL_H
#define ZQPLAYER_JAVACALL_H

class PlayerCallJava {
public:
    JavaVM *javaVM;
    jobject jobj;
    jmethodID jmid_onloading;
public:
    PlayerCallJava(JavaVM *javaVM, jobject *jobj);
    void callJavaByMethodID(jmethodID id);
    void onLoading();
};

#endif //ZQPLAYER_JAVACALL_H
