package com.netposa.component.sfjb.di.module;

import android.content.Context;

import com.jess.arms.di.scope.ActivityScope;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.Module;
import dagger.Provides;
import com.netposa.commonres.widget.Dialog.LoadingDialogFragment;
import com.netposa.commonres.widget.Dialog.LottieDialogFragment;
import com.netposa.component.sfjb.mvp.contract.FaceCompareResultContract;
import com.netposa.component.sfjb.mvp.model.FaceCompareResultModel;
import com.netposa.component.sfjb.mvp.model.entity.FaceCompareRequestEntity;
import com.netposa.component.sfjb.mvp.model.entity.FaceCompareResponseEntity;
import com.netposa.component.sfjb.mvp.model.entity.FaceDetailEntity;
import com.netposa.component.sfjb.mvp.ui.adapter.SfjbCompareResultAdapter;

import java.util.ArrayList;
import java.util.List;


@Module
public class FaceCompareResultModule {
    private FaceCompareResultContract.View view;
    private Context mContext;

    /**
     * 构建FaceComparResultModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public FaceCompareResultModule(Context context, FaceCompareResultContract.View view) {
        this.mContext = context;
        this.view = view;
    }

    @ActivityScope
    @Provides
    FaceCompareResultContract.View provideFaceCompareResultView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    Context provideContext() {
        return mContext;
    }

    @ActivityScope
    @Provides
    FaceCompareResultContract.Model provideFaceCompareResultModel(FaceCompareResultModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.ItemAnimator provideItemAnimator() {
        return new DefaultItemAnimator();
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new GridLayoutManager(mContext, 3);
    }

    @ActivityScope
    @Provides
    List<FaceCompareResponseEntity.ListBean> provideBeanList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    SfjbCompareResultAdapter provideSfjbCompareResultAdapter(List<FaceCompareResponseEntity.ListBean> list) {
        return new SfjbCompareResultAdapter(mContext, list);
    }

    @ActivityScope
    @Provides
    FaceCompareRequestEntity provideFaceCompareRequestEntity() {
        return new FaceCompareRequestEntity();
    }

    @ActivityScope
    @Provides
    FaceDetailEntity provideFaceDetailEntity() {
        return new FaceDetailEntity();
    }

    @ActivityScope
    @Provides
    LottieDialogFragment provideDialogFragment() {
        return new LoadingDialogFragment();
    }
}

