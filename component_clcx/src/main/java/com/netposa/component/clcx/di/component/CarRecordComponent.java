package com.netposa.component.clcx.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.clcx.di.module.CarRecordModule;

import com.netposa.component.clcx.mvp.ui.activity.CarRecordActivity;

@ActivityScope
@Component(modules = CarRecordModule.class, dependencies = AppComponent.class)
public interface CarRecordComponent {
    void inject(CarRecordActivity activity);
}