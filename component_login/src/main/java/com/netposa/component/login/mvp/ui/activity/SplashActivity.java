package com.netposa.component.login.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.core.RouterHub;
import com.netposa.common.service.location.LocationService;
import com.netposa.common.service.mqtt.PushService;
import com.netposa.component.login.di.component.DaggerSplashComponent;
import com.netposa.component.login.di.module.SplashModule;
import com.netposa.component.login.mvp.contract.SplashContract;
import com.netposa.component.login.mvp.presenter.SplashPresenter;
import com.netposa.component.login.R;

import static com.jess.arms.utils.Preconditions.checkNotNull;

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashContract.View {
    // 启动图显示延迟
    private static final int APP_START_DELAY_TIME = 1500;
    private long mStartTime;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSplashComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .splashModule(new SplashModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_splash; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mStartTime = System.currentTimeMillis();
        gotoMainPage();

    }

    private void gotoMainPage() {
        long endTime = System.currentTimeMillis();
        int interval = (int) (endTime - mStartTime);
        if (interval < APP_START_DELAY_TIME) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToLoginActivity();
                }
            }, APP_START_DELAY_TIME - interval);
        } else {
            goToLoginActivity();
        }
    }

    private void goToLoginActivity() {
        //启动mqtt服务
        PushService pushService = ARouter.getInstance().navigation(PushService.class);
        if(pushService!=null){
            pushService.initPushService();
        }
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        killMyself();
    }

    @Override
    public void showLoading(String message) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }
}
