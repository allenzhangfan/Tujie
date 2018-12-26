package com.netposa.component.sfjb.di.module;

import android.content.Context;

import com.jess.arms.di.scope.ActivityScope;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.Module;
import dagger.Provides;

import com.netposa.component.sfjb.mvp.contract.ChooseCompareLibContract;
import com.netposa.component.sfjb.mvp.model.ChooseCompareLibModel;
import com.netposa.component.sfjb.mvp.model.entity.LibTreeRequestEntity;
import com.netposa.component.sfjb.mvp.model.entity.OrgChoseEntity;
import com.netposa.component.sfjb.mvp.ui.adapter.ChooseLibAdapter;

import java.util.ArrayList;
import java.util.List;


@Module
public class ChooseCompareLibModule {
    private Context mContext;
    private ChooseCompareLibContract.View view;

    /**
     * 构建ChoseComparLibModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ChooseCompareLibModule(Context context, ChooseCompareLibContract.View view) {
        this.mContext = context;
        this.view = view;
    }

    @ActivityScope
    @Provides
    ChooseCompareLibContract.View provideChoseComparLibView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ChooseCompareLibContract.Model provideChoseComparLibModel(ChooseCompareLibModel model) {
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
    ChooseLibAdapter providerDeviceParentAdapter(List<OrgChoseEntity> data) {
        return new ChooseLibAdapter(data);
    }

    @ActivityScope
    @Provides
    LibTreeRequestEntity providerRequestEntity() {
        return new LibTreeRequestEntity();
    }

    @ActivityScope
    @Provides
    Context providerContext() {
        return mContext;
    }
}