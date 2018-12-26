package com.netposa.component.rltk.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.netposa.common.net.HttpResponseHandler;
import com.netposa.component.rltk.mvp.contract.ChooseDeviceContract;
import com.netposa.component.rltk.mvp.model.api.FaceLibraryService;
import com.netposa.component.rltk.mvp.model.entity.SearchDeviceRequestEntity;
import com.netposa.component.rltk.mvp.model.entity.SearchDeviceResponseEntity;

import io.reactivex.Observable;


@ActivityScope
public class ChooseDeviceModel extends BaseModel implements ChooseDeviceContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public ChooseDeviceModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<SearchDeviceResponseEntity> getDevicelist(SearchDeviceRequestEntity request) {
        return mRepositoryManager
                .obtainRetrofitService(FaceLibraryService.class)
                .getChoseDeviceList(request)
                .compose(HttpResponseHandler.handleResult());
    }
}