package com.netposa.component.ytst.di.module;

import android.content.Context;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.netposa.commonres.widget.Dialog.LoadingDialogFragment;
import com.netposa.commonres.widget.Dialog.LottieDialogFragment;
import com.netposa.component.ytst.mvp.contract.PictureSearchContract;
import com.netposa.component.ytst.mvp.model.PictureSearchModel;


@Module
public class PictureSearchModule {
    private PictureSearchContract.View view;
    private Context mContext;

    /**
     * 构建PictureSearchModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public PictureSearchModule(Context context,PictureSearchContract.View view) {
        mContext = context;
        this.view = view;
    }

    @ActivityScope
    @Provides
    PictureSearchContract.View providePictureSearchView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    PictureSearchContract.Model providePictureSearchModel(PictureSearchModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    Context provideContext() {
        return mContext;
    }

    @ActivityScope
    @Provides
    LottieDialogFragment provideDialogFragment() {
        return new LoadingDialogFragment();
    }
}