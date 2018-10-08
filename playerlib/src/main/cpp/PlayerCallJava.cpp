//
// Created by johnwu on 2018/10/5.
//

#include "PlayerCallJava.h"


PlayerCallJava::PlayerCallJava(JavaVM *jVM, jobject *jobject) {
    javaVM = jVM;
    JNIEnv *env;
    bool isAttached = false;
    jint status = javaVM->GetEnv((void **) &env, JNI_VERSION_1_6);
    if (status < 0) {
        status = javaVM->AttachCurrentThread(&env, NULL);//将当前线程注册到虚拟机中．为了获取JNIEnv， 子线程 需要
        if(status != JNI_OK){
            loge("获取JNIEnv 失败");
            return;
        }
        isAttached = true;
    }

    jobj = *jobject;
    //注意 报出JNI DETECTED ERROR IN APPLICATION 错误 发现最有可能的就是jclass 实例不能用局部引用，必须使用全局引用保持jclass实例在函数退出后还具有生命周期。
    jobj = env->NewGlobalRef(jobj);//全局引用

    //反射得到Class类型
    jclass player = env->GetObjectClass(jobj);
    //反射得到onLoading方法
    jmid_onLoading = env->GetMethodID(player, "onLoading", "()V");
    jmid_onError = env->GetMethodID(player, "onError", "(Ljava/lang/String;)V");
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
        status = javaVM->AttachCurrentThread(&env, NULL);//将当前线程注册到虚拟机中．为了获取JNIEnv， 子线程 需要
        if(status != JNI_OK){
            loge("获取JNIEnv 失败");
            return;
        }
        isAttached = true;
    }

    env->CallVoidMethod(jobj, jmid_onLoading);

    if (isAttached) {
        javaVM->DetachCurrentThread();
    }
}

void PlayerCallJava::onError(const char *msg){
    JNIEnv *env;
    bool isAttached = false;
    jint status = javaVM->GetEnv((void **) &env, JNI_VERSION_1_6);
    if (status < 0) {
        status = javaVM->AttachCurrentThread(&env, NULL);//将当前线程注册到虚拟机中．为了获取JNIEnv， 子线程 需要
        if(status != JNI_OK){
            loge("获取JNIEnv 失败");
            return;
        }
        isAttached = true;
    }
    jstring jmsg = env->NewStringUTF(msg);
    env->CallVoidMethod(jobj, jmid_onError, jmsg);
    env->DeleteLocalRef(jmsg);

    if (isAttached) {
        javaVM->DetachCurrentThread();
    }
}

void PlayerCallJava::initAudioTrack(int sampleRateInHz, int nb_channals) {
    JNIEnv *env;
    bool isAttached = false;
    jint status = javaVM->GetEnv((void **) &env, JNI_VERSION_1_6);
    if (status < 0) {
        status = javaVM->AttachCurrentThread(&env, NULL);//将当前线程注册到虚拟机中．为了获取JNIEnv， 子线程 需要
        if(status != JNI_OK){
            loge("获取JNIEnv 失败");
            return;
        }
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
        status = javaVM->AttachCurrentThread(&env, NULL);//将当前线程注册到虚拟机中．为了获取JNIEnv， 子线程 需要
        if(status != JNI_OK){
            loge("获取JNIEnv 失败");
            return;
        }
        isAttached = true;
    }

    env->CallVoidMethod(jobj, jmid_sendDataToAudioTrack, byteArray, size);

    if (isAttached) {
        javaVM->DetachCurrentThread();
    }
}


