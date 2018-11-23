package com.netposa.component.clcx.di.module;

import com.jess.arms.di.scope.ActivityScope;

import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;

import com.netposa.commonres.widget.CaptureTimeHelper;
import com.netposa.component.clcx.mvp.contract.QueryCarContract;
import com.netposa.component.clcx.mvp.model.QueryCarModel;
import com.netposa.component.clcx.mvp.ui.widget.BottomSheetDialogFragment;


@Module
public class QueryCarModule {
    private QueryCarContract.View view;
    private FragmentManager mFm;

    /**
     * 构建QueryCarModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public QueryCarModule(QueryCarContract.View view,FragmentManager fm) {
        this.view = view;
        mFm = fm;
    }

    @ActivityScope
    @Provides
    QueryCarContract.View provideQueryCarView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    QueryCarContract.Model provideQueryCarModel(QueryCarModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    CaptureTimeHelper provideCaptureTimeHelper(){
        return new CaptureTimeHelper(mFm);
    }

    @ActivityScope
    @Provides
    BottomSheetDialogFragment provideBottomSheetDialogFragment(){
        return new BottomSheetDialogFragment();
    }
}