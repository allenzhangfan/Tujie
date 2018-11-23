package com.netposa.component.jq.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.netposa.component.jq.mvp.contract.ControlTaskDetailContract;
import com.netposa.component.jq.mvp.model.ControlTaskDetailModel;


@Module
public class ControlTaskDetailModule {
    private ControlTaskDetailContract.View view;

    /**
     * 构建ControlTaskDetailModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ControlTaskDetailModule(ControlTaskDetailContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ControlTaskDetailContract.View provideControlTaskDetailView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ControlTaskDetailContract.Model provideControlTaskDetailModel(ControlTaskDetailModel model) {
        return model;
    }
}