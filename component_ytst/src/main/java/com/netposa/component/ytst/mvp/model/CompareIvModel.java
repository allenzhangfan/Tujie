package com.netposa.component.ytst.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.netposa.common.entity.FeatureByPathRequestEntity;
import com.netposa.common.entity.FeatureByPathResponseEntity;
import com.netposa.common.net.HttpResponseHandler;
import com.netposa.component.ytst.mvp.contract.CompareIvContract;
import com.netposa.component.ytst.mvp.model.api.YtstService;
import com.netposa.component.ytst.mvp.model.entity.CarDetailRequestEntity;
import com.netposa.component.ytst.mvp.model.entity.CarDetailResponseEntity;

import java.util.List;

import io.reactivex.Observable;


@ActivityScope
public class CompareIvModel extends BaseModel implements CompareIvContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public CompareIvModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<List<CarDetailResponseEntity>> getDetailVehicleInfo(CarDetailRequestEntity entity) {
        return mRepositoryManager
                .obtainRetrofitService(YtstService.class)
                .getDetailVehicleInfo(entity)
                .compose(HttpResponseHandler.handleResult());
    }

    @Override
    public Observable<FeatureByPathResponseEntity> getDetectImgFeatureByPath(FeatureByPathRequestEntity request) {
        return mRepositoryManager
                .obtainRetrofitService(YtstService.class)
                .getDetectImgFeatureByPath(request)
                .compose(HttpResponseHandler.handleResult());
    }
}