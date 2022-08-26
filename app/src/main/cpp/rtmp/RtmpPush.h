//
// Created by CL002 on 2022-8-25.
//

#ifndef RTMPDUMP_RTMPPUSH_H
#define RTMPDUMP_RTMPPUSH_H

#include <pthread.h>
#include "JniCallback.h"

extern "C"{
#include "../librtmp/rtmp.h"
};


class RtmpPush {

public:
    RtmpPush(JniCallback* jniCallback,const char* connectUrl);
    void init();
    void connect();


    ~RtmpPush();

public:
    RTMP * rtmp = NULL;
    JniCallback* jniCallback = NULL;
    char* connectUrl = NULL;


};


#endif //RTMPDUMP_RTMPPUSH_H
