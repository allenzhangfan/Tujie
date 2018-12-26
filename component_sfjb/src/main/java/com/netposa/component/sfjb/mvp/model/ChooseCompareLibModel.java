package com.netposa.component.sfjb.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.netposa.common.net.HttpResponseHandler;
import com.netposa.component.sfjb.mvp.contract.ChooseCompareLibContract;
import com.netposa.component.sfjb.mvp.model.api.SfjbService;
import com.netposa.component.sfjb.mvp.model.entity.LibTreeRequestEntity;
import com.netposa.component.sfjb.mvp.model.entity.LibTreeResponseEntity;

import io.reactivex.Observable;


@ActivityScope
public class ChooseCompareLibModel extends BaseModel implements ChooseCompareLibContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public ChooseCompareLibModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<LibTreeResponseEntity> getLibTreelist(LibTreeRequestEntity request) {
        return mRepositoryManager
                .obtainRetrofitService(SfjbService.class)
                .getFaceLibraryTree(request)
                .compose(HttpResponseHandler.handleResult());
    }
}