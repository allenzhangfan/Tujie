package com.netposa.component.jq.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;


import javax.inject.Inject;

import com.netposa.common.net.HttpResponseHandler;
import com.netposa.component.jq.mvp.contract.CarDeployContract;
import com.netposa.component.jq.mvp.model.api.JqService;
import com.netposa.component.jq.mvp.model.entity.AlarmListRequestEntity;
import com.netposa.component.jq.mvp.model.entity.AlarmListResponseEntity;

import io.reactivex.Observable;


@FragmentScope
public class CarDeployModel extends BaseModel implements CarDeployContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public CarDeployModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<AlarmListResponseEntity> getlistAlarmInfo(AlarmListRequestEntity okcRequestEntity) {
        return mRepositoryManager.obtainRetrofitService(JqService.class)
                .getlistAlarmInfo(okcRequestEntity)
                .compose(HttpResponseHandler.handleResult());
    }
}