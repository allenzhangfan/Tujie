package com.netposa.component.login.mvp.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;

import com.google.android.cameraview.CameraView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.constant.CommonConstant;
import com.netposa.common.log.Log;
import com.netposa.component.login.R2;
import com.netposa.component.login.di.component.DaggerFaceLoginComponent;
import com.netposa.component.login.di.module.FaceLoginModule;
import com.netposa.component.login.mvp.contract.FaceLoginContract;
import com.netposa.component.login.mvp.presenter.FaceLoginPresenter;
import com.netposa.component.login.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.component.login.app.LoginConstants.FACE_CAPTURE_PIC;

/**
 * Google开源项目CameraView   https://github.com/google/cameraview
 */
public class FaceLoginActivity extends BaseActivity<FaceLoginPresenter> implements FaceLoginContract.View {

    @BindView(R2.id.toolbar)
    Toolbar mToolbar;
    @BindView(R2.id.camera_view)
    CameraView mCameraView;
    @BindView(R2.id.take_pic)
    ImageButton mTakePic;

    //    private  CameraView mCameraView;
    private Handler mBackgroundHandler;

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
        return R.layout.take_front_camera; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mCameraView.setFacing(CameraView.FACING_FRONT);
        if (mCameraView != null) {
            mCameraView.addCallback(mCallback);
        }
        mTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCameraView.takePicture();
            }
        });
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

    @OnClick({R2.id.toolbar})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.toolbar) {
            killMyself();
        }
    }

    private CameraView.Callback mCallback = new CameraView.Callback() {

        @Override
        public void onCameraOpened(CameraView cameraView) {
            Log.d(TAG, "onCameraOpened");
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            Log.d(TAG, "onCameraClosed");
        }

        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {
            Log.d(TAG, "onPictureTaken " + data.length);
            getBackgroundHandler().post(new Runnable() {
                @Override
                public void run() {
                    File file = new File(CommonConstant.PICTURE_PATH,
                            FACE_CAPTURE_PIC);
                    OutputStream os = null;
                    try {
                        os = new FileOutputStream(file);
                        os.write(data);
                        os.close();
                    } catch (IOException e) {
                        Log.w(TAG, "Cannot write to " + file, e);
                    } finally {
                        if (os != null) {
                            try {
                                os.close();
                            } catch (IOException e) {
                                Log.e(TAG, "onPictureTaken :" + e.toString());
                            }
                        }
                    }
                    Intent cropIntent = new Intent(FaceLoginActivity.this, CropPictureActivity.class);
                    launchActivity(cropIntent);
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
}
