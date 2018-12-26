package com.netposa.component.rltk.di.module;

import android.content.Context;

import com.jess.arms.di.scope.ActivityScope;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.Module;
import dagger.Provides;

import com.netposa.component.rltk.mvp.contract.ChooseDeviceContract;
import com.netposa.component.rltk.mvp.model.ChooseDeviceModel;
import com.netposa.component.rltk.mvp.model.entity.OrgChoseEntity;
import com.netposa.component.rltk.mvp.model.entity.SearchDeviceRequestEntity;
import com.netposa.component.rltk.mvp.model.entity.SearchDeviceResponseEntity;
import com.netposa.component.rltk.mvp.ui.adapter.ChooseDeviceAdapter;

import java.util.ArrayList;
import java.util.List;


@Module
public class ChooseDeviceModule {
    private ChooseDeviceContract.View view;
    private Context mContext;

    /**
     * 构建ChoseDeviceModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ChooseDeviceModule(Context context, ChooseDeviceContract.View view) {
        this.mContext = context;
        this.view = view;
    }

    @ActivityScope
    @Provides
    ChooseDeviceContract.View provideChoseDeviceView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ChooseDeviceContract.Model provideChoseDeviceModel(ChooseDeviceModel model) {
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
    ChooseDeviceAdapter providerDeviceParentAdapter(List<OrgChoseEntity> data) {
        return new ChooseDeviceAdapter(data);
    }

    @ActivityScope
    @Provides
    SearchDeviceRequestEntity provideRequestEntity() {
        return new SearchDeviceRequestEntity();
    }

    @ActivityScope
    @Provides
    Context provideContext() {
        return mContext;
    }
}