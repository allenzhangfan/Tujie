package com.netposa.tujie.di.module;

import com.jess.arms.di.scope.ActivityScope;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;
import com.netposa.tujie.mvp.contract.HomeContract;
import com.netposa.tujie.mvp.model.HomeModel;
import com.netposa.tujie.mvp.ui.adapter.HomeFragmentAdapter;
import java.util.ArrayList;
import java.util.List;

@Module
public class HomeModule {

    private FragmentManager mFm;
    private HomeContract.View mView;

    /**
     * 构建HomeModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public HomeModule(FragmentManager fm,HomeContract.View view) {
        mFm = fm;
        mView = view;
    }

    @ActivityScope
    @Provides
    HomeContract.View provideHomeView() {
        return mView;
    }

    @ActivityScope
    @Provides
    HomeContract.Model provideHomeModel(HomeModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    List<Fragment> provideFragments(){
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    HomeFragmentAdapter provideFragmentAdapter(List<Fragment> fragmentList){
        return new HomeFragmentAdapter(mFm, fragmentList);
    }

}