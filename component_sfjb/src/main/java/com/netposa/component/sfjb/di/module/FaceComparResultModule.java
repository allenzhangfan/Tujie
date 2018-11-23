package com.netposa.component.sfjb.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.netposa.component.sfjb.mvp.contract.FaceComparResultContract;
import com.netposa.component.sfjb.mvp.model.FaceComparResultModel;


@Module
public class FaceComparResultModule {
    private FaceComparResultContract.View view;

    /**
     * 构建FaceComparResultModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public FaceComparResultModule(FaceComparResultContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    FaceComparResultContract.View provideFaceComparResultView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    FaceComparResultContract.Model provideFaceComparResultModel(FaceComparResultModel model) {
        return model;
    }
}