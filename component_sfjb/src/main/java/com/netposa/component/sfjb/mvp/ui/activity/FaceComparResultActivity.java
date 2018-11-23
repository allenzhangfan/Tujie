package com.netposa.component.sfjb.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.commonres.widget.CircleProgressView;
import com.netposa.component.sfjb.R2;
import com.netposa.component.sfjb.di.component.DaggerFaceComparResultComponent;
import com.netposa.component.sfjb.di.module.FaceComparResultModule;
import com.netposa.component.sfjb.mvp.contract.FaceComparResultContract;
import com.netposa.component.sfjb.mvp.presenter.FaceComparResultPresenter;
import com.netposa.component.sfjb.R;

import static com.jess.arms.utils.Preconditions.checkNotNull;

public class FaceComparResultActivity extends BaseActivity<FaceComparResultPresenter> implements FaceComparResultContract.View {

    @BindView(R2.id.similar_progresview)
    CircleProgressView mCircleProgressView;

    @BindView(R2.id.title_tv)
    TextView mTitle_txt;


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerFaceComparResultComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .faceComparResultModule(new FaceComparResultModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_face_compar_result; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mTitle_txt.setText(this.getString(R.string.recognition_result));
        mTitle_txt.getPaint().setFakeBoldText(true);

        mCircleProgressView.setScore(90, false,"%");
    }

    @Override
    public void showLoading(String message) {

    }


    @OnClick({R2.id.head_left_iv})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.head_left_iv) {
            killMyself();
        }
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
