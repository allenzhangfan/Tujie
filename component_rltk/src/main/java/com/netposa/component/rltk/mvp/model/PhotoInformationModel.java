package com.netposa.component.rltk.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.jess.arms.di.scope.ActivityScope;
import javax.inject.Inject;
import com.netposa.common.net.HttpResponseHandler;
import com.netposa.component.rltk.mvp.contract.PhotoInformationContract;
import com.netposa.component.rltk.mvp.model.api.FaceLibraryService;
import com.netposa.component.rltk.mvp.model.entity.FaceDetailResponseEntity;
import com.netposa.common.entity.FeatureByPathRequestEntity;
import com.netposa.common.entity.FeatureByPathResponseEntity;

import io.reactivex.Observable;


@ActivityScope
public class PhotoInformationModel extends BaseModel implements PhotoInformationContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public PhotoInformationModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<FaceDetailResponseEntity> getFace(String recordId) {
        return mRepositoryManager
                .obtainRetrofitService(FaceLibraryService.class)
                .getFaceInfo(recordId)
                .compose(HttpResponseHandler.handleResult());
    }

    @Override
    public Observable<FeatureByPathResponseEntity> getDetectImgFeatureByPath(FeatureByPathRequestEntity request) {
        return mRepositoryManager
                .obtainRetrofitService(FaceLibraryService.class)
                .getDetectImgFeatureByPath(request)
                .compose(HttpResponseHandler.handleResult());
    }
}