package com.netposa.component.clcx.di.module;

import android.content.Context;

import com.jess.arms.di.scope.ActivityScope;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.Module;
import dagger.Provides;

import com.netposa.component.clcx.mvp.contract.CarTypeContract;
import com.netposa.component.clcx.mvp.model.CarTypeModel;
import com.netposa.component.clcx.mvp.model.entity.CarTypeEntry;
import com.netposa.component.clcx.mvp.ui.adapter.CarTypeAdapter;

import java.util.ArrayList;
import java.util.List;


@Module
public class CarTypeModule {
    private CarTypeContract.View view;

    /**
     * 构建CarTypeModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public CarTypeModule(CarTypeContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CarTypeContract.View provideCarTypeView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CarTypeContract.Model provideCarTypeModel(CarTypeModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager((Context) view);
    }

    @ActivityScope
    @Provides
    RecyclerView.ItemAnimator provideItemAnimator() {
        return new DefaultItemAnimator();
    }

    @ActivityScope
    @Provides
    CarTypeAdapter provideCarTypeAdapter(ArrayList<CarTypeEntry> beanList) {
        return new CarTypeAdapter(beanList);
    }

    @ActivityScope
    @Provides
    ArrayList<CarTypeEntry> provideListCarTypeEntry() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    List<String> provideList() {
        return new ArrayList<>();
    }

}