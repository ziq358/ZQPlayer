//
// Created by johnwu on 2018/10/5.
//



#ifndef ZQPLAYER_JAVACALL_H
#define ZQPLAYER_JAVACALL_H
#import <jni.h>
#import "AndroidLog.h"

class PlayerCallJava {
public:
    JavaVM *javaVM;
    jobject jobj;
    jmethodID jmid_onLoading;
    jmethodID jmid_onPrepareFinished;
    jmethodID jmid_onPlaying;
    jmethodID jmid_onPause;
    jmethodID jmid_onStop;
    jmethodID jmid_onError;
    jmethodID jmid_initAudioTrack;
    jmethodID jmid_sendDataToAudioTrack;
    jmethodID jmid_initMediaCodec;
    jmethodID jmid_sendToMediaCodec;
public:
    PlayerCallJava(JavaVM *javaVM, jobject *jobj);
    void onLoading();
    void onPrepareFinished();
    void onPlaying();
    void onPause();
    void onStop();
    void onError(const char *msg);
    void initAudioTrack(int sampleRateInHz, int nb_channals);
    void sendDataToAudioTrack(uint8_t *out_buffer, int size);
    void initMediaCodec(const char *mimetype, int width, int height, int csd_0_size, int csd_1_size, uint8_t *csd_0, uint8_t *csd_1);
    void sendToMediaCodec(uint8_t *packet_data, int size, int pts);

};

#endif //ZQPLAYER_JAVACALL_H
