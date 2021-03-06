package com.netposa.component.clcx.di.module;

import android.content.Context;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.jess.arms.di.scope.ActivityScope;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.Module;
import dagger.Provides;

import com.netposa.common.utils.SizeUtils;
import com.netposa.commonres.widget.CustomerLoadMoreView;
import com.netposa.commonres.widget.itemDecoration.SpacesItemDecoration;
import com.netposa.component.clcx.mvp.contract.QueryResultContract;
import com.netposa.component.clcx.mvp.model.QueryResultModel;
import com.netposa.component.clcx.mvp.model.entity.QueryCarSearchEntity;
import com.netposa.component.clcx.mvp.model.entity.QueryCarSearchResponseEntity;
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
    public QueryResultModule(Context context, QueryResultContract.View view) {
        this.mContext = context;
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
        return new GridLayoutManager(mContext, 2);
    }

    @ActivityScope
    @Provides
    RecyclerView.ItemAnimator provideItemAnimator() {
        return new DefaultItemAnimator();
    }

    @ActivityScope
    @Provides
    RecyclerView.ItemDecoration provideGridItemDecoration() {
        int itemMargin = SizeUtils.dp2px(16);
        return new SpacesItemDecoration(itemMargin, itemMargin, itemMargin, itemMargin);
    }

    @ActivityScope
    @Provides
    List<QueryCarSearchResponseEntity> provideBeanList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    QueryCarSearchEntity provideRequestBean() {
        return new QueryCarSearchEntity();
    }

    @ActivityScope
    @Provides
    QueryResultAdapter provideCarAdapter(List<QueryCarSearchResponseEntity> list) {
        return new QueryResultAdapter(mContext, list);
    }

    @ActivityScope
    @Provides
    LoadMoreView providerLoadmoreView() {
        return new CustomerLoadMoreView();
    }
}