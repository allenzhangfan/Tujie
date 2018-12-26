package com.netposa.component.jq.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.commonres.widget.NoScrollViewPager;
import com.netposa.commonres.widget.TabLayoutHelper;
import com.netposa.component.jq.R2;
import com.netposa.component.jq.di.component.DaggerDailyComponent;
import com.netposa.component.jq.di.module.DailyModule;
import com.netposa.component.jq.mvp.contract.DailyContract;
import com.netposa.component.jq.mvp.presenter.DailyPresenter;
import com.netposa.component.jq.R;
import com.netposa.component.jq.mvp.ui.adapter.DailyPagerAdapter;

import javax.inject.Inject;

import static com.jess.arms.utils.Preconditions.checkNotNull;

public class DailyFragment extends BaseFragment<DailyPresenter> implements DailyContract.View {

    @BindView(R2.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R2.id.no_scroll_viewpager)
    NoScrollViewPager mViewPager;

    @Inject
    DailyPagerAdapter mDailyPagerAdapter;

    public static DailyFragment newInstance() {
        DailyFragment fragment = new DailyFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerDailyComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .dailyModule(new DailyModule(this, getChildFragmentManager()))
                .build()
                .inject(this);
    }

    @Override
    public View initContentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daily, container, false);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mViewPager.setPagerEnabled(true);
        mViewPager.setAdapter(mDailyPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager);
        TabLayoutHelper.setTabLayoutIndicatorWidth(mTabLayout, mViewPager);
        TabLayoutHelper.setTabTextBoldOnSelected(mTabLayout);
        TabLayoutHelper.setTabDivider(mTabLayout, 0, 36);
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading(String message) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {

    }

}
