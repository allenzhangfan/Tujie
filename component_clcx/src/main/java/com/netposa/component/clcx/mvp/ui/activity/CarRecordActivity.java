package com.netposa.component.clcx.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.constant.GlobalConstants;
import com.netposa.common.constant.UrlConstant;
import com.netposa.common.core.RouterHub;
import com.netposa.common.log.Log;
import com.netposa.common.utils.TimeUtils;
import com.netposa.commonres.widget.Dialog.LottieDialogFragment;
import com.netposa.component.clcx.R;
import com.netposa.component.clcx.R2;
import com.netposa.component.clcx.di.component.DaggerCarRecordComponent;
import com.netposa.component.clcx.di.module.CarRecordModule;
import com.netposa.component.clcx.mvp.contract.CarRecordContract;
import com.netposa.component.clcx.mvp.model.entity.CarDetailRequestEntity;
import com.netposa.component.clcx.mvp.model.entity.CarDetailResponseEntity;
import com.netposa.component.clcx.mvp.model.entity.CarDividerEntity;
import com.netposa.component.clcx.mvp.model.entity.CarInfoEntity;
import com.netposa.component.clcx.mvp.model.entity.CarNameEntity;
import com.netposa.component.clcx.mvp.model.entity.VehicleTrackRequestEntity;
import com.netposa.component.clcx.mvp.presenter.CarRecordPresenter;
import com.netposa.component.clcx.mvp.ui.adapter.CarRecordAdapter;
import java.text.DecimalFormat;
import java.util.List;
import javax.inject.Inject;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.common.constant.GlobalConstants.CAMERA_QIANG_JI;
import static com.netposa.common.constant.GlobalConstants.CAMERA_QIU_JI;
import static com.netposa.common.constant.GlobalConstants.KEY_IV_DETAIL;
import static com.netposa.common.constant.GlobalConstants.KEY_MOTIONNAME_DETAIL;
import static com.netposa.common.constant.GlobalConstants.KEY_SINGLE_CAMERA_LOCATION_LATITUDE;
import static com.netposa.common.constant.GlobalConstants.KEY_SINGLE_CAMERA_LOCATION_LONGITUDE;
import static com.netposa.common.constant.GlobalConstants.KEY_TIME_DETAIL;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_CAR_DETAIL;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_DICTIONARY_TYPE_END_TIME;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_DICTIONARY_TYPE_START_TIME;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_PIC_PATH;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_POSITION;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_VEHICLE_TRACK;


public class CarRecordActivity extends BaseActivity<CarRecordPresenter> implements CarRecordContract.View {

    @BindView(R2.id.head_left_iv)
    ImageButton mHeadLeftIv;
    @BindView(R2.id.title_tv)
    TextView mTitleTv;
    @BindView(R2.id.head_right_tv)
    TextView mHeadRightTv;
    @BindView(R2.id.rv_car_info)
    RecyclerView mRvCarInfo;
    @BindView(R2.id.iv_car)
    ImageView mIvCar;

    @Inject
    LottieDialogFragment mLoadingDialogFragment;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.ItemAnimator mItemAnimator;
    @Inject
    List<MultiItemEntity> mBeanList;
    @Inject
    CarRecordAdapter mAdapter;
    @Inject
    CarDetailResponseEntity mEntity;
    @Inject
    CarDetailRequestEntity entity;

