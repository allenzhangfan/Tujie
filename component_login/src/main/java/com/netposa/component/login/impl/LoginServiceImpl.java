package com.netposa.component.login.impl;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.netposa.common.core.RouterHub;
import com.netposa.common.log.Log;
import com.netposa.common.service.login.LoginService;

@Route(path = RouterHub.LOGIN_LOGIN_SERVICE, name = "登陆服务")
public class LoginServiceImpl implements LoginService {

    private static final String TAG = LoginServiceImpl.class.getSimpleName();
    private Context mContext;

    @Override
    public void login() {
        Log.d(TAG, "start to login----->");
        ARouter.getInstance().build(RouterHub.LOGIN_LOGIN_ACTIVITY).navigation(mContext);//发起无参数的路由操作
    }

    @Override
    public void init(Context context) {
        Log.d(TAG, "start to init----->");
        mContext = context;
    }
}
