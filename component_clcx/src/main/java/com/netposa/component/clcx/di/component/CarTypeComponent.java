package com.netposa.component.clcx.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.clcx.di.module.CarTypeModule;

import com.netposa.component.clcx.mvp.ui.activity.CarTypeActivity;

@ActivityScope
@Component(modules = CarTypeModule.class, dependencies = AppComponent.class)
public interface CarTypeComponent {
    void inject(CarTypeActivity activity);
}