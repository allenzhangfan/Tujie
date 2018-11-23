package com.netposa.component.jq.di.component;

import com.jess.arms.di.scope.FragmentScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.jq.di.module.JqModule;

import com.netposa.component.jq.mvp.ui.fragment.JqFragment;

@FragmentScope
@Component(modules = JqModule.class, dependencies = AppComponent.class)
public interface JqComponent {
    void inject(JqFragment fragment);
}