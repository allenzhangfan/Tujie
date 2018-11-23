package com.netposa.component.spjk.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.spjk.di.module.SpjkSearchModule;

import com.netposa.component.spjk.mvp.ui.activity.SpjkSearchActivity;

@ActivityScope
@Component(modules = SpjkSearchModule.class, dependencies = AppComponent.class)
public interface SpjkSearchComponent {
    void inject(SpjkSearchActivity activity);
}