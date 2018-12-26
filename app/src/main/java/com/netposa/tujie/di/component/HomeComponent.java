package com.netposa.tujie.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.netposa.tujie.di.module.HomeModule;
import com.netposa.tujie.mvp.ui.activity.HomeActivity;

@ActivityScope
@Component(modules = HomeModule.class, dependencies = AppComponent.class)
public interface HomeComponent {
    void inject(HomeActivity activity);
}