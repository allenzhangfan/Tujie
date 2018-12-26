package com.netposa.ffmpeg.decoder;

public class h264 {


    private long mdecoderh264Id;

    public interface ffmpeg_decoder_h264_callback {

        void onVideoSize(int width, int height);

        void onYUVData(byte[] data, int len);
    }

    private ffmpeg_decoder_h264_callback l;

    public h264(ffmpeg_decoder_h264_callback cb) {
        this.l = cb;
    }

    public void closeh264() {
        close();
        mdecoderh264Id = 0;
    }

    /**
     * 打开解码器
     *
     * @return 0 成功 -1 失败
     */
    public native int open();

    /**
     * 解码
     *
     * @param data h264数据
     * @param len  数据长度
     */
    public native void decode(byte[] data, int len);

    /**
     * 关闭解码器
     */
    public native void close();

    /**
     * 视频分辨率回调
     * 注意：如果每一个I帧前都有sps_pps，则会一直回调，上层引用需要根据需求处理
     *
     * @param width  分辨率-宽
     * @param height 分辨率-高
     */
    private void onVideoSize(int width, int height) {
        if (this.l != null) {
            l.onVideoSize(width, height);
        }
    }


    /**
     * h264解码完成的YUV回调
     *
     * @param data YUV数据
     * @param len  数据长度
     */
    private void onYUVFrame(byte[] data, int len) {
        if (this.l != null) {
            l.onYUVData(data, len);
        }
    }


}
