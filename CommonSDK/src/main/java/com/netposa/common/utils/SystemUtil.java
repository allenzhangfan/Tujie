package com.netposa.common.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

import java.util.List;

public class SystemUtil {

    private static final String TAG = SystemUtil.class.getSimpleName();

    //判断是否是快速点击(双击)，保证多次点击只响应一次点击事件
    private long lastClickTime = 0L; //上一次点击的时间

    public static String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> rapis = am.getRunningAppProcesses();
        if (rapis == null) {
            rapis = am.getRunningAppProcesses();
        }
        if (rapis != null) {
            for (RunningAppProcessInfo rapi : rapis) {
                if (rapi.pid == android.os.Process.myPid()) {
                    return rapi.processName;
                }
            }
        }
        return null;
    }

    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (timeD < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
