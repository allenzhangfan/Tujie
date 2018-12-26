package com.netposa.component.sfjb.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.netposa.common.net.HttpResponseHandler;
import com.netposa.component.sfjb.mvp.contract.FaceCompareResultContract;
import com.netposa.component.sfjb.mvp.model.api.SfjbService;
import com.netposa.component.sfjb.mvp.model.entity.FaceCompareRequestEntity;
import com.netposa.component.sfjb.mvp.model.entity.FaceCompareResponseEntity;
import io.reactivex.Observable;


@ActivityScope
public class FaceCompareResultModel extends BaseModel implements FaceCompareResultContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public FaceCompareResultModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<FaceCompareResponseEntity> getFaceCompareResult(FaceCompareRequestEntity request) {
        return mRepositoryManager.obtainRetrofitService(SfjbService.class)
                .getFaceCompareResult(request)
                .compose(HttpResponseHandler.handleResult());
    }

}