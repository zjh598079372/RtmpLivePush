#include <jni.h>
#include <string>

#include <pthread.h>
#include "rtmp/RtmpPush.h"
#include "rtmp/JniCallback.h"

extern "C"{
#include "librtmp/rtmp.h"
}

RtmpPush *rtmpPush = NULL;
JniCallback* jniCallback = NULL;
JavaVM *javaVm = NULL;
/*
* Set some test stuff up.
*
* Returns the JNI version on success, -1 on failure.
*/
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = NULL;
    jint result = -1;
    javaVm = vm;

    if (vm->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
        return -1;
    }
    assert(env != NULL);


    /* success -- return valid version number */
    result = JNI_VERSION_1_4;
    return result;
}



extern "C" JNIEXPORT jstring JNICALL
Java_com_chuoli_rtmpdump_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


extern "C"
JNIEXPORT void JNICALL
Java_com_chuoli_rtmpdump_MainActivity_initRtmp(JNIEnv *env, jobject thiz) {



}

extern "C"
JNIEXPORT void JNICALL
Java_com_chuoli_rtmpdump_livePush_RtmpPush_nInitConnect(JNIEnv *env, jobject thiz, jstring url) {

    const char* connectUrl = env->GetStringUTFChars(url,0);
    jniCallback = new JniCallback(env,javaVm,thiz);
    rtmpPush = new RtmpPush(jniCallback,connectUrl);
    rtmpPush->init();
    env->ReleaseStringUTFChars(url,connectUrl);
}