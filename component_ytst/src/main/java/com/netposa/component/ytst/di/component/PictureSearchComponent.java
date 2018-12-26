package com.netposa.component.ytst.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.ytst.di.module.PictureSearchModule;

import com.netposa.component.ytst.mvp.ui.activity.PictureSearchActivity;

@ActivityScope
@Component(modules = PictureSearchModule.class, dependencies = AppComponent.class)
public interface PictureSearchComponent {
    void inject(PictureSearchActivity activity);
}