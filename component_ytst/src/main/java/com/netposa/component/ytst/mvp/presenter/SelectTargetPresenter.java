package com.netposa.component.ytst.mvp.presenter;

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
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import okhttp3.MultipartBody;

import javax.inject.Inject;

import com.netposa.common.entity.HttpResponseEntity;
import com.netposa.common.log.Log;
import com.netposa.component.ytst.mvp.contract.SelectTargetContract;
import com.netposa.component.ytst.mvp.model.entity.ImgSearchRequestEntity;
import com.netposa.component.ytst.mvp.model.entity.UploadPicResponseEntity;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;


@ActivityScope
public class SelectTargetPresenter extends BasePresenter<SelectTargetContract.Model, SelectTargetContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public SelectTargetPresenter(SelectTargetContract.Model model, SelectTargetContract.View rootView) {
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

}
