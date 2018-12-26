package com.netposa.component.jq.di.module;


import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;

import com.jess.arms.di.scope.FragmentScope;
import com.netposa.component.jq.mvp.contract.DailyContract;
import com.netposa.component.jq.mvp.model.DailyModel;
import com.netposa.component.jq.mvp.ui.adapter.DailyPagerAdapter;


@Module
public class DailyModule {
    private DailyContract.View view;
    private FragmentManager mFm;

    /**
     * 构建DailyModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     * @param fm
     */
    public DailyModule(DailyContract.View view, FragmentManager fm) {
        this.view = view;
        mFm = fm;
    }

    @FragmentScope
    @Provides
    DailyContract.View provideDailyView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    DailyContract.Model provideDailyModel(DailyModel model) {
        return model;
    }

    @FragmentScope
    @Provides
    DailyPagerAdapter provideDailyPagerAdapter() {
        return new DailyPagerAdapter(mFm);
    }
}