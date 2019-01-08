package com.netposa.component.ytst.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.ytst.di.module.ShowTakePictureModule;

import com.netposa.component.ytst.mvp.ui.activity.ShowTakePictureActivity;

@ActivityScope
@Component(modules = ShowTakePictureModule.class, dependencies = AppComponent.class)
public interface ShowTakePictureComponent {
    void inject(ShowTakePictureActivity activity);
}