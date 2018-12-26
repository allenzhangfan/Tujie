package com.netposa.component.rltk.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.constant.UrlConstant;
import com.netposa.common.core.RouterHub;
import com.netposa.common.log.Log;
import com.netposa.common.utils.TimeUtils;
import com.netposa.common.utils.TujieImageUtils;
import com.netposa.commonres.widget.Dialog.LottieDialogFragment;
import com.netposa.commonres.widget.RoundImageView;
import com.netposa.component.rltk.R;
import com.netposa.component.rltk.R2;
import com.netposa.component.rltk.di.component.DaggerPhotoInformationComponent;
import com.netposa.component.rltk.di.module.PhotoInformationModule;
import com.netposa.component.rltk.mvp.contract.PhotoInformationContract;
import com.netposa.component.rltk.mvp.model.entity.FaceDetailResponseEntity;
import com.netposa.common.entity.FeatureByPathRequestEntity;
import com.netposa.common.entity.FeatureByPathResponseEntity;
import com.netposa.component.rltk.mvp.model.entity.PersonInfoEntity;
import com.netposa.component.rltk.mvp.presenter.PhotoInformationPresenter;
import com.netposa.component.rltk.mvp.ui.adapter.PhotoInformationAdapter;

import java.util.ArrayList;
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
import static com.netposa.common.constant.GlobalConstants.FACE_TYPE;
import static com.netposa.common.constant.GlobalConstants.KEY_DATA_KEY;
import static com.netposa.common.constant.GlobalConstants.KEY_DATA_TYPE;
import static com.netposa.common.constant.GlobalConstants.KEY_IV_DETAIL;
import static com.netposa.common.constant.GlobalConstants.KEY_MOTIONNAME_DETAIL;
import static com.netposa.common.constant.GlobalConstants.KEY_PICPATH;
import static com.netposa.common.constant.GlobalConstants.KEY_POSITION;
import static com.netposa.common.constant.GlobalConstants.KEY_SEESION;
import static com.netposa.common.constant.GlobalConstants.KEY_SINGLE_CAMERA_LOCATION_LATITUDE;
import static com.netposa.common.constant.GlobalConstants.KEY_SINGLE_CAMERA_LOCATION_LONGITUDE;
import static com.netposa.common.constant.GlobalConstants.KEY_TIME_DETAIL;
import static com.netposa.common.constant.GlobalConstants.TYPE_FAMALE;
import static com.netposa.common.constant.GlobalConstants.TYPE_MALE;
import static com.netposa.common.constant.GlobalConstants.TYPE_NO_WEAR_GLASS;
import static com.netposa.common.constant.GlobalConstants.TYPE_WEAR_GLASS;
import static com.netposa.component.rltk.app.RltkConstants.AGE_UNKNOW;
import static com.netposa.component.rltk.app.RltkConstants.KEY_TO_DETAIL;

public class PhotoInformationActivity extends BaseActivity<PhotoInformationPresenter> implements PhotoInformationContract.View {
    @BindView(R2.id.title_tv)
    TextView mTitleTv;
    @BindView(R2.id.head_right_tv)
    TextView mHeadRightTv;
    @BindView(R2.id.iv_car)
    RoundImageView mIvCar;
    @BindView(R2.id.rv_car_info)
    RecyclerView mRvCarInfo;

    @Inject
    LottieDialogFragment mLoadingDialogFragment;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.ItemAnimator mItemAnimator;
    @Inject
    List<MultiItemEntity> mBeanList;
    @Inject
    PhotoInformationAdapter mAdapter;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    FeatureByPathRequestEntity mFeatureByPathRequestEntity;

