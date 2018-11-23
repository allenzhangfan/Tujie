package com.netposa.component.login.mvp.presenter;

import android.Manifest;
import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import javax.inject.Inject;
import com.jess.arms.utils.PermissionUtil;
import com.netposa.common.app.ErrorDialog;
import com.netposa.common.constant.GlobalConstants;
import com.netposa.common.log.Log;
import com.netposa.common.utils.EncryptUtils;
import com.netposa.common.utils.NetworkUtils;
import com.netposa.common.utils.SPUtils;
import com.netposa.component.login.R;
import com.netposa.component.login.mvp.contract.LoginContract;
import com.netposa.component.login.mvp.model.entity.LoginRequestEntity;
import com.netposa.component.login.mvp.model.entity.LoginResponseEntity;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import java.util.List;


@ActivityScope
public class LoginPresenter extends BasePresenter<LoginContract.Model, LoginContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    RxPermissions mRxPermissions;
    @Inject
    Context mContext;
    @Inject
    FragmentManager mFm;
    @Inject
    LoginRequestEntity mRequestEntity;

    @Inject
    public LoginPresenter(LoginContract.Model model, LoginContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    /**
     * 权限请求
     */
    public void requestPermissions() {
        PermissionUtil.requestPermission(
                new PermissionUtil.RequestPermission() {
                    @Override
                    public void onRequestPermissionSuccess() {
                        Log.d(TAG, "Request permissions success");
                    }

                    @Override
                    public void onRequestPermissionFailure(List<String> permissions) {
                        Log.d(TAG, "Request permissions failure : " + permissions);
                        if (permissions.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            ErrorDialog
                                    .newInstance(mContext.getString(R.string.sdcard_write_permission))
                                    .show(mFm, "dialog");
                        }
                    }

                    @Override
                    public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
                        Log.d(TAG, "Request permissions failureWithAskNeverAgain : " + permissions);
                    }
                }
                , mRxPermissions
                , mErrorHandler
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.CAMERA
                , Manifest.permission.READ_PHONE_STATE
                , Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.ACCESS_FINE_LOCATION
                );
    }

    /**
     * 登录
     * @param username
     * @param password
     */
    public void login(String username, String password) {
        if (!NetworkUtils.isConnected()) {
            mRootView.showMessage(mContext.getString(R.string.network_disconnect));
            return;
        }
        String encryptPassword = EncryptUtils.encryptMD5ToString(password);
        mRequestEntity.setUsername(username);
        mRequestEntity.setPassword(encryptPassword);

        mModel.login(mRequestEntity)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading("");//显示下拉刷新的进度条
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();//隐藏下拉刷新的进度条
                })
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe(new ErrorHandleSubscriber<LoginResponseEntity>(mErrorHandler) {
                    @Override
                    public void onNext(LoginResponseEntity response) {
                        Log.d(TAG,new Gson().toJson(response));
                        Log.i(TAG, "onNext: token = " + response.getTokenId());
                        SPUtils.getInstance().put(GlobalConstants.TOKEN, response.getTokenId());
                        SPUtils.getInstance().put(GlobalConstants.HAS_LOGIN, true);
                        SPUtils.getInstance().put(GlobalConstants.CONFIG_LAST_USER_LOGIN_NAME,username);
                        mRootView.goToHomeActivity();
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
    }
}
