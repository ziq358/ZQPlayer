//
// Created by johnwu on 2019-08-06.
//
#import "LibInit.h"
JavaVM *javaVM;
jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    jint result = -1;
    javaVM = vm;
    JNIEnv *env;
    logd("JNI_OnLoad");
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        loge("GetEnv 失败");
        return result;
    }
    return JNI_VERSION_1_6;
}