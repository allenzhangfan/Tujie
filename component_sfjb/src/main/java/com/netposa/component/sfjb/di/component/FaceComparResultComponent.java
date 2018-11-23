package com.netposa.component.sfjb.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.sfjb.di.module.FaceComparResultModule;

import com.netposa.component.sfjb.mvp.ui.activity.FaceComparResultActivity;

@ActivityScope
@Component(modules = FaceComparResultModule.class, dependencies = AppComponent.class)
public interface FaceComparResultComponent {
    void inject(FaceComparResultActivity activity);
}