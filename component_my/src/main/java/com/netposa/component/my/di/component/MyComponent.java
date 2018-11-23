package com.netposa.component.my.di.component;

import com.jess.arms.di.scope.FragmentScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.netposa.component.my.di.module.MyModule;

import com.netposa.component.my.mvp.ui.fragment.MyFragment;

@FragmentScope
@Component(modules = MyModule.class, dependencies = AppComponent.class)
public interface MyComponent {
    void inject(MyFragment fragment);
}