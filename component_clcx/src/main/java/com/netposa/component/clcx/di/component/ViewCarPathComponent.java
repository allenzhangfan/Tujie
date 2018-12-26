package com.netposa.component.clcx.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.clcx.di.module.ViewCarPathModule;

import com.netposa.component.clcx.mvp.ui.activity.ViewCarPathActivity;

@ActivityScope
@Component(modules = ViewCarPathModule.class, dependencies = AppComponent.class)
public interface ViewCarPathComponent {
    void inject(ViewCarPathActivity activity);
}