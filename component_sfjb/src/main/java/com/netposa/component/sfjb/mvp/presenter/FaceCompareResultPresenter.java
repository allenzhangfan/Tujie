package com.netposa.component.sfjb.mvp.presenter;

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
import com.netposa.component.sfjb.R;
import com.netposa.component.sfjb.mvp.contract.FaceCompareResultContract;
import com.netposa.component.sfjb.mvp.model.entity.FaceCompareRequestEntity;
import com.netposa.component.sfjb.mvp.model.entity.FaceCompareResponseEntity;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;


@ActivityScope
public class FaceCompareResultPresenter extends BasePresenter<FaceCompareResultContract.Model, FaceCompareResultContract.View> {
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
    public FaceCompareResultPresenter(FaceCompareResultContract.Model model, FaceCompareResultContract.View rootView) {
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

    /**
     * 根据条件获取人脸比对结果
     */
    public void getFaceCompareResult(FaceCompareRequestEntity entity) {
        if (!NetworkUtils.isConnected()) {
            mRootView.showMessage(mContext.getString(R.string.network_disconnect));
            mRootView.hideLoading();
            return;
        }
        mModel.getFaceCompareResult(entity)
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
                .subscribe(new ErrorHandleSubscriber<FaceCompareResponseEntity>(mErrorHandler) {
                    @Override
                    public void onNext(FaceCompareResponseEntity response) {
                        Log.d(TAG, response.toString());
                        mRootView.showCompareFaceDetailSuccess(response);
                    }
                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.showCompareFaceDetailFailed();
                    }
                });
    }
}
