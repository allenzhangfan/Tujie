package com.netposa.component.sfjb.di.module;

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
import com.netposa.component.sfjb.mvp.contract.SearchLibContract;
import com.netposa.component.sfjb.mvp.model.SearchLibModel;
import com.netposa.component.room.entity.SfjbSearchHistoryEntity;
import com.netposa.component.sfjb.mvp.model.entity.SfjbSearchResultEntity;
import com.netposa.component.sfjb.mvp.ui.adapter.SfjbSearchHistoryAdapter;
import com.netposa.component.sfjb.mvp.ui.adapter.SfjbSearchResulthAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;


@Module
public class SearchLibModule {
    private Context mContext;
    private SearchLibContract.View view;

    /**
     * 构建SearchLibModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public SearchLibModule(Context context, SearchLibContract.View view) {
        this.mContext=context;
        this.view = view;
    }

    @ActivityScope
    @Provides
    Context provideContext(){
        return mContext;
    }
    @ActivityScope
    @Provides
    SearchLibContract.View provideSearchLibView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    SearchLibContract.Model provideSearchLibModel(SearchLibModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    @Named("SearchSfjbHistory")
    RecyclerView.LayoutManager provideSearchHistoryLayoutManager() {
        return new LinearLayoutManager(mContext);
    }

    @ActivityScope
    @Provides
    @Named("SearchSfjbResult")
    RecyclerView.LayoutManager provideSearchResultLayoutManager() {
        return new LinearLayoutManager(mContext);
    }
    @ActivityScope
    @Provides
    @Named("SearchSfjbHistory")
    RecyclerView.ItemAnimator provideSearchHistoryItemAnimator() {
        return new DefaultItemAnimator();
    }

    @ActivityScope
    @Provides
    @Named("SearchSfjbResult")
    RecyclerView.ItemAnimator provideSearchResultItemAnimator() {
        return new DefaultItemAnimator();
    }

    @ActivityScope
    @Provides
    SfjbSearchHistoryAdapter provideHomeAdapter(List<SfjbSearchHistoryEntity> beanList) {
        return new SfjbSearchHistoryAdapter(beanList);
    }
    @ActivityScope
    @Provides
    SfjbSearchResulthAdapter provideResultAdapter(List<SfjbSearchResultEntity> list){
        return new SfjbSearchResulthAdapter(list);
    }

    @ActivityScope
    @Provides
    LoadMoreView provideLoadMoreView() {
        return new CustomerLoadMoreView();
    }

    @ActivityScope
    @Provides
    List<SfjbSearchHistoryEntity> provideSearchHistoryBeanList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    List<SfjbSearchResultEntity> provideSearchResultBeanList() {
        return new ArrayList<>();
    }
}