package com.netposa.component.clcx.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.netposa.common.net.HttpResponseHandler;
import com.netposa.component.clcx.mvp.contract.ViewCarPathContract;
import com.netposa.component.clcx.mvp.model.api.ClcxService;
import com.netposa.component.clcx.mvp.model.entity.VehicleTrackRequestEntity;
import com.netposa.component.clcx.mvp.model.entity.VehicleTrackResponseEntity;

import io.reactivex.Observable;


@ActivityScope
public class ViewCarPathModel extends BaseModel implements ViewCarPathContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public ViewCarPathModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<VehicleTrackResponseEntity> getVehicleTrack(VehicleTrackRequestEntity entity) {
        return mRepositoryManager
                .obtainRetrofitService(ClcxService.class)
                .getVehicleTrack(entity)
                .compose(HttpResponseHandler.handleResult());
    }
}