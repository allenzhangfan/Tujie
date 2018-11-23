package com.netposa.ffmpeg.decoder;

public class idrinfo {

    public interface ffmpeg_decoder_idrinfo_callback{

        void onIDRInfo(int width, int height, byte[] buffer, int len);

    }

    private ffmpeg_decoder_idrinfo_callback l;

    public idrinfo(ffmpeg_decoder_idrinfo_callback cb) {
        this.l = cb;
    }



    public native boolean decoder(byte[] data,int len);


    /**
     * 回调信息，有可能buffer没有值，得做空判断
     * */
    private void onIDRinfo(int width,int height,byte[] buffer,int len){
        if(this.l != null){
            l.onIDRInfo(width,height,buffer,len);
        }
    }
}
