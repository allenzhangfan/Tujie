package com.netposa.component.jq.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.jess.arms.di.scope.FragmentScope;
import com.netposa.component.jq.di.module.FaceDeployModule;

import com.netposa.component.jq.mvp.ui.fragment.FaceDeployFragment;

@FragmentScope
@Component(modules = FaceDeployModule.class, dependencies = AppComponent.class)
public interface FaceDeployComponent {
    void inject(FaceDeployFragment fragment);
}