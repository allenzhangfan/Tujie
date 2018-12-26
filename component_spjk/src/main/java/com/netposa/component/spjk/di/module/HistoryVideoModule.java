package com.netposa.component.spjk.di.module;

import android.content.Context;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.jess.arms.di.scope.ActivityScope;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.Module;
import dagger.Provides;

import com.netposa.commonres.widget.CaptureTimeHelper;
import com.netposa.commonres.widget.CustomerLoadMoreView;
import com.netposa.component.room.entity.SpjkCollectionDeviceEntity;
import com.netposa.component.spjk.mvp.contract.HistoryVideoContract;
import com.netposa.component.spjk.mvp.model.HistoryVideoModel;
import com.netposa.component.spjk.mvp.model.entity.HistoryVideoRequestEntity;
import com.netposa.component.spjk.mvp.model.entity.HistoryVideoResponseEntity;
import com.netposa.component.spjk.mvp.ui.adapter.HistoryVideoAdapter;

import java.util.ArrayList;
import java.util.List;


@Module
public class HistoryVideoModule {
    private Context mContext;
    private HistoryVideoContract.View view;
    private FragmentManager mFm;

    /**
     * 构建HistoryVideoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public HistoryVideoModule(Context context, HistoryVideoContract.View view, FragmentManager fm) {
        mContext = context;
        this.view = view;
        mFm = fm;
    }

    @ActivityScope
    @Provides
    HistoryVideoContract.View provideHistoryVideoView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    HistoryVideoContract.Model provideHistoryVideoModel(HistoryVideoModel model) {
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
    List<HistoryVideoResponseEntity.VideoListEntity> provideBeanList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    HistoryVideoAdapter provideHomeAdapter(List<HistoryVideoResponseEntity.VideoListEntity> beanList) {
        return new HistoryVideoAdapter(beanList);
    }

    @ActivityScope
    @Provides
    Context provideContext() {
        return mContext;
    }

    @ActivityScope
    @Provides
    HistoryVideoRequestEntity provideRequestEntity() {
        return new HistoryVideoRequestEntity();
    }

    @ActivityScope
    @Provides
    LoadMoreView provideLoadMoreView() {
        return new CustomerLoadMoreView();
    }

    @ActivityScope
    @Provides
    CaptureTimeHelper provideCaptureTimeHelper() {
        return new CaptureTimeHelper(mFm);
    }

    @ActivityScope
    @Provides
    SpjkCollectionDeviceEntity provideSpjkCollectionDevice() {
        return new SpjkCollectionDeviceEntity();
    }
}