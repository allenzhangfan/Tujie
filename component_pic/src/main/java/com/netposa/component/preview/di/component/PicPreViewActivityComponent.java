package com.netposa.component.preview.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.preview.di.module.PicPreViewActivityModule;

import com.netposa.component.preview.mvp.ui.activity.PicPreViewActivity;

@ActivityScope
@Component(modules = PicPreViewActivityModule.class, dependencies = AppComponent.class)
public interface PicPreViewActivityComponent {
    void inject(PicPreViewActivity activity);
}