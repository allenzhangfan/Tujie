package com.netposa.component.jq.di.module;

import android.content.Context;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.jess.arms.di.scope.ActivityScope;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.Module;
import dagger.Provides;

import com.netposa.commonres.widget.CustomerLoadMoreView;
import com.netposa.component.jq.mvp.contract.JqSearchContract;
import com.netposa.component.jq.mvp.model.JqSearchModel;
import com.netposa.common.entity.push.JqItemEntity;
import com.netposa.component.room.entity.JqSearchHistoryEntity;
import com.netposa.component.jq.mvp.ui.adapter.JqSearchHistoryAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

@Module
public class JqSearchModule {
    private Context mContext;
    private JqSearchContract.View view;

    /**
     * 构建JqSearchModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public JqSearchModule(Context context, JqSearchContract.View view) {
        mContext = context;
        this.view = view;
    }

    @ActivityScope
    @Provides
    JqSearchContract.View provideJqSearchView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    @Named("SearchHistory")
    RecyclerView.LayoutManager provideSearchHistoryLayoutManager() {
        return new LinearLayoutManager(mContext);
    }

    @ActivityScope
    @Provides
    @Named("SearchResult")
    RecyclerView.LayoutManager provideSearchResultLayoutManager() {
        return new LinearLayoutManager(mContext);
    }

    @ActivityScope
    @Provides
    @Named("SearchHistory")
    RecyclerView.ItemAnimator provideSearchHistoryItemAnimator() {
        return new DefaultItemAnimator();
    }

    @ActivityScope
    @Provides
    @Named("SearchResult")
    RecyclerView.ItemAnimator provideSearchResultItemAnimator() {
        return new DefaultItemAnimator();
    }

    @ActivityScope
    @Provides
    JqSearchContract.Model provideJqSearchModel(JqSearchModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    List<JqSearchHistoryEntity> provideSearchHistoryBeanList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    JqSearchHistoryAdapter provideHomeAdapter(List<JqSearchHistoryEntity> beanList) {
        return new JqSearchHistoryAdapter(beanList);
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
    List<JqItemEntity> provideSearchResultBeanList() {
        return new ArrayList<>();
    }

}