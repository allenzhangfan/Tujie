package com.netposa.component.ytst.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.entity.HttpResponseEntity;
import com.netposa.common.log.Log;
import com.netposa.common.net.HttpConstant;
import com.netposa.common.utils.ImageUtils;
import com.netposa.commonres.widget.Dialog.LottieDialogFragment;
import com.netposa.component.ytst.R2;
import com.netposa.component.ytst.di.component.DaggerShowTakePictureComponent;
import com.netposa.component.ytst.di.module.ShowTakePictureModule;
import com.netposa.component.ytst.mvp.contract.ShowTakePictureContract;
import com.netposa.component.ytst.mvp.model.entity.UploadPicResponseEntity;
import com.netposa.component.ytst.mvp.presenter.ShowTakePicturePresenter;
import com.netposa.component.ytst.R;


import java.util.ArrayList;
import javax.inject.Inject;
import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.camera.mvp.app.Constants.CAMERRA_FLAG;
import static com.netposa.common.constant.GlobalConstants.KEY_PICPATH;
import static com.netposa.common.constant.GlobalConstants.KEY_POSITION;
import static com.netposa.common.constant.GlobalConstants.KEY_SEESION;
import static com.netposa.component.ytst.app.YtstConstants.KEY_INTENT_PIC;
import static com.netposa.component.ytst.app.YtstConstants.KEY_INTENT_TYPE;
import static com.netposa.component.ytst.app.YtstConstants.TYPE_CAR;


public class ShowTakePictureActivity extends BaseActivity<ShowTakePicturePresenter> implements ShowTakePictureContract.View {

    @BindView(R2.id.iv_img)
    ImageView mImageView;

    private String mUrlPath;
    private int mFacing;
    private String mType;//搜车或者人的标识
    private Bitmap mBitmap;

    @Inject
    LottieDialogFragment mLoadingDialogFragment;
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerShowTakePictureComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .showTakePictureModule(new ShowTakePictureModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_show_take_picture; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        Intent data = getIntent();
        if (data == null) {
            Log.e(TAG, "intent data is null,please check !");
            return;
        }
        mFacing = getIntent().getIntExtra(CAMERRA_FLAG, ImageUtils.FACING_BACK);
        mUrlPath = getIntent().getStringExtra(KEY_INTENT_PIC);// 拍照
        mType=getIntent().getStringExtra(KEY_INTENT_TYPE);

        // 根据摄像头前置选择处理
        mBitmap = ImageUtils.rotateBitmap(mUrlPath, mFacing);
        if (null != mBitmap) {
            mImageView.setImageBitmap(mBitmap);
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
        killMyself();
    }


    @OnClick({R2.id.toolbar, R2.id.submit_pic})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.toolbar) {
            killMyself();
        } else if (id == R.id.submit_pic) {
            showLoading("");
            mPresenter.compressImage(mBitmap);
        }
    }


    @Override
    public void killMyself() {
        finish();
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
                    showMessage(getString(R.string.take_pic_car));
                } else {
                    showMessage(getString(R.string.take_pic_face));
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
