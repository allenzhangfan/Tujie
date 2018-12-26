package com.netposa.component.ytst.di.module;

import android.content.Context;

import com.jess.arms.di.scope.ActivityScope;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.Module;
import dagger.Provides;

import com.netposa.component.room.entity.YtstCarAndPeopleEntity;
import com.netposa.component.ytst.mvp.contract.TrackContract;
import com.netposa.component.ytst.mvp.model.TrackModel;
import com.netposa.component.ytst.mvp.model.entity.ImgSearchResponseEntity;
import com.netposa.component.ytst.mvp.ui.adapter.TrackAdapter;

import java.util.ArrayList;
import java.util.List;


@Module
public class TrackModule {
    private TrackContract.View view;
    private Context mContext;
    private static final int APP_SPAN_COUNT = 3;

    /**
     * 构建TrackModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public TrackModule(Context context, TrackContract.View view) {
        mContext = context;
        this.view = view;
    }

    @ActivityScope
    @Provides
    TrackContract.View provideTrackView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    TrackContract.Model provideTrackModel(TrackModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    Context provideContext() {
        return mContext;
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new GridLayoutManager(mContext, APP_SPAN_COUNT, RecyclerView.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    RecyclerView.ItemAnimator provideItemAnimator() {
        return new DefaultItemAnimator();
    }


    @ActivityScope
    @Provides
    TrackAdapter provideTrackAdapter(List<YtstCarAndPeopleEntity> list) {
        return new TrackAdapter(mContext, list);
    }

    @ActivityScope
    @Provides
    List<YtstCarAndPeopleEntity> provideBeanList() {
        return new ArrayList<>();
    }
}