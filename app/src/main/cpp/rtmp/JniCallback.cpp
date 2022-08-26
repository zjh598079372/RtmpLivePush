//
// Created by CL002 on 2022-8-25.
//

#include "JniCallback.h"

JniCallback::JniCallback(JNIEnv *env, JavaVM *javaVm,jobject thiz) {

    this->env = env;
    this->javaVm = javaVm;
    jclass rtmpPushClass = env->GetObjectClass(thiz);
    onSuccessMethodID = env->GetMethodID(rtmpPushClass,"onSuccess","()V");
    onErrorMethodID = env->GetMethodID(rtmpPushClass,"onError","(ILjava/lang/String;)V");
    rtmpPushJobject = env->NewGlobalRef(thiz);

}


void JniCallback::onSuccess(THREAD_TYPE threadType) {
    if(threadType == MAIN_THREAD){
        env->CallVoidMethod(rtmpPushJobject,onSuccessMethodID);
    } else{
        JNIEnv* p_env = NULL;
        javaVm->AttachCurrentThread(&p_env,NULL);
        p_env->CallVoidMethod(rtmpPushJobject,onSuccessMethodID);
        javaVm->DetachCurrentThread();
    }


}

void JniCallback::onError(THREAD_TYPE threadType, int errorCode, char *msg) {

    if(threadType == MAIN_THREAD){
        jstring msgStr = env->NewStringUTF(msg);
        env->CallVoidMethod(rtmpPushJobject,onErrorMethodID,errorCode,msgStr);
        env->DeleteLocalRef(msgStr);
    } else{
        JNIEnv* p_env = NULL;
        javaVm->AttachCurrentThread(&p_env,NULL);
        jstring msgStr = p_env->NewStringUTF(msg);
        p_env->CallVoidMethod(rtmpPushJobject,onErrorMethodID,errorCode,msgStr);
        p_env->DeleteLocalRef(msgStr);
        javaVm->DetachCurrentThread();
    }

}

JniCallback::~JniCallback() {

    if(rtmpPushJobject){
        env->DeleteGlobalRef(rtmpPushJobject);
        rtmpPushJobject = NULL;
    }
}




