package com.netposa.component.sfjb.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.sfjb.di.module.JbResultModule;

import com.netposa.component.sfjb.mvp.ui.activity.JbResultActivity;

@ActivityScope
@Component(modules = JbResultModule.class, dependencies = AppComponent.class)
public interface JbResultComponent {
    void inject(JbResultActivity activity);
}