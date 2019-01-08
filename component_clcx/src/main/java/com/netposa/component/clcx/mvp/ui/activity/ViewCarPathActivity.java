package com.netposa.component.clcx.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.gyf.barlibrary.ImmersionBar;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.netposa.common.constant.GlobalConstants;
import com.netposa.common.constant.UrlConstant;
import com.netposa.common.core.RouterHub;
import com.netposa.common.entity.TrackEnity;
import com.netposa.common.log.Log;
import com.netposa.common.utils.ImageUtils;
import com.netposa.common.utils.TimeUtils;
import com.netposa.common.utils.TujieImageUtils;
import com.netposa.commonres.widget.Dialog.LottieDialogFragment;
import com.netposa.component.clcx.R;
import com.netposa.component.clcx.R2;
import com.netposa.component.clcx.di.component.DaggerViewCarPathComponent;
import com.netposa.component.clcx.di.module.ViewCarPathModule;
import com.netposa.component.clcx.mvp.contract.ViewCarPathContract;
import com.netposa.component.clcx.mvp.model.entity.VehicleTrackRequestEntity;
import com.netposa.component.clcx.mvp.model.entity.VehicleTrackResponseEntity;
import com.netposa.component.clcx.mvp.presenter.ViewCarPathPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.common.constant.GlobalConstants.KEY_IV_DETAIL;
import static com.netposa.common.constant.GlobalConstants.KEY_MOTIONNAME_DETAIL;
import static com.netposa.common.constant.GlobalConstants.KEY_TIME_DETAIL;
import static com.netposa.common.constant.GlobalConstants.KEY_TRACK_ENTITY;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_PIC_PATH;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_POSITION;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_VEHICLE_TRACK;

@Route(path = RouterHub.CLCX_VIEW_CAR_PATH_ACTIVITY)
public class ViewCarPathActivity extends BaseActivity<ViewCarPathPresenter> implements ViewCarPathContract.View, OnMapReadyCallback, MapboxMap.OnMapClickListener {

    @BindView(R2.id.title_tv)
    TextView mTVtTitle;
    @BindView(R2.id.mapView)
    MapView mMapView;
    @BindView(R2.id.ll_botttom_sheet)
    LinearLayout mLlBottomSheet;
    @BindView(R2.id.iv_camera)
    ImageView mIvCamera;
    @BindView(R2.id.tv_location_detail)
    TextView mTvLocationDetail;
    @BindView(R2.id.tv_time)
    TextView mTvTime;
    @BindView(R2.id.tv_show_detail)
    TextView mTvShowDetail;

    @Inject
    VehicleTrackRequestEntity mEntity;
    @Inject
    ImageLoader mImageloader;
    @Inject
    LottieDialogFragment mLoadingDialogFragment;

    private List<VehicleTrackResponseEntity.ListBean> mListBeans = new ArrayList<>();
    private MapboxMap mMapboxMap;
    private List<Point> mPointList = new ArrayList<>();
    private List<String> mIvList;
    private List<String> mAdressList;
    private List<Long> mTimeList;
    private List<VehicleTrackResponseEntity.ListBean> mDetailList; //详情
    private VehicleTrackResponseEntity.ListBean mDetail;
    private static final int FIRST = 0;//起始位置；
    private static final int MAX = 999;//结束位置
    private String mPicPath;
    private TrackEnity mTrackEnity; // 以图搜图的跳转传过来的bean
    private String mPosition;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerViewCarPathComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .viewCarPathModule(new ViewCarPathModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, GlobalConstants.MAP_KEY);
        setContentView(R.layout.activity_view_car_path);
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .init();
        //绑定到butterknife
        ButterKnife.bind(this);

