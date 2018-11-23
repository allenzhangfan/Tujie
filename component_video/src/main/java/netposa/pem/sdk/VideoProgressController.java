package netposa.pem.sdk;

/**
 * 获取pts线程
 * 负责刷新当前播放时间及seekbar进度
 */
public class VideoProgressController extends Thread {
    private boolean mIsRunning = true;
    private PemSdkListener mListener;
    private long firstPTS, currentPTS;

    private int tick = 1000;

    public void setTimeTick(int t) {
        this.tick = t;
    }

    public VideoProgressController(PemSdkListener listener) {
        this.mListener = listener;
    }

    public void setFirstPTS(long firstPTS) {
        this.firstPTS = firstPTS;
    }

    public void setCurrentPTS(long currentPTS) {
        this.currentPTS = currentPTS;
    }

    public void setIsRunning(boolean isRunning) {
        this.mIsRunning = isRunning;
    }

    public boolean getIsRunning() {
        return this.mIsRunning;
    }

    @Override
    public void run() {
        try {
            while (mIsRunning) {
                mListener.onSeekUpdate((int) (currentPTS - firstPTS) / tick);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
