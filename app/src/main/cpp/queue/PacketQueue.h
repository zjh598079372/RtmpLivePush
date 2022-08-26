//
// Created by CL002 on 2022-8-26.
//

#ifndef RTMPDUMP_PACKETQUEUE_H
#define RTMPDUMP_PACKETQUEUE_H

#include <mutex>
#include <queue>
extern "C"{
#include "../librtmp/rtmp.h"
};

using namespace std;

class PacketQueue {
public:
    PacketQueue();
    ~PacketQueue();
    void pushPacket(RTMPPacket* rtmpPacket);
    RTMPPacket* pop();
    void clear();

public:
queue<RTMPPacket*>* packetQueue = NULL;
pthread_mutex_t pMutex;
pthread_cond_t pCond;

};


#endif //RTMPDUMP_PACKETQUEUE_H
