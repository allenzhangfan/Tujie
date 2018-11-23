/**
 * Copyright (C) 2016 LingDaNet.Co.Ltd. All Rights Reserved.
 */
package com.netposa.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.netposa.common.constant.CommonConstant;

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
        initDir();
    }

    private static void initDir() {
        List<String> pathList = new ArrayList<>();
        pathList.add(CommonConstant.ROOT_PATH);
        pathList.add(CommonConstant.DB_PATH);
        pathList.add(CommonConstant.LOG_PATH);
        pathList.add(CommonConstant.DOWNLOAD_PATH);
        pathList.add(CommonConstant.CACHE_PATH);
        pathList.add(CommonConstant.PICTURE_PATH);
        pathList.add(CommonConstant.VIDEO_PATH);

        List<Boolean> mkdirResults = new ArrayList<>(pathList.size());
        Observable
                .fromIterable(pathList)
                .flatMap((Function<String, ObservableSource<Boolean>>) path -> {
                    boolean mkdirResult = FileUtils.createOrExistsDir(path);
                    Log.i("Utils", "flatMap:" + path + ",mkdirResult:" + mkdirResult);
                    mkdirResults.add(mkdirResult);
                    return Observable.fromIterable(mkdirResults);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean result) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e("Utils", "mkdirs onComplete");
                    }
                });
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
