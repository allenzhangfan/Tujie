package netposa.pem.sdk;

import android.opengl.GLSurfaceView;
import android.text.TextUtils;
import android.util.Log;

import com.netposa.common.constant.UrlConstant;
import com.netposa.mp4v2.mp4encoder;

import java.io.UnsupportedEncodingException;

import netposa.pem.sdk.decode.DecodeManager;
import netposa.pem.sdk.opengl.GLFrameRenderer;

public class PemSdkManager implements
        PEMSDK.OnMsgListener,
        PEMSDK.OnPtsUpdateListener,
        PEMSDK.OnH264DataUpdateListener,
        VideoHandle.onstream {

    private static final String TAG = PemSdkManager.class.getSimpleName();

    public final static int PEM_MSG_PLAY_SUCCESS = 900;
    public final static int PEM_MSG_PLAY_START = 1001;
    public final static int PEM_MSG_PLAY_PAUSE = 1002;
    public final static int PEM_MSG_PLAY_REPLAY = 1003;
    public final static int PEM_MSG_PLAY_SEEK = 1004;
    public final static int PEM_MSG_PLAY_RELEASE = 1005;

    public final static int PEM_MSG_ERROR_CALLBACK = 2001;
    public final static int SOCKETERROR_CONNECTCLOSE = -1002;
    public final static int SOCKETERROR_TIMEOUT = -1003;
    public final static int SOCKETERROR = -1005;

    private PEMSDK mPemsdk;

    public enum playStatus {
        play_success,
        play_failture,
        play_pause,
        play_seek,
        play_stop
    }

    private GLFrameRenderer mGLFRenderer;
    private GLSurfaceView mGLsurfaceview;
    private DecodeManager mDecodeManger;
    private PemSdkListener mListener;
    private VideoSpeedController mVideoSpeedController;
    private VideoProgressController mVideoProgressController;
    private boolean mIsFirstPts;
    private long mFirstPts;
    private long mCurrentPts;

    private int streamMode = -1;

    public boolean isCreateMp4Handle = false;

    private mp4encoder _mp4encoder;

    public playStatus _playStatus;

    private VideoHandle videoHandle;

    private PemSdkManager() {
    }

    public static PemSdkManager newInstance() {
        return new PemSdkManager();
    }

    /**
     * @param listener
     * @brief 初始化
     */
    public void init(PemSdkListener listener) {
        mListener = listener;
        // 初始化解码器
        mDecodeManger = new DecodeManager(listener);

        mPemsdk = new PEMSDK();
        mPemsdk.setOnMsgListener(this);
        mPemsdk.setOnPtsUpdateListener(this);
        mPemsdk.setOnH264DataUpdateListener(this);
        _mp4encoder = new mp4encoder();
        videoHandle = new VideoHandle(this);
    }

    public void setmGLsurfaceview(GLSurfaceView view) {
        this.mGLsurfaceview = view;
        // 初始化Render
        mGLsurfaceview.setEGLContextClientVersion(2);
        mGLFRenderer = new GLFrameRenderer(mGLsurfaceview);
        mGLsurfaceview.setRenderer(mGLFRenderer);
    }

    /**
     * 播放
     *
     * @param url          播放地址
     * @param streamMode   0 --标准rtsp 1 --非标准rtsp
     * @param gateway_ip   网关ip pvg+用
     * @param gateway_port 网关地址 pvg+用
     **/
    public void play(String url, int streamMode, String gateway_ip, int gateway_port) {
        if (TextUtils.isEmpty(url)) {
            Log.e(TAG, "url is null ,please check !");
            return;
        }
        url = UrlConstant.parsePlayUrl(url);
        Log.e(TAG, "pemsdk call play url->" + url);
        this.streamMode = streamMode;
        byte[] b_url, b_gip = null;
        try {
            b_url = url.getBytes("gb2312");
            b_gip = gateway_ip.getBytes("gb2312");

        } catch (UnsupportedEncodingException e) {
            return;
        }
        mPemsdk.Play(b_url, streamMode, b_gip, gateway_port);
    }

    /**
     * 暂停(实时视频不能暂停，历史视频可以暂停)
     **/
    public void pause() {
        Log.e(TAG, "pemsdk call pause !");
        mPemsdk.Pause();
    }

    /**
     * 恢复播放
     **/
    public void replay() {
        Log.e(TAG, "pemsdk call replay !");
        mPemsdk.Replay();
    }

    /**
     * 拖动
     *
     * @param seektime 要拖动到的时间
     **/
    public void seek(String seektime) {
        Log.e(TAG, "pemsdk call seek !");
        byte[] b_seektime = null;
        try {
            b_seektime = seektime.getBytes("gb2312");

        } catch (UnsupportedEncodingException e) {
            return;
        }
        mPemsdk.Seek(b_seektime);
    }

    /**
     * 退出播放调用
     **/
    public void stop() {
        Log.e(TAG, "pemsdk call stop!");
        mPemsdk.Stop();
    }

    /**
     * 创建mp4文件
     **/
    public int createMp4Handle(String path) {
        int ret = _mp4encoder.InitMp4File(path);
        if (ret == 0) {
            isCreateMp4Handle = true;
        } else {
            isCreateMp4Handle = false;
        }
        return ret;
    }

    /**
     * 视频录制结束，关闭
     **/
    public void closeMp4Handle() {
        isCreateMp4Handle = false;
        _mp4encoder.close();
    }


    /***
     * 拿到I420视频数据，进行OpenGL渲染
     */
    public void renderVideo(byte[] yuvData, int width, int height) {
        mGLFRenderer.openGlDraw(yuvData, width, height);
    }

    /**
     * 当视频分辨率变化时，重置
     */
    public void ResetrenderSolution() {
        mGLFRenderer.ResetSolution();
    }


    /**
     * 开始获取实时视频播放速度
     */
    public void startGetSpeed() {
        if (mVideoSpeedController == null) {
            mVideoSpeedController = new VideoSpeedController(mListener);
            mVideoSpeedController.start();
        }
    }

    /**
     * 停止获取实时视频播放速度的线程
     */
    public void stopGetSpeed() {
        if (mVideoSpeedController != null) {
            mVideoSpeedController.setIsRunning(false);
            mVideoSpeedController = null;
        }
    }

    /**
     * 停止获取录像播放进度的线程
     */
    public void stopGetSeek() {
        if (mVideoProgressController != null) {
            mVideoProgressController.setIsRunning(false);
        }
    }

    @Override
    public void _onstream(byte[] data, int len, boolean iskey, long pts) {
        mDecodeManger.startDecode(data, len);
    }


    /**
     * 手动或抛异常时调用此方法，停止解码
     */
    public void stopDecoder() {
        Log.d(TAG, "pemsdk call stopDecoder !");
        mDecodeManger.closeDecode();
    }


    public void clearRenderBuffer() {
        mGLFRenderer.clear();
    }

    /**
     * 释放资源
     */
    private void release() {
        Log.d(TAG, "pemsdk call release !");
        // 关闭解码
        stopDecoder();
        // 关闭获取视频速度的线程
        stopGetSpeed();
        // 关闭获取视频进度的线程
        stopGetSeek();
        clearRenderBuffer();
        videoHandle.release();
    }


    @Override
    public void onMsg(int what, int arg1, int arg2) {
        Log.i(TAG, "pemsdk get stream msg from native:" + what + ",arg1:" + arg1 + ",arg2:" + arg2);
        switch (what) {
            case PEM_MSG_PLAY_START:
                if (arg1 != PEM_MSG_PLAY_SUCCESS) {
                    _playStatus = playStatus.play_failture;
                    mListener.onError(what, arg1, arg2);
                } else {
                    videoHandle.onStart();
                    if (!mDecodeManger.isOpen) {
                        mDecodeManger.initDecoder();
                    }
                    mIsFirstPts = false;
                    _playStatus = playStatus.play_success;
                    mListener.prePlay(arg2);
                }
                break;
            case PEM_MSG_PLAY_PAUSE:
                break;
            case PEM_MSG_PLAY_REPLAY:
                break;
            case PEM_MSG_PLAY_SEEK:
                break;
            case PEM_MSG_PLAY_RELEASE:
                if (_playStatus == playStatus.play_success || _playStatus == playStatus.play_failture) {
                    _playStatus = playStatus.play_stop;
                    release();
                }
                break;
            case PEM_MSG_ERROR_CALLBACK:
                mListener.onError(what, arg1, arg2);
                break;
        }

    }


    @Override
    public void onH264DataUpdate(byte[] bytes, int i, int iskey) {
        if (mVideoSpeedController != null) {
            mVideoSpeedController.setLen(i);
        }

        boolean iskeyframe = false;
        if (iskey == 1) {
            iskeyframe = true;
        }
        videoHandle.onStream(bytes, i, iskeyframe);
        if (isCreateMp4Handle) {
            _mp4encoder.writeh264(bytes, i);
        }
    }

    @Override
    public void onPtsUpdate(long pts, Boolean isVideo) {
        if (!mIsFirstPts) {
            mVideoProgressController = new VideoProgressController(mListener);
            if (this.streamMode == 0) {
                mVideoProgressController.setTimeTick(90000);
            } else {
                mVideoProgressController.setTimeTick(1000);
            }
            mVideoProgressController.setFirstPTS(pts);
            mVideoProgressController.start();
            mIsFirstPts = true;
            mFirstPts = pts;
        }
        mCurrentPts = pts;
        mVideoProgressController.setCurrentPTS(mCurrentPts);
    }
}
