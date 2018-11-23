package com.netposa.component.spjk.di.module;

import android.content.Context;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.netposa.component.room.entity.SpjkCollectionDeviceEntiry;
import com.netposa.component.spjk.mvp.contract.HistoryVideoPlayContract;
import com.netposa.component.spjk.mvp.model.HistoryVideoPlayModel;

import java.util.concurrent.ArrayBlockingQueue;


@Module
public class HistoryVideoPlayModule {
    private HistoryVideoPlayContract.View view;

    /**
     * 构建HistoryVideoPlayModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public HistoryVideoPlayModule(HistoryVideoPlayContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    HistoryVideoPlayContract.View provideHistoryVideoPlayView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    HistoryVideoPlayContract.Model provideHistoryVideoPlayModel(HistoryVideoPlayModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    Context provideContext(){
        return (Context) view;
    }

    @ActivityScope
    @Provides
    ArrayBlockingQueue<String> provideBlockingQueueList(){
        return new ArrayBlockingQueue<>(30);
    }

    @ActivityScope
    @Provides
    SpjkCollectionDeviceEntiry provideSpjkCollectionDevice(){
        return new SpjkCollectionDeviceEntiry();
    }
}