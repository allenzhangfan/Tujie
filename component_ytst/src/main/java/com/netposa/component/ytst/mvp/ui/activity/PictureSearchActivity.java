package com.netposa.component.ytst.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.cameraview.CameraView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.core.RouterHub;
import com.netposa.common.entity.HttpResponseEntity;
import com.netposa.common.log.Log;
import com.netposa.common.net.HttpConstant;
import com.netposa.common.utils.Configuration;
import com.netposa.common.utils.ImageUtils;
import com.netposa.common.utils.RequestUtils;
import com.netposa.common.utils.TimeUtils;
import com.netposa.commonres.widget.Dialog.LottieDialogFragment;
import com.netposa.component.imageselect.util.ImageSelectUtil;
import com.netposa.component.ytst.R;
import com.netposa.component.ytst.R2;
import com.netposa.component.ytst.di.component.DaggerPictureSearchComponent;
import com.netposa.component.ytst.di.module.PictureSearchModule;
import com.netposa.component.ytst.mvp.contract.PictureSearchContract;
import com.netposa.component.ytst.mvp.model.entity.UploadPicResponseEntity;
import com.netposa.component.ytst.mvp.presenter.PictureSearchPresenter;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.util.ArrayList;


import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MultipartBody;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.common.constant.GlobalConstants.FACE_CAPTURE_PIC;
import static com.netposa.common.constant.GlobalConstants.KEY_PICPATH;
import static com.netposa.common.constant.GlobalConstants.KEY_POSITION;
import static com.netposa.common.constant.GlobalConstants.KEY_SEESION;
import static com.netposa.common.constant.GlobalConstants.PART_NAME_IMAGE;
import static com.netposa.component.imageselect.util.ImageSelectUtil.REQUEST_CODE_CHOOSE;
import static com.netposa.component.ytst.app.YtstConstants.TYPE_CAR;
import static com.netposa.component.ytst.app.YtstConstants.TYPE_FACE;

@Route(path = RouterHub.YTST_PICTURE_SEARCH_ACTIVITY)
public class PictureSearchActivity extends BaseActivity<PictureSearchPresenter> implements PictureSearchContract.View {

    @BindView(R2.id.tv_tab_search_face)
    TextView mTvSearchPepole;
    @BindView(R2.id.tv_tab_search_car)
    TextView mTvSearchCar;
    @BindView(R2.id.album_btn)
    ImageView mAlbumPic;
    @BindView(R2.id.camera_view_image)
    CameraView mCameraView;

    @Inject
    LottieDialogFragment mLoadingDialogFragment;

    private int mFacing;
    private Handler mBackgroundHandler;
    private String path = null;

    private String mType;//搜车或者人的标识

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPictureSearchComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .pictureSearchModule(new PictureSearchModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_picture_search; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mTvSearchPepole.setTextColor(getResources().getColor(R.color.color_FFFFFF));
        mTvSearchPepole.setBackgroundResource(R.drawable.bound_indictor_blue_shape);

        mCameraView.setFacing(CameraView.FACING_BACK);
        if (mCameraView != null) {
            mCameraView.addCallback(mCallback);
        }
        mPresenter.initLatestAlbum(this, mAlbumPic);
        mType = TYPE_FACE;
    }

    @OnClick({R2.id.head_left_iv,
            R2.id.tv_tab_search_face,
            R2.id.tv_tab_search_car,
            R2.id.capture_btn,
            R2.id.album_btn})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.head_left_iv) {
            killMyself();
        } else if (id == R.id.tv_tab_search_face) {
            mTvSearchPepole.setTextColor(getResources().getColor(R.color.color_FFFFFF));
            mTvSearchPepole.setBackgroundResource(R.drawable.bound_indictor_blue_shape);
            mTvSearchCar.setTextColor(getResources().getColor(R.color.color_2D87F9));
            mTvSearchCar.setBackgroundResource(R.drawable.bound_indictor_white_shape);
            mType = TYPE_FACE;
        } else if (id == R.id.tv_tab_search_car) {
            mTvSearchPepole.setTextColor(getResources().getColor(R.color.color_2D87F9));
            mTvSearchPepole.setBackgroundResource(R.drawable.bound_indictor_white_shape);
            mTvSearchCar.setTextColor(getResources().getColor(R.color.color_FFFFFF));
            mTvSearchCar.setBackgroundResource(R.drawable.bound_indictor_blue_shape);
            mType = TYPE_CAR;
        } else if (id == R.id.album_btn) {
            ImageSelectUtil.selectImages(this, 1);
        } else if (id == R.id.capture_btn) {
            Log.d(TAG, "takePicture");
            showLoading("");
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
                    int kb = 524288;// 1M=1024k=1048576  500kb 就压缩
                    Bitmap dealBitMap = null;
                    String picName = TimeUtils.millis2String(System.currentTimeMillis(), TimeUtils.LONG_DATE_FORMAT)
                            .concat("_")
                            .concat(FACE_CAPTURE_PIC);
                    File file = new File(Configuration.getPictureDirectoryPath(), picName);
                    boolean result = ImageUtils.save(data, file);
                    Log.i(TAG, "back camera , save picture result:" + result);
                    // 根据摄像头前置选择处理
                    Bitmap bmap = ImageUtils.rotateBitmap(file.getAbsolutePath(), mFacing);
                    int size = ImageUtils.getBitmapSize(bmap);
                    if (size > kb) {//压缩完了保存成jpg的图片
                        dealBitMap = ImageUtils.compressScale(bmap);
                        path = ImageUtils.saveBitmap(dealBitMap);
                    } else {
                        path = ImageUtils.saveBitmap(bmap);
                    }
                    runOnUiThread(() -> {
                        startUpload(path);
                    });
                }
            });
        }
    };

    private void startUpload(String path) {
        MultipartBody.Part part = RequestUtils.prepareFilePart(PART_NAME_IMAGE, path);
        mPresenter.uploadImage(part);
    }

    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
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
    public void killMyself() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            String pathStr = Matisse.obtainPathResult(data).get(0);
            if (!TextUtils.isEmpty(pathStr)) {
                showLoading("");
                startUpload(pathStr);
            }
        }
    }

    @Override
    public void uploadImageSuccess(HttpResponseEntity<UploadPicResponseEntity> entity) {
        if (entity.code == HttpConstant.IS_SUCCESS) {
            String sessionKey = entity.data.getSessionKey();
            if (TextUtils.isEmpty(sessionKey)) {
                showMessage(getString(R.string.no_recognized));
                return;
            }
            Log.d(TAG, sessionKey);
            String imgPath = entity.data.getImgPath();
            Log.d(TAG, sessionKey);
            ArrayList<UploadPicResponseEntity.DetectResultMapEntity> DetectResult = entity.data.getDetectResultMap();
            // 添加接口返回 是人是车的判断
            String dataType = DetectResult.get(0).getDataType().toLowerCase();
            if (!TextUtils.isEmpty(dataType) && mType.equals(dataType)) {
                Intent mIntent = new Intent(this, SelectTargetActivity.class);
                mIntent.putExtra(KEY_SEESION, sessionKey);
                mIntent.putExtra(KEY_PICPATH, imgPath);
                mIntent.putParcelableArrayListExtra(KEY_POSITION, DetectResult);
                launchActivity(mIntent);
            } else {
                if (mType.equals(TYPE_CAR)) {
                    showMessage(getString(R.string.take_pic_face));
                } else {
                    showMessage(getString(R.string.take_pic_car));
                }
            }

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
