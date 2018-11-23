package com.netposa.component.jq.di.module;

import android.content.Context;

import com.jess.arms.di.scope.FragmentScope;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.Module;
import dagger.Provides;

import com.netposa.component.jq.mvp.contract.JqContract;
import com.netposa.component.jq.mvp.model.JqModel;
import com.netposa.component.jq.mvp.model.entity.JqItemEntity;
import com.netposa.component.jq.mvp.ui.adapter.JqAdapter;

import java.util.ArrayList;
import java.util.List;


@Module
public class JqModule {
    private Context mContext;
    private JqContract.View view;

    /**
     * 构建JqModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public JqModule(Context context, JqContract.View view) {
        mContext = context;
        this.view = view;
    }

    @FragmentScope
    @Provides
    JqContract.View provideJqView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    JqContract.Model provideJqModel(JqModel model) {
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
    Context provideContext(){
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
        return new JqAdapter(mContext,beanList);
    }
}