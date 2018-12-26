package com.netposa.component.my.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.FragmentScope;

import javax.inject.Inject;

import com.netposa.common.net.HttpResponseHandler;
import com.netposa.component.my.mvp.contract.MyContract;
import com.netposa.component.my.mvp.model.api.MyService;
import com.netposa.component.my.mvp.model.entity.UpdateInfoEntity;

import io.reactivex.Observable;


@FragmentScope
public class MyModel extends BaseModel implements MyContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public MyModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }
}