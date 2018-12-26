package com.netposa.component.sfjb.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.sfjb.di.module.FaceCompareResultModule;

import com.netposa.component.sfjb.mvp.ui.activity.FaceCompareResultActivity;

@ActivityScope
@Component(modules = FaceCompareResultModule.class, dependencies = AppComponent.class)
public interface FaceCompareResultComponent {
    void inject(FaceCompareResultActivity activity);
}