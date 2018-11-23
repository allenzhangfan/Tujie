package com.netposa.tujie.mvp.ui.activity;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.netposa.common.core.RouterHub;
import com.netposa.common.log.Log;
import com.netposa.common.service.location.LocationService;
import com.netposa.common.utils.ToastUtils;
import com.netposa.commonres.widget.NoScrollViewPager;
import com.netposa.component.gzt.mvp.ui.fragment.GztFragment;
import com.netposa.component.jq.mvp.ui.fragment.JqFragment;
import com.netposa.component.my.mvp.ui.fragment.MyFragment;
import com.netposa.tujie.R;
import com.netposa.tujie.di.component.DaggerHomeComponent;
import com.netposa.tujie.di.module.HomeModule;
import com.netposa.tujie.mvp.contract.HomeContract;
import com.netposa.tujie.mvp.presenter.HomePresenter;
import com.netposa.tujie.mvp.ui.adapter.HomeFragmentAdapter;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

/**
 * Author：yeguoqiang
 * Created time：2018/10/26 12:57
 */
@Route(path = RouterHub.APP_HOME_ACTIVITY)
public class HomeActivity extends BaseActivity<HomePresenter> implements HomeContract.View {

    @BindView(R.id.view_pager)
    NoScrollViewPager mViewPager;
    @BindView(R.id.bottom_nav_view)
    BottomNavigationView mBottomNavigationView;

    @Inject
    HomeFragmentAdapter mAdapter;
    @Inject
    List<Fragment> mFragments;

    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private LocationService mLocationService;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerHomeComponent
                .builder()
                .appComponent(appComponent)
                .homeModule(new HomeModule(getSupportFragmentManager(), this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_home;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mFragments.clear();
        mFragments.add(JqFragment.newInstance());
        mFragments.add(GztFragment.newInstance());
        mFragments.add(MyFragment.newInstance());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setPagerEnabled(true);
        mViewPager.setOffscreenPageLimit(2);
        // 默认进入工作台界面
        mViewPager.setCurrentItem(1);
        mBottomNavigationView.setSelectedItemId(R.id.action_workbench);
        //noinspection Convert2Lambda
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                switch (itemId) {
                    case R.id.action_daily:
                        mViewPager.setCurrentItem(0);
                        return true;
                    case R.id.action_workbench:
                        mViewPager.setCurrentItem(1);
                        return true;
                    case R.id.action_my:
                        mViewPager.setCurrentItem(2);
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });
        mBottomNavigationView.setItemIconTintList(null);
        mOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                int selectedItemId = mBottomNavigationView.getSelectedItemId();
                Menu menu = mBottomNavigationView.getMenu();
                switch (selectedItemId) {
                    case R.id.action_daily:
                        menu.getItem(0).setChecked(false);
                    case R.id.action_workbench:
                        menu.getItem(1).setChecked(false);
                    case R.id.action_my:
                        menu.getItem(2).setChecked(false);
                    default:
                        break;
                }
                menu.getItem(position).setChecked(true);
            }
        };
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startLocationComponent();
    }

    @Override
    protected void onDestroy() {
        mViewPager.removeOnPageChangeListener(mOnPageChangeListener);
        stopLocationComponent();
        super.onDestroy();
    }

    private void startLocationComponent() {
        mLocationService = ARouter.getInstance().navigation(LocationService.class);
        if (mLocationService != null) {
            mLocationService.startService();
        }
        ensureGpsEnabled();
    }

    private void ensureGpsEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        assert locationManager != null;
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.please_open_gps_first)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    })
                    .show();
        }
    }

    private void stopLocationComponent() {
        if (mLocationService != null) {
            mLocationService.stopService();
        }
    }

    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            ToastUtils.showShort(R.string.exit_for_more_click);
            firstTime = secondTime;
        } else {
            finish();
        }
    }
}
