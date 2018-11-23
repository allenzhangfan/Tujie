package com.netposa.component.login.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.login.di.module.FaceLoginModule;

import com.netposa.component.login.mvp.ui.activity.FaceLoginActivity;

@ActivityScope
@Component(modules = FaceLoginModule.class, dependencies = AppComponent.class)
public interface FaceLoginComponent {
    void inject(FaceLoginActivity activity);
}