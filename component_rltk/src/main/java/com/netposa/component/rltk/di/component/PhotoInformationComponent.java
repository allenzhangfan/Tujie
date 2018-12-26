package com.netposa.component.rltk.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.rltk.di.module.PhotoInformationModule;

import com.netposa.component.rltk.mvp.ui.activity.PhotoInformationActivity;

@ActivityScope
@Component(modules = PhotoInformationModule.class, dependencies = AppComponent.class)
public interface PhotoInformationComponent {
    void inject(PhotoInformationActivity activity);
}