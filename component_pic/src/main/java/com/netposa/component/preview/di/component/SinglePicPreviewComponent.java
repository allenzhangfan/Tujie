package com.netposa.component.preview.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.preview.di.module.SinglePicPreviewModule;

import com.netposa.component.preview.mvp.ui.activity.SinglePicPreviewActivity;

@ActivityScope
@Component(modules = SinglePicPreviewModule.class, dependencies = AppComponent.class)
public interface SinglePicPreviewComponent {
    void inject(SinglePicPreviewActivity activity);
}