package com.netposa.component.spjk.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.netposa.common.net.HttpResponseHandler;
import com.netposa.component.spjk.mvp.contract.SpjkContract;
import com.netposa.component.spjk.mvp.model.api.SpjkService;
import com.netposa.component.spjk.mvp.model.entity.OneKilometerCamerasRequestEntity;
import com.netposa.component.spjk.mvp.model.entity.OneKilometerCamerasResponseEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;


@ActivityScope
public class SpjkModel extends BaseModel implements SpjkContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public SpjkModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<List<OneKilometerCamerasResponseEntity>> getNeighbouringDevice(OneKilometerCamerasRequestEntity okcRequestEntity) {
        return mRepositoryManager.obtainRetrofitService(SpjkService.class)
                .getNeighbouringDevice(okcRequestEntity)
                .compose(HttpResponseHandler.handleResult());
    }
}