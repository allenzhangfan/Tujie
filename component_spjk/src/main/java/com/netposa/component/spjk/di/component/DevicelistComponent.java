package com.netposa.component.spjk.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import com.jess.arms.di.component.AppComponent;
import com.netposa.component.spjk.di.module.DevicelistModule;
import com.netposa.component.spjk.mvp.ui.activity.DevicelistActivity;


@ActivityScope
@Component(modules = DevicelistModule.class, dependencies = AppComponent.class)
public interface DevicelistComponent {
    void inject(DevicelistActivity activty);
}