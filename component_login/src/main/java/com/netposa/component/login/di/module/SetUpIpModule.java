package com.netposa.component.login.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.netposa.component.login.mvp.contract.SetUpIpContract;
import com.netposa.component.login.mvp.model.SetUpIpModel;


@Module
public class SetUpIpModule {
    private SetUpIpContract.View view;

    /**
     * 构建SetUpIpModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public SetUpIpModule(SetUpIpContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    SetUpIpContract.View provideSetUpIpView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    SetUpIpContract.Model provideSetUpIpModel(SetUpIpModel model) {
        return model;
    }
}