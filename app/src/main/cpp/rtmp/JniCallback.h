//
// Created by CL002 on 2022-8-25.
//

#ifndef RTMPDUMP_JNICALLBACK_H
#define RTMPDUMP_JNICALLBACK_H

#include <jni.h>

enum THREAD_TYPE {
    MAIN_THREAD,
    CHLID_THREAD

};

class JniCallback {
private:
    JNIEnv *env = NULL;
    JavaVM *javaVm = NULL;
    jobject rtmpPushJobject = NULL;
    jmethodID onSuccessMethodID = NULL;
    jmethodID onErrorMethodID = NULL;


public:
    JniCallback(JNIEnv *env, JavaVM *javaVm,jobject thiz);

    void initInvokeMethod();

    void onSuccess(THREAD_TYPE threadType);

    void onError(THREAD_TYPE threadType,int errorCode, char *msg);


    ~JniCallback();


};


#endif //RTMPDUMP_JNICALLBACK_H
