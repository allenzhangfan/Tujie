package com.netposa.camera.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.netposa.camera.mvp.contract.FaceLoginContract;
import com.netposa.camera.mvp.model.FaceLoginModel;


@Module
public class FaceLoginModule {
    private FaceLoginContract.View view;

    /**
     * 构建FaceLoginModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public FaceLoginModule(FaceLoginContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    FaceLoginContract.View provideFaceLoginView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    FaceLoginContract.Model provideFaceLoginModel(FaceLoginModel model) {
        return model;
    }
}