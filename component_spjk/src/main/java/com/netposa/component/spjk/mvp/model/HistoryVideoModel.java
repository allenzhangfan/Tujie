package com.netposa.component.spjk.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.netposa.common.net.HttpResponseHandler;
import com.netposa.component.spjk.mvp.contract.HistoryVideoContract;
import com.netposa.component.spjk.mvp.model.api.SpjkService;
import com.netposa.component.spjk.mvp.model.entity.HistoryVideoRequestEntity;
import com.netposa.component.spjk.mvp.model.entity.HistoryVideoResponseEntity;

import java.util.List;

import io.reactivex.Observable;


@ActivityScope
public class HistoryVideoModel extends BaseModel implements HistoryVideoContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public HistoryVideoModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<HistoryVideoResponseEntity> getlistHistoryVideo(HistoryVideoRequestEntity requestEntity) {
        return mRepositoryManager.obtainRetrofitService(SpjkService.class)
                .getlistHistoryVideo(requestEntity)
                .compose(HttpResponseHandler.handleResult());
    }
}