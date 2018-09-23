#include "native-lib.h"

extern "C"{

    jstring Java_com_zq_zqplayer_MainActivity_stringFromJNI(JNIEnv* env, jobject cls) {
        std::string hello = "Hello from c++----";
        return env->NewStringUTF(hello.c_str());
    }

}

