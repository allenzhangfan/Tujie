package com.netposa.component.sfjb.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import butterknife.BindView;
import butterknife.OnClick;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.constant.UrlConstant;
import com.netposa.common.log.Log;
import com.netposa.common.utils.StringUtils;
import com.netposa.commonres.widget.CircleProgressView;
import com.netposa.commonres.widget.Dialog.LottieDialogFragment;
import com.netposa.commonres.widget.RoundImageView;
import com.netposa.component.sfjb.R2;
import com.netposa.component.sfjb.di.component.DaggerFaceCompareResultComponent;
import com.netposa.component.sfjb.di.module.FaceCompareResultModule;
import com.netposa.component.sfjb.mvp.contract.FaceCompareResultContract;
import com.netposa.component.sfjb.mvp.model.entity.FaceCompareRequestEntity;
import com.netposa.component.sfjb.mvp.model.entity.FaceCompareResponseEntity;
import com.netposa.component.sfjb.mvp.model.entity.FaceDetailEntity;
import com.netposa.component.sfjb.mvp.presenter.FaceCompareResultPresenter;
import com.netposa.component.sfjb.R;
import com.netposa.component.sfjb.mvp.ui.adapter.SfjbCompareResultAdapter;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.common.constant.GlobalConstants.KEY_FEATUR;
import static com.netposa.common.constant.GlobalConstants.KEY_ZHUAPAI_URL;
import static com.netposa.common.constant.GlobalConstants.PAGE_SIZE_DEFAULT;
import static com.netposa.component.sfjb.app.SfjbConstants.KEY_TO_FACE_CAMPARE;
import static com.netposa.component.sfjb.app.SfjbConstants.KEY_TO_LIB_SEARCH_LIB;
import static com.netposa.component.sfjb.app.SfjbConstants.KEY_TO_LIB_SEARCH_ORG;

