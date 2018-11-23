package com.netposa.component.jq.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.jq.di.module.JqSearchModule;

import com.netposa.component.jq.mvp.ui.activity.JqSearchActivity;

@ActivityScope
@Component(modules = JqSearchModule.class, dependencies = AppComponent.class)
public interface JqSearchComponent {
    void inject(JqSearchActivity activity);
}