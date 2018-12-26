package com.netposa.component.spjk.di.module;

import android.content.Context;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.jess.arms.di.scope.ActivityScope;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.Module;
import dagger.Provides;

import com.netposa.commonres.widget.CustomerLoadMoreView;
import com.netposa.component.room.entity.SpjkSearchHistoryEntity;
import com.netposa.component.spjk.mvp.contract.SpjkSearchContract;
import com.netposa.component.spjk.mvp.model.SpjkSearchModel;
import com.netposa.component.spjk.mvp.model.entity.SpjkItemEntity;
import com.netposa.component.spjk.mvp.model.entity.SpjkSearchRequestEntity;
import com.netposa.component.spjk.mvp.model.entity.SpjkSearchResponseEntity;
import com.netposa.component.spjk.mvp.ui.adapter.SpjkSearchHistoryAdapter;
import com.netposa.component.spjk.mvp.ui.adapter.SpjkSearchResulthAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;


@Module
public class SpjkSearchModule {
    private Context mContext;
    private SpjkSearchContract.View view;

    /**
     * 构建SearchResultModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public SpjkSearchModule(Context context, SpjkSearchContract.View view) {
        this.mContext = context;
        this.view = view;
    }

    @ActivityScope
    @Provides
    SpjkSearchContract.View provideSearchResultView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    SpjkSearchContract.Model provideSearchResultModel(SpjkSearchModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    @Named("SearchSpjkHistory")
    RecyclerView.LayoutManager provideSearchHistoryLayoutManager() {
        return new LinearLayoutManager(mContext);
    }

    @ActivityScope
    @Provides
    @Named("SearchSpjkResult")
    RecyclerView.LayoutManager provideSearchResultLayoutManager() {
        return new LinearLayoutManager(mContext);
    }

    @ActivityScope
    @Provides
    @Named("SearchSpjkHistory")
    RecyclerView.ItemAnimator provideSearchHistoryItemAnimator() {
        return new DefaultItemAnimator();
    }

    @ActivityScope
    @Provides
    @Named("SearchSpjkResult")
    RecyclerView.ItemAnimator provideSearchResultItemAnimator() {
        return new DefaultItemAnimator();
    }

    @ActivityScope
    @Provides
    Context provideContext() {
        return mContext;
    }

    @ActivityScope
    @Provides
    LoadMoreView provideLoadMoreView() {
        return new CustomerLoadMoreView();
    }

    @ActivityScope
    @Provides
    List<SpjkSearchHistoryEntity> provideSearchHistoryBeanList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    SpjkSearchHistoryAdapter provideHomeAdapter(List<SpjkSearchHistoryEntity> beanList) {
        return new SpjkSearchHistoryAdapter(beanList);
    }

    @ActivityScope
    @Provides
    List<SpjkItemEntity> provideSearchMatchBeanList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    SpjkSearchResulthAdapter providematchAdapter(List<SpjkSearchResponseEntity.ListBean> beanList) {
        return new SpjkSearchResulthAdapter(beanList);
    }

    @ActivityScope
    @Provides
    SpjkSearchRequestEntity providerRequest() {
        return new SpjkSearchRequestEntity();
    }

    @ActivityScope
    @Provides
    List<SpjkSearchResponseEntity.ListBean> providerSearchList() {
        return new ArrayList<>();
    }

}