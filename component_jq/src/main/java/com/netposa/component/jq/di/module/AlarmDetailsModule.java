package com.netposa.component.jq.di.module;

import android.content.Context;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.netposa.commonres.widget.Dialog.LoadingDialog;
import com.netposa.commonres.widget.Dialog.LoadingDialogFragment;
import com.netposa.commonres.widget.Dialog.LottieDialogFragment;
import com.netposa.component.jq.mvp.contract.AlarmDetailsContract;
import com.netposa.component.jq.mvp.model.AlarmDetailsModel;
import com.netposa.component.jq.mvp.model.entity.ProcessAlarmRequestEntity;


@Module
public class AlarmDetailsModule {
    private Context mContext;
    private AlarmDetailsContract.View view;

    /**
     * 构建AlarmCarDetailsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public AlarmDetailsModule(Context context, AlarmDetailsContract.View view) {
        this.mContext = context;
        this.view = view;
    }

    @ActivityScope
    @Provides
    AlarmDetailsContract.View provideAlarmCarDetailsView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    AlarmDetailsContract.Model provideAlarmCarDetailsModel(AlarmDetailsModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    Context provideCOntext() {
        return mContext;
    }

    @ActivityScope
    @Provides
    ProcessAlarmRequestEntity provideEntity(){return new ProcessAlarmRequestEntity();}

    @ActivityScope
    @Provides
    LottieDialogFragment provideDialogFragment() { return new LoadingDialogFragment(); }
}