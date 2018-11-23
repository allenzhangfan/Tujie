package com.netposa.component.jq.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.netposa.component.jq.mvp.contract.CapturePictureContract;
import com.netposa.component.jq.mvp.model.CapturePictureModel;


@Module
public class CapturePictureModule {
    private CapturePictureContract.View view;

    /**
     * 构建CapturePictureModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public CapturePictureModule(CapturePictureContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CapturePictureContract.View provideCapturePictureView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CapturePictureContract.Model provideCapturePictureModel(CapturePictureModel model) {
        return model;
    }
}