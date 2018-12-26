package com.netposa.component.spjk.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.gyf.barlibrary.ImmersionBar;
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
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.netposa.common.constant.GlobalConstants;
import com.netposa.common.log.Log;
import com.netposa.common.service.location.IntegratedLocation;
import com.netposa.common.service.location.LocationService;
import com.netposa.common.utils.SizeUtils;
import com.netposa.common.utils.SystemUtil;
import com.netposa.component.room.entity.SpjkCollectionDeviceEntity;
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
import static com.netposa.common.constant.GlobalConstants.DEFAULT_MAPBOX_CAMERAZOOM;
import static com.netposa.common.constant.GlobalConstants.ZOOM_IN_MAX;
import static com.netposa.common.constant.GlobalConstants.ZOOM_OUT_MIN;
import static com.netposa.component.spjk.app.SpjkConstants.DEFAULT_LATITUDE;
import static com.netposa.component.spjk.app.SpjkConstants.DEFAULT_LONGITUDE;
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
    @BindView(R2.id.ll_zoom)
    LinearLayout mLlZoom;
    @BindView(R2.id.iv_location)
    ImageView mIvLocation;

    @Inject
    SpjkCollectionDeviceEntity mSpjkCollectionDeviceEntity;
    @Inject
    SystemUtil mSystemUtil;


    /**************************MAPBOX**************************/
    //IntRange函数限定参数范围
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
    private boolean mCansroll;//bottomsheet能否滑动
    private int mIvLocationLeftMargin, mIvLocationTopMargin, mIvLocationRightMargin, mIvLocationBottomMargin;
    private int mLlZoomLeftMargin, mLlZoomTopMargin, mLlZoomRightMargin, mLlZoomBottomMargin;
    private RelativeLayout.LayoutParams mIvLocationLayoutParams, mLlZoomLayoutParams;

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
        Mapbox.getInstance(this, GlobalConstants.MAP_KEY);
        setContentView(R.layout.activity_neighbouring_devices);
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .init();
        //绑定到butterknife
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            mActiveCameraId = savedInstanceState.getString(mResumeCameraId);
        }

        mMapView.setStyleUrl("asset://gaode-vector-bright-local.json");
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        mTVtTitle.setText(R.string.neighbouring_devices);
        mBottomSheetBehavior = BottomSheetBehavior.from(mLlBottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int state) {
                switch (state) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        mCansroll = false;
                        Log.d(TAG, "STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        mCansroll = true;
                        Log.d(TAG, "STATE_SETTLING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        if (mCansroll) {
                            mIvLocationTopMargin = mIvLocationTopMargin - SizeUtils.dp2px(48);
                            mIvLocationBottomMargin = mIvLocationBottomMargin + SizeUtils.dp2px(48);
                            mLlZoomTopMargin = mLlZoomTopMargin - SizeUtils.dp2px(48);
                            mLlZoomBottomMargin = mLlZoomBottomMargin + SizeUtils.dp2px(48);
                            Log.d(TAG, "STATE_EXPANDED t:" + mIvLocationTopMargin + ",b:" + mIvLocationBottomMargin);
                            mIvLocationLayoutParams.setMargins(
                                    mIvLocationLeftMargin,
                                    mIvLocationTopMargin,
                                    mIvLocationRightMargin,
                                    mIvLocationBottomMargin);
                            mIvLocation.setLayoutParams(mIvLocationLayoutParams);
                            mLlZoomLayoutParams.setMargins(
                                    mLlZoomLeftMargin,
                                    mLlZoomTopMargin,
                                    mLlZoomRightMargin,
                                    mLlZoomBottomMargin);

                        }
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.d(TAG, "STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        mIvLocationTopMargin = mIvLocationTopMargin + SizeUtils.dp2px(48);
                        mIvLocationBottomMargin = mIvLocationBottomMargin - SizeUtils.dp2px(48);
                        mLlZoomTopMargin = mLlZoomTopMargin + SizeUtils.dp2px(48);
                        mLlZoomBottomMargin = mLlZoomBottomMargin - SizeUtils.dp2px(48);
                        Log.d(TAG, "STATE_HIDDEN t:" + mIvLocationTopMargin + ",b:" + mIvLocationBottomMargin);
                        mIvLocationLayoutParams.setMargins(
                                mIvLocationLeftMargin,
                                mIvLocationTopMargin,
                                mIvLocationRightMargin,
                                mIvLocationBottomMargin);
                        mIvLocation.setLayoutParams(mIvLocationLayoutParams);
                        mLlZoomLayoutParams.setMargins(
                                mLlZoomLeftMargin,
                                mLlZoomTopMargin,
                                mLlZoomRightMargin,
                                mLlZoomBottomMargin);
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        Log.d(TAG, "STATE_HALF_EXPANDED");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        Intent data = getIntent();
        if (data == null) {
            Log.e(TAG, "intent data is null,please check !");
            return;
        }
        mCurrentDevice = data.getParcelableExtra(KEY_NEIGHBOURING_DEVICES);
        Double latitude = Double.valueOf(mCurrentDevice.getLatitude());
        Double longitude = Double.valueOf(mCurrentDevice.getLongitude());
        mCurrentLatlng = new LatLng(latitude, longitude);
        mActiveCameraId = mCurrentDevice.getId();
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
        if (!TextUtils.isEmpty(mActiveCameraId)) {
            mIsFollow = false;
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
        }
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
        if (!TextUtils.isEmpty(mActiveCameraId)) {
            outState.putString(mResumeCameraId, mActiveCameraId);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mIvLocationLayoutParams = (RelativeLayout.LayoutParams) mIvLocation.getLayoutParams();
        mIvLocationLeftMargin = mIvLocationLayoutParams.leftMargin;
        mIvLocationTopMargin = mIvLocationLayoutParams.topMargin;
        mIvLocationRightMargin = mIvLocationLayoutParams.rightMargin;
        mIvLocationBottomMargin = mIvLocationLayoutParams.bottomMargin;

        mLlZoomLayoutParams = (RelativeLayout.LayoutParams) mLlZoom.getLayoutParams();
        mLlZoomLeftMargin = mLlZoomLayoutParams.leftMargin;
        mLlZoomTopMargin = mLlZoomLayoutParams.topMargin;
        mLlZoomRightMargin = mLlZoomLayoutParams.rightMargin;
        mLlZoomBottomMargin = mLlZoomLayoutParams.bottomMargin;
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return 0; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        //如果initContentLayout返回0请不要再调用initView方法
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
            R2.id.iv_location,
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
        } else if (id == R.id.iv_location) {
            if (!mSystemUtil.isFastDoubleClick()) {
                doLocation();
                focusToCenterInMap();
            }
        } else if (id == R.id.iv_follow) {
            mSpjkCollectionDeviceEntity.setCamerid(mActiveCameraId);
            mSpjkCollectionDeviceEntity.setCamername(mCarmeraName);
            String camerType = String.valueOf(mCameraTypeInt);
            if (TextUtils.isEmpty(camerType)) {//默认枪机
                mSpjkCollectionDeviceEntity.setCamertype(CAMERA_QIANG_JI);
            } else {
                mSpjkCollectionDeviceEntity.setCamertype(mCameraTypeInt);
            }
            if (mIsFollow) {// 已经关注了 做删除操作
                mPresenter.deleteDevice(mActiveCameraId);
                setFollowImageResource(false);
                mIsFollow = false;
                showToast(false);
            } else {// 关注操作
                mPresenter.insterDevice(mSpjkCollectionDeviceEntity);
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
        boundsArea.alpha(0.15f);
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
        //默认一公里范围圈
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
        mLocationService.addLocationListenerBoundLifecycle(new LocationService.LocationListener() {
            @Override
            public void onLocationChanged(@NonNull IntegratedLocation location) {
                if (location != null) {
                    mCurrentLatlng = new LatLng(location.getLatitude(), location.getLongitude());
                    Log.i(TAG, "doLocation success for " + location.getLocateMethod()
                            + " !!\ncurrentLatlng: " + mCurrentLatlng
                            + "\naddress:" + location.getAddress());
                } else {
                    Log.e(TAG, "doLocation failed !!");
                }
            }
        }, this);
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