package com.netposa.component.jq.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.constant.GlobalConstants;
import com.netposa.common.constant.UrlConstant;
import com.netposa.common.core.RouterHub;
import com.netposa.common.entity.push.AlarmCarDetailResponseForExt;
import com.netposa.common.entity.push.AlarmPeopleDetailResponseForExt;
import com.netposa.common.log.Log;
import com.netposa.common.utils.KeyboardUtils;
import com.netposa.common.utils.TimeUtils;
import com.netposa.commonres.widget.CircleProgressView;
import com.netposa.commonres.widget.Dialog.LottieDialogFragment;
import com.netposa.commonres.widget.RoundImageView;
import com.netposa.component.jq.R;
import com.netposa.component.jq.R2;
import com.netposa.component.jq.di.component.DaggerAlarmDetailsComponent;
import com.netposa.component.jq.di.module.AlarmDetailsModule;
import com.netposa.component.jq.mvp.contract.AlarmDetailsContract;
import com.netposa.component.jq.mvp.model.entity.AlarmDetailForIdResponseEntity;
import com.netposa.component.jq.mvp.model.entity.ProcessAlarmResponseEntity;
import com.netposa.component.jq.mvp.presenter.AlarmDetailsPresenter;
import com.netposa.component.jq.mvp.ui.fragment.CarDeployFragment;
import com.netposa.component.jq.mvp.ui.fragment.FaceDeployFragment;

import java.util.ArrayList;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.common.constant.GlobalConstants.KEY_IV_DETAIL;
import static com.netposa.common.constant.GlobalConstants.KEY_JQ_ITEM;
import static com.netposa.common.constant.GlobalConstants.KEY_MOTIONNAME_DETAIL;
import static com.netposa.common.constant.GlobalConstants.KEY_POSITION;
import static com.netposa.common.constant.GlobalConstants.KEY_SINGLE_CAMERA_LOCATION_LATITUDE;
import static com.netposa.common.constant.GlobalConstants.KEY_SINGLE_CAMERA_LOCATION_LONGITUDE;
import static com.netposa.common.constant.GlobalConstants.KEY_TIME_DETAIL;
import static com.netposa.common.constant.GlobalConstants.KEY_TITLE;
import static com.netposa.common.constant.GlobalConstants.KEY_TYPE_DETAIL;
import static com.netposa.common.constant.GlobalConstants.TYPE_CAR_DEPLOY;
import static com.netposa.common.constant.GlobalConstants.TYPE_FACE_DEPLOY;
import static com.netposa.common.constant.GlobalConstants.TYPE_FAMALE;
import static com.netposa.common.constant.GlobalConstants.TYPE_INVALID;
import static com.netposa.common.constant.GlobalConstants.TYPE_MALE;
import static com.netposa.common.constant.GlobalConstants.TYPE_SUSPENDING;
import static com.netposa.common.constant.GlobalConstants.TYPE_VALID;
import static com.netposa.component.jq.app.JqConstants.CAR_TO_DETAIL_RESULTCODE;
import static com.netposa.component.jq.app.JqConstants.FACE_TO_DETAIL_RESULTCODE;
import static com.netposa.component.jq.app.JqConstants.KEY_DETAILS;
import static com.netposa.component.jq.app.JqConstants.LOCATION;

public class AlarmDetailsActivity extends BaseActivity<AlarmDetailsPresenter> implements AlarmDetailsContract.View {

