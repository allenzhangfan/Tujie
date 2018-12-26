package com.netposa.camera.mvp.model;

import android.app.Application;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.jess.arms.di.scope.ActivityScope;
import javax.inject.Inject;
import com.netposa.camera.mvp.contract.CropPictureContract;
import com.netposa.camera.mvp.model.api.ImageUploadService;
import com.netposa.camera.mvp.model.entity.UploadResponseEntity;
import com.netposa.common.entity.HttpResponseEntity;
import com.netposa.common.net.HttpResponseHandler;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;


@ActivityScope
public class CropPictureModel extends BaseModel implements CropPictureContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public CropPictureModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<HttpResponseEntity<UploadResponseEntity>> uploadImage(MultipartBody.Part file) {
        return mRepositoryManager.obtainRetrofitService(ImageUploadService.class)
                .uploadImage(file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable faceLogin(MultipartBody.Part file, String name) {
        return mRepositoryManager.obtainRetrofitService(ImageUploadService.class)
                .faceLogin(file,name)
                .subscribeOn(Schedulers.io())
                .compose(HttpResponseHandler.handleResult());
    }
}