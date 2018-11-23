package com.netposa.component.spjk.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.netposa.component.spjk.di.module.SpjkModule;
import com.netposa.component.spjk.mvp.ui.activity.SpjkActivity;
import dagger.Component;

@ActivityScope
@Component(modules = SpjkModule.class, dependencies = AppComponent.class)
public interface SpjkComponent {
    void inject(SpjkActivity activity);
}