package com.netposa.component.ytst.di.module;

import android.content.Context;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.jess.arms.di.scope.ActivityScope;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.Module;
import dagger.Provides;

import com.netposa.common.entity.FeatureByPathRequestEntity;
import com.netposa.commonres.widget.Dialog.LoadingDialogFragment;
import com.netposa.commonres.widget.Dialog.LottieDialogFragment;
import com.netposa.component.ytst.mvp.contract.CompareIvContract;
import com.netposa.component.ytst.mvp.model.CompareIvModel;
import com.netposa.component.ytst.mvp.model.entity.CarDetailRequestEntity;
import com.netposa.component.ytst.mvp.model.entity.CarDetailResponseEntity;
import com.netposa.component.ytst.mvp.ui.adapter.CarRecordAdapter;

import java.util.ArrayList;
import java.util.List;


@Module
public class CompareIvModule {
    private Context mContext;
    private CompareIvContract.View view;

    /**
     * 构建CompareIvModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public CompareIvModule(Context context, CompareIvContract.View view) {
        this.mContext = context;
        this.view = view;
    }

    @ActivityScope
    @Provides
    CompareIvContract.View provideCompareIvView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CompareIvContract.Model provideCompareIvModel(CompareIvModel model) {
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
    CarRecordAdapter providerAdapter(List<MultiItemEntity> list) {
        return new CarRecordAdapter(list);
    }

    @ActivityScope
    @Provides
    CarDetailResponseEntity providerResponseEntity() {
        return new CarDetailResponseEntity();
    }

    @ActivityScope
    @Provides
    CarDetailRequestEntity providerRequestEntity() {
        return new CarDetailRequestEntity();
    }

    @ActivityScope
    @Provides
    FeatureByPathRequestEntity providerByPathRequestEntity() { return new FeatureByPathRequestEntity(); }
}