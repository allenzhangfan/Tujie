package com.netposa.camera.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.cameraview.CameraView;
import com.google.android.cameraview.R;
import com.google.android.cameraview.R2;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.camera.di.component.DaggerFaceLoginComponent;
import com.netposa.camera.di.module.FaceLoginModule;
import com.netposa.camera.mvp.contract.FaceLoginContract;
import com.netposa.camera.mvp.presenter.FaceLoginPresenter;
import com.netposa.common.log.Log;
import com.netposa.common.utils.Configuration;
import com.netposa.common.utils.ImageUtils;
import com.netposa.common.utils.TimeUtils;
import com.netposa.component.imageselect.util.ImageSelectUtil;
import com.zhihu.matisse.Matisse;

import java.io.File;

import javax.inject.Inject;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.camera.mvp.app.Constants.ALBUM_PIC_PATH;
import static com.netposa.camera.mvp.app.Constants.CAMERRA_FLAG;
import static com.netposa.camera.mvp.app.Constants.CAPTURE_PIC_PATH;
import static com.netposa.common.constant.GlobalConstants.FACE_CAPTURE_PIC;
import static com.netposa.common.constant.GlobalConstants.HAS_FACE;
import static com.netposa.common.core.RouterHub.CAMERA_FACE_LOGIN_ACTIVITY;
import static com.netposa.component.imageselect.util.ImageSelectUtil.REQUEST_CODE_CHOOSE;

/**
 * Google开源项目CameraView   https://github.com/google/cameraview
 */
@Route(path = CAMERA_FACE_LOGIN_ACTIVITY)
public class FaceLoginActivity extends BaseActivity<FaceLoginPresenter> implements FaceLoginContract.View {

    @BindView(R2.id.toolbar)
    Toolbar mToolbar;
    @BindView(R2.id.camera_view_image)
    CameraView mCameraView;
    @BindView(R2.id.capture_btn)
    ImageButton mTakePic;
    @BindView(R2.id.album_btn)
    ImageView mAlbumBtn;
    @BindView(R2.id.change_camera)
    ImageButton mChangeCamera;
    @BindView(R2.id.tv_title)
    TextView mTitle;

    @Inject
    ImageLoader mImageLoader;
    private Handler mBackgroundHandler;
    private String mTypeStr;
    private int mFacing;//记录当前camera是前置还是后置的状态

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerFaceLoginComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .faceLoginModule(new FaceLoginModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_face_login; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        Intent data = getIntent();
        if (data == null) {
            Log.e(TAG, "intent data is null,please check !");
            return;
        }
        mTypeStr = data.getStringExtra(HAS_FACE);
        // 登陆模块
        if (!TextUtils.isEmpty(mTypeStr)) {
            mTitle.setText(getString(R.string.face_login));
            mAlbumBtn.setVisibility(View.INVISIBLE);
            mChangeCamera.setVisibility(View.INVISIBLE);
        } else {// 身份鉴别
            mPresenter.initLatestAlbum(this, mAlbumBtn);
            mTitle.setText(getString(R.string.sfjb));
            mAlbumBtn.setVisibility(View.VISIBLE);
            mChangeCamera.setVisibility(View.VISIBLE);
        }
        mCameraView.setFacing(CameraView.FACING_BACK);
        if (mCameraView != null) {
            mCameraView.addCallback(mCallback);
        }
    }

    @Override
    public void showLoading(String message) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCameraView.start();
    }

    @Override
    protected void onPause() {
        mCameraView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler.getLooper().quitSafely();
            } else {
                mBackgroundHandler.getLooper().quit();
            }
            mBackgroundHandler = null;
        }
    }

    @OnClick({R2.id.toolbar, R2.id.change_camera, R2.id.album_btn, R2.id.capture_btn})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.toolbar) {
            killMyself();
        } else if (id == R.id.change_camera) {
            if (mCameraView != null) {
                mFacing = mFacing == CameraView.FACING_FRONT ? CameraView.FACING_BACK : CameraView.FACING_FRONT;
                mCameraView.setFacing(mFacing);
                Log.i(TAG, mFacing == CameraView.FACING_FRONT ? "已切换至前置摄像头!" : "已切换至后置摄像头!");
            }
        } else if (id == R.id.album_btn) {
            ImageSelectUtil.selectImages(this, 1);
        } else if (id == R.id.capture_btn) {
            mCameraView.takePicture();
        }
    }

    private CameraView.Callback mCallback = new CameraView.Callback() {

        @Override
        public void onCameraOpened(CameraView cameraView) {
            Log.d(TAG, "onCameraOpened");
            mFacing = cameraView.getFacing();
            cameraView.setAutoFocus(true);//自动对焦
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            Log.d(TAG, "onCameraClosed");
            super.onCameraClosed(cameraView);
        }

        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {
            Log.d(TAG, "onPictureTaken " + data.length);
            getBackgroundHandler().post(new Runnable() {
                @Override
                public void run() {
                    String picName = TimeUtils.millis2String(System.currentTimeMillis(), TimeUtils.LONG_DATE_FORMAT)
                            .concat("_")
                            .concat(FACE_CAPTURE_PIC);
                    File file = new File(Configuration.getPictureDirectoryPath(), picName);
                    boolean result = ImageUtils.save(data, file);
                    Log.i(TAG, "back camera , save picture result:" + result);
                    runOnUiThread(() -> {
                        Intent intent = new Intent(FaceLoginActivity.this, CropPictureActivity.class);
                        intent.putExtra(HAS_FACE, mTypeStr);
                        intent.putExtra(CAMERRA_FLAG, mFacing);
                        intent.putExtra(CAPTURE_PIC_PATH, file.getAbsolutePath());
                        launchActivity(intent);
                    });

                }
            });
        }
    };

    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            Intent startIntent = new Intent(this, CropPictureActivity.class);
            startIntent.putExtra(HAS_FACE, mTypeStr);
            startIntent.putExtra(ALBUM_PIC_PATH, Matisse.obtainPathResult(data).get(0));
            launchActivity(startIntent);
        }
    }
}
