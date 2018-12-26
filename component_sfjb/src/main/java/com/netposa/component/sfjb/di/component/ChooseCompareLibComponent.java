package com.netposa.component.sfjb.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.netposa.component.sfjb.di.module.ChooseCompareLibModule;
import com.netposa.component.sfjb.mvp.ui.activity.ChooseCompareLibActivity;
import dagger.Component;

@ActivityScope
@Component(modules = ChooseCompareLibModule.class, dependencies = AppComponent.class)
public interface ChooseCompareLibComponent {
    void inject(ChooseCompareLibActivity activity);
}