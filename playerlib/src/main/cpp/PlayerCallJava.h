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
    jmethodID jmid_onLoading;
    jmethodID jmid_onError;
    jmethodID jmid_onPrepareFinished;
    jmethodID jmid_initAudioTrack;
    jmethodID jmid_sendDataToAudioTrack;
public:
    PlayerCallJava(JavaVM *javaVM, jobject *jobj);
    void onLoading();
    void onError(const char *msg);
    void onPrepareFinished();
    void initAudioTrack(int sampleRateInHz, int nb_channals);
    void sendDataToAudioTrack(jbyteArray byteArray, int size);

};

#endif //ZQPLAYER_JAVACALL_H
