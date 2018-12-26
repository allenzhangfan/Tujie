package com.netposa.component.jq.di.module;


import android.content.Context;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.Module;
import dagger.Provides;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.jess.arms.di.scope.FragmentScope;
import com.netposa.common.utils.SystemUtil;
import com.netposa.commonres.widget.CustomerLoadMoreView;
import com.netposa.component.jq.mvp.contract.FaceDeployContract;
import com.netposa.component.jq.mvp.model.FaceDeployModel;
import com.netposa.component.jq.mvp.model.entity.AlarmListRequestEntity;
import com.netposa.common.entity.push.JqItemEntity;
import com.netposa.component.jq.mvp.ui.adapter.JqAdapter;

import java.util.ArrayList;
import java.util.List;


@Module
public class FaceDeployModule {

    private Context mContext;
    private FaceDeployContract.View view;

    /**
     * 构建FaceDeployModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public FaceDeployModule(FaceDeployContract.View view,Context context) {
        this.view = view;
        mContext = context;
    }

    @FragmentScope
    @Provides
    FaceDeployContract.View provideFaceDeployView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    FaceDeployContract.Model provideFaceDeployModel(FaceDeployModel model) {
        return model;
    }

    @FragmentScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(mContext);
    }

    @FragmentScope
    @Provides
    RecyclerView.ItemAnimator provideItemAnimator() {
        return new DefaultItemAnimator();
    }

    @FragmentScope
    @Provides
    Context provideContext() {
        return mContext;
    }

    @FragmentScope
    @Provides
    List<JqItemEntity> provideBeanList() {
        return new ArrayList<>();
    }

    @FragmentScope
    @Provides
    JqAdapter provideHomeAdapter(List<JqItemEntity> beanList) {
        return new JqAdapter(mContext, beanList);
    }

    @FragmentScope
    @Provides
    AlarmListRequestEntity provideAlarmListRequestEntity(){
        return new AlarmListRequestEntity();
    }

    @FragmentScope
    @Provides
    LoadMoreView providerLoadmoreView() {
        return new CustomerLoadMoreView();
    }

    @FragmentScope
    @Provides
    SystemUtil providerSystemUtil() {
        return new SystemUtil();
    }
}