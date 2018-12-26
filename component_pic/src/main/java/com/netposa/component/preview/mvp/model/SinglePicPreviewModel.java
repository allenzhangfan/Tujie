package com.netposa.component.preview.mvp.model;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.netposa.common.constant.GlobalConstants;
import com.netposa.common.core.RouterHub;
import com.netposa.common.log.Log;
import com.netposa.common.net.HttpResponseHandler;
import com.netposa.common.utils.EncryptUtils;
import com.netposa.common.utils.NetworkUtils;
import com.netposa.common.utils.SPUtils;
import com.netposa.component.pic.R;
import com.netposa.component.preview.mvp.contract.SinglePicPreviewContract;
import com.netposa.component.preview.mvp.model.api.PicService;
import com.netposa.component.preview.mvp.model.entity.FeatureByPathRequestEntity;
import com.netposa.component.preview.mvp.model.entity.FeatureByPathResponseEntity;
import com.netposa.component.room.DbHelper;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;

import androidx.lifecycle.LifecycleOwner;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.netposa.common.constant.GlobalConstants.CONFIG_LAST_USER_GENDER;
import static com.netposa.common.constant.GlobalConstants.CONFIG_LAST_USER_GROUP;
import static com.netposa.common.constant.GlobalConstants.CONFIG_LAST_USER_POLICE_NO;
import static com.netposa.common.constant.GlobalConstants.CONFIG_LAST_USER_TEL_NO;


@ActivityScope
public class SinglePicPreviewModel extends BaseModel implements SinglePicPreviewContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public SinglePicPreviewModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<FeatureByPathResponseEntity> getDetectImgFeatureByPath(FeatureByPathRequestEntity request) {
        return mRepositoryManager
                .obtainRetrofitService(PicService.class)
                .getDetectImgFeatureByPath(request)
                .compose(HttpResponseHandler.handleResult());
    }
}