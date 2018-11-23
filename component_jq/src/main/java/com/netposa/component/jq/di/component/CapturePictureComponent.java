package com.netposa.component.jq.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.jq.di.module.CapturePictureModule;

import com.netposa.component.jq.mvp.ui.activity.CapturePictureActivity;

@ActivityScope
@Component(modules = CapturePictureModule.class, dependencies = AppComponent.class)
public interface CapturePictureComponent {
    void inject(CapturePictureActivity activity);
}