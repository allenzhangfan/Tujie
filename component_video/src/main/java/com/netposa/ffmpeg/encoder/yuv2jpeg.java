package com.netposa.ffmpeg.encoder;

public class yuv2jpeg {

    public native boolean encoder(byte[] filepath,byte[] yuvdata,int width,int height);
}
