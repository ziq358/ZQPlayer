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
    jmid_onPrepareFinished = env->GetMethodID(player, "onPrepareFinished", "()V");
    jmid_onPlaying = env->GetMethodID(player, "onPlaying", "()V");
    jmid_onPause = env->GetMethodID(player, "onPause", "()V");
    jmid_onStop = env->GetMethodID(player, "onStop", "()V");
    jmid_onError = env->GetMethodID(player, "onError", "(Ljava/lang/String;)V");
    jmid_initAudioTrack = env->GetMethodID(player, "initAudioTrack", "(II)V");
    jmid_sendDataToAudioTrack = env->GetMethodID(player, "sendDataToAudioTrack", "([BI)V");
    jmid_initMediaCodec = env->GetMethodID(player, "initMediaCodec", "(Ljava/lang/String;II[B[B)V");
    jmid_sendToMediaCodec = env->GetMethodID(player, "sendToMediaCodec", "([BII)V");

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

void PlayerCallJava::onPrepareFinished() {
    JNIEnv *env;
    bool isAttached = false;
    jint status = javaVM->GetEnv((void **) &env, JNI_VERSION_1_6);
    if (status < 0) {
        status = javaVM->AttachCurrentThread(&env, NULL);
        if(status != JNI_OK){
            loge("获取JNIEnv 失败");
            return;
        }
        isAttached = true;
    }

    env->CallVoidMethod(jobj, jmid_onPrepareFinished);

    if (isAttached) {
        javaVM->DetachCurrentThread();
    }
}

void PlayerCallJava::onPlaying() {
    JNIEnv *env;
    bool isAttached = false;
    jint status = javaVM->GetEnv((void **) &env, JNI_VERSION_1_6);
    if (status < 0) {
        status = javaVM->AttachCurrentThread(&env, NULL);
        if(status != JNI_OK){
            loge("获取JNIEnv 失败");
            return;
        }
        isAttached = true;
    }

    env->CallVoidMethod(jobj, jmid_onPlaying);

    if (isAttached) {
        javaVM->DetachCurrentThread();
    }
}

void PlayerCallJava::onPause() {
    JNIEnv *env;
    bool isAttached = false;
    jint status = javaVM->GetEnv((void **) &env, JNI_VERSION_1_6);
    if (status < 0) {
        status = javaVM->AttachCurrentThread(&env, NULL);
        if(status != JNI_OK){
            loge("获取JNIEnv 失败");
            return;
        }
        isAttached = true;
    }

    env->CallVoidMethod(jobj, jmid_onPause);

    if (isAttached) {
        javaVM->DetachCurrentThread();
    }
}

void PlayerCallJava::onStop() {
    JNIEnv *env;
    bool isAttached = false;
    jint status = javaVM->GetEnv((void **) &env, JNI_VERSION_1_6);
    if (status < 0) {
        status = javaVM->AttachCurrentThread(&env, NULL);
        if(status != JNI_OK){
            loge("获取JNIEnv 失败");
            return;
        }
        isAttached = true;
    }

    env->CallVoidMethod(jobj, jmid_onStop);

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

void PlayerCallJava::sendDataToAudioTrack(uint8_t *out_buffer, int size){
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

    jbyteArray audio_sample_array = env->NewByteArray(size);
    env->SetByteArrayRegion(audio_sample_array, 0, size, (const jbyte *) out_buffer);//该函数将本地的数组数据拷贝到了 Java 端的数组中
    env->CallVoidMethod(jobj, jmid_sendDataToAudioTrack, audio_sample_array, size);
    env->DeleteLocalRef(audio_sample_array);//删除 obj 所指向的局部引用。

    if (isAttached) {
        javaVM->DetachCurrentThread();
    }
}

void PlayerCallJava::initMediaCodec(const char *mimetype, int width, int height, int csd_0_size, int csd_1_size, uint8_t *csd_0, uint8_t *csd_1){
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
    jstring vedioMimetype = env->NewStringUTF(mimetype);
    jbyteArray csd0 = env->NewByteArray(csd_0_size);
    env->SetByteArrayRegion(csd0, 0, csd_0_size, (const jbyte *) csd_0);
    jbyteArray csd1 = env->NewByteArray(csd_1_size);
    env->SetByteArrayRegion(csd0, 0, csd_1_size, (const jbyte *) csd_1);

    env->CallVoidMethod(jobj, jmid_initMediaCodec, vedioMimetype, width, height, csd0, csd1);

    env->DeleteLocalRef(vedioMimetype);
    env->DeleteLocalRef(csd0);
    env->DeleteLocalRef(csd1);

    if (isAttached) {
        javaVM->DetachCurrentThread();
    }
}

void PlayerCallJava::sendToMediaCodec(uint8_t *packet_data, int size, int pts){
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
    jbyteArray data = env->NewByteArray(size);
    env->SetByteArrayRegion(data, 0, size, (jbyte*)packet_data);
    env->CallVoidMethod(jobj, jmid_sendToMediaCodec, data, size, pts);
    env->DeleteLocalRef(data);

    if (isAttached) {
        javaVM->DetachCurrentThread();
    }
}


