package com.netposa.component.spjk.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.netposa.common.net.HttpResponseHandler;
import com.netposa.component.spjk.mvp.contract.SpjkSearchContract;
import com.netposa.component.spjk.mvp.model.api.SpjkService;
import com.netposa.component.spjk.mvp.model.entity.SpjkListDeviceRequestEntity;
import com.netposa.component.spjk.mvp.model.entity.SpjkListDeviceResponseEntity;
import com.netposa.component.spjk.mvp.model.entity.SpjkSearchRequestEntity;
import com.netposa.component.spjk.mvp.model.entity.SpjkSearchResponseEntity;

import io.reactivex.Observable;


@ActivityScope
public class SpjkSearchModel extends BaseModel implements SpjkSearchContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public SpjkSearchModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<SpjkSearchResponseEntity> getDevice(SpjkSearchRequestEntity request) {
        return mRepositoryManager
                .obtainRetrofitService(SpjkService.class)
                .getSpjkSearchResult(request)
                .compose(HttpResponseHandler.handleResult());
    }

    @Override
    public Observable<SpjkListDeviceResponseEntity> getOrganizeId(SpjkListDeviceRequestEntity request) {
        return mRepositoryManager
                .obtainRetrofitService(SpjkService.class)
                .getSpjkDeviceList(request)
                .compose(HttpResponseHandler.handleResult());
    }
}