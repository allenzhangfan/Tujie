package com.netposa.component.sfjb.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.sfjb.di.module.ChoseComparLibModule;

import com.netposa.component.sfjb.mvp.ui.activity.ChoseComparLibActivity;

@ActivityScope
@Component(modules = ChoseComparLibModule.class, dependencies = AppComponent.class)
public interface ChoseComparLibComponent {
    void inject(ChoseComparLibActivity activity);
}