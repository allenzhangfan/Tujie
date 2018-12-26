package com.netposa.component.clcx.mvp.presenter;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import javax.inject.Inject;

import com.netposa.common.log.Log;
import com.netposa.common.utils.NetworkUtils;
import com.netposa.component.clcx.R;
import com.netposa.component.clcx.mvp.contract.ViewCarPathContract;
import com.netposa.component.clcx.mvp.model.entity.QueryCarSearchResponseEntity;
import com.netposa.component.clcx.mvp.model.entity.VehicleTrackRequestEntity;
import com.netposa.component.clcx.mvp.model.entity.VehicleTrackResponseEntity;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;

import java.util.List;


@ActivityScope
public class ViewCarPathPresenter extends BasePresenter<ViewCarPathContract.Model, ViewCarPathContract.View> {
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
    public ViewCarPathPresenter(ViewCarPathContract.Model model, ViewCarPathContract.View rootView) {
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

    public void getVehicleTrack(VehicleTrackRequestEntity entity) {
        if (!NetworkUtils.isConnected()) {
            mRootView.showMessage(mContext.getString(R.string.network_disconnect));
            mRootView.hideLoading();
            return;
        }
        mModel.getVehicleTrack(entity)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> mRootView.showLoading(""))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (mRootView != null) mRootView.hideLoading();
                })
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe(new ErrorHandleSubscriber<VehicleTrackResponseEntity>(mErrorHandler) {
                    @Override
                    public void onNext(VehicleTrackResponseEntity responseEntity) {
                        Log.d("getVehicleTrack", responseEntity.toString());
                        mRootView.getListSuccese(responseEntity);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.d("Throwable", t.toString());
                        mRootView.getListFailed();
                    }
                });
    }
}