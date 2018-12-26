package com.netposa.component.clcx.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.netposa.common.net.HttpResponseHandler;
import com.netposa.component.clcx.mvp.contract.QueryResultContract;
import com.netposa.component.clcx.mvp.model.api.ClcxService;
import com.netposa.component.clcx.mvp.model.entity.QueryCarSearchEntity;
import com.netposa.component.clcx.mvp.model.entity.QueryCarSearchResponseEntity;

import java.util.List;

import io.reactivex.Observable;


@ActivityScope
public class QueryResultModel extends BaseModel implements QueryResultContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public QueryResultModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<List<QueryCarSearchResponseEntity>> getDevicelist(QueryCarSearchEntity request) {
        return mRepositoryManager
                .obtainRetrofitService(ClcxService.class)
                .getSearchVehicle(request)
                .compose(HttpResponseHandler.handleResult());
    }
}