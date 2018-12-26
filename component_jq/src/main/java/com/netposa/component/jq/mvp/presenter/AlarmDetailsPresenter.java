package com.netposa.component.jq.mvp.presenter;

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
import com.netposa.common.constant.GlobalConstants;
import com.netposa.common.log.Log;
import com.netposa.common.utils.NetworkUtils;
import com.netposa.common.utils.SPUtils;
import com.netposa.component.jq.R;
import com.netposa.component.jq.mvp.contract.AlarmDetailsContract;
import com.netposa.component.jq.mvp.model.entity.AlarmDetailForIdResponseEntity;
import com.netposa.component.jq.mvp.model.entity.AlarmDetailResponseEntity;
import com.netposa.component.jq.mvp.model.entity.AlarmListResponseEntity;
import com.netposa.component.jq.mvp.model.entity.ProcessAlarmRequestEntity;
import com.netposa.component.jq.mvp.model.entity.ProcessAlarmResponseEntity;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;


@ActivityScope
public class AlarmDetailsPresenter extends BasePresenter<AlarmDetailsContract.Model, AlarmDetailsContract.View> {
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
    ProcessAlarmRequestEntity mProcessRequestEntity;

    @Inject
    public AlarmDetailsPresenter(AlarmDetailsContract.Model model, AlarmDetailsContract.View rootView) {
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

    // 有效无效处理
    public void getProcessAlarmInfo(String id, String note, int status) {
        if (!NetworkUtils.isConnected()) {
            mRootView.showMessage(mContext.getString(R.string.network_disconnect));
            mRootView.hideLoading();
            return;
        }
        mProcessRequestEntity.setId(id);
        mProcessRequestEntity.setNote(note);
        mProcessRequestEntity.setUserName(SPUtils.getInstance().getString(GlobalConstants.CONFIG_LAST_USER_NICKNAME, ""));
        mProcessRequestEntity.setAlarmStatus(status);
        mModel.getProcessAlarmInfo(mProcessRequestEntity)
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
                .subscribe(new ErrorHandleSubscriber<ProcessAlarmResponseEntity>(mErrorHandler) {
                    @Override
                    public void onNext(ProcessAlarmResponseEntity reponse) {
                        Log.i(TAG, "getProcessAlarmInfo:" + reponse);
                        mRootView.getProcessAlarmInfoSuccess(reponse);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        Log.i(TAG, "getProcessAlarmInfo:" + t.getMessage());
                        mRootView.getProcessAlarmInfoFail();
                    }
                });
    }

    // 根据ID获取详情
    public void getAlarmInfo(String id) {
        if (!NetworkUtils.isConnected()) {
            mRootView.showMessage(mContext.getString(R.string.network_disconnect));
            mRootView.hideLoading();
            return;
        }
        mModel.getAlarmInfo(id)
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
                .subscribe(new ErrorHandleSubscriber<AlarmDetailForIdResponseEntity>(mErrorHandler) {
                    @Override
                    public void onNext(AlarmDetailForIdResponseEntity reponse) {
                        Log.i(TAG, "getAlarmInfo:" + reponse);
                        mRootView.getAlarmInfoSuccess(reponse);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        Log.i(TAG, "getAlarmInfo:" + t.getMessage());
                        mRootView.getAlarmInfoFail();
                    }
                });
    }
}
