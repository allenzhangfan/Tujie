package com.netposa.component.login.mvp.model;

import android.app.Application;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.jess.arms.di.scope.ActivityScope;
import javax.inject.Inject;

import com.netposa.common.net.HttpResponseHandler;
import com.netposa.component.login.mvp.contract.LoginContract;
import com.netposa.component.login.mvp.model.api.LoginService;
import com.netposa.component.login.mvp.model.entity.LoginRequestEntity;
import com.netposa.component.login.mvp.model.entity.LoginResponseEntity;
import io.reactivex.Observable;

@ActivityScope
public class LoginModel extends BaseModel implements LoginContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;


    @Inject
    public LoginModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<LoginResponseEntity> login(LoginRequestEntity request) {
//        RetrofitUrlManager.getInstance().putDomain(GlobalConstants.KEY_BASE_URL, SPUtils.getInstance().getString(GlobalConstants.URL_BASE));
        return mRepositoryManager
                .obtainRetrofitService(LoginService.class)
                .login(request)
                .compose(HttpResponseHandler.handleResult());
    }
}