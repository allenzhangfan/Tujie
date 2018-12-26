package com.netposa.ffmpeg;

import com.netposa.ffmpeg.decoder.h264;
import com.netposa.ffmpeg.decoder.idrinfo;
import com.netposa.ffmpeg.encoder.yuv2jpeg;

public class ffmpegkit {

    static {
        System.loadLibrary("ffmpeg");
        System.loadLibrary("ffmpeg-jni");
    }

    public h264 m_h264;

    public idrinfo _idrinfo;

    public yuv2jpeg _yuv2jpeg;

    public ffmpegkit() {

    }

    /**
     * h264解码器对象
     */
    public void ffmpegkit_decoder_h264(h264.ffmpeg_decoder_h264_callback l) {
        m_h264 = new h264(l);
    }

    /**
     * 根据I帧获取I帧信息类
     */
    public void ffmpegkit_decoder_idr(idrinfo.ffmpeg_decoder_idrinfo_callback l) {
        _idrinfo = new idrinfo(l);
    }

    public void ffmpegkit_encoder_yuv2jpeg() {
        _yuv2jpeg = new yuv2jpeg();
    }
}
