package com.netposa.component.preview.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.gyf.barlibrary.ImmersionBar;
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
import com.netposa.component.imagedownload.ImageDownloadUtil;
import com.netposa.component.imagedownload.LoadErrorEntity;
import com.netposa.component.pic.R;
import com.netposa.component.pic.R2;
import com.netposa.component.preview.di.component.DaggerSinglePicPreviewComponent;
import com.netposa.component.preview.di.module.SinglePicPreviewModule;
import com.netposa.component.preview.mvp.contract.SinglePicPreviewContract;
import com.netposa.component.preview.mvp.model.entity.FeatureByPathRequestEntity;
import com.netposa.component.preview.mvp.model.entity.FeatureByPathResponseEntity;
import com.netposa.component.preview.mvp.presenter.SinglePicPreviewPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.common.constant.GlobalConstants.FACE_TYPE;
import static com.netposa.common.constant.GlobalConstants.KEY_DATA_KEY;
import static com.netposa.common.constant.GlobalConstants.KEY_DATA_TYPE;
import static com.netposa.common.constant.GlobalConstants.KEY_FACE_NAME;
import static com.netposa.common.constant.GlobalConstants.KEY_IV_DETAIL;
import static com.netposa.common.constant.GlobalConstants.KEY_MOTIONNAME_DETAIL;
import static com.netposa.common.constant.GlobalConstants.KEY_PICPATH;
import static com.netposa.common.constant.GlobalConstants.KEY_POSITION;
import static com.netposa.common.constant.GlobalConstants.KEY_SEESION;
import static com.netposa.common.constant.GlobalConstants.KEY_TIME_DETAIL;
import static com.netposa.common.constant.GlobalConstants.KEY_TITLE;
import static com.netposa.common.constant.GlobalConstants.KEY_TYPE_DETAIL;
import static com.netposa.common.constant.GlobalConstants.VEHICLE_TYPE;

@Route(path = RouterHub.PIC_SINGLE_PIC_PREVIEW_ACTIVITY)
public class SinglePicPreviewActivity extends BaseActivity<SinglePicPreviewPresenter> implements SinglePicPreviewContract.View {

    @BindView(R2.id.title_tv)
    TextView mTVtTitle;
    @BindView(R2.id.iv_capture)
    PhotoView mIvCapture;
    @BindView(R2.id.tv_adddress)
    TextView mTvAdddress;
    @BindView(R2.id.tv_alarm_time)
    TextView mTvTime;
    @BindView(R2.id.ll_title)
    LinearLayout mTitleLayout;
    @BindView(R2.id.ry_next)
    RelativeLayout mRyNext;
    @BindView(R2.id.ry_downIv)
    RelativeLayout mRyDownIv;

    @Inject
    ImageLoader mImageLoader;
    @Inject
    ImageDownloadUtil mImageDownloadUtil;
    @Inject
    FeatureByPathRequestEntity mFeatureByPathRequestEntity;
    @Inject
    LottieDialogFragment mLoadingDialogFragment;

