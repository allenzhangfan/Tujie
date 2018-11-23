package com.netposa.component.spjk.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.MapboxConstants;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.netposa.common.constant.GlobalConstants;
import com.netposa.common.log.Log;
import com.netposa.common.service.location.LocationService;
import com.netposa.component.room.entity.SpjkCollectionDeviceEntiry;
import com.netposa.component.spjk.R2;
import com.netposa.component.spjk.di.component.DaggerNeighbouringDevicesComponent;
import com.netposa.component.spjk.di.module.NeighbouringDevicesModule;
import com.netposa.component.spjk.mvp.contract.NeighbouringDevicesContract;
import com.netposa.component.spjk.mvp.model.entity.DeviceInfoResponseEntity;
import com.netposa.component.spjk.mvp.model.entity.OneKilometerCamerasResponseEntity;
import com.netposa.component.spjk.mvp.presenter.NeighbouringDevicesPresenter;
import com.netposa.component.spjk.R;
import com.netposa.component.spjk.mvp.ui.util.MapBoxUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.common.constant.GlobalConstants.CAMERA_QIANG_JI;
import static com.netposa.component.spjk.app.SpjkConstants.DEFAULT_LATITUDE;
import static com.netposa.component.spjk.app.SpjkConstants.DEFAULT_LONGITUDE;
import static com.netposa.component.spjk.app.SpjkConstants.DEFAULT_MAPBOX_CAMERAZOOM;
import static com.netposa.component.spjk.app.SpjkConstants.KEY_CAMERA_ATTENTION_FLAG;
import static com.netposa.component.spjk.app.SpjkConstants.KEY_NEIGHBOURING_DEVICES;
import static com.netposa.component.spjk.app.SpjkConstants.KEY_SINGLE_CAMERA_ID;
import static com.netposa.component.spjk.app.SpjkConstants.RESUME_CAMERA_ID;

public class NeighbouringDevicesActivity extends BaseActivity<NeighbouringDevicesPresenter> implements NeighbouringDevicesContract.View, OnMapReadyCallback, MapboxMap.OnMapClickListener, MapboxMap.OnMarkerClickListener {

    @BindView(R2.id.ll_title)
    LinearLayout mllTitle;
    @BindView(R2.id.title_tv)
    TextView mTVtTitle;
    @BindView(R2.id.ll_botttom_sheet)
    LinearLayout mLlBottomSheet;
    @BindView(R2.id.iv_camera)
    ImageView mIvCamera;
    @BindView(R2.id.tv_location_gereral)
    TextView mTvLocationGeneral;
    @BindView(R2.id.tv_location_detail)
    TextView mTvLocationDetail;
    @BindView(R2.id.iv_follow)
    ImageView mIvFollow;
    @BindView(R2.id.nearby_mapView)
    MapView mMapView;
    @BindView(R2.id.tv_follow)
    TextView mTvFollow;
    @Inject
    SpjkCollectionDeviceEntiry mSpjkCollectionDeviceEntiry;


    /**************************MAPBOX**************************/
    //IntRange函数限定参数范围
    private static final float ZOOM_IN_MAX = MapboxConstants.MAXIMUM_ZOOM;//缩小范围
    private static final float ZOOM_OUT_MIN = MapboxConstants.MINIMUM_ZOOM;//放大范围
    private float mCurrentZoomValue = DEFAULT_MAPBOX_CAMERAZOOM;//xml中默认大小(mapbox_cameraZoom)
    private LatLng mCurrentLatlng;
    private MapboxMap mMapboxMap;
    //地图上被选中的camera
    private String mActiveCameraId;
    //缓存两个参数(camera点位、camera type)
    List<Map<LatLng, OneKilometerCamerasResponseEntity>> mCamerasAttrCache = null;
    //一公里范围圈内获取到的camera
    List<OneKilometerCamerasResponseEntity> mCamerasCache;
    /**************************MAPBOX**************************/
    //底部弹出框
    private BottomSheetBehavior<LinearLayout> mBottomSheetBehavior;
    //底部弹出框是否关注设备
    private boolean mIsFollow = false;
    private LocationService mLocationService;
    private OneKilometerCamerasResponseEntity mActiveCamera;

