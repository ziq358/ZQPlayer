package com.zq.playerlib;

public class ZQPusher {

    static {
        System.loadLibrary("player-lib");
    }

    public native int initVideo(String url,int width,int height);

    public native int onFrameCallback(byte[] buffer);

}
