//
// Created by CL002 on 2022-8-26.
//

#include "PacketQueue.h"

PacketQueue::PacketQueue() {
    packetQueue = new queue<RTMPPacket*>();
    pthread_mutex_init(&pMutex,NULL);
    pthread_cond_init(&pCond,NULL);

}

void PacketQueue::pushPacket(RTMPPacket *rtmpPacket) {

    pthread_mutex_lock(&pMutex);
    packetQueue->push(rtmpPacket);
    pthread_cond_signal(&pCond);
    pthread_mutex_unlock(&pMutex);

}

RTMPPacket *PacketQueue::pop() {
    pthread_mutex_lock(&pMutex);
    while (packetQueue->empty()){
        pthread_cond_wait(&pCond,&pMutex);
    }
    RTMPPacket* rtmpPacket = packetQueue->front();
    packetQueue->pop();
    pthread_mutex_unlock(&pMutex);

    return rtmpPacket;
}

void PacketQueue::clear() {
    pthread_mutex_lock(&pMutex);
    while (!packetQueue->empty()){
        RTMPPacket* rtmpPacket =  packetQueue->front();
        packetQueue->pop();
        RTMPPacket_Free(rtmpPacket);
        free(rtmpPacket);

    }

    pthread_mutex_unlock(&pMutex);

}

PacketQueue::~PacketQueue() {
    clear();
    pthread_mutex_destroy(&pMutex);
    pthread_cond_destroy(&pCond);

}