    @BindView(R2.id.title_tv)
    TextView mTitle_txt;
    @BindView(R2.id.ll_confirm)
    LinearLayout mLlConfirm;
    @BindView(R2.id.content)
    LinearLayout mContent;
    @BindView(R2.id.cl_no_content)
    ConstraintLayout mClNoContent;
    @BindView(R2.id.ll_jq_person_details)
    LinearLayout mLlJqPersonDetails;
    @BindView(R2.id.ll_jq_car_details)
    LinearLayout mLlJqCarDetails;
    @BindView(R2.id.similar_progresview)
    CircleProgressView mCircleProgressView;
    @BindView(R2.id.tv_similarity)
    TextView mTvSimilarity;
    @BindView(R2.id.tv_car_number)
    TextView mTvCarNumber;
    @BindView(R2.id.tv_detail_type_car)
    TextView mTvDetailTypeCar;
    @BindView(R2.id.tv_detail_type_person)
    TextView mTvDetailTypePerson;
    @BindView(R2.id.capture_name)
    TextView mCaptureName;
    @BindView(R2.id.capture_gender)
    TextView mCaptureGender;
    @BindView(R2.id.captute_id_card)
    TextView mCaptuteIdCard;
    @BindView(R2.id.capture_address)
    TextView mCaptureAddress;
    @BindView(R2.id.capture_lib)
    TextView mCaptureLib;
    @BindView(R2.id.capture_time)
    TextView mCaptureTime;
    @BindView(R2.id.capture_lib_name)
    TextView mCaptureLibName;
    @BindView(R2.id.camera_addr)
    TextView mCameraAddr;
    @BindView(R2.id.detail_content)
    TextView mDetailContent;
    @BindView(R2.id.tips_msg)
    EditText mTipsMsg;
    @BindView(R2.id.car_ku_type)
    TextView mCarKuType;
    @BindView(R2.id.camera_address)
    TextView mCameraAddress;
    @BindView(R2.id.car_capture_time)
    TextView mCarCaptureTime;
    @BindView(R2.id.control_car_type)
    TextView mControlCarType;
    @BindView(R2.id.deal_person)
    TextView mDealPerson;
    @BindView(R2.id.captureImg)
    RoundImageView mCaptureImg;
    @BindView(R2.id.controlImg)
    RoundImageView mControlImg;
    @BindView(R2.id.tv_tips_msg)
    TextView mTvTipsMsg;
    @BindView(R2.id.tv_no_content)
    TextView mTvNoContent;
    @BindView(R2.id.iv_no_content)
    ImageView mIvNoContent;
    @BindView(R2.id.rl_deal_person)
    RelativeLayout mRlDealPerson;
    @BindView(R2.id.common_devider)
    View mCommonDevider;
    @BindView(R2.id.tv_car_brand)
    TextView mTvCarBrand;
    @BindView(R2.id.tv_car_type)
    TextView mTvCarType;

    @Inject
    ImageLoader mImageLoader;
    @Inject
    LottieDialogFragment mLoadingDialogFragment;
    @Inject
    Gson mGson;

    int itemHandleType;
    private int itemType;
    private String mId;
    private int mValid;
    private String mTargetImage;
    private double mLongitude, mLatitude;
    private AlarmDetailForIdResponseEntity mAlarmDetailForIdResponseEntity;
    private String mUrl;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerAlarmDetailsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .alarmDetailsModule(new AlarmDetailsModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_alarm_details; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mTitle_txt.setText(this.getString(R.string.alarm_car_details));
        mTvNoContent.setText(R.string.no_detail_content);
        mIvNoContent.setImageResource(R.drawable.ic_no_follow);
        Intent data = getIntent();
        if (data == null) {
            Log.i(TAG, "intent data is null,please check !");
        }
        mId = getIntent().getStringExtra(KEY_JQ_ITEM);
        mPresenter.getAlarmInfo(mId);
    }

