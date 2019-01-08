package netposa.pem.sdk;

import com.netposa.common.log.Log;

public class PEMSDK {

    private long mRealId = 0;

    public PEMSDK() {
        try {
            System.loadLibrary("PEMSDK");
        } catch (UnsatisfiedLinkError localUnsatisfiedLinkError) {
            try {
                System.load("/system/lib/libPEMSDK.so");
            } catch (UnsatisfiedLinkError e) {
                Log.e("PEMSDK", "Can't load library");
            }
        }
    }


    public native void Play(byte[] requestUrl, int streamMode, byte[] gatewap_ip, int gateway_port);

    public native void Pause();

    public native void Replay();

    public native void Seek(byte[] seektime);

    public native void Stop();


    public interface OnH264DataUpdateListener {
        void onH264DataUpdate(byte[] var1, int var2, int iskey);
    }

    public interface OnPtsUpdateListener {
        void onPtsUpdate(long pts, Boolean isVideo);
    }

    public interface OnMsgListener {
        void onMsg(int what, int arg1, int arg2);
    }

    private OnH264DataUpdateListener onH264DataUpdateListener;
    private OnPtsUpdateListener onPtsUpdateListener;
    private OnMsgListener onMsgListener;

    /**
     * 设置h264数据回调
     **/
    public void setOnH264DataUpdateListener(OnH264DataUpdateListener l) {
        this.onH264DataUpdateListener = l;
    }

    public void setOnPtsUpdateListener(OnPtsUpdateListener l) {
        this.onPtsUpdateListener = l;
    }

    public void setOnMsgListener(OnMsgListener l) {
        this.onMsgListener = l;
    }


    /**
     * 消息回调
     **/
    private void onPoseMsg(int what, int arg1, int arg2) {
        if (onMsgListener != null) {
            onMsgListener.onMsg(what, arg1, arg2);
        }
    }

    /**
     * h264视频数据回调
     **/
    private void onH264CallBack(byte[] buffer, int len, int iskeyframe, long pts) {
        if (onH264DataUpdateListener != null) {
            onH264DataUpdateListener.onH264DataUpdate(buffer, len, iskeyframe);
        }
        if (onPtsUpdateListener != null) {
            onPtsUpdateListener.onPtsUpdate(pts, true);
        }
    }

    public enum PTZCommand {
        /**
         * ��
         */
        CMD_RIGHT(0), // ��
        /**
         * ����
         */
        CMD_RIGHTUP(1), // ����
        /**
         * ��
         */
        CMD_UP(2), // ��
        /**
         * ����
         */
        CMD_LEFTUP(3), //
        /**
         * ��
         */
        CMD_LEFT(4), //
        /**
         * ����
         */
        CMD_LEFTDOWN(5), // ����
        /**
         * ��
         */
        CMD_DOWN(6), // ��
        /**
         * ����
         */
        CMD_RIGHTDOWN(7), // ����
        /**
         * ɨ��
         */
        CMD_SCAN(8), // ɨ��
        CMD_HALT(9), //
        /**
         * ��Ȧ param <0��Ȧ��С��>0��Ȧ�Ŵ�=0ֹͣ����
         */
        CMD_IRIS(10), // ��Ȧ param<0��Ȧ��С��>0��Ȧ�Ŵ�=0ֹͣ����
        /**
         * ���� param <0�����С(���ʱ�С),>0������(���ʱ��),=0ֹͣ����
         */
        CMD_ZOOM(11), // ���� param<0�����С(���ʱ�С),>0������(���ʱ��),=0ֹͣ����
        /**
         * ���� param<0������,>0����ǰ��,=0ֹͣ����
         */
        CMD_FOCUS(12), // ���� param<0������,>0����ǰ��,=0ֹͣ����
        /**
         * Ԥ��λ paramΪת��ԤԤ�õ�����
         */
        CMD_VIEW(13), // Ԥ��λ paramΪת��ԤԤ�õ�����
        /**
         * ����Ԥ��λ paramΪ����Ԥ�õ�����
         */
        CMD_SETVIEW(14), // ����Ԥ��λ paramΪ����Ԥ�õ�����
        /**
         * ����
         */
        CMD_AUX(15), // ����
        CMD_WASH(16), //
        CMD_WIPE(17), //
        /**
         * �ƹ�
         */
        CMD_LIGHT(18), // �ƹ�
        CMD_POWER(19), //
        CMD_FLASHBACK(20), //
        CMD_DEVKEY(21), //
        CMD_LOCKCAM(22), //
        CMD_UNLOCKCAM(23), //
        CMD_EXCLUSIVE(24), //
        CMD_INCLUSIVE(25), //
        CMD_GET_OPINFO(26), //
        CMD_CHANGE_MY_LEVEL(27), //
        PARAM_DEFAULT_ZOOM_IN(-5), //
        PARAM_DEFAULT_ZOOM_OUT(5), //
        PARAM_DEFAULT_SPEED(5), //
        PARAM_DEFAULT_STOP(0);//

        PTZCommand(int ni) {
            nativeInt = ni;
        }

        final int nativeInt;

        /**
         * ��ö��ת���ɶ�Ӧ��intֵ
         *
         * @return ��Ӧ��intֵ
         */
        public int toInt() {
            return this.nativeInt;
        }
    }


}
