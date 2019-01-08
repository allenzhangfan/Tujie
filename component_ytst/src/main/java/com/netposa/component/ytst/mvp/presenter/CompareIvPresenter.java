package com.netposa.component.ytst.mvp.presenter;

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

import com.netposa.common.entity.FeatureByPathRequestEntity;
import com.netposa.common.entity.FeatureByPathResponseEntity;
import com.netposa.common.log.Log;
import com.netposa.common.utils.NetworkUtils;
import com.netposa.component.ytst.R;
import com.netposa.component.ytst.mvp.contract.CompareIvContract;
import com.netposa.component.ytst.mvp.model.entity.CarDetailRequestEntity;
import com.netposa.component.ytst.mvp.model.entity.CarDetailResponseEntity;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;

import java.util.ArrayList;
import java.util.List;


@ActivityScope
public class CompareIvPresenter extends BasePresenter<CompareIvContract.Model, CompareIvContract.View> {
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
    CarDetailRequestEntity entity;
    @Inject
    FeatureByPathRequestEntity mFeatureByPathRequestEntity;

    @Inject
    public CompareIvPresenter(CompareIvContract.Model model, CompareIvContract.View rootView) {
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

    public void getDetailVehicleInfo(String recordId) {
        if (!NetworkUtils.isConnected()) {
            mRootView.showMessage(mContext.getString(R.string.network_disconnect));
            mRootView.hideLoading();
            return;
        }
        List<String> mData = new ArrayList<>();
        mData.add(recordId);
        entity.setRecordId(mData);
        mModel.getDetailVehicleInfo(entity)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> mRootView.showLoading(""))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (mRootView != null) mRootView.hideLoading();
                })
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe(new ErrorHandleSubscriber<List<CarDetailResponseEntity>>(mErrorHandler) {
                    @Override
                    public void onNext(List<CarDetailResponseEntity> responseEntity) {
                        Log.d("getDetailVehicleInfo：", responseEntity.toString());
                        mRootView.getDetailVehicleInfoSuc(responseEntity);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.d("getDetailVehicleInfo: ", t.toString());
                        mRootView.getDetailVehicleInfoFail();
                    }
                });
    }

    public void getDetectImgFeatureByPath() {
        if (!NetworkUtils.isConnected()) {
            mRootView.showMessage(mContext.getString(com.netposa.component.pic.R.string.network_disconnect));
            mRootView.hideLoading();
            return;
        }
        mModel.getDetectImgFeatureByPath(mFeatureByPathRequestEntity)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading("");//显示下拉刷新的进度条
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();//隐藏下拉刷新的进度条
                })
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe(new ErrorHandleSubscriber<FeatureByPathResponseEntity>(mErrorHandler) {
                    @Override
                    public void onNext(FeatureByPathResponseEntity response) {
                        mRootView.getDetectImgFeatureByPathSucess(response);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.getDetectImgFeatureByPathFail();
                    }
                });
    }
}
