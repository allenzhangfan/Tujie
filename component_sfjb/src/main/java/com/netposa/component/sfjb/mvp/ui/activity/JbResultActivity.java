package com.netposa.component.sfjb.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.constant.UrlConstant;
import com.netposa.common.core.RouterHub;
import com.netposa.common.log.Log;
import com.netposa.common.utils.StringUtils;
import com.netposa.common.utils.TimeUtils;
import com.netposa.commonres.widget.CircleProgressView;
import com.netposa.commonres.widget.Dialog.LottieDialogFragment;
import com.netposa.commonres.widget.RoundImageView;
import com.netposa.component.sfjb.R;
import com.netposa.component.sfjb.R2;
import com.netposa.component.sfjb.di.component.DaggerJbResultComponent;
import com.netposa.component.sfjb.di.module.JbResultModule;
import com.netposa.component.sfjb.mvp.contract.JbResultContract;
import com.netposa.component.sfjb.mvp.model.entity.DetailInfoEntity;
import com.netposa.component.sfjb.mvp.model.entity.FaceDetailEntity;
import com.netposa.component.sfjb.mvp.model.entity.PersonInfoEntity;
import com.netposa.component.sfjb.mvp.presenter.JbResultPresenter;
import com.netposa.component.sfjb.mvp.ui.adapter.LibPhotoAdapter;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.common.constant.GlobalConstants.KEY_IV_DETAIL;
import static com.netposa.common.constant.GlobalConstants.KEY_MOTIONNAME_DETAIL;
import static com.netposa.common.constant.GlobalConstants.KEY_TIME_DETAIL;
import static com.netposa.common.constant.GlobalConstants.KEY_TITLE;
import static com.netposa.common.constant.GlobalConstants.TYPE_FAMALE;
import static com.netposa.common.constant.GlobalConstants.TYPE_MALE;
import static com.netposa.component.sfjb.app.SfjbConstants.KEY_TO_FACE_CAMPARE;


@Route(path = RouterHub.SFJB_JB_RESULT_ACTIVITY)
public class JbResultActivity extends BaseActivity<JbResultPresenter> implements JbResultContract.View {

    @BindView(R2.id.title_tv)
    TextView mTitleTv;
    @BindView(R2.id.head_right_tv)
    TextView mHeadRightTv;
    @BindView(R2.id.iv_car)
    RoundImageView mIvCar;
    @BindView(R2.id.rv_car_info)
    RecyclerView mRvCarInfo;
    @BindView(R2.id.simil_txt)
    TextView mSimil_Txt;
    @BindView(R2.id.similar_progresview)
    CircleProgressView mCircleProgressView;

    @Inject
    LottieDialogFragment mLoadingDialogFragment;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.ItemAnimator mItemAnimator;
    @Inject
    List<MultiItemEntity> mBeanList;
    @Inject
    LibPhotoAdapter mAdapter;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    FaceDetailEntity mFaceDetailEntity;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerJbResultComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .jbResultModule(new JbResultModule(this, this))
                .build().
                inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_jb_result; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        Intent data = getIntent();
        if (data == null) {
            Log.e(TAG, "intent data is null,please check !");
            return;
        }
        mFaceDetailEntity = data.getParcelableExtra(KEY_TO_FACE_CAMPARE);
        mTitleTv.setText(this.getString(R.string.compare_lib_iv));
        mRvCarInfo.setLayoutManager(mLayoutManager);
        mRvCarInfo.setItemAnimator(mItemAnimator);
        mRvCarInfo.setAdapter(mAdapter);
        showPersonIv();
        showDetailInfo();
    }

    private void showPersonIv() {

        mImageLoader.loadImage(this, ImageConfigImpl
                .builder()
                .cacheStrategy(0)
                .placeholder(R.drawable.ic_image_default)
                .errorPic(R.drawable.ic_image_load_failed)
                .url(UrlConstant.parseImageUrl(mFaceDetailEntity.getPicUrl()))
                .imageView(mIvCar)
                .build());
        double similarity = mFaceDetailEntity.getSimilarity();
        mCircleProgressView.setScore(StringUtils.dealSimilarity(similarity), true);
        mSimil_Txt.setText(getResources().getString(R.string.similarity_percent, StringUtils.dealSimilarityBackInt(similarity)));
    }

    private void showDetailInfo() {
        PersonInfoEntity entity1 = new PersonInfoEntity();
        entity1.setTitle(getString(R.string.name));
        entity1.setDividerVisable(true);
        entity1.setDescription(mFaceDetailEntity.getName());
        mBeanList.add(entity1);

        PersonInfoEntity entity2 = new PersonInfoEntity();
        entity2.setTitle(getString(R.string.sex));
        entity2.setDividerVisable(true);
        String gender = mFaceDetailEntity.getGender();
        if (TextUtils.isEmpty(gender)) {
            entity2.setDescription(getString(R.string.clcx_unknow));
        } else {
            if (gender.equals(TYPE_MALE)) {
                entity2.setDescription(getString(R.string.male));
            } else if (gender.equals(TYPE_FAMALE)) {
                entity2.setDescription(getString(R.string.famale));
            } else {
                entity2.setDescription(getString(R.string.clcx_unknow));
            }
        }
        mBeanList.add(entity2);

        PersonInfoEntity entity3 = new PersonInfoEntity();
        entity3.setTitle(getString(R.string.person_id));
        entity3.setDividerVisable(true);
        entity3.setDescription(mFaceDetailEntity.getIdCard());
        mBeanList.add(entity3);

        PersonInfoEntity entity4 = new PersonInfoEntity();
        entity4.setTitle(getString(R.string.belong_ku));
        entity4.setDividerVisable(true);
        entity4.setDescription(mFaceDetailEntity.getLibName());
        mBeanList.add(entity4);

        PersonInfoEntity entity5 = new PersonInfoEntity();
        entity5.setTitle(getString(R.string.creat_date));
        entity5.setDividerVisable(true);
        entity5.setDescription(TimeUtils.millis2String(mFaceDetailEntity.getCreateTime()));
        mBeanList.add(entity5);

        DetailInfoEntity entity6 = new DetailInfoEntity();
        entity6.setTitle(getString(R.string.detail_info));
        entity6.setDescription(mFaceDetailEntity.getRemark());
        mBeanList.add(entity6);
        mAdapter.notifyDataSetChanged();
    }

    @OnClick({R2.id.head_left_iv,
            R2.id.ll_detail})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.head_left_iv) {
            killMyself();
        }
        if (id == R.id.ll_detail) {
            if (mFaceDetailEntity != null) {
                ARouter.getInstance().build(RouterHub.PIC_SINGLE_PIC_PREVIEW_ACTIVITY)
                        .withString(KEY_IV_DETAIL, mFaceDetailEntity.getPicUrl())
                        .withString(KEY_MOTIONNAME_DETAIL, mFaceDetailEntity.getLibName())
                        .withString(KEY_TITLE, getString(R.string.look_orginal_iv))
                        .withLong(KEY_TIME_DETAIL, mFaceDetailEntity.getCreateTime())
                        .navigation(this);
            }
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
}
