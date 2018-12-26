package com.netposa.component.gzt.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.jess.arms.di.scope.FragmentScope;
import com.netposa.component.gzt.di.module.GztModule;

import com.netposa.component.gzt.mvp.ui.fragment.GztFragment;

@FragmentScope
@Component(modules = GztModule.class, dependencies = AppComponent.class)
public interface GztComponent {
    void inject(GztFragment fragment);
}