package com.netposa.common.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

import java.util.List;

public class SystemUtil {

    private static final String TAG = SystemUtil.class.getSimpleName();

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

}
