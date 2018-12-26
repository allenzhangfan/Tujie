package com.netposa.component.jq.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.netposa.common.net.HttpResponseHandler;
import com.netposa.component.jq.mvp.contract.AlarmDetailsContract;
import com.netposa.component.jq.mvp.model.api.JqService;
import com.netposa.component.jq.mvp.model.entity.AlarmDetailForIdResponseEntity;
import com.netposa.component.jq.mvp.model.entity.AlarmDetailResponseEntity;
import com.netposa.component.jq.mvp.model.entity.ProcessAlarmRequestEntity;
import com.netposa.component.jq.mvp.model.entity.ProcessAlarmResponseEntity;

import io.reactivex.Observable;


@ActivityScope
public class AlarmDetailsModel extends BaseModel implements AlarmDetailsContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public AlarmDetailsModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<ProcessAlarmResponseEntity> getProcessAlarmInfo(ProcessAlarmRequestEntity entity) {
        return mRepositoryManager.obtainRetrofitService(JqService.class)
                .getProcessAlarmInfo(entity)
                .compose(HttpResponseHandler.handleResult());
    }

    @Override
    public Observable<AlarmDetailForIdResponseEntity> getAlarmInfo(String id) {
        return mRepositoryManager.obtainRetrofitService(JqService.class)
                .getAlarmInfo(id)
                .compose(HttpResponseHandler.handleResult());
    }
}