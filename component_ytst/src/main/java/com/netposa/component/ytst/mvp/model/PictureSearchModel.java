package com.netposa.component.ytst.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;
import com.netposa.common.entity.HttpResponseEntity;
import com.netposa.component.ytst.mvp.contract.PictureSearchContract;
import com.netposa.component.ytst.mvp.model.api.YtstService;
import com.netposa.component.ytst.mvp.model.entity.UploadPicResponseEntity;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;


@ActivityScope
public class PictureSearchModel extends BaseModel implements PictureSearchContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public PictureSearchModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<HttpResponseEntity<UploadPicResponseEntity>> uploadImage(MultipartBody.Part file) {
        return mRepositoryManager.obtainRetrofitService(YtstService.class)
                .uploadImage(file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}