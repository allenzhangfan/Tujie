package com.netposa.component.sfjb.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.netposa.component.sfjb.mvp.contract.GetImageContract;
import com.netposa.component.sfjb.mvp.model.GetImageModel;


@Module
public class GetImageModule {
    private GetImageContract.View view;

    /**
     * 构建GetImageModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public GetImageModule(GetImageContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    GetImageContract.View provideGetImageView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    GetImageContract.Model provideGetImageModel(GetImageModel model) {
        return model;
    }
}