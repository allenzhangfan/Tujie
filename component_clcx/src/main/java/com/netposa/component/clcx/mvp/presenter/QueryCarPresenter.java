package com.netposa.component.clcx.mvp.presenter;

import android.app.Application;
import android.content.Context;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import javax.inject.Inject;
import com.netposa.component.clcx.mvp.contract.QueryCarContract;


@ActivityScope
public class QueryCarPresenter extends BasePresenter<QueryCarContract.Model, QueryCarContract.View> {
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
    public QueryCarPresenter(QueryCarContract.Model model, QueryCarContract.View rootView) {
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
