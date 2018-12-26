package com.netposa.tujie.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.netposa.common.net.HttpResponseHandler;
import com.netposa.component.my.mvp.model.api.MyService;
import com.netposa.component.my.mvp.model.entity.UpdateInfoEntity;
import com.netposa.tujie.mvp.contract.HomeContract;

import io.reactivex.Observable;


@ActivityScope
public class HomeModel extends BaseModel implements HomeContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public HomeModel(IRepositoryManager repositoryManager) {
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