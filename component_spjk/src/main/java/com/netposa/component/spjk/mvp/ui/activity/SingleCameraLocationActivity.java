package com.netposa.component.spjk.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.gyf.barlibrary.ImmersionBar;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.netposa.common.constant.GlobalConstants;
import com.netposa.common.core.RouterHub;
import com.netposa.common.log.Log;
import com.netposa.component.spjk.R2;
import com.netposa.component.spjk.di.component.DaggerSingleCameraLocationComponent;
import com.netposa.component.spjk.di.module.SingleCameraLocationModule;
import com.netposa.component.spjk.mvp.contract.SingleCameraLocationContract;
import com.netposa.component.spjk.mvp.presenter.SingleCameraLocationPresenter;
import com.netposa.component.spjk.R;
import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.common.constant.GlobalConstants.KEY_SINGLE_CAMERA_LOCATION_LATITUDE;
import static com.netposa.common.constant.GlobalConstants.KEY_SINGLE_CAMERA_LOCATION_LONGITUDE;

@Route(path = RouterHub.SPJK_SINGLE_CAMERA_LOCATION_ACTIVITY)
public class SingleCameraLocationActivity extends BaseActivity<SingleCameraLocationPresenter> implements SingleCameraLocationContract.View, OnMapReadyCallback {

    @BindView(R2.id.title_tv)
    TextView mTVtTitle;
    @BindView(R2.id.mapView)
    MapView mMapView;

    private LatLng mDeviceLatLng;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSingleCameraLocationComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .singleCameraLocationModule(new SingleCameraLocationModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, GlobalConstants.MAP_KEY);
        setContentView(R.layout.activity_single_camera_location);
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .init();
        //绑定到butterknife
        ButterKnife.bind(this);

        mMapView.setStyleUrl("asset://gaode-vector-bright-local.json");
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        mTVtTitle.setText(R.string.device_map_position);

        Intent data = getIntent();
        if (data == null) {
            Log.e(TAG, "intent data is null,please check !");
            return;
        }

        String latitude = data.getStringExtra(KEY_SINGLE_CAMERA_LOCATION_LATITUDE);
        String longitude = data.getStringExtra(KEY_SINGLE_CAMERA_LOCATION_LONGITUDE);
        mDeviceLatLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return 0; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

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
        finish();
    }

    @OnClick({
            R2.id.head_left_iv,
    })
    public void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.head_left_iv) {
            killMyself();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        Icon pointIcon = IconFactory.getInstance(this).fromResource(R.drawable.ic_map_point);
        mapboxMap.addMarker(new MarkerOptions()
                .position(mDeviceLatLng)
                .icon(pointIcon));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }
}
