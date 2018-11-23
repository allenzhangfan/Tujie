package com.netposa.component.gzt.di.module;

import android.content.Context;

import com.jess.arms.di.scope.FragmentScope;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.Module;
import dagger.Provides;

import com.netposa.component.gzt.mvp.contract.GztContract;
import com.netposa.component.gzt.mvp.model.GztModel;
import com.netposa.component.gzt.mvp.model.entity.GztEntity;
import com.netposa.component.gzt.mvp.ui.adapter.GztAdapter;

import java.util.ArrayList;
import java.util.List;


@Module
public class GztModule {
    private static final int APP_SPAN_COUNT = 3;
    private Context mContext;
    private GztContract.View mView;

    /**
     * 构建GztModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public GztModule(Context context,GztContract.View view) {
        this.mContext = context;
        this.mView = view;
    }

    @FragmentScope
    @Provides
    GztContract.View provideGztView() {
        return this.mView;
    }

    @FragmentScope
    @Provides
    GztContract.Model provideGztModel(GztModel model) {
        return model;
    }

    @FragmentScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new GridLayoutManager(mContext, APP_SPAN_COUNT);
    }

    @FragmentScope
    @Provides
    RecyclerView.ItemAnimator provideItemAnimator() {
        return new DefaultItemAnimator();
    }

    @FragmentScope
    @Provides
    List<GztEntity> provideBeanList() {
        return new ArrayList<>();
    }

    @FragmentScope
    @Provides
    GztAdapter provideHomeAdapter(List<GztEntity> beanList) {
        return new GztAdapter(beanList);
    }
}