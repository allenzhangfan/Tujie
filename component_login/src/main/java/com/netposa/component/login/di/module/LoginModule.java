package com.netposa.component.login.di.module;

import android.content.Context;
import com.jess.arms.di.scope.ActivityScope;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;
import com.netposa.commonres.modle.LoadingDialog;
import com.netposa.component.login.mvp.contract.LoginContract;
import com.netposa.component.login.mvp.model.LoginModel;
import com.netposa.component.login.mvp.model.entity.LoginRequestEntity;
import com.tbruyelle.rxpermissions2.RxPermissions;

@Module
public class LoginModule {
    private Context mContext;
    private LoginContract.View view;
    private FragmentManager mFm;

    /**
     * 构建LoginModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public LoginModule(Context context,LoginContract.View view,FragmentManager fm) {
        mContext = context;
        this.view = view;
        mFm = fm;
    }

    @ActivityScope
    @Provides
    LoginContract.View provideLoginView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    LoginContract.Model provideLoginModel(LoginModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    Context provideContext(){
        return mContext;
    }

    @ActivityScope
    @Provides
    FragmentManager provideFragmentManager(){
        return mFm;
    }

    @ActivityScope
    @Provides
    RxPermissions provideRxPermissions() {
        return new RxPermissions((FragmentActivity) view);
    }

    @ActivityScope
    @Provides
    LoginRequestEntity provideRequestEntity(){
        return new LoginRequestEntity();
    }

    @ActivityScope
    @Provides
    LoadingDialog provideLoadingDialog() {
        return new LoadingDialog(mContext);
    }
}