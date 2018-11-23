package com.netposa.component.spjk.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.spjk.di.module.HistoryVideoPlayModule;

import com.netposa.component.spjk.mvp.ui.activity.HistoryVideoPlayActivity;

@ActivityScope
@Component(modules = HistoryVideoPlayModule.class, dependencies = AppComponent.class)
public interface HistoryVideoPlayComponent {
    void inject(HistoryVideoPlayActivity activity);
}