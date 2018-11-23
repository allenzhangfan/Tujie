package com.netposa.component.spjk.mvp.presenter;

import android.app.Application;
import android.content.Context;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import androidx.lifecycle.LifecycleOwner;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import javax.inject.Inject;

import com.netposa.common.log.Log;
import com.netposa.common.utils.NetworkUtils;
import com.netposa.component.spjk.R;
import com.netposa.component.spjk.mvp.contract.DevicelistContract;
import com.netposa.component.spjk.mvp.model.entity.SpjkListDeviceRequestEntity;
import com.netposa.component.spjk.mvp.model.entity.SpjkListDeviceResponseEntity;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
@ActivityScope
public class DevicelistPresenter extends BasePresenter<DevicelistContract.Model, DevicelistContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    Context mContext;
    @Inject
    SpjkListDeviceRequestEntity mRequestEntity;

    @Inject
    public DevicelistPresenter(DevicelistContract.Model model, DevicelistContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void getSpjkDevicelist(String id) {
        if (!NetworkUtils.isConnected()) {
            mRootView.showMessage(mContext.getString(R.string.network_disconnect));
            return;
        }
        mRequestEntity.setId(id);
        mModel.getDevicelist(mRequestEntity)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> mRootView.showLoading(""))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (mRootView != null) mRootView.hideLoading();
                })
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe(new ErrorHandleSubscriber<SpjkListDeviceResponseEntity>(mErrorHandler) {
                    @Override
                    public void onNext(SpjkListDeviceResponseEntity responseEntity) {
                        Log.d("responseEntity", responseEntity.toString());
                        mRootView.getListSuccess(responseEntity);
                    }

                    @Override
                    public void onError(Throwable t) {
                        mRootView.getListFailed();
                    }
                });
    }
}
