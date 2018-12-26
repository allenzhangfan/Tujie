package com.netposa.component.rltk.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.netposa.common.net.HttpResponseHandler;
import com.netposa.component.rltk.mvp.contract.FaceLibraryContract;
import com.netposa.component.rltk.mvp.model.api.FaceLibraryService;
import com.netposa.component.rltk.mvp.model.entity.FaceLibraryRequestEntity;
import com.netposa.component.rltk.mvp.model.entity.FaceLibraryResponseEntity;

import java.util.List;

import io.reactivex.Observable;


@ActivityScope
public class FaceLibraryModel extends BaseModel implements FaceLibraryContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public FaceLibraryModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<FaceLibraryResponseEntity> getFaceList(FaceLibraryRequestEntity request) {
        return mRepositoryManager
                .obtainRetrofitService(FaceLibraryService.class)
                .getFaceList(request)
                .compose(HttpResponseHandler.handleResult());
    }
}