    DeviceInfoResponseEntity mCurrentDevice;
    private Icon mBoxOnline, mDomeOnline, mDomeActive, mBoxActive;
    private String mCarmeraName;
    private int mCameraTypeInt;
    private String mResumeCameraId = RESUME_CAMERA_ID;
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerNeighbouringDevicesComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .neighbouringDevicesModule(new NeighbouringDevicesModule(this, this, getSupportFragmentManager()))
                .build()
                .inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            mActiveCameraId=savedInstanceState.getString(mResumeCameraId);
        }
        mPresenter.requestPermission();
        Mapbox.getInstance(this, GlobalConstants.MAP_KEY);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        Intent data = getIntent();
        if (data == null) {
            Log.e(TAG, "intent data is null,please check !");
            return;
        }
        mCurrentDevice = data.getParcelableExtra(KEY_NEIGHBOURING_DEVICES);
        Double latitude = Double.valueOf(mCurrentDevice.getLatitude());
        Double longitude = Double.valueOf(mCurrentDevice.getLongitude());
        mCurrentLatlng = new LatLng(latitude, longitude);
        mActiveCameraId=mCurrentDevice.getId();
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
        if (!TextUtils.isEmpty(mActiveCameraId)){
            mIsFollow=false;
            mPresenter.checkDevice(mActiveCameraId);
        }
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
        if (mMapboxMap != null) {
            mMapboxMap.removeOnMapClickListener(this);
            mMapboxMap.removeOnMapClickListener(this);
        }
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
        if (!TextUtils.isEmpty(mActiveCameraId)){
            outState.putString(mResumeCameraId, mActiveCameraId);
        }
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_neighbouring_devices; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mTVtTitle.setText(R.string.neighbouring_devices);
        mBottomSheetBehavior = BottomSheetBehavior.from(mLlBottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

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
            R2.id.nearby_iv_zoom_in,
            R2.id.nearby_iv_zoom_out,
            R2.id.nearby_iv_location,
            R2.id.iv_follow,
            R2.id.ll_camera_info})
    public void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.head_left_iv) {
            killMyself();
        } else if (id == R.id.nearby_iv_zoom_in) {
            if (mCurrentZoomValue < ZOOM_IN_MAX) {
                mCurrentZoomValue++;
                zoomInAndOut();
            }
        } else if (id == R.id.nearby_iv_zoom_out) {
            if (mCurrentZoomValue > ZOOM_OUT_MIN) {
                mCurrentZoomValue--;
                zoomInAndOut();
            }
        } else if (id == R.id.nearby_iv_location) {
            if (!isFastDoubleClick()) {
                doLocation();
                focusToCenterInMap();
            }
        } else if (id == R.id.iv_follow) {
            mSpjkCollectionDeviceEntiry.setCamerid(mActiveCameraId);
            mSpjkCollectionDeviceEntiry.setCamername(mCarmeraName);
            String camerType = String.valueOf(mCameraTypeInt);
            if (TextUtils.isEmpty(camerType)) {//默认枪机
                mSpjkCollectionDeviceEntiry.setCamertype(CAMERA_QIANG_JI);
            } else {
                mSpjkCollectionDeviceEntiry.setCamertype(mCameraTypeInt);
            }
            if (mIsFollow) {// 已经关注了 做删除操作
                mPresenter.deleteDevice(mActiveCameraId);
                setFollowImageResource(false);
                mIsFollow = false;
                showToast(false);
            } else {// 关注操作
                mPresenter.insterDevice(mSpjkCollectionDeviceEntiry);
                setFollowImageResource(true);
                showToast(true);
                mIsFollow = true;
            }
        } else if (id == R.id.ll_camera_info) {
            Intent intent = new Intent(this, VideoPlayActivity.class);
            String cameraId = mActiveCamera.getId();
            intent.putExtra(KEY_SINGLE_CAMERA_ID, cameraId);
            intent.putExtra(KEY_CAMERA_ATTENTION_FLAG, mIsFollow);
            launchActivity(intent);
        }
    }

    private void zoomInAndOut() {
        if (mCurrentLatlng != null) {
            Log.d(TAG, "start to zoom,currentLatlng：" + mCurrentLatlng);
            CameraPosition position = new CameraPosition.Builder()
                    .zoom(mCurrentZoomValue) // Sets the zoom
                    .build(); // Creates a CameraPosition from the builder
            mMapboxMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(position), 500);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        mMapboxMap = mapboxMap;
        mapboxMap.addOnMapClickListener(this);
        mapboxMap.setOnMarkerClickListener(this);
        //获取到系统的经纬度以后再上传至后台请求一公里范围内的摄像头
        mPresenter.getNeighbouringDevice(getCircleGeometryPoints());
        IconFactory iconFactory = IconFactory.getInstance(this);
        mBoxOnline = iconFactory.fromResource(R.drawable.ic_box_camera_online);
        mDomeOnline = iconFactory.fromResource(R.drawable.ic_dome_camera_online);
        mDomeActive = iconFactory.fromResource(R.drawable.ic_dome_camera_active);
        mBoxActive = iconFactory.fromResource(R.drawable.ic_box_camera_active);
        focusToCenterInMap();

    }

    /**
     * 锁定当前mobile定位的位置
     */
    private void focusToCenterInMap() {
        CameraPosition position = new CameraPosition.Builder()
                .target(mCurrentLatlng) // Sets the new camera position
                .build(); // Creates a CameraPosition from the builder
        mMapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), 500);
    }

    //展示一公里范围圈
    private void showOneKilometerBoundsArea() {
        PolygonOptions boundsArea = new PolygonOptions();
        List<Point> circleGeometry = getCircleGeometryPoints();
        for (Point point : circleGeometry) {
            LatLng latLng = new LatLng();
            latLng.setLatitude(point.latitude());
            latLng.setLongitude(point.longitude());
            boundsArea.add(latLng);
        }
        boundsArea.alpha(0.25f);
        boundsArea.fillColor(ContextCompat.getColor(this, R.color.app_theme_color));
        mMapboxMap.addPolygon(boundsArea);
    }

    private List<Point> getCircleGeometryPoints() {
        LatLng centerPoint;
        if (mCurrentLatlng != null) {
            centerPoint = mCurrentLatlng;
        } else {
            centerPoint = new LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
        }
        Log.d(TAG, centerPoint == mCurrentLatlng ? "定位成功" : "定位失败");
        return MapBoxUtil.getInstance().createCircleGeometry(centerPoint, 1000);
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    private void doLocation() {
        mLocationService = ARouter.getInstance().navigation(LocationService.class);
        Location location = getLocation();
        Log.e(TAG, "getLocation :" + location);
        if (location != null) {
            mCurrentLatlng = new LatLng(location.getLatitude(), location.getLongitude());
            Log.i(TAG, "location latitude: " + location.getLatitude() + ",longitude:" + location.getLongitude());
        } else {
            Log.e(TAG, "location is null");
        }
    }

    private Location getLocation() {
        if (mLocationService != null) {
            return mLocationService.getLocation();
        }
        return null;
    }

    @Override
    public void onGetNeighbouringDeviceSuccess(List<OneKilometerCamerasResponseEntity> cameras) {
        if (cameras.size() == 0) {
            Log.e(TAG, "no cameras get !!");
            return;
        }
        if (mMapboxMap == null) {
            Log.e(TAG, "mapbox map do not ready !!");
            return;
        }
        mCamerasCache = cameras;
        mCamerasAttrCache = new ArrayList<>(cameras.size());
        Map<LatLng, OneKilometerCamerasResponseEntity> cameraAttr = new HashMap<>();
        MarkerOptions markerOptions = new MarkerOptions();

        //0枪机,1球机
        for (OneKilometerCamerasResponseEntity camera : cameras) {
            LatLng point = new LatLng(Double.valueOf(camera.getLatitude()), Double.valueOf(camera.getLongitude()));
            int cameraTypeInt = getCameraTypeInt(camera);
            cameraAttr.put(point, camera);
            mCamerasAttrCache.add(cameraAttr);

            if (cameraTypeInt == CAMERA_QIANG_JI) {
                if (point.equals(mCurrentLatlng)) {
                    mActiveCamera = camera;
                    markerOptions.icon(mBoxActive);
                    showBottomSheet(cameraTypeInt, camera);
                } else {
                    markerOptions.icon(mBoxOnline);
                }
            } else {
                if (point.equals(mCurrentLatlng)) {
                    mActiveCamera = camera;
                    markerOptions.icon(mDomeActive);
                    showBottomSheet(cameraTypeInt, camera);
                } else {
                    markerOptions.icon(mDomeOnline);
                }
            }
            markerOptions.position(point);
            mMapboxMap.addMarker(markerOptions);
        }
        // oneKilometer bounds area
        showOneKilometerBoundsArea();
    }

    @Override
    public void onGetNeighbouringDeviceFail() {

    }

    @Override
    public void checkDeviceSuccess(int count) {
        // 查询展示
        if (count > 0) {
            mIsFollow = true;//已经关注了
            setFollowImageResource(true);
        } else {
            setFollowImageResource(false);
        }
    }

    @Override
    public void checkDeviceFail() {

    }

    //判断是否是快速点击(双击)，保证多次点击只响应一次点击事件
    private long lastClickTime = 0L; //上一次点击的时间

    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (timeD < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker activeMarker) {
        Log.d(TAG, "onMarkerClick");
        mCurrentLatlng = activeMarker.getPosition();
        if (mCamerasAttrCache != null) {
            for (Map<LatLng, OneKilometerCamerasResponseEntity> latLngListEntityMap : mCamerasAttrCache) {
                mActiveCamera = latLngListEntityMap.get(mCurrentLatlng);
                mActiveCameraId = mActiveCamera.getId();
                mCameraTypeInt = getCameraTypeInt(mActiveCamera);
                if (mCameraTypeInt == CAMERA_QIANG_JI) {
                    activeMarker.setIcon(mBoxActive);
                    showBottomSheet(mCameraTypeInt, mActiveCamera);
                    inVerseCameraIcon(activeMarker);
                    return true;
                } else {
                    activeMarker.setIcon(mDomeActive);
                    showBottomSheet(mCameraTypeInt, mActiveCamera);
                    inVerseCameraIcon(activeMarker);
                    return true;
                }
            }
        }
        return true;
    }

    /**
     * 一公里范围内只能有一个active图标
     *
     * @param activeMarker
     */
    private void inVerseCameraIcon(Marker activeMarker) {
        List<Marker> markers = mMapboxMap.getMarkers();
        for (int i = 0; i < markers.size(); i++) {
            Marker marker = markers.get(i);
            OneKilometerCamerasResponseEntity cameraEntity = mCamerasCache.get(i);
            if (!marker.equals(activeMarker)) {
                int cameraTypeInt = getCameraTypeInt(cameraEntity);
                if (cameraTypeInt == CAMERA_QIANG_JI) {
                    marker.setIcon(mBoxOnline);
                } else {
                    marker.setIcon(mDomeOnline);
                }
            }
        }
    }

    /**
     * 服务器有的cameraType返回null,默认枪机
     *
     * @param camera
     * @return
     */
    private int getCameraTypeInt(OneKilometerCamerasResponseEntity camera) {
        String cameraTypeStr = camera.getCameraType();
        int cameraTypeInt = CAMERA_QIANG_JI;
        if (!TextUtils.isEmpty(cameraTypeStr)) {
            cameraTypeInt = Integer.valueOf(cameraTypeStr);
        }
        return cameraTypeInt;
    }

    public void showBottomSheet(int cameraType, OneKilometerCamerasResponseEntity camera) {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        mCarmeraName = camera.getName();
        String address = camera.getAddress();
        if (cameraType == CAMERA_QIANG_JI) {
            mIvCamera.setImageResource(R.drawable.ic_box_camera_circle);
        } else {
            mIvCamera.setImageResource(R.drawable.ic_dome_camera_circle);
        }
        if (TextUtils.isEmpty(address)) {
            mTvLocationDetail.setVisibility(View.GONE);
        } else {
            mTvLocationDetail.setText(address);
        }
        mTvLocationGeneral.setText(mCarmeraName);
        setFollowImageResource(mIsFollow);
    }

    private void setFollowImageResource(boolean isFollowImgClicked) {
        if (isFollowImgClicked) {// 已关注  显示红心tr
            mIvFollow.setImageResource(R.drawable.ic_add_follow);
            mTvFollow.setText(getString(R.string.unsubscribe));
        } else {// 取消关注
            mIvFollow.setImageResource(R.drawable.ic_cancel_follow);
            mTvFollow.setText(getString(R.string.subscribe));
        }
    }

    private void showToast(boolean flag) {
        if (flag) {
            showMessage(getString(R.string.subscribe));
        } else {
            showMessage(getString(R.string.unsubscribe));
        }
    }

}