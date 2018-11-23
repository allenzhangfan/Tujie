package com.netposa.component.sfjb.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.sfjb.di.module.GetImageModule;

import com.netposa.component.sfjb.mvp.ui.activity.GetImageActivity;

@ActivityScope
@Component(modules = GetImageModule.class, dependencies = AppComponent.class)
public interface GetImageComponent {
    void inject(GetImageActivity activity);
}