    private String mIvUrl, mTime, mAdress, mName;
    private String mType;
    private String mPosition;
    private String mTitle;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSinglePicPreviewComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .singlePicPreviewModule(new SinglePicPreviewModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return 0; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_pic_preview);
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarColor(R.color.color_000000)
                .statusBarDarkFont(true, 0.2f)
                .init();
        //绑定到butterknife
        ButterKnife.bind(this);
        getIntentData();
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
    }

    private void getIntentData() {
        //动态显示title
        Intent dataIntent = getIntent();
        showLoading("");
        if (dataIntent != null) {
            mIvUrl = dataIntent.getStringExtra(KEY_IV_DETAIL);
            mName = dataIntent.getStringExtra(KEY_FACE_NAME);
            mType = dataIntent.getStringExtra(KEY_TYPE_DETAIL);
            mPosition = dataIntent.getStringExtra(KEY_POSITION);
            mTitle = dataIntent.getStringExtra(KEY_TITLE);
            long time = dataIntent.getLongExtra(KEY_TIME_DETAIL, 0);
            if (time == 0) {
                mTvTime.setText(mName);
            } else {
                mTime = TimeUtils.millis2String(time);
                mTvTime.setText(mTime);
            }
            mAdress = dataIntent.getStringExtra(KEY_MOTIONNAME_DETAIL);
            showInfo();
        }
        if (TextUtils.isEmpty(mTitle)) {
            mTVtTitle.setText(getText(R.string.show_details));
        } else {
            mTVtTitle.setText(mTitle);
            mRyNext.setVisibility(View.INVISIBLE);
        }
        mTVtTitle.setTextColor(getResources().getColor(R.color.color_FFFFFF));
        mTitleLayout.setBackgroundColor(Color.parseColor("#000000"));
        hideLoading();
    }

    private void showInfo() {
        //根据url 画框
        String point = mPosition;
        //给图片中的人脸、车加框
        String newUrl = mIvUrl;
        List<String> list = new ArrayList<>();
        if (!TextUtils.isEmpty(point)) {
            point = point.replace(".", ",");
            list.add(point);
            newUrl = TujieImageUtils.circleTarget(mIvUrl, list);
        }
        mImageLoader.loadImage(this, ImageConfigImpl
                .builder()
                .cacheStrategy(0)
                .placeholder(R.drawable.ic_image_default)
                .errorPic(R.drawable.ic_image_load_failed)
                .url(UrlConstant.parseImageUrl(newUrl))
                .imageView(mIvCapture)
                .build());
        mTvAdddress.setText(mAdress);
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
    public void getDetectImgFeatureByPathSucess(FeatureByPathResponseEntity entity) {
        String sessionKey = entity.getSessionKey();
        Log.d(TAG, sessionKey);
        if (TextUtils.isEmpty(sessionKey)) {
            showMessage(getString(R.string.no_recognized));
            return;
        }
        String imgPath = entity.getImgPath();
        Log.d(TAG, sessionKey);
        String dataKey = entity.getDataKey();
        String datatype = entity.getDataType();
        if (datatype.equals(FACE_TYPE) || datatype.equals(VEHICLE_TYPE)) {
            if (!TextUtils.isEmpty(mPosition)) {
                String position = mPosition.replace('.', ',');
                ARouter.getInstance().build(RouterHub.YTST_SELECT_TARGET_ACTIVITY)
                        .withString(KEY_SEESION, sessionKey)
                        .withString(KEY_PICPATH, imgPath)
                        .withString(KEY_DATA_KEY, dataKey)
                        .withString(KEY_DATA_TYPE, datatype)
                        .withString(KEY_POSITION, position)
                        .navigation(this);
            }
        } else {
            showMessage(getString(R.string.no_tag));
        }
    }

    @Override
    public void getDetectImgFeatureByPathFail(String message) {
        showMessage(message);
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

    @OnClick({R2.id.head_left_iv})
    public void onClickView(View v) {
        int id = v.getId();
        if (id == R.id.head_left_iv) {
            killMyself();
        }
    }

    @OnClick({R2.id.ry_next, R2.id.ry_downIv})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.ry_next) {
            setRequest();
        } else if (id == R.id.ry_downIv) {
            mImageDownloadUtil.downloadImage(UrlConstant.parseImageUrl(mIvUrl), new ImageDownloadUtil.ImageLoadListener() {
                @Override
                public void onLoadingStarted(String downloadUrl) {
                    Log.i(TAG, "onLoadingStarted downloadUrl:" + downloadUrl);
                }

                @Override
                public void onLoadingFailed(String downloadUrl, LoadErrorEntity errorEntity) {
                    Log.i(TAG, "onLoadingFailed errorEntity:" + errorEntity.toString());
                }

                @Override
                public void onLoadingComplete(String downloadUrl, String downloadPath, boolean saveResult, Drawable drawable) {
                    showMessage(getResources().getString(R.string.download_image_success, downloadPath));
                    Log.i(TAG, "onLoadingComplete saveResult:" + saveResult);
                }
            });
        }
    }

    private void setRequest() {
        mFeatureByPathRequestEntity.setImgPath(mIvUrl);
        mPresenter.getDetectImgFeatureByPath();
    }
}