    @Override
    public void getAlarmInfoSuccess(AlarmDetailForIdResponseEntity entity) {
        showNoData(false);
        mAlarmDetailForIdResponseEntity = entity;
        String valid = entity.getAlarmStatus();
        if (!TextUtils.isEmpty(valid)) {
            mValid = Integer.parseInt(valid);
        }
        if (!TextUtils.isEmpty(entity.getAlarmType())) {
            itemType = Integer.parseInt(entity.getAlarmType());
        }
        //车辆
        if (entity.getAlarmType().equals(TYPE_CAR_DEPLOY + "")) {
            mLlJqPersonDetails.setVisibility(View.GONE);
            mLlJqCarDetails.setVisibility(View.VISIBLE);
            setCarData(entity);
        } else if (entity.getAlarmType().equals(TYPE_FACE_DEPLOY + "")) {
            mLlJqPersonDetails.setVisibility(View.VISIBLE);
            mLlJqCarDetails.setVisibility(View.GONE);
            mCircleProgressView.setScore(entity.getScore(), true);
            mTvSimilarity.setText(getResources().getString(R.string.similarity_percent, entity.getScore()));
            setPeopleData(entity);
        }

        if (mValid == TYPE_VALID) {
            mTvTipsMsg.setVisibility(View.VISIBLE);
            mTipsMsg.setVisibility(View.GONE);
            mLlConfirm.setVisibility(View.GONE);
            mCommonDevider.setVisibility(View.VISIBLE);
            mRlDealPerson.setVisibility(View.VISIBLE);
            mTvDetailTypeCar.setText(getString(R.string.valid));
            mTvDetailTypeCar.setTextColor(ContextCompat.getColor(this, R.color.color_2CCE9A));
            mTvDetailTypeCar.setBackground(ContextCompat.getDrawable(this, R.drawable.alarm_effective_bg));
            mTvDetailTypePerson.setText(getString(R.string.valid));
            mTvDetailTypePerson.setTextColor(ContextCompat.getColor(this, R.color.color_2CCE9A));
            mTvDetailTypePerson.setBackground(ContextCompat.getDrawable(this, R.drawable.alarm_effective_bg));
        } else if (mValid == TYPE_INVALID) {
            mTvTipsMsg.setVisibility(View.VISIBLE);
            mTipsMsg.setVisibility(View.GONE);
            mLlConfirm.setVisibility(View.GONE);
            mCommonDevider.setVisibility(View.VISIBLE);
            mRlDealPerson.setVisibility(View.VISIBLE);
            mTvDetailTypeCar.setText(getString(R.string.invalid));
            mTvDetailTypeCar.setTextColor(ContextCompat.getColor(this, R.color.color_C0C5D1));
            mTvDetailTypeCar.setBackground(ContextCompat.getDrawable(this, R.drawable.alarm_uneffect_bg));
            mTvDetailTypePerson.setText(getString(R.string.invalid));
            mTvDetailTypePerson.setTextColor(ContextCompat.getColor(this, R.color.color_C0C5D1));
            mTvDetailTypePerson.setBackground(ContextCompat.getDrawable(this, R.drawable.alarm_uneffect_bg));
        } else if (mValid == TYPE_SUSPENDING) {
            mTvTipsMsg.setVisibility(View.GONE);
            mTipsMsg.setVisibility(View.VISIBLE);
            mLlConfirm.setVisibility(View.VISIBLE);
            mCommonDevider.setVisibility(View.GONE);
            mRlDealPerson.setVisibility(View.GONE);
            mTvDetailTypeCar.setText(getString(R.string.suspending));
            mTvDetailTypeCar.setTextColor(ContextCompat.getColor(this, R.color.color_FF503A));
            mTvDetailTypeCar.setBackground(ContextCompat.getDrawable(this, R.drawable.alarm_undeal_bg));
            mTvDetailTypePerson.setText(getString(R.string.suspending));
            mTvDetailTypePerson.setTextColor(ContextCompat.getColor(this, R.color.color_FF503A));
            mTvDetailTypePerson.setBackground(ContextCompat.getDrawable(this, R.drawable.alarm_undeal_bg));
        }
    }

    private AlarmCarDetailResponseForExt parseUrl(String ext) {
        AlarmCarDetailResponseForExt temp = mGson.fromJson(ext, AlarmCarDetailResponseForExt.class);
        return temp;
    }

    private AlarmPeopleDetailResponseForExt parsePersonUrl(String ext) {
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonElements = jsonParser.parse(ext).getAsJsonArray();//获取JsonArray对象
        ArrayList<AlarmPeopleDetailResponseForExt> beanList = new ArrayList<>(jsonElements.size());
        for (JsonElement bean : jsonElements) {
            AlarmPeopleDetailResponseForExt temp = mGson.fromJson(bean, AlarmPeopleDetailResponseForExt.class);//解析
            beanList.add(temp);
        }
        return beanList.get(0);
    }

    @Override
    public void getAlarmInfoFail() {
        showNoData(true);
    }

