package com.netposa.component.spjk.di.module;

import android.content.Context;

import com.jess.arms.di.scope.ActivityScope;
import com.netposa.commonres.widget.Dialog.LoadingDialogFragment;
import com.netposa.commonres.widget.Dialog.LottieDialogFragment;
import com.netposa.component.spjk.mvp.contract.DevicelistContract;
import com.netposa.component.spjk.mvp.model.DevicelistModel;
import com.netposa.component.spjk.mvp.model.entity.SpjkListDeviceRequestEntity;
import com.netposa.component.spjk.mvp.model.entity.SpjkListDeviceResponseEntity;
import com.netposa.component.spjk.mvp.ui.adapter.DeviceParentAdapter;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.Module;
import dagger.Provides;

import java.util.ArrayList;
import java.util.List;


@Module
public class DevicelistModule {
    private Context mContext;
    private DevicelistContract.View view;

    /**
     * 构建DevicelistModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public DevicelistModule(Context context, DevicelistContract.View view) {
        this.mContext = context;
        this.view = view;
    }

    @ActivityScope
    @Provides
    DevicelistContract.View provideDevicelistView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    DevicelistContract.Model provideDevicelistModel(DevicelistModel model) {
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
    Context provideContext() {
        return mContext;
    }

    @ActivityScope
    @Provides
    List<SpjkListDeviceResponseEntity.DeviceTreeListBean> provideBeanList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    DeviceParentAdapter providerDeviceParentAdapter(List<SpjkListDeviceResponseEntity.DeviceTreeListBean> data) {
        return new DeviceParentAdapter(data);
    }

    @ActivityScope
    @Provides
    SpjkListDeviceRequestEntity provideRequestEntity() {
        return new SpjkListDeviceRequestEntity();
    }

    @ActivityScope
    @Provides
    LottieDialogFragment provideDialogFragment() {
        return new LoadingDialogFragment();
    }
}