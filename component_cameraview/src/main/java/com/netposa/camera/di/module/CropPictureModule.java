package com.netposa.camera.di.module;

import android.content.Context;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.netposa.camera.mvp.contract.CropPictureContract;
import com.netposa.camera.mvp.model.CropPictureModel;
import com.netposa.commonres.widget.Dialog.LoadingDialogFragment;
import com.netposa.commonres.widget.Dialog.LottieDialogFragment;


@Module
public class CropPictureModule {
    private CropPictureContract.View view;
    private Context mContext;

    /**
     * 构建CropPictureModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public CropPictureModule(Context context,CropPictureContract.View view) {
        mContext = context;
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