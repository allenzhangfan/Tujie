package com.netposa.component.clcx.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.clcx.di.module.QueryResultModule;

import com.netposa.component.clcx.mvp.ui.activity.QueryResultActivity;

@ActivityScope
@Component(modules = QueryResultModule.class, dependencies = AppComponent.class)
public interface QueryResultComponent {
    void inject(QueryResultActivity activity);
}