package com.netposa.component.gzt.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.FragmentScope;

import javax.inject.Inject;

import com.netposa.common.net.HttpResponseHandler;
import com.netposa.component.gzt.mvp.contract.GztContract;
import com.netposa.component.gzt.mvp.model.api.GztService;
import com.netposa.component.gzt.mvp.model.entity.AllDictionaryResponseEntity;

import io.reactivex.Observable;


@FragmentScope
public class GztModel extends BaseModel implements GztContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public GztModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<AllDictionaryResponseEntity> getDictionaryAll() {
        return mRepositoryManager
                .obtainRetrofitService(GztService.class)
                .getAll()
                .compose(HttpResponseHandler.handleResult());
    }
}