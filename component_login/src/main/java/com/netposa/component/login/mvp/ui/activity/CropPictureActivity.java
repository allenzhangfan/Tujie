package com.netposa.component.login.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

import com.edmodo.cropper.CropImageView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.constant.CommonConstant;
import com.netposa.common.utils.ImageUtils;
import com.netposa.component.login.R2;
import com.netposa.component.login.di.component.DaggerCropPictureComponent;
import com.netposa.component.login.di.module.CropPictureModule;
import com.netposa.component.login.mvp.contract.CropPictureContract;
import com.netposa.component.login.mvp.presenter.CropPicturePresenter;
import com.netposa.component.login.R;

import java.io.File;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.component.login.app.LoginConstants.FACE_CAPTURE_PIC;

/**
 * CropImageView   https://github.com/edmodo/cropper
 */

public class CropPictureActivity extends BaseActivity<CropPicturePresenter> implements CropPictureContract.View {

    @BindView(R2.id.cropImageView)
    CropImageView mCropImageView;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCropPictureComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .cropPictureModule(new CropPictureModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_cropperpic; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }


    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        File file = new File(CommonConstant.PICTURE_PATH, FACE_CAPTURE_PIC);
        Bitmap bitmap = ImageUtils.imgToBitmap(file.getPath());
//        Bitmap bitmap =  getSmallBitmap(file.getPath());
//        ImageUtils.recyleBitMap(bitmap);
        if (null!=bitmap){
            mCropImageView.setImageBitmap(bitmap);
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


    @OnClick({R2.id.crop_pic, R2.id.toolbar})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.crop_pic) {
//            //获取裁剪的图片
//            Bitmap cropBitMap = mCropImageView.getCroppedImage();
//            mCropImageView.setImageBitmap(cropBitMap);
        } else if (id == R.id.toolbar) {
            killMyself();
        }
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {//图片所在SD卡的路径
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, 480, 700);//自定义一个宽和高
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
        final int height = options.outHeight;//获取图片的高
        final int width = options.outWidth;//获取图片的框
        int inSampleSize = 4;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;//求出缩放值
    }

}
