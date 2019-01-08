package com.netposa.camera.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MultipartBody;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.edmodo.cropper.CropImageView;
import com.google.android.cameraview.R;
import com.google.android.cameraview.R2;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.AppManager;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.camera.di.component.DaggerCropPictureComponent;
import com.netposa.camera.di.module.CropPictureModule;
import com.netposa.camera.mvp.contract.CropPictureContract;
import com.netposa.camera.mvp.model.entity.UploadResponseEntity;
import com.netposa.camera.mvp.presenter.CropPicturePresenter;
import com.netposa.common.core.RouterHub;
import com.netposa.common.entity.HttpResponseEntity;
import com.netposa.common.log.Log;
import com.netposa.common.net.HttpConstant;
import com.netposa.common.utils.ImageUtils;
import com.netposa.common.utils.RequestUtils;
import com.netposa.commonres.widget.Dialog.LottieDialogFragment;

import javax.inject.Inject;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.camera.mvp.app.Constants.ALBUM_PIC_PATH;
import static com.netposa.camera.mvp.app.Constants.CAMERRA_FLAG;
import static com.netposa.camera.mvp.app.Constants.CAPTURE_PIC_PATH;
import static com.netposa.common.constant.GlobalConstants.HAS_FACE;
import static com.netposa.common.constant.GlobalConstants.KEY_FEATUR;
import static com.netposa.common.constant.GlobalConstants.KEY_ZHUAPAI_URL;
import static com.netposa.common.constant.GlobalConstants.PART_NAME_IMAGE;
import static com.netposa.common.core.RouterHub.CAMERA_CROP_PICTURE_ACTIVITY;

@Route(path = CAMERA_CROP_PICTURE_ACTIVITY)
public class CropPictureActivity extends BaseActivity<CropPicturePresenter> implements CropPictureContract.View {

    @BindView(R2.id.cropImageView)
    CropImageView mCropImageView;

    @Inject
    LottieDialogFragment mLoadingDialogFragment;

    private String mTypeStr;
    private String mUrlPath, mAlbumPath;
    private int mFacing;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCropPictureComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .cropPictureModule(new CropPictureModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_crop_picture; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }


    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        Bitmap bitmap = null;
        Intent data = getIntent();
        if (data == null) {
            Log.e(TAG, "intent data is null,please check !");
            return;
        }
        mTypeStr = data.getStringExtra(HAS_FACE);
        mUrlPath = getIntent().getStringExtra(CAPTURE_PIC_PATH);// 拍照
        mAlbumPath = getIntent().getStringExtra(ALBUM_PIC_PATH);// 相册图片
        mFacing = getIntent().getIntExtra(CAMERRA_FLAG, ImageUtils.FACING_BACK);
        if (null != mUrlPath) {
            bitmap = ImageUtils.rotateBitmap(mUrlPath, mFacing);
        } else {
            bitmap = ImageUtils.imgToBitmap(mAlbumPath);
        }
        if (null != bitmap) {
            mCropImageView.setImageBitmap(bitmap);
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

    @Override
    public void killMyself() {
        finish();
    }


    @OnClick({R2.id.crop_pic, R2.id.toolbar})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.crop_pic) {
            mPresenter.faceLoginOrCropImage(mCropImageView.getCroppedImage(),mTypeStr);
        } else if (id == R.id.toolbar) {
            killMyself();
        }
    }

    @Override
    public void goToHomeActivity() {
        ARouter.getInstance().build(RouterHub.APP_HOME_ACTIVITY).navigation(this);
        AppManager.getAppManager().killAll();//登录成功后kill掉之前的所有activity
    }

    @Override
    public void uploadImageSuccess(HttpResponseEntity<UploadResponseEntity> entity) {
        if (entity.code == HttpConstant.IS_SUCCESS) {
            String featur = entity.data.getFeature();
            String sourceUrl = entity.data.getSourceUrl();
            Log.d(TAG, featur);
            ARouter.getInstance().build(RouterHub.SFJB_CHOSE_LIB_ACTIVITY)
                    .withString(KEY_FEATUR, featur)
                    .withString(KEY_ZHUAPAI_URL, sourceUrl)
                    .navigation(this);
        } else {
            String message = entity.message;
            if (!TextUtils.isEmpty(message)) {
                showMessage(message);
            }
        }

    }

    @Override
    public void uploadImageFail() {
        showMessage(getString(R.string.update_failed));
    }

}
