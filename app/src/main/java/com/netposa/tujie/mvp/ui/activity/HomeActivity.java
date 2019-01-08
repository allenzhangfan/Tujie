package com.netposa.tujie.mvp.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DeviceUtils;
import com.netposa.common.alive.SinglePixelUtil;
import com.netposa.common.constant.UrlConstant;
import com.netposa.common.core.RouterHub;
import com.netposa.common.log.Log;
import com.netposa.common.service.location.LocationService;
import com.netposa.common.utils.AppUtils;
import com.netposa.commonres.widget.Dialog.UpdateDialog;
import com.netposa.commonres.widget.NoScrollViewPager;
import com.netposa.component.gzt.mvp.ui.fragment.GztFragment;
import com.netposa.component.jq.mvp.ui.fragment.DailyFragment;
import com.netposa.component.my.mvp.model.entity.UpdateInfoEntity;
import com.netposa.component.my.mvp.ui.fragment.MyFragment;
import com.netposa.tujie.R;
import com.netposa.tujie.di.component.DaggerHomeComponent;
import com.netposa.tujie.di.module.HomeModule;
import com.netposa.tujie.mvp.contract.HomeContract;
import com.netposa.tujie.mvp.presenter.HomePresenter;
import com.netposa.tujie.mvp.ui.adapter.HomeFragmentAdapter;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.common.constant.GlobalConstants.REQUEST_CODE_JQ;

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
    // 锁屏广播接收器
    private SreenBroadcastReceiver mScreenReceiver;
    private SinglePixelUtil mSinglePixelUtil;
    private int mCurrentVersion;
    private Dialog mUpdateDialog;
    private String mTruePath;

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
        mPresenter.getUpdateJson(this);
        mFragments.clear();
        mFragments.add(DailyFragment.newInstance());
        mFragments.add(GztFragment.newInstance());
        mFragments.add(MyFragment.newInstance());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setPagerEnabled(false);
        mViewPager.setOffscreenPageLimit(2);
        // 默认进入工作台界面
        mViewPager.setCurrentItem(1);
        mBottomNavigationView.setSelectedItemId(R.id.action_workbench);
        //noinspection Convert2Lambda
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                switch (itemId) {
                    case R.id.action_daily:
                        requestPermissions(new String[]{
                                        Manifest.permission.READ_PHONE_STATE},
                                REQUEST_CODE_JQ);
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
        //one pixel proguard
        mSinglePixelUtil = SinglePixelUtil.getInstance(this);
        mScreenReceiver = new SreenBroadcastReceiver();
        IntentFilter screenFilter = new IntentFilter();
        screenFilter.addAction(Intent.ACTION_SCREEN_ON);
        screenFilter.addAction(Intent.ACTION_SCREEN_OFF);
        screenFilter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(mScreenReceiver, screenFilter);
        mCurrentVersion = DeviceUtils.getVersionCode(this);
    }

    @Override
    protected void onDestroy() {
        mViewPager.removeOnPageChangeListener(mOnPageChangeListener);
        super.onDestroy();
        if (mScreenReceiver != null) {
            //与SinglePixelUtil解绑
            this.unregisterReceiver(mScreenReceiver);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "requestCode:" + requestCode + ",permissions:" +
                Arrays.toString(permissions) + ",grantResults:" +
                Arrays.toString(grantResults));
    }

    @Override
    public void getUpdateJsonSuc(UpdateInfoEntity entity) {
        int updateVersion = entity.getVersionNumber();
        boolean forceUpdate = entity.isForceUpdate();
        int minVersion = entity.getMinVersion();
        String description = entity.getVersionDescription();
        if (!TextUtils.isEmpty(entity.getRelativePath())) {
            String path = entity.getRelativePath();
            String rePath = path.substring(1, path.length());
            mTruePath = UrlConstant.sBaseUrl + rePath;
            Log.i(TAG, "getUpdateJsonSuc url:" + mTruePath);
            if (mCurrentVersion < updateVersion) {
                if (mCurrentVersion < minVersion) {
                    showUpdateDialog(true, description, mTruePath);
                } else {
                    showUpdateDialog(forceUpdate, description, mTruePath);
                }
            }
        } else {
            showMessage(getString(com.netposa.component.my.R.string.no_new_version));
        }
    }

    private void showUpdateDialog(boolean isForce, String description, String path) {
        mUpdateDialog = UpdateDialog.showUpgradeDialog(isForce, description, this,
                v -> {
                    downloadApk(path, isForce);
                }, v -> {
                    if (mUpdateDialog != null && mUpdateDialog.isShowing()) {
                        mUpdateDialog.dismiss();
                        mUpdateDialog = null;
                    }
                });

    }

    private void downloadApk(String path, boolean isForce) {
        if (!isForce) {
            if (mUpdateDialog != null && mUpdateDialog.isShowing()) {
                mUpdateDialog.dismiss();
                mUpdateDialog = null;
            }
        }
        AppUtils.gotoDownloadApk(path);
    }

    @Override
    public void getUpdateJsonFail() {

    }

    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            showMessage(getString(R.string.exit_for_more_click));
            firstTime = secondTime;
        } else {
            finish();
        }
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

    public class SreenBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "监听到系统广播:" + action);
            if (Intent.ACTION_SCREEN_ON.equals(action)) {         // 开屏
                onSreenOn();
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {  // 锁屏
                onSreenOff();
            } /*else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁
                mStateReceiverListener.onUserPresent();
            }*/
        }
    }

    //one pixel proguard app alive
    public void onSreenOn() {
        //亮屏后移除"1像素"
        mSinglePixelUtil.finishActivity();
    }

    public void onSreenOff() {
        //启动1像素保活
        mSinglePixelUtil.startActivity();
    }
}
