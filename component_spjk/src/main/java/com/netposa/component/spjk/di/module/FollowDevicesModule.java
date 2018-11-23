package com.netposa.component.spjk.di.module;

import android.content.Context;

import com.jess.arms.di.scope.ActivityScope;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.Module;
import dagger.Provides;

import com.netposa.component.room.entity.SpjkCollectionDeviceEntiry;
import com.netposa.component.spjk.mvp.contract.FollowDevicesContract;
import com.netposa.component.spjk.mvp.model.FollowDevicesModel;
import com.netposa.component.spjk.mvp.model.entity.HistoryVideoResponseEntity;
import com.netposa.component.spjk.mvp.ui.adapter.HistoryVideoAdapter;

import java.util.ArrayList;
import java.util.List;


@Module
public class FollowDevicesModule {
    private Context mContext;
    private FollowDevicesContract.View view;

    /**
     * 构建FollowDevicesModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public FollowDevicesModule(Context context,FollowDevicesContract.View view) {
        mContext = context;
        this.view = view;
    }

    @ActivityScope
    @Provides
    FollowDevicesContract.View provideFollowDevicesView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    FollowDevicesContract.Model provideFollowDevicesModel(FollowDevicesModel model) {
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
    HistoryVideoAdapter provideHomeAdapter(List<HistoryVideoResponseEntity.VideoListEntity> beanList) {
        return new HistoryVideoAdapter(beanList);
    }

    @ActivityScope
    @Provides
    Context provideContext(){
        return mContext;
    }

    @ActivityScope
    @Provides
    List<SpjkCollectionDeviceEntiry> provideBeanList() {
        return new ArrayList<>();
    }
}