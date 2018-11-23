package com.netposa.component.spjk.mvp.presenter;

import android.annotation.SuppressLint;
import android.app.Application;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import androidx.lifecycle.LifecycleOwner;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;

import com.netposa.common.log.Log;
import com.netposa.component.room.dao.DbHelper;
import com.netposa.component.room.entity.SpjkCollectionDeviceEntiry;
import com.netposa.component.room.entity.SpjkSearchHistoryEntity;
import com.netposa.component.spjk.mvp.contract.FollowDevicesContract;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;

import java.util.List;


@ActivityScope
public class FollowDevicesPresenter extends BasePresenter<FollowDevicesContract.Model, FollowDevicesContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    private final DbHelper mDbHelper;

    @Inject
    public FollowDevicesPresenter(FollowDevicesContract.Model model, FollowDevicesContract.View rootView) {
        super(model, rootView);
        mDbHelper = DbHelper.getInstance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    @SuppressLint("CheckResult")
    public void getAll() {
        mDbHelper.getAllCollectionDevice()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading("");//显示下拉刷新的进度条
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();//隐藏下拉刷新的进度条
                })
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe(new ErrorHandleSubscriber<List<SpjkCollectionDeviceEntiry>>(mErrorHandler) {
                    @Override
                    public void onNext(List<SpjkCollectionDeviceEntiry> response) {
                        Log.e(TAG, "getAll :" + response.toString());
                        mRootView.loadDataSuccess(response);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        Log.e(TAG, "loadDataForFirstTimeFail");
                        mRootView.loadDataFailed();
                    }

                });
    }
}
