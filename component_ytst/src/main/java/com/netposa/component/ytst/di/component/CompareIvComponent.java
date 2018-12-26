package com.netposa.component.ytst.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.ytst.di.module.CompareIvModule;

import com.netposa.component.ytst.mvp.ui.activity.CompareIvActivity;

@ActivityScope
@Component(modules = CompareIvModule.class, dependencies = AppComponent.class)
public interface CompareIvComponent {
    void inject(CompareIvActivity activity);
}