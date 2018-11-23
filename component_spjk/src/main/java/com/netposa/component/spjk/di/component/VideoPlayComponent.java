package com.netposa.component.spjk.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.spjk.di.module.VideoPlayModule;

import com.netposa.component.spjk.mvp.ui.activity.VideoPlayActivity;

@ActivityScope
@Component(modules = VideoPlayModule.class, dependencies = AppComponent.class)
public interface VideoPlayComponent {
    void inject(VideoPlayActivity activity);
}