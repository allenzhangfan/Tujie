package com.netposa.component.spjk.di.module;

import android.content.Context;

import com.jess.arms.di.scope.ActivityScope;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;

import com.netposa.common.utils.SystemUtil;
import com.netposa.component.room.entity.SpjkCollectionDeviceEntity;
import com.netposa.component.spjk.mvp.contract.NeighbouringDevicesContract;
import com.netposa.component.spjk.mvp.model.NeighbouringDevicesModel;
import com.netposa.component.spjk.mvp.model.entity.OneKilometerCamerasRequestEntity;

@Module
public class NeighbouringDevicesModule {
    private Context mContext;
    private NeighbouringDevicesContract.View view;
    private FragmentManager mFm;

    /**
     * 构建NeighbouringDevicesModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param context
     * @param view
     * @param fm
     */
    public NeighbouringDevicesModule(Context context, NeighbouringDevicesContract.View view, FragmentManager fm) {
        mContext = context;
        this.view = view;
        mFm = fm;
    }

    @ActivityScope
    @Provides
    NeighbouringDevicesContract.View provideNeighbouringDevicesView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    NeighbouringDevicesContract.Model provideNeighbouringDevicesModel(NeighbouringDevicesModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    Context provideContext() {
        return mContext;
    }

    @ActivityScope
    @Provides
    OneKilometerCamerasRequestEntity provideOneKilometerCamerasRequestEntity() {
        return new OneKilometerCamerasRequestEntity();
    }

    @ActivityScope
    @Provides
    FragmentManager provideFragmentManager() {
        return mFm;
    }

    @ActivityScope
    @Provides
    SpjkCollectionDeviceEntity provideSpjkCollectionDevice() {
        return new SpjkCollectionDeviceEntity();
    }

    @ActivityScope
    @Provides
    SystemUtil provideSystemUtil(){
        return new SystemUtil();
    }
}