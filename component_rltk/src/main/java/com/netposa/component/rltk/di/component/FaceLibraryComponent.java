package com.netposa.component.rltk.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.rltk.di.module.FaceLibraryModule;

import com.netposa.component.rltk.mvp.ui.activity.FaceLibraryActivity;

@ActivityScope
@Component(modules = FaceLibraryModule.class, dependencies = AppComponent.class)
public interface FaceLibraryComponent {
    void inject(FaceLibraryActivity activity);
}