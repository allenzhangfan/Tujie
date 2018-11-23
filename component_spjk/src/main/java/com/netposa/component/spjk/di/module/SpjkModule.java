package com.netposa.component.spjk.di.module;

import android.content.Context;

import com.jess.arms.di.scope.ActivityScope;
import com.netposa.component.room.entity.SpjkCollectionDeviceEntiry;
import com.netposa.component.spjk.mvp.contract.SpjkContract;
import com.netposa.component.spjk.mvp.model.SpjkModel;
import com.netposa.component.spjk.mvp.model.entity.OneKilometerCamerasRequestEntity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;


@Module
public class SpjkModule {
    private Context mContext;
    private SpjkContract.View view;
    private FragmentManager mFm;

    /**
     * 构建SpjkModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public SpjkModule(Context context,SpjkContract.View view,FragmentManager fm) {
        mContext = context;
        this.view = view;
        mFm = fm;
    }

    @ActivityScope
    @Provides
    SpjkContract.View provideSpjkView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    SpjkContract.Model provideSpjkModel(SpjkModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    Context provideContext(){
        return mContext;
    }

    @ActivityScope
    @Provides
    OneKilometerCamerasRequestEntity provideOneKilometerCamerasRequestEntity(){
        return new OneKilometerCamerasRequestEntity();
    }

    @ActivityScope
    @Provides
    RxPermissions provideRxPermissions() {
        return new RxPermissions((FragmentActivity) view);
    }

    @ActivityScope
    @Provides
    FragmentManager provideFragmentManager(){
        return mFm;
    }

    @ActivityScope
    @Provides
    SpjkCollectionDeviceEntiry provideSpjkCollectionDevice(){
        return new SpjkCollectionDeviceEntiry();
    }
}