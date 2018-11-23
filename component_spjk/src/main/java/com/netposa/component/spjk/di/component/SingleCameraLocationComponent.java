package com.netposa.component.spjk.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.spjk.di.module.SingleCameraLocationModule;

import com.netposa.component.spjk.mvp.ui.activity.SingleCameraLocationActivity;

@ActivityScope
@Component(modules = SingleCameraLocationModule.class, dependencies = AppComponent.class)
public interface SingleCameraLocationComponent {
    void inject(SingleCameraLocationActivity activity);
}