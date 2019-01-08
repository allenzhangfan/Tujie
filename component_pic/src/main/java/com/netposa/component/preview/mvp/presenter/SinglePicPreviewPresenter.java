package com.netposa.component.preview.mvp.presenter;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.netposa.common.utils.NetworkUtils;
import com.netposa.component.pic.R;
import com.netposa.component.preview.mvp.contract.SinglePicPreviewContract;
import com.netposa.component.preview.mvp.model.entity.FeatureByPathRequestEntity;
import com.netposa.component.preview.mvp.model.entity.FeatureByPathResponseEntity;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;

import javax.inject.Inject;

import androidx.lifecycle.LifecycleOwner;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;


@ActivityScope
public class SinglePicPreviewPresenter extends BasePresenter<SinglePicPreviewContract.Model, SinglePicPreviewContract.View> {
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
    FeatureByPathRequestEntity mFeatureByPathRequestEntity;

    @Inject
    public SinglePicPreviewPresenter(SinglePicPreviewContract.Model model, SinglePicPreviewContract.View rootView) {
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

    public void getDetectImgFeatureByPath() {
        if (!NetworkUtils.isConnected()) {
            mRootView.showMessage(mContext.getString(R.string.network_disconnect));
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
                        String message=t.getMessage();
                        if (!TextUtils.isEmpty(message)){
                            mRootView.getDetectImgFeatureByPathFail(message);
                        }
                    }
                });
    }
}
