//
// Created by johnwu on 2018/10/5.
//

#include "PlayerCallJava.h"


PlayerCallJava::PlayerCallJava(JavaVM *jVM, jobject *jobject) {
    javaVM = jVM;
    jobj = *jobject;

    JNIEnv *env;
    bool isAttached = false;
    jint status = javaVM->GetEnv((void **) &env, JNI_VERSION_1_6);
    if (status < 0) {
        javaVM->AttachCurrentThread(&env, NULL);//将当前线程注册到虚拟机中．为了获取JNIEnv， 子线程 需要
        isAttached = true;
    }

    //反射得到Class类型
    jclass player = env->GetObjectClass(jobj);
    //反射得到onLoading方法
    jmid_onLoading = env->GetMethodID(player, "onLoading", "()V");
    jmid_initAudioTrack = env->GetMethodID(player, "initAudioTrack", "(II)V");
    jmid_sendDataToAudioTrack = env->GetMethodID(player, "sendDataToAudioTrack", "([BI)V");


    if (isAttached) {
        javaVM->DetachCurrentThread();
    }

}

void PlayerCallJava::onLoading() {
    JNIEnv *env;
    bool isAttached = false;
    jint status = javaVM->GetEnv((void **) &env, JNI_VERSION_1_6);
    if (status < 0) {
        javaVM->AttachCurrentThread(&env, NULL);//将当前线程注册到虚拟机中．为了获取JNIEnv， 子线程 需要
        isAttached = true;
    }

    env->CallVoidMethod(jobj, jmid_onLoading);

    if (isAttached) {
        javaVM->DetachCurrentThread();
    }
}

void PlayerCallJava::initAudioTrack(int sampleRateInHz, int nb_channals) {
    JNIEnv *env;
    bool isAttached = false;
    jint status = javaVM->GetEnv((void **) &env, JNI_VERSION_1_6);
    if (status < 0) {
        javaVM->AttachCurrentThread(&env, NULL);//将当前线程注册到虚拟机中．为了获取JNIEnv， 子线程 需要
        isAttached = true;
    }

    env->CallVoidMethod(jobj, jmid_initAudioTrack, sampleRateInHz, nb_channals);

    if (isAttached) {
        javaVM->DetachCurrentThread();
    }
}

void PlayerCallJava::sendDataToAudioTrack(jbyteArray byteArray, int size){
    JNIEnv *env;
    bool isAttached = false;
    jint status = javaVM->GetEnv((void **) &env, JNI_VERSION_1_6);
    if (status < 0) {
        javaVM->AttachCurrentThread(&env, NULL);//将当前线程注册到虚拟机中．为了获取JNIEnv， 子线程 需要
        isAttached = true;
    }

    env->CallVoidMethod(jobj, jmid_sendDataToAudioTrack, byteArray, size);

    if (isAttached) {
        javaVM->DetachCurrentThread();
    }
}


