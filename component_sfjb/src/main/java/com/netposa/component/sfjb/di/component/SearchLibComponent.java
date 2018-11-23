package com.netposa.component.sfjb.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.sfjb.di.module.SearchLibModule;

import com.netposa.component.sfjb.mvp.ui.activity.SearchLibActivity;

@ActivityScope
@Component(modules = SearchLibModule.class, dependencies = AppComponent.class)
public interface SearchLibComponent {
    void inject(SearchLibActivity activity);
}