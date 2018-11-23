package netposa.pem.sdk;

/**
 * 获取实时视频的速度
 */
public class VideoSpeedController extends Thread {

    private boolean mIsRunning = true;
    private PemSdkListener mListener;
    private int len = 0;

    public VideoSpeedController(PemSdkListener listener) {
        this.mListener = listener;
    }

    public void setIsRunning(boolean isRunning) {
        this.mIsRunning = isRunning;
    }

    public boolean getIsRunning() {
        return this.mIsRunning;
    }

    public void setLen(int l) {
        this.len += l;
    }

    @Override
    public void run() {
        try {
            while (mIsRunning) {
                mListener.onSpeedUpdate(VideoSpeedController.this.len);
                this.len = 0;
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
