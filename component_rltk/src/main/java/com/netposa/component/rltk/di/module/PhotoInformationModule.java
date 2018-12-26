package com.netposa.component.rltk.di.module;

import android.content.Context;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.jess.arms.di.scope.ActivityScope;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.Module;
import dagger.Provides;

import com.netposa.commonres.widget.Dialog.LoadingDialogFragment;
import com.netposa.commonres.widget.Dialog.LottieDialogFragment;
import com.netposa.component.rltk.mvp.contract.PhotoInformationContract;
import com.netposa.component.rltk.mvp.model.PhotoInformationModel;
import com.netposa.common.entity.FeatureByPathRequestEntity;
import com.netposa.component.rltk.mvp.ui.adapter.PhotoInformationAdapter;

import java.util.ArrayList;
import java.util.List;


@Module
public class PhotoInformationModule {
    private Context mContext;
    private PhotoInformationContract.View view;

    /**
     * 构建PhotoInformationModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public PhotoInformationModule(Context context, PhotoInformationContract.View view) {
        this.mContext = context;
        this.view = view;
    }

    @ActivityScope
    @Provides
    PhotoInformationContract.View providePhotoInformationView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    PhotoInformationContract.Model providePhotoInformationModel(PhotoInformationModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    Context provideContext() {
        return mContext;
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(mContext);
    }

    @ActivityScope
    @Provides
    RecyclerView.ItemAnimator provideItemAnimator() {
        return new DefaultItemAnimator();
    }

    @ActivityScope
    @Provides
    List<MultiItemEntity> provideBeanList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    LottieDialogFragment provideDialogFragment() {
        return new LoadingDialogFragment();
    }

    @ActivityScope
    @Provides
    PhotoInformationAdapter providerAdapter(List<MultiItemEntity> list) {
        return new PhotoInformationAdapter(list);
    }

    @ActivityScope
    @Provides
    FeatureByPathRequestEntity providerFeatureByPathRequestEntity() {
        return new FeatureByPathRequestEntity();
    }
}