    @Override
    public void getProcessAlarmInfoSuccess(ProcessAlarmResponseEntity entity) {
        showMessage(getString(R.string.deal_suc));
        killMyself();
    }

    @Override
    public void getProcessAlarmInfoFail() {

    }

    private void showIv(AlarmDetailForIdResponseEntity entity) {
        mImageLoader.loadImage(this, ImageConfigImpl
                .builder()
                .cacheStrategy(0)
                .placeholder(R.drawable.ic_image_default)
                .errorPic(R.drawable.ic_image_load_failed)
                .url(UrlConstant.parseImageUrl(entity.getImageUrl()))
                .imageView(mCaptureImg)
                .build());
        mImageLoader.loadImage(this, ImageConfigImpl
                .builder()
                .cacheStrategy(0)
                .placeholder(R.drawable.ic_image_default)
                .errorPic(R.drawable.ic_image_load_failed)
                .url(UrlConstant.parseImageUrl(mTargetImage))
                .imageView(mControlImg)
                .build());
    }

    private void setPeopleData(AlarmDetailForIdResponseEntity entity) {
        String ext = entity.getExt();
        if (!TextUtils.isEmpty(ext)) {
            AlarmPeopleDetailResponseForExt peopleTemp = parsePersonUrl(ext);
            if (TextUtils.isEmpty(peopleTemp.getSex())) {
                mCaptureGender.setText(getString(R.string.other));
            } else if (TYPE_MALE.equals(peopleTemp.getSex())) {
                mCaptureGender.setText(getString(R.string.male));
            } else if (TYPE_FAMALE.equals(peopleTemp.getSex())) {
                mCaptureGender.setText(getString(R.string.famale));
            }
            mCaptureAddress.setText(peopleTemp.getCensus());
            mCaptureLib.setText(peopleTemp.getLibName());
            mDetailContent.setText(peopleTemp.getRemark());//详细信息
            mTargetImage = peopleTemp.getTargetImage();
        }
        mCaptureName.setText(entity.getName());
        mCaptuteIdCard.setText(entity.getIdCard());
        mCameraAddr.setText(entity.getAlarmAddress());
        mCaptureTime.setText(TimeUtils.millis2String(entity.getAlarmTime()));
        mCaptureLibName.setText(entity.getTaskName());
        mLongitude = entity.getLongitude();
        mLatitude = entity.getLatitude();
        showIv(entity);
        if (itemHandleType != TYPE_SUSPENDING) {
            mTvTipsMsg.setText(entity.getNote()); //备注
            mDealPerson.setText(entity.getUserName()); // 处理人
        }
    }

