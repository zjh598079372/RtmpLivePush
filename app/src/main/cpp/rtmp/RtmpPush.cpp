//
// Created by CL002 on 2022-8-25.
//

#include <malloc.h>
#include "RtmpPush.h"

#include <string>

RtmpPush::RtmpPush(JniCallback* jniCallback,const char* connectUrl) {
    this->jniCallback = jniCallback;
    this->connectUrl = (char*)malloc(sizeof(char)*strlen(connectUrl)+1);
    memcpy(this->connectUrl,connectUrl,strlen(connectUrl));
}

RtmpPush::~RtmpPush() {

    if(connectUrl){
        free(connectUrl);
        connectUrl = NULL;
    }
}

void* rtmpConnectThread(void* context){
    RtmpPush* rtmpPush =(RtmpPush *)(context);
    rtmpPush->connect();
    return 0;
}

void RtmpPush::init() {
    pthread_t pthread = NULL;
    pthread_create(&pthread,NULL,rtmpConnectThread,this);
    pthread_detach(pthread);

}

void RtmpPush::connect() {
    rtmp = RTMP_Alloc();
    RTMP_Init(rtmp);
    rtmp->Link.timeout = 10;
    rtmp->Link.lFlags |= RTMP_LF_LIVE;
    RTMP_SetupURL(rtmp,connectUrl);
    RTMP_EnableWrite(rtmp);
    //开始连接

    if(!RTMP_Connect(rtmp,NULL)){
        jniCallback->onError(CHLID_THREAD,01,"连接失败");
        return;
    }

    if(!RTMP_ConnectStream(rtmp,NULL)){
        jniCallback->onError(CHLID_THREAD,02,"连接流失败");
        return;
    }



}
