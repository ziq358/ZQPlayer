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
    jclass david_player = env->GetObjectClass(jobj);
    //反射得到onLoading方法
    jmid_onloading = env->GetMethodID(david_player, "onLoading", "()V");


    if (isAttached) {
        javaVM->DetachCurrentThread();
    }

}




void PlayerCallJava::onLoading() {
    callJavaByMethodID(jmid_onloading);
}


void PlayerCallJava::callJavaByMethodID(jmethodID id){
    JNIEnv *env;
    bool isAttached = false;
    jint status = javaVM->GetEnv((void **) &env, JNI_VERSION_1_6);
    if (status < 0) {
        javaVM->AttachCurrentThread(&env, NULL);//将当前线程注册到虚拟机中．为了获取JNIEnv， 子线程 需要
        isAttached = true;
    }
    env->CallVoidMethod(jobj, id);
    if (isAttached) {
        javaVM->DetachCurrentThread();
    }
}