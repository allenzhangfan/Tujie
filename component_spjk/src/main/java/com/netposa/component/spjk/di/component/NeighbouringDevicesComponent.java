package com.netposa.component.spjk.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.spjk.di.module.NeighbouringDevicesModule;

import com.netposa.component.spjk.mvp.ui.activity.NeighbouringDevicesActivity;

@ActivityScope
@Component(modules = NeighbouringDevicesModule.class, dependencies = AppComponent.class)
public interface NeighbouringDevicesComponent {
    void inject(NeighbouringDevicesActivity activity);
}