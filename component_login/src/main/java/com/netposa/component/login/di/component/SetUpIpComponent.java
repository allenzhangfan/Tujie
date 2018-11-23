package com.netposa.component.login.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.login.di.module.SetUpIpModule;

import com.netposa.component.login.mvp.ui.activity.SetUpIpActivity;

@ActivityScope
@Component(modules = SetUpIpModule.class, dependencies = AppComponent.class)
public interface SetUpIpComponent {
    void inject(SetUpIpActivity activity);
}