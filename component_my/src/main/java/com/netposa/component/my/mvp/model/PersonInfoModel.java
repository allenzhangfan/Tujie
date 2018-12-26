package com.netposa.component.my.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.netposa.common.net.HttpResponseHandler;
import com.netposa.component.my.mvp.contract.PersonInfoContract;
import com.netposa.component.my.mvp.model.api.MyService;
import com.netposa.component.my.mvp.model.entity.LogOutResponseEntity;

import io.reactivex.Observable;


@ActivityScope
public class PersonInfoModel extends BaseModel implements PersonInfoContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public PersonInfoModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<LogOutResponseEntity> logOut() {
        return mRepositoryManager
                .obtainRetrofitService(MyService.class)
                .logOut()
                .compose(HttpResponseHandler.handleResult());
    }
}