    private String mRecordId;
    private FaceDetailResponseEntity mFaceDetailEntity;
    private String mPosition;
    private String mImg; //画框图


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPhotoInformationComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .photoInformationModule(new PhotoInformationModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_photo_information; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        Intent data = getIntent();
        if (data == null) {
            Log.e(TAG, "intent data is null,please check !");
            return;
        }
        mRecordId = data.getStringExtra(KEY_TO_DETAIL);
        mTitleTv.setText(getString(R.string.rltk_photo_information));
        mHeadRightTv.setVisibility(View.VISIBLE);
        mHeadRightTv.setTextColor(this.getResources().getColor(R.color.app_theme_color));
        mHeadRightTv.setText(getString(R.string.ytst));
        mRvCarInfo.setLayoutManager(mLayoutManager);
        mRvCarInfo.setItemAnimator(mItemAnimator);
        mRvCarInfo.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            int id = view.getId();
            PersonInfoEntity mEntity = (PersonInfoEntity) adapter.getItem(position);
            if (id == R.id.iv_location) {
                if (!TextUtils.isEmpty(String.valueOf(mEntity.getmLatitude())) && !TextUtils.isEmpty(String.valueOf(mEntity.getmLongitude()))) {
                    goSingleCameraLocationActivity(mEntity);
                } else {
                    showMessage(getString(R.string.device_info_do_not_receive_yet));
                }
            }
        });
        mPresenter.getDetailInfo(mRecordId);

    }

    @Override
    public void getDetectImgFeatureByPathSucess(FeatureByPathResponseEntity entity) {
        String sessionKey = entity.getSessionKey();
        Log.d(TAG, sessionKey);
        String imgPath = entity.getImgPath();
        Log.d(TAG, sessionKey);
        String dataKey = entity.getDataKey();
        String datatype = entity.getDataType();
        ARouter.getInstance().build(RouterHub.YTST_SELECT_TARGET_ACTIVITY)
                .withString(KEY_SEESION, sessionKey)
                .withString(KEY_PICPATH, imgPath)
                .withString(KEY_DATA_KEY, dataKey)
                .withString(KEY_DATA_TYPE, datatype)
                .withString(KEY_POSITION, mPosition)
                .navigation(this);
    }

    @Override
    public void getDetectImgFeatureByPathFail() {

    }

    @Override
    public void getSuceese(FaceDetailResponseEntity entity) {
        if (entity != null) {
            mFaceDetailEntity = entity;
        }
        showPersonIv(entity);
        ShowPersonInfo(entity);
        mPosition = mFaceDetailEntity.getLocation().replace('.', ',');
        if (!TextUtils.isEmpty(mPosition)) {
            List<String> list = new ArrayList<>();
            list.add(mPosition);
            mImg = TujieImageUtils.circleTarget(mFaceDetailEntity.getSceneImg(), list);
        } else {
            mImg = mFaceDetailEntity.getSceneImg();
        }
    }

    private void showPersonIv(FaceDetailResponseEntity mEntity) {
        mImageLoader.loadImage(this, ImageConfigImpl
                .builder()
                .cacheStrategy(0)
                .placeholder(R.drawable.ic_image_default)
                .errorPic(R.drawable.ic_image_load_failed)
                .url(UrlConstant.parseImageUrl(mEntity.getTraitImg()))
                .imageView(mIvCar)
                .build());
    }

    private void ShowPersonInfo(FaceDetailResponseEntity entity) {
        PersonInfoEntity entity1 = new PersonInfoEntity();
        entity1.setTitle(getString(R.string.capture_device));
        entity1.setDividerVisable(true);
        entity1.setGpsVisable(true);
        entity1.setmLatitude(entity.getLatitude());
        entity1.setmLongitude(entity.getLongitude());
        entity1.setDescription(entity.getDeviceName());
        mBeanList.add(entity1);

        PersonInfoEntity entity2 = new PersonInfoEntity();
        entity2.setTitle(getString(R.string.clxc_device_type));
        entity2.setDividerVisable(true);
        entity2.setGpsVisable(false);
        if (TextUtils.isEmpty(entity.getCameraType())) {
            entity2.setDescription(getString(R.string.clcx_unknow));
        } else {
            if (Integer.valueOf(entity.getCameraType()) == CAMERA_QIANG_JI) {
                entity2.setDescription(getString(R.string.qiangji));
            } else if (Integer.valueOf(entity.getCameraType()) == CAMERA_QIU_JI) {
                entity2.setDescription(getString(R.string.qiu_ji));
            }
        }
        mBeanList.add(entity2);

        PersonInfoEntity entity3 = new PersonInfoEntity();
        entity3.setTitle(getString(R.string.clcx_capture_time));
        entity3.setDividerVisable(true);
        entity3.setGpsVisable(false);
        entity3.setDescription(TimeUtils.millis2String(entity.getAbsTime()));
        mBeanList.add(entity3);

        PersonInfoEntity entity4 = new PersonInfoEntity();
        entity4.setTitle(getString(R.string.sex));
        entity4.setDividerVisable(true);
        entity4.setGpsVisable(false);
        entity4.setDescription(entity.getGender());
        if (TextUtils.isEmpty(entity.getGender())) {
            entity4.setDescription(getString(R.string.clcx_unknow));
        } else {
            if (entity.getGender().equals(TYPE_MALE)) {
                entity4.setDescription(getString(R.string.male));
            } else if (entity.getGender().equals(TYPE_FAMALE)) {
                entity4.setDescription(getString(R.string.famale));
            } else {
                entity4.setDescription(getString(R.string.clcx_unknow));
            }
        }
        mBeanList.add(entity4);

        PersonInfoEntity entity5 = new PersonInfoEntity();
        entity5.setTitle(getString(R.string.age));
        entity5.setDividerVisable(true);
        entity5.setGpsVisable(false);
        if (entity.getAge() == AGE_UNKNOW) {
            entity5.setDescription(getString(R.string.clcx_unknow));
        } else {
            entity5.setDescription(entity.getAge() + "");
        }
        mBeanList.add(entity5);

        PersonInfoEntity entity6 = new PersonInfoEntity();
        entity6.setTitle(getString(R.string.glasses));
        entity6.setDividerVisable(true);
        entity6.setGpsVisable(false);
        if (String.valueOf(entity.getEyeGlass()).equals(TYPE_NO_WEAR_GLASS)) {
            entity6.setDescription(getString(R.string.no_wear_glasses));
        } else if (String.valueOf(entity.getEyeGlass()).equals(TYPE_WEAR_GLASS)) {
            entity6.setDescription(getString(R.string.wear_glasses));
        } else {
            entity6.setDescription(getString(R.string.clcx_unknow));
        }
        mBeanList.add(entity6);
        mAdapter.notifyDataSetChanged();
    }

    public void goSingleCameraLocationActivity(PersonInfoEntity mEntity) {
        String latitude = String.valueOf(mEntity.getmLatitude());
        String longitude = String.valueOf(mEntity.getmLongitude());
        ARouter.getInstance().build(RouterHub.SPJK_SINGLE_CAMERA_LOCATION_ACTIVITY)
                .withString(KEY_SINGLE_CAMERA_LOCATION_LATITUDE, latitude)
                .withString(KEY_SINGLE_CAMERA_LOCATION_LONGITUDE, longitude)
                .navigation(this);
    }

    @Override
    public void getFailed() {

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

    @OnClick({R2.id.head_left_iv, R2.id.head_right_tv, R2.id.ll_detail})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.head_left_iv) {
            killMyself();
        }
        if (id == R.id.head_right_tv) {
            setRequest();
        }
        if (id == R.id.ll_detail) {
            if (mFaceDetailEntity != null) {
                ARouter.getInstance().build(RouterHub.PIC_SINGLE_PIC_PREVIEW_ACTIVITY)
                        .withString(KEY_IV_DETAIL, mImg)
                        .withString(KEY_MOTIONNAME_DETAIL, mFaceDetailEntity.getDeviceName())
                        .withString(KEY_POSITION, mFaceDetailEntity.getLocation())
                        .withLong(KEY_TIME_DETAIL, mFaceDetailEntity.getAbsTime())
                        .navigation(this);
            }
        }
    }

    private void setRequest() {
        mFeatureByPathRequestEntity.setImgPath(mImg);
        mPresenter.getDetectImgFeatureByPath();
    }
}
