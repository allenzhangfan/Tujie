package com.netposa.component.my.di.module;

import android.content.Context;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.jess.arms.di.scope.ActivityScope;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.Module;
import dagger.Provides;

import com.netposa.commonres.widget.Dialog.LoadingDialogFragment;
import com.netposa.commonres.widget.Dialog.LottieDialogFragment;
import com.netposa.commonres.widget.Dialog.SweetDialog;
import com.netposa.component.my.mvp.contract.PersonInfoContract;
import com.netposa.component.my.mvp.model.PersonInfoModel;
import com.netposa.component.my.mvp.ui.adapter.PersonInfoAdapter;

import java.util.ArrayList;
import java.util.List;

@Module
public class PersonInfoModule {
    private Context mContext;
    private PersonInfoContract.View view;

    /**
     * 构建PersonInfoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public PersonInfoModule(Context context, PersonInfoContract.View view) {
        mContext = context;
        this.view = view;
    }

    @ActivityScope
    @Provides
    PersonInfoContract.View providePersonInfoView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    PersonInfoContract.Model providePersonInfoModel(PersonInfoModel model) {
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
    List<MultiItemEntity> provideBeanList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    PersonInfoAdapter provideSocialRelationsAdapter(List<MultiItemEntity> beanlist) {
        return new PersonInfoAdapter(beanlist);
    }

    @ActivityScope
    @Provides
    LottieDialogFragment provideDialogFragment() {
        return new LoadingDialogFragment();
    }

    @ActivityScope
    @Provides
    SweetDialog provideSweetDialog() {
        return new SweetDialog(mContext);
    }
}