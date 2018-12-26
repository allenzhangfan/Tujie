package com.netposa.component.my.mvp.presenter;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
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

import com.netposa.common.core.RouterHub;
import com.netposa.common.log.Log;
import com.netposa.common.service.mqtt.PushService;
import com.netposa.component.my.mvp.contract.PersonInfoContract;
import com.netposa.component.my.mvp.model.entity.LogOutResponseEntity;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;


@ActivityScope
public class PersonInfoPresenter extends BasePresenter<PersonInfoContract.Model, PersonInfoContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public PersonInfoPresenter(PersonInfoContract.Model model, PersonInfoContract.View rootView) {
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

    public void logOut() {
        mModel.logOut()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> mRootView.showLoading(""))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (mRootView != null) mRootView.hideLoading();
                })
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe(new ErrorHandleSubscriber<LogOutResponseEntity>(mErrorHandler) {
                    @Override
                    public void onNext(LogOutResponseEntity objectEntity) {
                        Log.i(TAG, "logOut success !");
                        //退出登录后不再接收MQTT消息
                        PushService pushService = (PushService) ARouter.getInstance().build(RouterHub.MQTT_PUSH_SERVICE).navigation();
                        pushService.closeMQTT();
                        mRootView.logOutSuccess();
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(TAG, "logOut error !");
                        mRootView.logOutFail();
                    }
                });
    }
}
