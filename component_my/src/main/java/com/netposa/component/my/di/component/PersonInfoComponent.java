package com.netposa.component.my.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.my.di.module.PersonInfoModule;

import com.netposa.component.my.mvp.ui.activity.PersonInfoActivity;

@ActivityScope
@Component(modules = PersonInfoModule.class, dependencies = AppComponent.class)
public interface PersonInfoComponent {
    void inject(PersonInfoActivity activity);
}