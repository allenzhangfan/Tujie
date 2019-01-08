package com.netposa.component.ytst.di.module;

import android.content.Context;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.netposa.commonres.widget.Dialog.LoadingDialogFragment;
import com.netposa.commonres.widget.Dialog.LottieDialogFragment;
import com.netposa.component.ytst.mvp.contract.ShowTakePictureContract;
import com.netposa.component.ytst.mvp.model.ShowTakePictureModel;


@Module
public class ShowTakePictureModule {
    private ShowTakePictureContract.View view;

    /**
     * 构建ShowTakePictureModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ShowTakePictureModule(ShowTakePictureContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ShowTakePictureContract.View provideShowTakePictureView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ShowTakePictureContract.Model provideShowTakePictureModel(ShowTakePictureModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    LottieDialogFragment provideDialogFragment() {
        return new LoadingDialogFragment();
    }

    @ActivityScope
    @Provides
    Context provideContext() {
        return (Context) view;
    }
}