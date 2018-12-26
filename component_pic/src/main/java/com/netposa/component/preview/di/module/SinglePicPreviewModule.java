package com.netposa.component.preview.di.module;

import android.content.Context;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.netposa.commonres.widget.Dialog.LoadingDialogFragment;
import com.netposa.commonres.widget.Dialog.LottieDialogFragment;
import com.netposa.component.imagedownload.ImageDownloadUtil;
import com.netposa.component.preview.mvp.contract.SinglePicPreviewContract;
import com.netposa.component.preview.mvp.model.SinglePicPreviewModel;
import com.netposa.component.preview.mvp.model.entity.FeatureByPathRequestEntity;


@Module
public class SinglePicPreviewModule {
    private SinglePicPreviewContract.View view;

    private Context mContext;

    /**
     * 构建SinglePicPreviewModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public SinglePicPreviewModule(Context context,SinglePicPreviewContract.View view)
    {
        mContext = context;
        this.view = view;
    }

    @ActivityScope
    @Provides
    Context provideContext() {
        return mContext;
    }


    @ActivityScope
    @Provides
    SinglePicPreviewContract.View provideSinglePicPreviewView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    SinglePicPreviewContract.Model provideSinglePicPreviewModel(SinglePicPreviewModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    ImageDownloadUtil provideImageDownloadUtil() {
        return new ImageDownloadUtil((Context) view);
    }

    @ActivityScope
    @Provides
    FeatureByPathRequestEntity provideFeatureByPathRequestEntity() {
        return new FeatureByPathRequestEntity();
    }

    @ActivityScope
    @Provides
    LottieDialogFragment provideDialogFragment() {
        return new LoadingDialogFragment();
    }
}