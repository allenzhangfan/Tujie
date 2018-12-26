package com.netposa.camera.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.camera.di.module.FaceLoginModule;

import com.netposa.camera.mvp.ui.activity.FaceLoginActivity;

@ActivityScope
@Component(modules = FaceLoginModule.class, dependencies = AppComponent.class)
public interface FaceLoginComponent {
    void inject(FaceLoginActivity activity);
}