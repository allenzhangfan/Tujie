package com.netposa.component.ytst.di.module;

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
import com.netposa.component.ytst.mvp.contract.SelectTargetContract;
import com.netposa.component.ytst.mvp.model.SelectTargetModel;
import com.netposa.component.ytst.mvp.model.entity.ImgSearchRequestEntity;
import com.netposa.component.ytst.mvp.model.entity.PonitEntity;
import com.netposa.component.ytst.mvp.ui.adapter.SelectTargetAdapter;

import java.util.ArrayList;
import java.util.List;


@Module
public class SelectTargetModule {

    private static final int APP_SPAN_COUNT = 2;
    private Context mContext;
    private SelectTargetContract.View view;

    /**
     * 构建SelectTargetModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public SelectTargetModule(Context context, SelectTargetContract.View view) {
        mContext = context;
        this.view = view;
    }

    @ActivityScope
    @Provides
    SelectTargetContract.View provideSelectTargetView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    SelectTargetContract.Model provideSelectTargetModel(SelectTargetModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    Context provideContext() {
        return mContext;
    }


    @ActivityScope
    @Provides
    LoadMoreView providerLoadmoreView() {
        return new CustomerLoadMoreView();
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new GridLayoutManager(mContext, APP_SPAN_COUNT, RecyclerView.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    RecyclerView.ItemAnimator provideItemAnimator() {
        return new DefaultItemAnimator();
    }

    @ActivityScope
    @Provides
    RecyclerView.ItemDecoration provideGridItemDecoration() {
        int itemMargin = SizeUtils.dp2px(16);// 整个RecyclerView与左右两侧的间距
        return new SpacesItemDecoration(itemMargin, itemMargin, itemMargin, itemMargin);
    }

    @ActivityScope
    @Provides
    List<PonitEntity> provideBeanList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    SelectTargetAdapter provideFaceLibraryAdapter(List<PonitEntity> beanList) {
        return new SelectTargetAdapter(mContext, beanList);
    }

    @ActivityScope
    @Provides
    ImgSearchRequestEntity provideImgSearchRequestEntity() {
        return new ImgSearchRequestEntity();
    }
}