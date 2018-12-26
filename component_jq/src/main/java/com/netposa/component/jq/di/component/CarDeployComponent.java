package com.netposa.component.jq.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.jess.arms.di.scope.FragmentScope;
import com.netposa.component.jq.di.module.CarDeployModule;

import com.netposa.component.jq.mvp.ui.fragment.CarDeployFragment;

@FragmentScope
@Component(modules = CarDeployModule.class, dependencies = AppComponent.class)
public interface CarDeployComponent {
    void inject(CarDeployFragment fragment);
}