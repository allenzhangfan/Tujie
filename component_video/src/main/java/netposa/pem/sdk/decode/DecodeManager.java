package netposa.pem.sdk.decode;

import com.netposa.common.log.Log;

import com.netposa.ffmpeg.decoder.h264;
import com.netposa.ffmpeg.decoder.idrinfo;
import com.netposa.ffmpeg.ffmpegkit;

import java.io.IOException;

import netposa.pem.sdk.PemSdkListener;
import netposa.pem.sdk.PemSdkUtils;

public class DecodeManager {

    private static final String TAG = DecodeManager.class.getSimpleName();

    private static final int HardMode = 0;
    private static final int FFmegMode = 1;

    private PemSdkListener mListener;
    //知否支持硬解
    private boolean mSupportHardDecode;
    private int mCurrentDeceodeMode = -1;
    //是否第一次开启
    private boolean mIsFirstFlag;

    private int mVideoWidth;
    private int mVideoHeight;
    //视频尺寸是否发生改变
    private boolean mIsvideosizeChange;

    private HardDecoder mHardDecoder;

    private ffmpegkit mFFkit;
    private h264.ffmpeg_decoder_h264_callback mFFmpegCallBack;

    /**
     * 解码器状态
     **/
    private boolean mIsOpen;

    public boolean isOpen() {
        return mIsOpen;
    }

    public void setOpen(boolean open) {
        mIsOpen = open;
    }

    public DecodeManager(PemSdkListener listener) {
        this.mIsFirstFlag = true;
        this.mListener = listener;
        this.mSupportHardDecode = PemSdkUtils.isSupportMediaCodecHardDecoder() && (PemSdkUtils.getSdkVersion() >= 21);
        this.mSupportHardDecode = false;
        mFFkit = new ffmpegkit();
        initDecoder();
    }

    public void initDecoder() {
        if (mSupportHardDecode) {
            mCurrentDeceodeMode = HardMode;
            mHardDecoder = new HardDecoder(mListener, mVideoWidth, mVideoHeight);
            mIsOpen = true;
        } else {
            mCurrentDeceodeMode = FFmegMode;
            try {
                initFFmpegDecode();
                if (mFFkit.m_h264.open() == 0) {
                    mIsOpen = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.i(TAG, "initDecoder decodeManger isOpen:" + mIsOpen);
    }

    /**
     * 解码
     *
     * @param bytes
     * @param len
     */
    public void startDecode(byte[] bytes, int len) {
        Log.e(TAG, "startDecode--->");
        if (mSupportHardDecode) {
            hardDecode(bytes, len);
        } else {
            ffmpegDecode(bytes, len);
        }
    }

    /**
     * 硬解
     */
    private void hardDecode(byte[] bytes, int len) {
        if (mIsFirstFlag) {
            mIsFirstFlag = false;
            try {
                initFFmpegSize(bytes, len);
                mHardDecoder.initial(bytes, len);
                mListener.getOpenState(true);
            } catch (IOException e) {
                mListener.getOpenState(false);
            }
        }
        mHardDecoder.setVideoData(bytes);
    }


    private void initFFmpegSize(byte[] bytes, int len) {
        idrinfo.ffmpeg_decoder_idrinfo_callback idrcb = new idrinfo.ffmpeg_decoder_idrinfo_callback() {
            @Override
            public void onIDRInfo(int width, int height, byte[] buffer, int len) {
                mVideoWidth = width;
                mVideoHeight = height;
            }
        };
        mFFkit.ffmpegkit_decoder_idr(idrcb);
        boolean isSupport = mFFkit._idrinfo.decoder(bytes, len);
        if (!isSupport) {
            throw new RuntimeException("无法获取I帧信息类");
        }
    }

    /**
     * 软解
     */
    private void ffmpegDecode(byte[] bytes, int len) {
        if (mIsFirstFlag) {
            mIsFirstFlag = false;
            mListener.getOpenState(true);
        }
        //实时视频返回再继续播放可能会解码失败(此处有bug)
        mFFkit.m_h264.decode(bytes, len);
    }

    /**
     * 初始化ffmpeg
     */
    private void initFFmpegDecode() {
        mFFmpegCallBack = new h264.ffmpeg_decoder_h264_callback() {
            @Override
            public void onVideoSize(int width, int height) {
                if (!mIsvideosizeChange) {
                    mIsvideosizeChange = true;
                    mVideoWidth = width;
                    mVideoHeight = height;
                }
            }

            @Override
            public void onYUVData(byte[] data, int len) {
                mListener.getYUVI420(data, mVideoWidth, mVideoHeight);
            }
        };
        mFFkit.ffmpegkit_decoder_h264(mFFmpegCallBack);

    }

    public boolean ismSupportHardDecode() {
        return mSupportHardDecode;
    }

    /**
     * 关闭转码
     */
    public void closeDecode() {
        mIsFirstFlag = true;
        mIsvideosizeChange = false;
        mIsOpen = false;
        switch (mCurrentDeceodeMode) {
            case HardMode:
                mHardDecoder.stopDecode();
                break;
            case FFmegMode:
                mFFkit.m_h264.closeh264();
                break;
            default:
                mFFkit.m_h264.closeh264();
                break;
        }
    }
}
