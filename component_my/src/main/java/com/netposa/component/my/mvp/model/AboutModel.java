package com.netposa.component.my.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.netposa.common.net.HttpResponseHandler;
import com.netposa.component.my.mvp.contract.AboutContract;
import com.netposa.component.my.mvp.model.api.MyService;
import com.netposa.component.my.mvp.model.entity.UpdateInfoEntity;

import io.reactivex.Observable;


@ActivityScope
public class AboutModel extends BaseModel implements AboutContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public AboutModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<UpdateInfoEntity> getUpdateJson() {
        return mRepositoryManager
                .obtainRetrofitService(MyService.class)
                .getUpdateJson()
                .compose(HttpResponseHandler.handleResult());
    }
}