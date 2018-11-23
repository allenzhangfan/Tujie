package com.netposa.component.sfjb.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;


import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.constant.CommonConstant;
import com.netposa.common.utils.ImageUtils;
import com.netposa.common.utils.ToastUtils;
import com.netposa.component.sfjb.R2;
import com.netposa.component.sfjb.di.component.DaggerFaceCropComponent;
import com.netposa.component.sfjb.di.module.FaceCropModule;
import com.netposa.component.sfjb.mvp.contract.FaceCropContract;
import com.netposa.component.sfjb.mvp.presenter.FaceCropPresenter;
import com.netposa.component.sfjb.R;
import com.edmodo.cropper.CropImageView;


import java.io.File;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.component.sfjb.app.SfjbConstants.CAPTURE_PIC_PATH;
import static com.netposa.component.sfjb.app.SfjbConstants.FACE_CAPTURE_PIC;

public class FaceCropActivity extends BaseActivity<FaceCropPresenter> implements FaceCropContract.View {

    @BindView(R2.id.faceCropImageView)
    CropImageView mCropImageView;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerFaceCropComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .faceCropModule(new FaceCropModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_face_crop; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        Bitmap bitmap=null;
        String pathString = getIntent().getStringExtra(CAPTURE_PIC_PATH);
        if (null!=pathString){
            bitmap = ImageUtils.imgToBitmap(pathString);
        }else{
            File file = new File(CommonConstant.PICTURE_PATH, FACE_CAPTURE_PIC);
            bitmap = ImageUtils.imgToBitmap(file.getPath());
        }
        if (null!=bitmap){
            mCropImageView.setImageBitmap(bitmap);
        }
    }

    @OnClick({R2.id.toolbar,R2.id.crop_pic_face})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.toolbar) {
            killMyself();
        } else if (id==R.id.crop_pic_face){
            // 截取
            final Bitmap croppedImage = mCropImageView.getCroppedImage();
            launchActivity(new Intent(this,FaceComparResultActivity.class));
        }
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
}
