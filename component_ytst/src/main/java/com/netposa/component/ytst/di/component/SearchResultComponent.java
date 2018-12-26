package com.netposa.component.ytst.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.ytst.di.module.SearchResultModule;

import com.netposa.component.ytst.mvp.ui.activity.SearchResultActivity;

@ActivityScope
@Component(modules = SearchResultModule.class, dependencies = AppComponent.class)
public interface SearchResultComponent {
    void inject(SearchResultActivity activity);
}