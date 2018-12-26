package com.netposa.commonres.app;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.netposa.common.core.ActivityLifecycleCallbacksImpl;
import com.netposa.common.log.Log;
import com.yalantis.ucrop.UCropActivity;

public class ActivityLifecycleImpl extends ActivityLifecycleCallbacksImpl {
    private static final String TAG = ActivityLifecycleImpl.class.getSimpleName();
    //打开的Activity数量统计
    private int activityStartCount = 0;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (UCropActivity.class.isAssignableFrom(activity.getClass())) {
            if (Build.VERSION.SDK_INT >= 23) {
                activity.getWindow().getDecorView()
                        .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        activityStartCount++;
        //数值从0变到1说明是从后台切到前台
        if (activityStartCount == 1) {
            Log.d(TAG, "-----------------app switch to foreground-----------------");
            //从后台切到前台
//            if (activity.getClass().getSimpleName().equals("HomeActivity")) {
//                //检测app更新
//                EventBus.getDefault().post(new AppUpdateEnity());
//            }
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        activityStartCount--;
        //数值从1到0说明是从前台切到后台
        if (activityStartCount == 0) {
            Log.d(TAG, "-----------------app switch to background-----------------");
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
}
