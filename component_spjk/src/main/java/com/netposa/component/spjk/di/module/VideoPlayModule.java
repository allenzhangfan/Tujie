package com.netposa.component.spjk.di.module;

import android.content.Context;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.netposa.component.room.entity.SpjkCollectionDeviceEntiry;
import com.netposa.component.spjk.mvp.contract.VideoPlayContract;
import com.netposa.component.spjk.mvp.model.VideoPlayModel;
import com.netposa.component.spjk.mvp.model.entity.PtzDirectionRequestEntity;

import java.util.concurrent.ArrayBlockingQueue;


@Module
public class VideoPlayModule {
    private VideoPlayContract.View view;

    /**
     * 构建RealVideoPlayModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public VideoPlayModule(VideoPlayContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    VideoPlayContract.View provideRealVideoPlayView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    VideoPlayContract.Model provideRealVideoPlayModel(VideoPlayModel model) {
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
    PtzDirectionRequestEntity providePtzDirectionRequestEntity(){ return new PtzDirectionRequestEntity();}

    @ActivityScope
    @Provides
    SpjkCollectionDeviceEntiry provideSpjkCollectionDevice(){
        return new SpjkCollectionDeviceEntiry();
    }
}