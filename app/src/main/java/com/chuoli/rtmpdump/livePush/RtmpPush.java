package com.chuoli.rtmpdump.livePush;

import android.util.Log;

import java.util.zip.Inflater;

public class RtmpPush {


    public native void nInitConnect(String url);

    private ResultCallback mResultCallback;

    public void onSuccess(){
        if(mResultCallback != null){
            mResultCallback.onSuccess();
        }
    }

    public void onError(int errorCode, String msg){
        if(mResultCallback != null){
            mResultCallback.onError(errorCode,msg);
        }
    };

    public ResultCallback getResultCallback() {
        return mResultCallback;
    }

    public RtmpPush setResultCallback(ResultCallback resultCallback) {
        this.mResultCallback = resultCallback;
        return this;
    }

    public interface ResultCallback{
        void onSuccess();
        void onError(int errorCode, String msg);

    }
}
