package com.netposa.component.ytst.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;
import com.netposa.common.net.HttpResponseHandler;
import com.netposa.component.ytst.mvp.contract.SearchResultContract;
import com.netposa.component.ytst.mvp.model.api.YtstService;
import com.netposa.component.ytst.mvp.model.entity.ImgSearchRequestEntity;
import com.netposa.component.ytst.mvp.model.entity.ImgSearchResponseEntity;

import java.util.List;
import io.reactivex.Observable;



@ActivityScope
public class SearchResultModel extends BaseModel implements SearchResultContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public SearchResultModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }
    @Override
    public Observable<List<ImgSearchResponseEntity>> imgSearch(ImgSearchRequestEntity entity) {
        return mRepositoryManager
                .obtainRetrofitService(YtstService.class)
                .imgSearch(entity)
                .compose(HttpResponseHandler.handleResult());
    }
}