    private void setCarData(AlarmDetailForIdResponseEntity entity) {
        String ext = entity.getExt();
        if (!TextUtils.isEmpty(ext)) {
            AlarmCarDetailResponseForExt carTemp = parseUrl(ext);
            mTvCarNumber.setText(carTemp.getAlarmObjName());
            mCarKuType.setText(carTemp.getLibName());
            mCaptureLib.setText(carTemp.getLibName());
            mTargetImage = carTemp.getImageURLs();
        }
        mCarCaptureTime.setText(TimeUtils.millis2String(entity.getAlarmTime()));
        mControlCarType.setText(entity.getTaskName()); // 布控任务名称
        mCameraAddress.setText(entity.getAlarmAddress()); //摄像机名称
        mTvCarBrand.setText(entity.getVehicleBrand());
        mTvCarType.setText(entity.getVehicleType());
        mLongitude = entity.getLongitude();
        mLatitude = entity.getLatitude();
        showIv(entity);
        if (itemHandleType != TYPE_SUSPENDING) {
            mTvTipsMsg.setText(entity.getNote()); //备注
            mDealPerson.setText(entity.getUserName()); // 处理人
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

    @OnClick({R2.id.head_left_iv,
            R2.id.control_car_type,
            R2.id.rl_camera_address,
            R2.id.rl_camera_addr,
            R2.id.capture_lib_name,
            R2.id.btn_no,
            R2.id.btn_yes,
            R2.id.ll_capture})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.head_left_iv) {
            killMyself();
        } else if (i == R.id.control_car_type) {
            Intent intent = new Intent(this, ControlTaskDetailActivity.class);
            intent.putExtra(KEY_DETAILS, mAlarmDetailForIdResponseEntity);
            launchActivity(intent);
        } else if (i == R.id.capture_lib_name) {
            Intent intent = new Intent(this, ControlTaskDetailActivity.class);
            intent.putExtra(KEY_DETAILS, mAlarmDetailForIdResponseEntity);
            launchActivity(intent);
        } else if (i == R.id.rl_camera_address) {
            goSingleCameraLocationActivity(mLongitude, mLatitude);
        } else if (i == R.id.rl_camera_addr) {
            goSingleCameraLocationActivity(mLongitude, mLatitude);
        } else if (i == R.id.btn_no) {
            setValid(TYPE_INVALID);
        } else if (i == R.id.btn_yes) {
            setValid(TYPE_VALID);
        } else if (i == R.id.ll_capture) {
            if (mAlarmDetailForIdResponseEntity != null) {
                if (itemType == TYPE_FACE_DEPLOY) {
                    mUrl = mAlarmDetailForIdResponseEntity.getSceneImg();
                } else if (itemType == TYPE_CAR_DEPLOY) {
                    mUrl = mAlarmDetailForIdResponseEntity.getImageUrl();
                }
                String typeDetail = itemType == TYPE_FACE_DEPLOY ? GlobalConstants.FACE_TYPE : GlobalConstants.VEHICLE_TYPE;
                ARouter.getInstance().build(RouterHub.PIC_SINGLE_PIC_PREVIEW_ACTIVITY)
                        .withString(KEY_IV_DETAIL, mUrl)
                        .withString(KEY_MOTIONNAME_DETAIL, mAlarmDetailForIdResponseEntity.getAlarmAddress())
                        .withString(KEY_TYPE_DETAIL, typeDetail)
                        .withString(KEY_TITLE, getString(R.string.capture_image))
                        .withString(KEY_POSITION, mAlarmDetailForIdResponseEntity.getPosition())
                        .withLong(KEY_TIME_DETAIL, mAlarmDetailForIdResponseEntity.getAlarmTime())
                        .navigation(this);
            }
        }
    }

    private void setValid(int typeInvalid) {
        if (itemType == TYPE_FACE_DEPLOY) {
            Intent intent = new Intent(this, CarDeployFragment.class);
            setResult(FACE_TO_DETAIL_RESULTCODE, intent);
        } else {
            Intent intent = new Intent(this, FaceDeployFragment.class);
            setResult(CAR_TO_DETAIL_RESULTCODE, intent);
        }
        String note = mTipsMsg.getText().toString();
        mPresenter.getProcessAlarmInfo(mId, note, typeInvalid);
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
        KeyboardUtils.hideSoftInput(this, mTipsMsg);
        finish();
    }

    public void goSingleCameraLocationActivity(Double longitude, Double latitude) {
        if (latitude == null && latitude == null) {
            showMessage(getString(R.string.device_info_do_not_receive_yet));
            return;
        } else {
            String mLatitude = String.valueOf(latitude);
            String mLongitude = String.valueOf(longitude);
            if (mLatitude.equals(LOCATION) && mLongitude.equals(LOCATION)) {
                showMessage(getString(R.string.device_info_do_not_receive_yet));
                return;
            }
            ARouter.getInstance().build(RouterHub.SPJK_SINGLE_CAMERA_LOCATION_ACTIVITY)
                    .withString(KEY_SINGLE_CAMERA_LOCATION_LATITUDE, mLatitude)
                    .withString(KEY_SINGLE_CAMERA_LOCATION_LONGITUDE, mLongitude)
                    .navigation(this);
        }
    }

    private void showNoData(boolean show) {
        if (show) {
            mLlConfirm.setVisibility(View.GONE);
            mContent.setVisibility(View.GONE);
            mClNoContent.setVisibility(View.VISIBLE);
        } else {
            mLlConfirm.setVisibility(View.VISIBLE);
            mContent.setVisibility(View.VISIBLE);
            mClNoContent.setVisibility(View.GONE);
        }
    }
}