        mMapView.setStyleUrl("asset://gaode-vector-bright-local.json");
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        mTVtTitle.setText(R.string.view_path);
        getIntentData();
    }


    private void getIntentData() {
        Intent data = getIntent();
        if (data == null) {
            Log.e(TAG, "intent data is null,please check !");
            return;
        }
        mPicPath = data.getStringExtra(KEY_PIC_PATH);
        mEntity = data.getParcelableExtra(KEY_VEHICLE_TRACK);
        mTrackEnity = data.getParcelableExtra(KEY_TRACK_ENTITY);
        mPosition = data.getStringExtra(KEY_POSITION);
    }

    @Override
    public void getListSuccese(VehicleTrackResponseEntity entity) {
        int size = entity.getList().size();
        if (null != entity && size > 0) {
            if (size > 10) {
                mListBeans = entity.getList().subList(0, 10);
            } else {
                mListBeans = entity.getList();
            }
            mIvList = new ArrayList<>();
            mAdressList = new ArrayList<>();
            mTimeList = new ArrayList<>();
            mDetailList = new ArrayList<>();
            for (VehicleTrackResponseEntity.ListBean bean : mListBeans) {
                mPointList.add(Point.fromLngLat(bean.getX(), bean.getY()));
                mIvList.add(bean.getSceneImg());
                mAdressList.add(bean.getMonitorName());
                mTimeList.add(bean.getPassTime());
                mDetailList.add(bean);
            }
            if (mPointList.size() > 0) {
                showCarPath();
            }
            if (mListBeans.size() > 0) {
                showBottomSheet(0);//默认展示第一个
            }
        }
    }

    @Override
    public void getListFailed() {

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
        mLoadingDialogFragment.show(getSupportFragmentManager(), "LoadingDialog");
    }

    @Override
    public void hideLoading() {
        mLoadingDialogFragment.dismissAllowingStateLoss();
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

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        mMapboxMap = mapboxMap;
        mapboxMap.addOnMapClickListener(this);
        if (mTrackEnity == null) {
            mEntity.setFirst(FIRST);
            mEntity.setMax(MAX);
            mPresenter.getVehicleTrack(mEntity);
        } else {// 从以图搜图跳转过来的处理
            for (int i = 0; i < mTrackEnity.getPoint().size(); i++) {
                String str = mTrackEnity.getPoint().get(i);
                String[] split = str.split(",");
                mPointList.add(Point.fromLngLat(Double.parseDouble(split[0]), Double.parseDouble(split[1])));
            }
            showCarPath();
            showBottomSheet(0);
        }
    }

    private void showCarPath() {
        //默认焦点定焦在中心marker上
        int centerIndex = mPointList.size() % 2 == 0 ? (mPointList.size() / 2 - 1) : (mPointList.size() / 2);
        Point lastPoint = mPointList.get(centerIndex);
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(lastPoint.latitude(), lastPoint.longitude())) // Sets the new camera position
                .build(); // Creates a CameraPosition from the builder
        mMapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), 500);

        //画线
        List<Point> routeCoordinates = new ArrayList<>(mPointList.size());
        for (int i = 0; i < mPointList.size(); i++) {
            routeCoordinates.add(mPointList.get(i));
        }
        LineString lineString = LineString.fromLngLats(routeCoordinates);
        FeatureCollection featureCollection =
                FeatureCollection.fromFeatures(new Feature[]{Feature.fromGeometry(lineString)});
        Source geoJsonSource = new GeoJsonSource("line-source", featureCollection);
        mMapboxMap.addSource(geoJsonSource);
        LineLayer lineLayer = new LineLayer("linelayer", "line-source");
        lineLayer.setProperties(
                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                PropertyFactory.lineWidth(2f),
                PropertyFactory.lineColor(ContextCompat.getColor(this, R.color.color_2D87F9))
        );
        mMapboxMap.addLayer(lineLayer);

        //添加点位
        for (int i = 0; i < mPointList.size(); i++) {
            Feature markerFeature = Feature.fromGeometry(mPointList.get(i));
            FeatureCollection markerConllection = FeatureCollection.fromFeature(markerFeature);
            GeoJsonSource markerSource = new GeoJsonSource("marker-source" + i, markerConllection);
            mMapboxMap.addSource(markerSource);
            Bitmap bitmap;
            if (i != mPointList.size() - 1) {
                bitmap = ImageUtils.drawTextToBitmap(ContextCompat.getDrawable(this, R.drawable.ic_trajectory_point), String.valueOf(i + 1), R.color.white);
            } else {
                bitmap = ImageUtils.drawTextToBitmap(ContextCompat.getDrawable(this, R.drawable.ic_trajectory_point_active), String.valueOf(i + 1), R.color.white);
            }
            // Add the marker image to map
            mMapboxMap.addImage("my-marker-image" + i, bitmap);
            SymbolLayer symbolLayer = new SymbolLayer("marker-layer" + i, "marker-source" + i)
                    .withProperties(
                            PropertyFactory.iconImage("my-marker-image" + i)
                    );
            mMapboxMap.addLayer(symbolLayer);
        }
    }

    @OnClick({
            R2.id.head_left_iv,
            R2.id.ll_botttom_sheet
    })
    public void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.head_left_iv) {
            killMyself();
        } else if (id == R.id.ll_botttom_sheet) {
            String picUrl = null;
            long time = 0;
            String titleName = null;
            if (mEntity != null) {
                if (mDetail != null) {
                    picUrl = mDetail.getSceneImg();
                    time = mDetail.getPassTime();
                    titleName = mDetail.getMonitorName();
                } else {
                    showMessage(getString(R.string.no_position));
                    return;
                }
            } else {// 以图搜图传过来的参数处理
                String str = mTrackEnity.getLocation();
                if (!TextUtils.isEmpty(str)) {
                    str = str.replace(".", ",");
                    picUrl = mTrackEnity.getSceneImg();
                    titleName = mTrackEnity.getTitleName();
                    time = mTrackEnity.getStartTime();
                    mPosition=str;
                }
            }
            ARouter.getInstance().build(RouterHub.PIC_SINGLE_PIC_PREVIEW_ACTIVITY)
                    .withString(KEY_IV_DETAIL, picUrl)
                    .withString(KEY_MOTIONNAME_DETAIL, titleName)
                    .withString(KEY_POSITION, mPosition)
                    .withLong(KEY_TIME_DETAIL, time)
                    .navigation(this);
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {
        handleClickIcon(mMapboxMap.getProjection().toScreenLocation(point));
    }

    /**
     * This method handles click events for SymbolLayer symbols.
     * <p>
     * When a SymbolLayer icon is clicked, we moved that feature to the selected state.
     * </p>
     *
     * @param screenPoint the point on screen clicked
     */
    private void handleClickIcon(PointF screenPoint) {
        for (int i = 0; i < mPointList.size(); i++) {
            List<Feature> features = mMapboxMap.queryRenderedFeatures(screenPoint, "marker-layer" + i);
            Bitmap bitmap;
            if (features.size() > 0) {
                bitmap = ImageUtils.drawTextToBitmap(ContextCompat.getDrawable(this, R.drawable.ic_trajectory_point_active), String.valueOf(i + 1), R.color.white);
            } else {
                bitmap = ImageUtils.drawTextToBitmap(ContextCompat.getDrawable(this, R.drawable.ic_trajectory_point), String.valueOf(i + 1), R.color.white);
            }
            mMapboxMap.addImage("my-marker-image" + i, bitmap);
            showBottomSheet(i);
        }
    }

    private void showBottomSheet(int position) {
        if (null != mEntity) {
            mDetail = mDetailList.get(position);
            mTvLocationDetail.setText(mAdressList.get(position));
            mTvTime.setText(TimeUtils.millis2String(mTimeList.get(position)));
        } else {
            mPicPath = mTrackEnity.getCropPic();
            mTvTime.setText(TimeUtils.millis2String(mTrackEnity.getStartTime()));
            mTvLocationDetail.setText(mTrackEnity.getTitleName());
        }
        mImageloader.loadImage(this, ImageConfigImpl
                .builder()
                .cacheStrategy(0)
                .placeholder(R.drawable.ic_image_default)
                .errorPic(R.drawable.ic_image_load_failed)
                .url(UrlConstant.parseImageUrl(mPicPath))
                .imageView(mIvCamera)
                .build());
    }
}

