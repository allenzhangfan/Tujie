package com.netposa.component.login.mvp.presenter;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import javax.inject.Inject;

import com.netposa.common.constant.GlobalConstants;
import com.netposa.common.constant.MqttConstants;
import com.netposa.common.constant.UrlConstant;
import com.netposa.common.core.RouterHub;
import com.netposa.common.log.Log;
import com.netposa.common.service.mqtt.PushService;
import com.netposa.common.utils.EncryptUtils;
import com.netposa.common.utils.FileUtils;
import com.netposa.common.utils.NetworkUtils;
import com.netposa.common.utils.SPUtils;
import com.netposa.component.login.R;
import com.netposa.component.login.mvp.contract.LoginContract;
import com.netposa.component.login.mvp.model.entity.LoginRequestEntity;
import com.netposa.common.entity.login.LoginResponseEntity;
import com.netposa.component.room.DbHelper;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;

import java.util.ArrayList;
import java.util.List;

import static com.netposa.common.constant.GlobalConstants.CONFIG_LAST_USER_GENDER;
import static com.netposa.common.constant.GlobalConstants.CONFIG_LAST_USER_GROUP;
import static com.netposa.common.constant.GlobalConstants.CONFIG_LAST_USER_LOGIN_ID;
import static com.netposa.common.constant.GlobalConstants.CONFIG_LAST_USER_POLICE_NO;
import static com.netposa.common.constant.GlobalConstants.CONFIG_LAST_USER_TEL_NO;

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
    Context mContext;
    @Inject
    FragmentManager mFm;
    @Inject
    LoginRequestEntity mRequestEntity;
    @Inject
    Gson mGson;

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
     * 登录
     *
     * @param username
     * @param password
     */
    public void login(String username, String password) {
        if (!NetworkUtils.isConnected()) {
            mRootView.showMessage(mContext.getString(R.string.network_disconnect));
            mRootView.hideLoading();
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
                        Log.d(TAG, new Gson().toJson(response));
                        Log.i(TAG, "onNext: token = " + response.getTokenId());
                        // 登录成功 判断是否切换用户 切换了用户 把 HAS_LOGIN 设置false
                        String saveName = SPUtils.getInstance().getString(GlobalConstants.CONFIG_LAST_USER_LOGIN_NAME);
                        if (!TextUtils.isEmpty(saveName) && !(saveName.equals(username))) {
                            SPUtils.getInstance().put(GlobalConstants.HAS_FACE, false);
                        }
                        SPUtils.getInstance().put(GlobalConstants.TOKEN, response.getTokenId());
                        LoginResponseEntity.UserEntity loginUserEntity = response.getUser();
//                        SPUtils.getInstance().put(GlobalConstants.HAS_LOGIN, true);
                        SPUtils.getInstance().put(GlobalConstants.CONFIG_LAST_USER_LOGIN_NAME, username);
                        SPUtils.getInstance().put(GlobalConstants.CONFIG_LAST_USER_NICKNAME, loginUserEntity.getName());
                        SPUtils.getInstance().put(CONFIG_LAST_USER_GENDER, loginUserEntity.getGender());
                        SPUtils.getInstance().put(CONFIG_LAST_USER_GROUP, loginUserEntity.getOrgName());
                        SPUtils.getInstance().put(CONFIG_LAST_USER_POLICE_NO, loginUserEntity.getPoliceNo());
                        SPUtils.getInstance().put(CONFIG_LAST_USER_TEL_NO, loginUserEntity.getPhoneNo());
                        SPUtils.getInstance().put(CONFIG_LAST_USER_LOGIN_ID, loginUserEntity.getId());
                        //各种url配置
                        SPUtils.getInstance().put(MqttConstants.MQTT_SERVER_URI, UrlConstant.parseMqttUrl(response.getMqttOuterIp()));
                        SPUtils.getInstance().put(GlobalConstants.IMAGE_URL, response.getPictureOuterIp());
                        SPUtils.getInstance().put(GlobalConstants.PLAY_URL, response.getVideoOuterIp());
                        // 登录的时候删除上次缓存的推送消息
                        DbHelper
                                .getInstance()
                                .deleteAllAlarmMessages()
                                .subscribeOn(Schedulers.io())
                                .subscribe();

                        PushService pushService = (PushService) ARouter.getInstance().build(RouterHub.MQTT_PUSH_SERVICE).navigation();
                        pushService.activateMQTT();
                        mRootView.goToHomeActivity();
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
    }

    public void initDirs() {
        List<String> pathList = new ArrayList<>();
        pathList.add(GlobalConstants.ROOT_PATH);
        pathList.add(GlobalConstants.DB_PATH);
        pathList.add(GlobalConstants.LOG_PATH);
        pathList.add(GlobalConstants.DOWNLOAD_PATH);
        pathList.add(GlobalConstants.CACHE_PATH);
        pathList.add(GlobalConstants.PICTURE_PATH);
        pathList.add(GlobalConstants.VIDEO_PATH);

        List<Boolean> mkdirResults = new ArrayList<>(pathList.size());
        Observable
                .fromIterable(pathList)
                .flatMap((Function<String, ObservableSource<Boolean>>) path -> {
                    boolean mkdirResult = FileUtils.createOrExistsDir(path);
                    Log.i(TAG, "path:" + path + ",mkdirResult:" + mkdirResult);
                    mkdirResults.add(mkdirResult);
                    return Observable.fromIterable(mkdirResults);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean result) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e("Utils", "mkdirs onComplete");
                    }
                });
    }
}
