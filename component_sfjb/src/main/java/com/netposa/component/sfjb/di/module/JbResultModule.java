package com.netposa.component.sfjb.di.module;

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
import com.netposa.component.sfjb.mvp.contract.JbResultContract;
import com.netposa.component.sfjb.mvp.model.JbResultModel;
import com.netposa.component.sfjb.mvp.model.entity.FaceDetailEntity;
import com.netposa.component.sfjb.mvp.ui.adapter.LibPhotoAdapter;

import java.util.ArrayList;
import java.util.List;


@Module
public class JbResultModule {
    private Context mContext;
    private JbResultContract.View view;

    /**
     * 构建JbResultModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public JbResultModule(Context context, JbResultContract.View view) {
        this.mContext = context;
        this.view = view;
    }

    @ActivityScope
    @Provides
    JbResultContract.View provideJbResultView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    JbResultContract.Model provideJbResultModel(JbResultModel model) {
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
    LibPhotoAdapter providerAdapter(List<MultiItemEntity> list) {
        return new LibPhotoAdapter(list);
    }

    @ActivityScope
    @Provides
    FaceDetailEntity provideFaceDetailEntity(){ return  new FaceDetailEntity();}
}