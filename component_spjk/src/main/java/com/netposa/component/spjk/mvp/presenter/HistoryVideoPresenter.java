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
import com.netposa.component.spjk.mvp.contract.HistoryVideoContract;
import com.netposa.component.spjk.mvp.model.entity.HistoryVideoRequestEntity;
import com.netposa.component.spjk.mvp.model.entity.HistoryVideoResponseEntity;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;

import static com.netposa.common.constant.GlobalConstants.PAGE_SIZE_DEFAULT;


@ActivityScope
public class HistoryVideoPresenter extends BasePresenter<HistoryVideoContract.Model, HistoryVideoContract.View> {
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
    HistoryVideoRequestEntity mRequestEntity;
    private HistoryVideoRequestEntity.PageInfoBean mPageBean;

    @Inject
    public HistoryVideoPresenter(HistoryVideoContract.Model model, HistoryVideoContract.View rootView) {
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


    public void getCameraHistoryList(long startTime, long endTime, String cameraId, int page) {
        Log.i(TAG, "start to getNeighbouringDevice");
        if (!NetworkUtils.isConnected()) {
            mRootView.showMessage(mContext.getString(R.string.network_disconnect));
            mRootView.hideLoading();
            return;
        }
        mRequestEntity.setBeginTime(startTime);
        mRequestEntity.setEndTime(endTime);
        mRequestEntity.setCameraId(cameraId);
        mPageBean = new HistoryVideoRequestEntity.PageInfoBean();
        mPageBean.setCurrentPage(page);
        mPageBean.setPageSize(PAGE_SIZE_DEFAULT);
        mModel.getlistHistoryVideo(mRequestEntity)
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
                .subscribe(new ErrorHandleSubscriber<HistoryVideoResponseEntity>(mErrorHandler) {
                    @Override
                    public void onNext(HistoryVideoResponseEntity responseEntity) {
                        Log.d(TAG, "getCameraHistoryList:" + responseEntity);
                        mRootView.getVideoSucess(responseEntity);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        Log.d(TAG, "getCameraHistoryList error");
                        mRootView.getVideoFAiled();
                    }
                });

    }
}
