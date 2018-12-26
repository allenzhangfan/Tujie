package com.netposa.component.sfjb.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.netposa.common.net.HttpResponseHandler;
import com.netposa.component.sfjb.mvp.contract.SearchLibContract;
import com.netposa.component.sfjb.mvp.model.api.SfjbService;
import com.netposa.component.sfjb.mvp.model.entity.SearchFaceLibRequestEntity;
import com.netposa.component.sfjb.mvp.model.entity.SearchFaceLibResponseEntity;

import io.reactivex.Observable;


@ActivityScope
public class SearchLibModel extends BaseModel implements SearchLibContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public SearchLibModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<SearchFaceLibResponseEntity> getSearch(SearchFaceLibRequestEntity entity) {
        return mRepositoryManager
                .obtainRetrofitService(SfjbService.class)
                .getSearchFaceLib(entity)
                .compose(HttpResponseHandler.handleResult());
    }
}