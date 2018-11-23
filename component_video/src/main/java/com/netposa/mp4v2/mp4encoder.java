package com.netposa.mp4v2;

import java.io.UnsupportedEncodingException;

public class mp4encoder {

    private long mEncoerId = 0;

    public mp4encoder() {
        try {
            System.loadLibrary("mp4encoder");
        } catch (UnsatisfiedLinkError localUnsatisfiedLinkError) {
            try {
                System.load("/system/lib/libmp4encoder.so");
            } catch (UnsatisfiedLinkError e) {
            }
        }
    }

    public int InitMp4File(String path)
    {
        byte[] filepath = null;
        try {
            filepath = path.getBytes("gb2312");

        } catch (UnsupportedEncodingException e) {
            return -1;
        }
        return init(filepath);
    }

    public native int init(byte[] filefullpath);

    public native void writeh264(byte[] h264data,int len);

    public native void close();

}
