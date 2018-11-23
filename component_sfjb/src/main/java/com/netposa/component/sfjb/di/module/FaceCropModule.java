package com.netposa.component.sfjb.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.netposa.component.sfjb.mvp.contract.FaceCropContract;
import com.netposa.component.sfjb.mvp.model.FaceCropModel;


@Module
public class FaceCropModule {
    private FaceCropContract.View view;

    /**
     * 构建FaceCropModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public FaceCropModule(FaceCropContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    FaceCropContract.View provideFaceCropView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    FaceCropContract.Model provideFaceCropModel(FaceCropModel model) {
        return model;
    }
}