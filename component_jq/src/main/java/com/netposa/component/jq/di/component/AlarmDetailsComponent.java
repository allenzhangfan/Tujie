package com.netposa.component.jq.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.jq.di.module.AlarmDetailsModule;

import com.netposa.component.jq.mvp.ui.activity.AlarmDetailsActivity;

@ActivityScope
@Component(modules = AlarmDetailsModule.class, dependencies = AppComponent.class)
public interface AlarmDetailsComponent {
    void inject(AlarmDetailsActivity activity);
}