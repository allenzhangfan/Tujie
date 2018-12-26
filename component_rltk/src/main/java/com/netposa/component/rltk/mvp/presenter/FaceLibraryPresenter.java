package com.netposa.component.rltk.mvp.presenter;

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
import com.netposa.component.rltk.R;
import com.netposa.component.rltk.mvp.contract.FaceLibraryContract;
import com.netposa.component.rltk.mvp.model.entity.FaceLibraryRequestEntity;
import com.netposa.component.rltk.mvp.model.entity.FaceLibraryResponseEntity;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;

import java.util.ArrayList;
import java.util.List;


@ActivityScope
public class FaceLibraryPresenter extends BasePresenter<FaceLibraryContract.Model, FaceLibraryContract.View> {
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
    FaceLibraryRequestEntity mRequest;

    @Inject
    public FaceLibraryPresenter(FaceLibraryContract.Model model, FaceLibraryContract.View rootView) {
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

    public void getFaceList() {
        if (!NetworkUtils.isConnected()) {
            mRootView.showMessage(mContext.getString(R.string.network_disconnect));
            mRootView.hideLoading();
            return;
        }
        mModel.getFaceList(mRequest)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> mRootView.showLoading(""))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (mRootView != null) mRootView.hideLoading();
                })
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe(new ErrorHandleSubscriber<FaceLibraryResponseEntity>(mErrorHandler) {
                    @Override
                    public void onNext(FaceLibraryResponseEntity response) {
                        Log.d(TAG,"getDataSuccess: "+ response.toString());
                        mRootView.getDataSuccess(response);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        Log.d(TAG,"onError: "+t.toString());
                        mRootView.getDataFailed();
                    }
                });
    }
}
