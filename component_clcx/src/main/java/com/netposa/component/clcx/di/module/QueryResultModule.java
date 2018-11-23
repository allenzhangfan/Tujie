package com.netposa.component.clcx.di.module;

import android.content.Context;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.jess.arms.di.scope.ActivityScope;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.Module;
import dagger.Provides;

import com.netposa.commonres.widget.CustomerLoadMoreView;
import com.netposa.component.clcx.mvp.contract.QueryResultContract;
import com.netposa.component.clcx.mvp.model.QueryResultModel;
import com.netposa.component.clcx.mvp.model.entity.QueryResultEntity;
import com.netposa.component.clcx.mvp.ui.adapter.QueryResultAdapter;

import java.util.ArrayList;
import java.util.List;


@Module
public class QueryResultModule {
    private Context mContext;
    private QueryResultContract.View view;

    /**
     * 构建QueryResultModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public QueryResultModule(Context context,QueryResultContract.View view) {
        this.mContext=context;
        this.view = view;
    }

    @ActivityScope
    @Provides
    QueryResultContract.View provideQueryResultView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    QueryResultContract.Model provideQueryResultModel(QueryResultModel model) {
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
        return new GridLayoutManager(mContext,2);
    }

    @ActivityScope
    @Provides
    RecyclerView.ItemAnimator provideItemAnimator() {
        return new DefaultItemAnimator();
    }

    @ActivityScope
    @Provides
    List<QueryResultEntity> provideBeanList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    QueryResultAdapter provideCarAdapter(List<QueryResultEntity> list){
        return new QueryResultAdapter(list);
    }

    @ActivityScope
    @Provides
    LoadMoreView providerLoadmoreView(){
        return new CustomerLoadMoreView();
    }
}