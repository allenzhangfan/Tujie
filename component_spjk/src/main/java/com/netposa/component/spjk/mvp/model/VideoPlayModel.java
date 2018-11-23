package com.netposa.component.spjk.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.netposa.common.net.HttpResponseHandler;
import com.netposa.component.spjk.mvp.contract.VideoPlayContract;
import com.netposa.component.spjk.mvp.model.api.SpjkService;
import com.netposa.component.spjk.mvp.model.entity.DeviceInfoResponseEntity;
import com.netposa.component.spjk.mvp.model.entity.PtzDirectionRequestEntity;

import io.reactivex.Observable;


@ActivityScope
public class VideoPlayModel extends BaseModel implements VideoPlayContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public VideoPlayModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<DeviceInfoResponseEntity> getDeviceInfo(String carmerId) {
        return mRepositoryManager.obtainRetrofitService(SpjkService.class)
                .getDeviceInfo(carmerId)
                .compose(HttpResponseHandler.handleResult());
    }

    @Override
    public Observable<Object> setPtzDirection(PtzDirectionRequestEntity ptzDirectionRequestEntity) {
        return mRepositoryManager.obtainRetrofitService(SpjkService.class)
                .setPtzDirection(ptzDirectionRequestEntity)
                .compose(HttpResponseHandler.handleResult());
    }
}