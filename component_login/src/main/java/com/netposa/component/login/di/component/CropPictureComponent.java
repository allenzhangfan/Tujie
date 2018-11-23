package com.netposa.component.login.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.login.di.module.CropPictureModule;

import com.netposa.component.login.mvp.ui.activity.CropPictureActivity;

@ActivityScope
@Component(modules = CropPictureModule.class, dependencies = AppComponent.class)
public interface CropPictureComponent {
    void inject(CropPictureActivity activity);
}