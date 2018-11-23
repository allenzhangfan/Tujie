package com.netposa.component.spjk.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.netposa.component.spjk.mvp.contract.SingleCameraLocationContract;
import com.netposa.component.spjk.mvp.model.SingleCameraLocationModel;


@Module
public class SingleCameraLocationModule {
    private SingleCameraLocationContract.View view;

    /**
     * 构建SingleCameraLocationModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public SingleCameraLocationModule(SingleCameraLocationContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    SingleCameraLocationContract.View provideSingleCameraLocationView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    SingleCameraLocationContract.Model provideSingleCameraLocationModel(SingleCameraLocationModel model) {
        return model;
    }
}