/**
 * Copyright (C) 2016 LingDaNet.Co.Ltd. All Rights Reserved.
 */
package com.netposa.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.netposa.common.constant.GlobalConstants;
import com.netposa.common.log.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者：谢小明
 * 创建日期：2016/9/28
 * 邮箱：xiexm0902@lingdanet.com
 * 描述：Utils初始化相关
 */
public class Utils {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        Utils.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }
}
