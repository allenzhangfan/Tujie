package com.netposa.common.alive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.lang.ref.WeakReference;

/**
 * 锁屏后1像素保活工具类
 */

public class SinglePixelUtil {

    private Context mContext;
    private static SinglePixelUtil mSinglePixelUtil;
    // 使用弱引用，防止内存泄漏
    private WeakReference<Activity> mActivityRef;

    private SinglePixelUtil(Context mContext) {
        this.mContext = mContext;
    }

    // 单例模式
    public static SinglePixelUtil getInstance(Context context) {
        if (mSinglePixelUtil == null) {
            mSinglePixelUtil = new SinglePixelUtil(context);
        }
        return mSinglePixelUtil;
    }

    // 获得SinglePixelActivity的引用
    public void setSingleActivity(Activity mActivity) {
        mActivityRef = new WeakReference<>(mActivity);
    }

    // 启动SinglePixelActivity
    public void startActivity() {
        Intent intent = new Intent(mContext, SinglePixelActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    // 结束SinglePixelActivity
    public void finishActivity() {
        if (mActivityRef != null) {
            Activity mActivity = mActivityRef.get();
            if (mActivity != null) {
                mActivity.finish();
            }
        }
    }
}
