package com.netposa.component.sfjb.mvp.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.cameraview.CameraView;
import com.gyf.barlibrary.ImmersionBar;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.constant.CommonConstant;
import com.netposa.common.log.Log;
import com.netposa.component.imageselect.util.ImageSelectUtil;
import com.netposa.component.sfjb.R2;
import com.netposa.component.sfjb.di.component.DaggerGetImageComponent;
import com.netposa.component.sfjb.di.module.GetImageModule;
import com.netposa.component.sfjb.mvp.contract.GetImageContract;
import com.netposa.component.sfjb.mvp.presenter.GetImagePresenter;
import com.netposa.component.sfjb.R;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Inject;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.common.core.RouterHub.SFJB_GET_IMAGE_ACTIVITY;
import static com.netposa.component.imageselect.util.ImageSelectUtil.REQUEST_CODE_CHOOSE;
import static com.netposa.component.sfjb.app.SfjbConstants.CAPTURE_PIC_PATH;
import static com.netposa.component.sfjb.app.SfjbConstants.FACE_CAPTURE_PIC;

@Route(path = SFJB_GET_IMAGE_ACTIVITY)
public class GetImageActivity extends BaseActivity<GetImagePresenter> implements GetImageContract.View {

    @BindView(R2.id.camera_view_image)
    CameraView mCameraView;
    @BindView(R2.id.album_btn)
    ImageView mAlbumBtn;

    @Inject
    ImageLoader mImageLoader;

    private Handler mBackgroundHandler;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerGetImageComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .getImageModule(new GetImageModule(this))
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
        setContentView(R.layout.activity_get_image);
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarColor(R.color.black_alpha3)
                .statusBarDarkFont(false, 0.2f)
                .init();
        //绑定到butterknife
        ButterKnife.bind(this);

        if (mCameraView != null) {
            mCameraView.setFacing(CameraView.FACING_FRONT);
            mCameraView.addCallback(mCallback);
        }
        initLatestAlbum();
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void showLoading(String message) {

    }

    @Override
    public void hideLoading() {

    }

    @OnClick({R2.id.toolbar, R2.id.change_camera, R2.id.album_btn, R2.id.capture_btn})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.toolbar) {
            killMyself();
        } else if (id == R.id.change_camera) {
            if (mCameraView != null) {
                int facing = mCameraView.getFacing();
                mCameraView.setFacing(facing == CameraView.FACING_FRONT ?
                        CameraView.FACING_BACK : CameraView.FACING_FRONT);
            }
        } else if (id == R.id.album_btn) {
            ImageSelectUtil.selectImages(this, 1);
        } else if (id == R.id.capture_btn) {
            mCameraView.takePicture();
        }
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

                    launchActivity(new Intent(GetImageActivity.this, FaceCropActivity.class));
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

    private void initLatestAlbum() {
        Cursor cursor = getApplicationContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                ContactsContract.Contacts.Photo._ID + " desc");
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                String mediaPath = cursor.getString(cursor
                        .getColumnIndex(MediaStore.MediaColumns.DATA));
                mImageLoader.loadImage(this, ImageConfigImpl
                        .builder()
                        .cacheStrategy(0)
                        .placeholder(R.drawable.ic_image_default)
                        .url(mediaPath)
                        .imageView(mAlbumBtn)
                        .build());
            }
        }
        cursor.close();
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            Intent startIntent = new Intent(GetImageActivity.this, FaceCropActivity.class);
            startIntent.putExtra(CAPTURE_PIC_PATH, Matisse.obtainPathResult(data).get(0));
            launchActivity(startIntent);
        }
    }
}
