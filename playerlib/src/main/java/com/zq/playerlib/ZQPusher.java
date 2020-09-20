package com.zq.playerlib;

public class ZQPusher {

    static {
        System.loadLibrary("player-lib");
    }

    public native int initVideo(String url,int width,int height);

    public native int onFrameCallback(byte[] buffer);

    public native int pushVideo(String videoFilePath, String pushUrl);

}
