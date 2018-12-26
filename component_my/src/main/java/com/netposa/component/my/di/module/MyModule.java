package com.netposa.component.my.di.module;

import android.content.Context;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.di.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

import com.netposa.component.my.mvp.contract.MyContract;
import com.netposa.component.my.mvp.model.MyModel;
import com.netposa.component.my.mvp.model.entity.MenuEntity;
import com.netposa.component.my.mvp.ui.adapter.MyMenuAdapter;

import java.util.ArrayList;
import java.util.List;


@Module
public class MyModule {
    private Context mContext;
    private MyContract.View view;

    /**
     * 构建MyModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MyModule(Context context,MyContract.View view) {
        this.mContext=context;
        this.view = view;
    }

    @FragmentScope
    @Provides
    MyContract.View provideMyView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    MyContract.Model provideMyModel(MyModel model) {
        return model;
    }

    @FragmentScope
    @Provides
    List<MenuEntity> provideBeanList() {
        return new ArrayList<>();
    }

    @FragmentScope
    @Provides
    MyMenuAdapter provideAdapter(List<MenuEntity> beanList) {
        return new MyMenuAdapter(beanList);
    }

    @FragmentScope
    @Provides
    Context provideContext(){
        return mContext;
    }
}