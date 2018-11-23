package netposa.pem.sdk;

/**
 * 定义一组与用户Activity交互的方法
 */

public interface PemSdkListener {

    /**
     * 播放前，底层跑上来一些参数，
     * 例如播放pfs的视频长度，单位s，目前先加一个
     */
    void prePlay(int pfslen);

    /**
     * 获取开流的状态
     *
     * @param openState
     */
    void getOpenState(boolean openState);

    /**
     * 获取到的I420视频数据
     *
     * @param i420Data
     * @param width
     * @param height
     */
    void getYUVI420(byte[] i420Data, int width, int height);

    /**
     * 播放PFS录像的进度更新回调
     *
     * @param progress
     */
    void onSeekUpdate(int progress);

    /**
     * 播放速度的回调
     */
    void onSpeedUpdate(int speed);

    /**
     * 错误回调
     */
    void onError(int what, int arg1, int arg2);
}
