package com.netposa.component.clcx.di.module;

import android.content.Context;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.netposa.commonres.widget.Dialog.LoadingDialogFragment;
import com.netposa.commonres.widget.Dialog.LottieDialogFragment;
import com.netposa.component.clcx.mvp.contract.ViewCarPathContract;
import com.netposa.component.clcx.mvp.model.ViewCarPathModel;
import com.netposa.component.clcx.mvp.model.entity.VehicleTrackRequestEntity;


@Module
public class ViewCarPathModule {
    private ViewCarPathContract.View view;

    /**
     * 构建ViewCarPathModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ViewCarPathModule(ViewCarPathContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ViewCarPathContract.View provideViewCarPathView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ViewCarPathContract.Model provideViewCarPathModel(ViewCarPathModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    Context provideContext() {
        return (Context) view;
    }

    @ActivityScope
    @Provides
    VehicleTrackRequestEntity provideVehicleTrackEntity(){
        return  new VehicleTrackRequestEntity();
    }

    @ActivityScope
    @Provides
    LottieDialogFragment provideDialogFragment() {
        return new LoadingDialogFragment();
    }

}