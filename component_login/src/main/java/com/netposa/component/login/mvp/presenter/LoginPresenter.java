package com.netposa.component.login.mvp.presenter;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

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
import com.netposa.component.room.entity.LoginConfigEntity;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;

import org.greenrobot.eventbus.EventBus;

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
    LoginConfigEntity mLoginConfigEntity;
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
    @SuppressLint("CheckResult")
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
                    @SuppressLint("CheckResult")
                    @Override
                    public void onNext(LoginResponseEntity response) {
                        Log.i(TAG, "login response: " + response.toString());
                        LoginResponseEntity.UserEntity loginUserEntity = response.getUser();
                        SPUtils.getInstance().put(GlobalConstants.TOKEN, response.getTokenId());
                        LoginConfigEntity loginConfigEntityInDb = DbHelper.getInstance().findByUsername(username);
                        LoginConfigEntity temp;
                        Log.i(TAG, "loginConfigEntityInDb: " + loginConfigEntityInDb);
                        if (null != loginConfigEntityInDb) {
                            temp = loginConfigEntityInDb;
                            //人脸登录开关与每个人的个人信息绑定在一起
                            SPUtils.getInstance().put(GlobalConstants.HAS_FACE, loginConfigEntityInDb.isFaceLoginOpened());
                        } else {
                            mLoginConfigEntity.setUsername(username);
                            mLoginConfigEntity.setPassword(encryptPassword);
                            mLoginConfigEntity.setId(loginUserEntity.getId());
                            mLoginConfigEntity.setIdCardNumber(loginUserEntity.getIdCardNumber());
                            mLoginConfigEntity.setGender(loginUserEntity.getGender());
                            mLoginConfigEntity.setAdmin(loginUserEntity.isAdmin());
                            mLoginConfigEntity.setPhoneNo(loginUserEntity.getPhoneNo());
                            mLoginConfigEntity.setUserOrg(loginUserEntity.getUserOrg());
                            mLoginConfigEntity.setOrgName(loginUserEntity.getOrgName());
                            mLoginConfigEntity.setName(loginUserEntity.getName());
                            mLoginConfigEntity.setRegisterTime(loginUserEntity.getRegisterTime());
                            mLoginConfigEntity.setPoliceNo(loginUserEntity.getPoliceNo());
                            mLoginConfigEntity.setStatus(loginUserEntity.getStatus());
                            mLoginConfigEntity.setFaceLoginOpened(false);
                            temp = mLoginConfigEntity;
                            SPUtils.getInstance().put(GlobalConstants.HAS_FACE, false);
                            DbHelper
                                    .getInstance()
                                    .insertLoginConfig(mLoginConfigEntity)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                                    .subscribe(new Observer<Boolean>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {
                                            Log.d(TAG, "insertLoginConfig onSubscribe");
                                        }

                                        @Override
                                        public void onNext(Boolean result) {
                                            Log.d(TAG, "insertLoginConfig onNext");
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Log.d(TAG, "insertLoginConfig onError");
                                        }

                                        @Override
                                        public void onComplete() {
                                            Log.d(TAG, "insertLoginConfig onComplete");
                                        }
                                    });
                        }
                        SPUtils.getInstance().put(GlobalConstants.CONFIG_LAST_USER_LOGIN_NAME, username);
                        SPUtils.getInstance().put(GlobalConstants.CONFIG_LAST_USER_NICKNAME, loginUserEntity.getName());
                        SPUtils.getInstance().put(CONFIG_LAST_USER_GENDER, loginUserEntity.getGender());
                        SPUtils.getInstance().put(CONFIG_LAST_USER_GROUP, loginUserEntity.getOrgName());
                        SPUtils.getInstance().put(CONFIG_LAST_USER_POLICE_NO, loginUserEntity.getPoliceNo());
                        SPUtils.getInstance().put(CONFIG_LAST_USER_TEL_NO, loginUserEntity.getPhoneNo());
                        SPUtils.getInstance().put(CONFIG_LAST_USER_LOGIN_ID, loginUserEntity.getId());
                        //刷新我的模块个人信息
                        EventBus.getDefault().post(temp);
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
