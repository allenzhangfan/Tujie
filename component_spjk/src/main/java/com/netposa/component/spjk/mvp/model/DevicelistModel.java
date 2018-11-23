package com.netposa.component.spjk.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.jess.arms.di.scope.ActivityScope;
import javax.inject.Inject;

import com.netposa.common.net.HttpResponseHandler;
import com.netposa.component.spjk.mvp.contract.DevicelistContract;
import com.netposa.component.spjk.mvp.model.api.SpjkService;
import com.netposa.component.spjk.mvp.model.entity.SpjkListDeviceRequestEntity;
import com.netposa.component.spjk.mvp.model.entity.SpjkListDeviceResponseEntity;

import io.reactivex.Observable;


@ActivityScope
public class DevicelistModel extends BaseModel implements DevicelistContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public DevicelistModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<SpjkListDeviceResponseEntity> getDevicelist(SpjkListDeviceRequestEntity request) {
        return mRepositoryManager
                .obtainRetrofitService(SpjkService.class)
                .getSpjkDeviceList(request)
                .compose(HttpResponseHandler.handleResult());
    }
}