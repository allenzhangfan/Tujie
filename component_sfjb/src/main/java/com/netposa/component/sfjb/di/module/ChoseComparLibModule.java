package com.netposa.component.sfjb.di.module;

import android.content.Context;

import com.jess.arms.di.scope.ActivityScope;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.Module;
import dagger.Provides;

import com.netposa.component.sfjb.mvp.contract.ChoseComparLibContract;
import com.netposa.component.sfjb.mvp.model.ChoseComparLibModel;
import com.netposa.component.sfjb.mvp.model.entity.OrgChoseEntity;
import com.netposa.component.sfjb.mvp.ui.adapter.ChoseLibAdapter;

import java.util.ArrayList;
import java.util.List;


@Module
public class ChoseComparLibModule {
    private Context mContext;
    private ChoseComparLibContract.View view;

    /**
     * 构建ChoseComparLibModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ChoseComparLibModule(Context context, ChoseComparLibContract.View view) {
        this.mContext = context;
        this.view = view;
    }

    @ActivityScope
    @Provides
    ChoseComparLibContract.View provideChoseComparLibView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ChoseComparLibContract.Model provideChoseComparLibModel(ChoseComparLibModel model) {
        return model;
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
    List<OrgChoseEntity> provideBeanList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    ChoseLibAdapter providerDeviceParentAdapter(List<OrgChoseEntity> data) {
        return new ChoseLibAdapter(data);
    }
}