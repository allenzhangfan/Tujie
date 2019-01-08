package netposa.pem.sdk;

import java.util.LinkedList;

public class VideoHandle {

    public interface onstream {
        void _onstream(byte[] data, int len, boolean iskey, long pts);
    }

    private onstream l;
    private Object mLock = new Object();
    private LinkedList<VideoFrame> cache;
    private LVPIC timer;
    private boolean bcrun = false;
    private int MaxCacheSize = 40;

    public VideoHandle(onstream _l) {
        this.l = _l;
    }

    public void onStart() {
        startCacheThread();
    }

    public void release() {
        stopCacheThread();
    }

    public void onStream(byte[] data, int dataLen, boolean isKey) {
        byte[] tmp = new byte[dataLen];
        System.arraycopy(data, 0, tmp, 0, dataLen);
        Encache(new VideoFrame(tmp, isKey));
    }

    private void Encache(VideoFrame stream) {
        synchronized (mLock) {
            if (cache != null) {
                cache.addLast(stream);
                timer.AddOne();
            }
        }
    }


    private void stopCacheThread() {
        setRun(false);
        synchronized (mLock) {
            if (cache != null) {
                for (VideoFrame frame : cache) {
                    frame.buffer = null;
                }

                cache.clear();
                cache = null;
            }
        }
        if (timer != null) {
            timer.Reset();
            timer = null;
        }
    }

    private void setRun(boolean isrun) {
        synchronized (mLock) {
            bcrun = isrun;
        }
    }

    private boolean isRun() {
        synchronized (mLock) {
            return bcrun;
        }
    }


    private void startCacheThread() {
        stopCacheThread();
        Runnable run = new Runnable() {

            @Override
            public void run() {
                boolean hasIFrame = false;
                while (isRun()) {
                    synchronized (mLock) {
                        if (timer != null && cache != null) {
                            //Log.i("pemsdk","cache.size() = "+cache.size()+"");
                            if (cache.size() > 0 && timer.CanPlayOne()) { // 平滑
                                VideoFrame s;
                                if (cache.size() > MaxCacheSize) {// 缓冲MaxCacheSize帧以上开始丢帧
                                    hasIFrame = false;
                                    for (int i = 0; i < cache.size(); i++) {
                                        VideoFrame v = cache.get(i);
                                        if (v.isIFrame) {
                                            hasIFrame = true;
                                            break;
                                        }
                                    }
                                    if (hasIFrame) {
                                        do {
                                            s = cache.poll();
                                        } while (s != null && !s.isIFrame);
                                    } else {
                                        s = cache.poll();
                                    }
                                } else {
                                    s = cache.poll();
                                }
                                if (s != null && timer != null) {
                                    if (l != null) {
                                        l._onstream(s.buffer, s.buffer.length,
                                                s.isIFrame,
                                                timer.getTimeStampMillis());
                                        s.buffer = null;
                                    }
                                }
                            }
                        }
                    }
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        setRun(true);
        cache = new LinkedList<VideoFrame>();
        timer = new LVPIC();
        new Thread(run).start();
    }

    private class IntervalC // 计数平均间隔时间计算器
    {
        public IntervalC() {
            m_span = 10 * 1000;// 默认10秒
            adds = new LinkedList<Long>();
        }

        public void SetCalTime(long span) // 设置平均间隔计算时间
        {
            m_span = span;
        }

        long m_span;
        LinkedList<Long> adds;

        public void AddOne()// 添加一个计数
        {
            long t = System.currentTimeMillis();
            //System.out.println("timer add one is:"+t);
            adds.addLast(t);
            while (adds.size() > 0 && (t - adds.getFirst() > m_span)) {
                adds.removeFirst();
            }

        }

        public void Reset()// 重置
        {
            adds.clear();
        }

        public long GetInterval()// 获取平均间隔时间
        {
            if (adds.size() < 2) {
                return 0;
            }

            //long t = System.currentTimeMillis();
            long t = adds.getLast();
            t = t - adds.getFirst();
            t = t / adds.size();
            if (t < 0) {
                return 0;
            }
            if (t > 100) {
                t = 100;
            }

            t -= 1;
            return t;
        }
    }

    private class LVPIC {
        public LVPIC() {
            m_c = new IntervalC();
            m_lpt = 0;
            m_startTime = System.currentTimeMillis();
        }

        public void SetCalTime(long span) {
            m_c.SetCalTime(span);
        }

        public void AddOne() {
            m_c.AddOne();
        }

        public void Reset() {
            m_lpt = 0;
            m_startTime = 0;
            m_c.Reset();
        }

        public boolean CanPlayOne() {
            long lt = System.currentTimeMillis();
            if (m_lpt > lt) {
                m_lpt = lt;
            }
            if (lt < m_lpt + m_c.GetInterval())//* 9 / 10)// 以1.1倍速度播放
            {
                return false;
            }
            m_lpt = lt;
            return true;
        }

        public long getTimeStampMillis() {
            return m_lpt;
        }

        long m_lpt;
        IntervalC m_c;
        long m_startTime;
    }

    public class VideoFrame {
        public boolean isIFrame;
        public byte[] buffer;

        public VideoFrame(byte[] buffer, boolean isIFrame) {
            this.isIFrame = isIFrame;
            this.buffer = buffer;
        }
    }
}