public class FaceCompareResultActivity extends BaseActivity<FaceCompareResultPresenter> implements FaceCompareResultContract.View,
         BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R2.id.similar_progresview)
    CircleProgressView mCircleProgressView;
    @BindView(R2.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R2.id.title_tv)
    TextView mTitle_txt;
    @BindView(R2.id.iv_control)
    RoundImageView mControlImg;
    @BindView(R2.id.name_txt)
    TextView mName;
    @BindView(R2.id.ku_name_tv)
    TextView mKuName;
    @BindView(R2.id.iv_captureImg)
    RoundImageView mZhuaPaiImg;
    @BindView(R2.id.ll_container)
    LinearLayout mContainer;
    @BindView(R2.id.cl_no_content)
    ConstraintLayout mNoContent;
    @BindView(R2.id.iv_no_content)
    ImageView mNoContentImg;
    @BindView(R2.id.tv_no_content)
    TextView mTvNoContent;

    @Inject
    SfjbCompareResultAdapter mAdapter;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.ItemAnimator mItemAnimator;
    @Inject
    List<FaceCompareResponseEntity.ListBean> mBeanList;
    @Inject
    FaceCompareRequestEntity mFaceCompareRequestEntity;
    @Inject
    FaceDetailEntity mFaceDetailEntity;
    @Inject
    LottieDialogFragment mLoadingDialogFragment;

    private String mFeature;// 人脸查询需要的参数
    private ArrayList<String> mOrgIds; //组织ID；
    private ArrayList<String> mLibIds;// 库ID；
    private int mCurrentPage = 1;
    private String mZhuaPaiUrl;
    private ImageLoader mImageLoader;
    private int postion = 0;
    private List<Integer> mChooseBeanList = new ArrayList<>();// 选择的处理
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerFaceCompareResultComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .faceCompareResultModule(new FaceCompareResultModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_face_compare_result; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        init();
        getIntentDate();
        mPresenter.getFaceCompareResult(getFaceCompareRequestEntity());
        // 点击adapter里数据 切换界面右上角的图片
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            postion = position;
            String tempUrl=null;
            if (mChooseBeanList.size() > 0) {
                if (position == mChooseBeanList.get(0)) { //如果同一个位置
                    mBeanList.get(position).setSelect(false);
                    mChooseBeanList.clear();
                } else {
                    mBeanList.get(mChooseBeanList.get(0)).setSelect(false);
                    mBeanList.get(position).setSelect(true);
                    mChooseBeanList.clear();
                    mChooseBeanList.add(position);
                    tempUrl = mBeanList.get(position).getPhotoInfoExts().get(0).getUrl();
                    // 相似度
                    double similarity= mBeanList.get(position).getPhotoInfoExts().get(0).getScore();
                    mCircleProgressView.setScore(StringUtils.dealSimilarity(similarity), true, "%");
                    // 人名
                    mName.setText(mBeanList.get(position).getName());
                    // 库名
                    mKuName.setText( mBeanList.get(position).getLibName());
                }
            }else {
                mChooseBeanList.add(position);
                mBeanList.get(mChooseBeanList.get(0)).setSelect(true);
                tempUrl = mBeanList.get(position).getPhotoInfoExts().get(0).getUrl();
            }
           if (!TextUtils.isEmpty(tempUrl)){
               showIvImg(tempUrl, mControlImg);
           }
            mAdapter.notifyItemRangeChanged(0, mBeanList.size());
        });
    }

    private void init() {
        mTitle_txt.setText(this.getString(R.string.recognition_result));
        mTvNoContent.setText(R.string.no_result_message);
        mTitle_txt.getPaint().setFakeBoldText(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        ((SimpleItemAnimator) mItemAnimator).setSupportsChangeAnimations(false);
        mRecyclerView.setItemAnimator(mItemAnimator);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mImageLoader = ArmsUtils.obtainAppComponentFromContext(this).imageLoader();
    }

    private void getIntentDate() {
        Intent data = getIntent();
        if (data == null) {
            Log.e(TAG, "intent data is null,please check !");
            return;
        }
        mOrgIds = data.getStringArrayListExtra(KEY_TO_LIB_SEARCH_ORG);
        mLibIds = data.getStringArrayListExtra(KEY_TO_LIB_SEARCH_LIB);
        mFeature = data.getStringExtra(KEY_FEATUR);
        mZhuaPaiUrl = data.getStringExtra(KEY_ZHUAPAI_URL);
    }

    /**
     * 加载显示布控位置的图片
     */
    private void showIvImg(String url, ImageView view) {
        mImageLoader.loadImage(this, ImageConfigImpl
                .builder()
                .cacheStrategy(0)
                .placeholder(R.drawable.ic_image_default)
                .errorPic(R.drawable.ic_image_load_failed)
                .url(UrlConstant.parseImageUrl(url))
                .imageView(view)
                .build());
    }

    private FaceCompareRequestEntity getFaceCompareRequestEntity() {
        mFaceCompareRequestEntity.setLibList(mLibIds);
        mFaceCompareRequestEntity.setOrgList(mOrgIds);
        mFaceCompareRequestEntity.setCurrentPage(mCurrentPage);
        mFaceCompareRequestEntity.setPageSize(20);
        mFaceCompareRequestEntity.setFeature(mFeature);
        mFaceCompareRequestEntity.setSimilarity(80);
        return mFaceCompareRequestEntity;
    }

    @Override
    public void showLoading(String message) {
        mLoadingDialogFragment.show(getSupportFragmentManager(), "LoadingDialog");
    }

    @Override
    public void hideLoading() {
        mLoadingDialogFragment.dismissAllowingStateLoss();
    }

    @OnClick({R2.id.head_left_iv, R2.id.ll_control})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.head_left_iv) {
            killMyself();
        } else if (id == R.id.ll_control) {
            goToJbResultActivity();
        }
    }

    private void goToJbResultActivity() {
        if (mBeanList.size() > 0) {
            FaceCompareResponseEntity.ListBean bean = mBeanList.get(postion);
            Log.d(TAG, bean.toString());
            mFaceDetailEntity.setPicUrl(bean.getPhotoInfoExts().get(0).getUrl());
            mFaceDetailEntity.setSimilarity(bean.getPhotoInfoExts().get(0).getScore());
            mFaceDetailEntity.setName(bean.getName());
            mFaceDetailEntity.setGender(bean.getGender());
            mFaceDetailEntity.setIdCard(bean.getIdCard());
            mFaceDetailEntity.setLibName(bean.getLibName());
            mFaceDetailEntity.setCreateTime(bean.getCreateTime());
            mFaceDetailEntity.setRemark(bean.getRemark());
            Intent mIntent = new Intent(this, JbResultActivity.class);
            mIntent.putExtra(KEY_TO_FACE_CAMPARE, mFaceDetailEntity);
            launchActivity(mIntent);
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

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public void showCompareFaceDetailSuccess(FaceCompareResponseEntity responseEntity) {
        List<FaceCompareResponseEntity.ListBean> responseEntityList = responseEntity.getList();
        if (responseEntityList.size() > 0) {
            showNoDataPage(false);
            showIvImg(mZhuaPaiUrl, mZhuaPaiImg);
            for (int i=0;i<responseEntity.getList().size();i++){
                //默认第一个元素select true
                if (i==0){
                    responseEntityList.get(0).setSelect(true);
                }else {
                    responseEntityList.get(i).setSelect(false);
                }
            }
            mChooseBeanList.add(0);// 默认第一个选中
            // 右上角图片信息
            List<FaceCompareResponseEntity.ListBean.PhotoInfoExtsBean> PhotoInfoExtsBean = responseEntityList.get(0).getPhotoInfoExts();
            //右边的布控图片url
            String bukongPicUrl = PhotoInfoExtsBean.get(0).getUrl();
            String name = responseEntityList.get(0).getName();
            if (!TextUtils.isEmpty(name)) {
                mName.setText(name);
            }
            //相似度
            double similarity = PhotoInfoExtsBean.get(0).getScore();
            mCircleProgressView.setScore(StringUtils.dealSimilarity(similarity), true, "%");
            //人口库
            String kuName = responseEntityList.get(0).getLibName();
            if (!TextUtils.isEmpty(kuName)) {
                mKuName.setText(kuName);
            }
            //布控图片
            showIvImg(bukongPicUrl, mControlImg);
            if (mCurrentPage == 1) {
//                showNoDataPage(false);
                mBeanList.clear();
            }
            mBeanList.addAll(responseEntityList);
            mAdapter.notifyDataSetChanged();
            if (responseEntityList.size() < PAGE_SIZE_DEFAULT) {
                mAdapter.loadMoreEnd(false);
            } else {
                mAdapter.loadMoreComplete();
            }
        } else {
            showNoDataPage(true);
        }
    }

    @Override
    public void showCompareFaceDetailFailed() {
        showNoDataPage(true);
    }

    @Override
    public void onLoadMoreRequested() {
        mCurrentPage++;
        mPresenter.getFaceCompareResult(getFaceCompareRequestEntity());
        Log.i(TAG, "onLoadMoreRequested:" + mCurrentPage);
    }

    //显示无数据页面
    private void showNoDataPage(boolean flag) {
        if (flag){
            mNoContent.setVisibility(View.VISIBLE);
            mContainer.setVisibility(View.GONE);
            mNoContentImg.setImageResource(R.drawable.ic_no_comparison_result);
        }else {
            mNoContent.setVisibility(View.GONE);
            mContainer.setVisibility(View.VISIBLE);
        }
    }
}