    private VehicleTrackRequestEntity mVehicleTrackEntity;
    private String mRecordId;
    private ImageLoader mImageLoader;
    private long mStartTime;
    private long mEndTime;
    private String mPicPath;
    private String mPosition;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCarRecordComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .carRecordModule(new CarRecordModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_car_record; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        Intent data = getIntent();
        if (data == null) {
            Log.e(TAG, "intent data is null,please check !");
            return;
        }
        mPicPath = data.getStringExtra(KEY_PIC_PATH);
        mVehicleTrackEntity = new VehicleTrackRequestEntity();
        mImageLoader = ArmsUtils.obtainAppComponentFromContext(this).imageLoader();
        mRecordId = data.getStringExtra(KEY_CAR_DETAIL);
        mStartTime = data.getLongExtra(KEY_DICTIONARY_TYPE_START_TIME, 0);
        mEndTime = data.getLongExtra(KEY_DICTIONARY_TYPE_END_TIME, 0);
        mPosition=data.getStringExtra(KEY_POSITION);
        mTitleTv.setText(R.string.clcx_record);
        mHeadRightTv.setVisibility(View.VISIBLE);
        mHeadRightTv.setTextColor(this.getResources().getColor(R.color.app_theme_color));
        mHeadRightTv.setText(getString(R.string.wheel_path));
        mRvCarInfo.setLayoutManager(mLayoutManager);
        mRvCarInfo.setItemAnimator(mItemAnimator);
        mRvCarInfo.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> goSingleCameraLocationActivity());
        mPresenter.getDetailInfo(mRecordId);
    }

    @Override
    public void getSuccess(List<CarDetailResponseEntity> entity) {
        if (null != entity && entity.size() > 0) {
            mEntity = entity.get(0);
            showCarIv();
            ShowCarInfo();
        }
    }

    private void showCarIv() {
        mImageLoader.loadImage(this, ImageConfigImpl
                .builder()
                .cacheStrategy(0)
                .placeholder(R.drawable.ic_image_default)
                .errorPic(R.drawable.ic_image_load_failed)
                .url(UrlConstant.parseImageUrl(mPicPath))
                .imageView(mIvCar)
                .build());
    }

    private void ShowCarInfo() {

        CarNameEntity entity2 = new CarNameEntity();
        entity2.setName(getString(R.string.clcx_car_info));
        mBeanList.add(entity2);

        CarInfoEntity entity3 = new CarInfoEntity();
        entity3.setTitle(getString(R.string.clcx_car_num));
        entity3.setDividerVisable(true);
        entity3.setGpsVisable(false);
        if (TextUtils.isEmpty(mEntity.getPlateNumber())) {
            entity3.setDescription(getString(R.string.clcx_unidentified));
            mVehicleTrackEntity.setPlateNumber("");
        } else {
            entity3.setDescription(mEntity.getPlateNumber());
            mVehicleTrackEntity.setPlateNumber(mEntity.getPlateNumber());
        }
        mBeanList.add(entity3);

        CarInfoEntity entity4 = new CarInfoEntity();
        entity4.setTitle(getString(R.string.clcx_car_speed));
        entity4.setDividerVisable(true);
        entity4.setGpsVisable(false);
        if (TextUtils.isEmpty(mEntity.getSpeed())) {
            entity4.setDescription("0 KM/h");
        } else {
            DecimalFormat df = new DecimalFormat("0");
            entity4.setDescription(df.format(Double.parseDouble(mEntity.getSpeed())) + " KM/h");
        }
        mBeanList.add(entity4);

        CarInfoEntity entity5 = new CarInfoEntity();
        entity5.setTitle(getString(R.string.clxc_cross_device));
        entity5.setDividerVisable(true);
        entity5.setGpsVisable(true);
        if (TextUtils.isEmpty(mEntity.getDeviceName())) {
            entity5.setDescription(getString(R.string.clcx_bayonet_info));
        } else {
            entity5.setDescription(mEntity.getDeviceName());
        }
        mBeanList.add(entity5);

        CarInfoEntity entity6 = new CarInfoEntity();
        entity6.setTitle(getString(R.string.clxc_device_type));
        entity6.setDividerVisable(true);
        entity6.setGpsVisable(false);
        if (TextUtils.isEmpty(mEntity.getCameraType())) {
            entity6.setDescription(getString(R.string.clcx_unknow));
        } else {
            if (Integer.valueOf(mEntity.getCameraType()) == CAMERA_QIANG_JI) {
                entity6.setDescription(getString(R.string.qiangji));
            } else if (Integer.valueOf(mEntity.getCameraType()) == CAMERA_QIU_JI) {
                entity6.setDescription(getString(R.string.qiu_ji));
            }
        }
        mBeanList.add(entity6);

        CarInfoEntity entity7 = new CarInfoEntity();
        entity7.setTitle(getString(R.string.violation_information));
        entity7.setDividerVisable(true);
        entity7.setGpsVisable(false);
        entity7.setDescription(mEntity.getIllegaTypeName());//违章信息
        mBeanList.add(entity7);

        CarInfoEntity entity8 = new CarInfoEntity();
        entity8.setTitle(getString(R.string.clcx_capture_time));
        entity8.setDividerVisable(false);
        entity8.setGpsVisable(false);
        entity8.setDescription(TimeUtils.millis2String(mEntity.getAbsTime()));
        mBeanList.add(entity8);

        CarDividerEntity entity9 = new CarDividerEntity();
        mBeanList.add(entity9);

        CarNameEntity entity10 = new CarNameEntity();
        entity10.setName(getString(R.string.clcx_second_car_info));
        mBeanList.add(entity10);

        CarInfoEntity entity11 = new CarInfoEntity();
        entity11.setTitle(getString(R.string.sport_speed));
        entity11.setDividerVisable(true);
        entity11.setGpsVisable(false);
        entity11.setDescription(mEntity.getMoveSpeedName()); //运动速度
        mBeanList.add(entity11);

        CarInfoEntity entity12 = new CarInfoEntity();
        entity12.setTitle(getString(R.string.sport_direction));
        entity12.setDividerVisable(true);
        entity12.setGpsVisable(false);
        entity12.setDescription(mEntity.getMoveDirectionName());//运动方向
        mBeanList.add(entity12);

        CarInfoEntity entity13 = new CarInfoEntity();
        entity13.setTitle(getString(R.string.plate_number_color));
        entity13.setDividerVisable(true);
        entity13.setDescription(mEntity.getPlateColorName());//车牌颜色
        entity13.setGpsVisable(false);
        mBeanList.add(entity13);

        CarInfoEntity entity14 = new CarInfoEntity();
        entity14.setTitle(getString(R.string.car_type));
        entity14.setDividerVisable(true);
        entity14.setDescription(mEntity.getVehicleTypeName());//车辆类型
        entity14.setGpsVisable(false);
        mBeanList.add(entity14);

        CarInfoEntity entity15 = new CarInfoEntity();
        entity15.setTitle(getString(R.string.car_grand));
        entity15.setDividerVisable(true);
        entity15.setDescription(mEntity.getVehicleBrandName());//车辆品牌
        entity15.setGpsVisable(false);
        mBeanList.add(entity15);

        CarInfoEntity entity16 = new CarInfoEntity();
        entity16.setTitle(getString(R.string.car_child_type));
        entity16.setDividerVisable(true);
        entity16.setDescription(mEntity.getVehicleSubBrandName());//车型子类
        entity16.setGpsVisable(false);
        mBeanList.add(entity16);

        CarInfoEntity entity17 = new CarInfoEntity();
        entity17.setTitle(getString(R.string.car_year_type));
        entity17.setDividerVisable(true);
        entity17.setDescription(mEntity.getVehicleBrandName());//车辆年款（vm/nk）
        entity17.setGpsVisable(false);
        mBeanList.add(entity17);

        CarInfoEntity entity18 = new CarInfoEntity();
        entity18.setTitle(getString(R.string.is_special_car));
        entity18.setDividerVisable(true);
        entity18.setDescription(mEntity.getSpecialCarName());//是否特殊车辆
        entity18.setGpsVisable(false);
        mBeanList.add(entity18);

        CarInfoEntity entity19 = new CarInfoEntity();
        entity19.setTitle(getString(R.string.safety_belt));
        entity19.setDividerVisable(true);
        entity19.setDescription(mEntity.getSafetyBeltNumName());//安全带
        entity19.setGpsVisable(false);
        mBeanList.add(entity19);

        CarInfoEntity entity20 = new CarInfoEntity();
        entity20.setTitle(getString(R.string.annual_survey));
        entity20.setDividerVisable(true);
        entity20.setDescription(mEntity.getMoveDirectionName()); //年检标
        entity20.setGpsVisable(false);
        mBeanList.add(entity20);

        CarInfoEntity entity21 = new CarInfoEntity();
        entity21.setTitle(getString(R.string.is_telephone));
        entity21.setDividerVisable(true);
        entity21.setDescription(mEntity.getIsCallPhoneName()); //是否打电话
        entity21.setGpsVisable(false);
        mBeanList.add(entity21);

        CarInfoEntity entity22 = new CarInfoEntity();
        entity22.setTitle(getString(R.string.sun_visor));
        entity22.setDividerVisable(true);
        entity22.setDescription(mEntity.getVehicleSunvisorNumName());//遮阳板
        entity22.setGpsVisable(false);
        mBeanList.add(entity22);

        CarInfoEntity entity23 = new CarInfoEntity();
        entity23.setTitle(getString(R.string.is_visor_face));
        entity23.setDividerVisable(true);
        entity23.setDescription(mEntity.getShieldFaceName());
        mBeanList.add(entity23);

        CarInfoEntity entity24 = new CarInfoEntity();
        entity24.setTitle(getString(R.string.other_marker));
        entity24.setDividerVisable(false);
        entity24.setGpsVisable(false);
        entity24.setDescription(mEntity.getMarkerTypeName());//其他标志物
        mBeanList.add(entity24);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getFailed() {

    }

    public void goSingleCameraLocationActivity() {
        if (mEntity.getLatitude() == null && mEntity.getLongitude() == null) {
            showMessage(getString(R.string.device_info_do_not_receive_yet));
            return;
        } else {
            String latitude = String.valueOf(mEntity.getLatitude());
            String longitude = String.valueOf(mEntity.getLongitude());
            ARouter.getInstance().build(RouterHub.SPJK_SINGLE_CAMERA_LOCATION_ACTIVITY)
                    .withString(KEY_SINGLE_CAMERA_LOCATION_LATITUDE, latitude)
                    .withString(KEY_SINGLE_CAMERA_LOCATION_LONGITUDE, longitude)
                    .navigation(this);
        }
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

    @OnClick({R2.id.head_left_iv,
            R2.id.head_right_tv,
            R2.id.ll_detail})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.head_left_iv) {
            killMyself();
        } else if (id == R.id.head_right_tv) {
            setRequestData(); //封装请求参数
            Intent intent = new Intent(this, ViewCarPathActivity.class);
            intent.putExtra(KEY_VEHICLE_TRACK, mVehicleTrackEntity);
            intent.putExtra(KEY_PIC_PATH, mPicPath);
            intent.putExtra(GlobalConstants.KEY_POSITION,mPosition);
            launchActivity(intent);
        } else if (id == R.id.ll_detail) {
            if (mEntity != null) {
                ARouter.getInstance().build(RouterHub.PIC_SINGLE_PIC_PREVIEW_ACTIVITY)
                        .withString(KEY_IV_DETAIL, mEntity.getSceneImg())
                        .withString(KEY_MOTIONNAME_DETAIL, mEntity.getDeviceName())
                        .withString(GlobalConstants.KEY_POSITION,mPosition)
                        .withLong(KEY_TIME_DETAIL, mEntity.getAbsTime())
                        .navigation(this);
            }
        }
    }

    private void setRequestData() {
        if (TextUtils.isEmpty(mEntity.getVehicleColor())) {
            mVehicleTrackEntity.setVehicleColor("");
        } else {
            mVehicleTrackEntity.setVehicleColor(mEntity.getVehicleColor());
        }
        mEntity.getVehicleColor();
        mVehicleTrackEntity.setStartTime(mStartTime);
        mVehicleTrackEntity.setEndTime(mEndTime);
    }

    @Override
    public void killMyself() {
        finish();
    }

}
