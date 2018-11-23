package com.netposa.component.jq.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.netposa.component.jq.mvp.contract.AlarmDetailsContract;
import com.netposa.component.jq.mvp.model.AlarmDetailsModel;


@Module
public class AlarmDetailsModule {
    private AlarmDetailsContract.View view;

    /**
     * 构建AlarmCarDetailsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public AlarmDetailsModule(AlarmDetailsContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    AlarmDetailsContract.View provideAlarmCarDetailsView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    AlarmDetailsContract.Model provideAlarmCarDetailsModel(AlarmDetailsModel model) {
        return model;
    }
}