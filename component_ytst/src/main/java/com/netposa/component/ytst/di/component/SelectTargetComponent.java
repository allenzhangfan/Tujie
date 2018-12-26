package com.netposa.component.ytst.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.ytst.di.module.SelectTargetModule;

import com.netposa.component.ytst.mvp.ui.activity.SelectTargetActivity;

@ActivityScope
@Component(modules = SelectTargetModule.class, dependencies = AppComponent.class)
public interface SelectTargetComponent {
    void inject(SelectTargetActivity activity);
}