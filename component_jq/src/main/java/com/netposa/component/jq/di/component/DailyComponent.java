package com.netposa.component.jq.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.jess.arms.di.scope.FragmentScope;
import com.netposa.component.jq.di.module.DailyModule;

import com.netposa.component.jq.mvp.ui.fragment.DailyFragment;

@FragmentScope
@Component(modules = DailyModule.class, dependencies = AppComponent.class)
public interface DailyComponent {
    void inject(DailyFragment fragment);
}