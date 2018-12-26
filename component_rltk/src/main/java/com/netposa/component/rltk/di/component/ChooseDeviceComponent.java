package com.netposa.component.rltk.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.rltk.di.module.ChooseDeviceModule;

import com.netposa.component.rltk.mvp.ui.activity.ChooseDeviceActivity;

@ActivityScope
@Component(modules = ChooseDeviceModule.class, dependencies = AppComponent.class)
public interface ChooseDeviceComponent {
    void inject(ChooseDeviceActivity activity);
}