package com.netposa.component.spjk.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.spjk.di.module.HistoryVideoModule;

import com.netposa.component.spjk.mvp.ui.activity.HistoryVideoActivity;

@ActivityScope
@Component(modules = HistoryVideoModule.class, dependencies = AppComponent.class)
public interface HistoryVideoComponent {
    void inject(HistoryVideoActivity activity);
}