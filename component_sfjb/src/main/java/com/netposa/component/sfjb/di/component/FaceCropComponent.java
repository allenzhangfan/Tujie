package com.netposa.component.sfjb.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.sfjb.di.module.FaceCropModule;

import com.netposa.component.sfjb.mvp.ui.activity.FaceCropActivity;

@ActivityScope
@Component(modules = FaceCropModule.class, dependencies = AppComponent.class)
public interface FaceCropComponent {
    void inject(FaceCropActivity activity);
}