package com.netposa.component.clcx.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.clcx.di.module.QueryCarModule;

import com.netposa.component.clcx.mvp.ui.activity.QueryCarActivity;

@ActivityScope
@Component(modules = QueryCarModule.class, dependencies = AppComponent.class)
public interface QueryCarComponent {
    void inject(QueryCarActivity activity);
}