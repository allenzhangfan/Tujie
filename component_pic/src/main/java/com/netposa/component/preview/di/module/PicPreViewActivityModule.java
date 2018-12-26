package com.netposa.component.preview.di.module;

import android.view.View;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.netposa.component.preview.mvp.contract.PicPreViewActivityContract;
import com.netposa.component.preview.mvp.model.PicPreViewActivityModel;

import java.util.ArrayList;
import java.util.List;


@Module
public class PicPreViewActivityModule {
    private PicPreViewActivityContract.View view;

    /**
     * 构建PicPreViewActivityModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public PicPreViewActivityModule(PicPreViewActivityContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    PicPreViewActivityContract.View providePicPreViewActivityView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    PicPreViewActivityContract.Model providePicPreViewActivityModel(PicPreViewActivityModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    List<View> provideViewList() {
        return new ArrayList<>();
    }
}