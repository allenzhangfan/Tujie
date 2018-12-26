package com.netposa.component.ytst.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.ytst.di.module.TrackModule;

import com.netposa.component.ytst.mvp.ui.activity.TrackActivity;

@ActivityScope
@Component(modules = TrackModule.class, dependencies = AppComponent.class)
public interface TrackComponent {
    void inject(TrackActivity activity);
}