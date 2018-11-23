package com.netposa.component.login.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.netposa.component.login.mvp.contract.CropPictureContract;
import com.netposa.component.login.mvp.model.CropPictureModel;


@Module
public class CropPictureModule {
    private CropPictureContract.View view;

    /**
     * 构建CropPictureModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public CropPictureModule(CropPictureContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CropPictureContract.View provideCropPictureView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CropPictureContract.Model provideCropPictureModel(CropPictureModel model) {
        return model;
    }
}