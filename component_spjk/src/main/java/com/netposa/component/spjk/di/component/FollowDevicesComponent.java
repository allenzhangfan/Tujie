package com.netposa.component.spjk.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.spjk.di.module.FollowDevicesModule;

import com.netposa.component.spjk.mvp.ui.activity.FollowDevicesActivity;

@ActivityScope
@Component(modules = FollowDevicesModule.class, dependencies = AppComponent.class)
public interface FollowDevicesComponent {
    void inject(FollowDevicesActivity activity);
}