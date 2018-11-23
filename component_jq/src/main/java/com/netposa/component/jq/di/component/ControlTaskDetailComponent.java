package com.netposa.component.jq.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.jq.di.module.ControlTaskDetailModule;

import com.netposa.component.jq.mvp.ui.activity.ControlTaskDetailActivity;

@ActivityScope
@Component(modules = ControlTaskDetailModule.class, dependencies = AppComponent.class)
public interface ControlTaskDetailComponent {
    void inject(ControlTaskDetailActivity activity);
}