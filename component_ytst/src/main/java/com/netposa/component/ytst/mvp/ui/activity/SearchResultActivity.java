package com.netposa.component.ytst.mvp.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.log.Log;
import com.netposa.common.utils.TujieImageUtils;
import com.netposa.commonres.widget.Dialog.LottieDialogFragment;
import com.netposa.commonres.widget.RoundImageView;
import com.netposa.component.ytst.R;
import com.netposa.component.ytst.R2;
import com.netposa.component.ytst.di.component.DaggerSearchResultComponent;
import com.netposa.component.ytst.di.module.SearchResultModule;
import com.netposa.component.ytst.mvp.contract.SearchResultContract;
import com.netposa.component.ytst.mvp.model.entity.ImgSearchRequestEntity;
import com.netposa.component.ytst.mvp.model.entity.ImgSearchResponseEntity;
import com.netposa.component.ytst.mvp.presenter.SearchResultPresenter;
import com.netposa.component.ytst.mvp.ui.adapter.SearchResultAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.common.constant.GlobalConstants.KEY_POSITION;
import static com.netposa.component.ytst.app.YtstConstants.KEY_IMGSEARCH;
import static com.netposa.component.ytst.app.YtstConstants.KEY_INTENT_PIC;
import static com.netposa.component.ytst.app.YtstConstants.KEY_TO_COMPARE;
import static com.netposa.component.ytst.app.YtstConstants.KEY_TO_COMPARE_CAR;
import static com.netposa.component.ytst.app.YtstConstants.KEY_TO_COMPARE_CAR_IV;
import static com.netposa.component.ytst.app.YtstConstants.KEY_TO_COMPARE_CAR_SCORE;
import static com.netposa.component.ytst.app.YtstConstants.KEY_TO_COMPARE_PERSON;
import static com.netposa.component.ytst.app.YtstConstants.KEY_TO_COMPARE_RECORD_ID;
import static com.netposa.component.ytst.app.YtstConstants.TYPE_CAR;
import static com.netposa.component.ytst.app.YtstConstants.TYPE_FACE;

public class SearchResultActivity extends BaseActivity<SearchResultPresenter> implements SearchResultContract.View {

    @BindView(R2.id.title_tv)
    TextView mTitleTv;
    @BindView(R2.id.head_right_tv)
    TextView mRightTv;
    @BindView(R2.id.rv_recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R2.id.tv_time_sort)
    TextView mTimeSort;
    @BindView(R2.id.tv_similarity_sort)
    TextView mSimilaritySort;
    @BindView(R2.id.iv_captureImg)
    RoundImageView mRIVimg;
    @BindView(R2.id.ll_full_layout)
    LinearLayout mContainerLayout;
    @BindView(R2.id.cl_no_content)
    ConstraintLayout mNoContentLayout;
    @BindView(R2.id.iv_no_content)
    ImageView mNoContentImg;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.ItemAnimator mItemAnimator;
    @Inject
    RecyclerView.ItemDecoration mItemDecoration;
    @Inject
    SearchResultAdapter mAdapter;
    @Inject
    ArrayList<ImgSearchResponseEntity.DataBean> mBeanList;
    @Inject
    LottieDialogFragment mLoadingDialogFragment;

    private ImgSearchRequestEntity mEntity;
    private boolean mSortDateFlag = false;
    private boolean mSortSimilarityFlag = false;
    private Intent mData;
    private ImageLoader mImageLoader;
    private String mPicPath;


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSearchResultComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .searchResultModule(new SearchResultModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_search_result_ytst; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        initTiteBar();
        initRecyclerView();
        mEntity.setDistance("80");//相似度
        mPresenter.imgSearch(mEntity);
    }

    private void initTiteBar() {
        mTitleTv.setText(getString(R.string.search_result));
        mRightTv.setText(getString(R.string.guiji_analysis));
        mRightTv.setTextColor(getResources().getColor(R.color.color_2D87F9));
        mRightTv.setTextSize(15);
        mRightTv.setVisibility(View.VISIBLE);
        mData = getIntent();
        if (mData == null) {
            Log.e(TAG, "intent mData is null,please check !");
            return;
        }
        mEntity = mData.getParcelableExtra(KEY_IMGSEARCH);
    }

    private void initRecyclerView() {
        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setItemAnimator(mItemAnimator);
        mRecyclerview.addItemDecoration(mItemDecoration);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            ImgSearchResponseEntity.DataBean data = mBeanList.get(position);
            String type = data.get_type();
            Intent intent = new Intent(this, CompareIvActivity.class);
            if (!TextUtils.isEmpty(type)) {
                if (type.equals(TYPE_CAR)) {
                    String recordId = data.get_recordId(); // ID
                    intent.putExtra(KEY_TO_COMPARE, type); //类型
                    intent.putExtra(KEY_TO_COMPARE_RECORD_ID, recordId);
                    intent.putExtra(KEY_TO_COMPARE_CAR,data);
                    intent.putExtra(KEY_TO_COMPARE_CAR_SCORE, data.getScore());
                    intent.putExtra(KEY_TO_COMPARE_CAR_IV, data.getTraitImg());
                    intent.putExtra(KEY_POSITION,data.getLocation());
                } else if (type.equals(TYPE_FACE)) {
                    intent.putExtra(KEY_TO_COMPARE, type); //类型
                    intent.putExtra(KEY_TO_COMPARE_PERSON, data);
                }
                launchActivity(intent);
            }
        });
    }

    @OnClick({R2.id.head_left_iv,
            R2.id.tv_time_sort,
            R2.id.tv_similarity_sort,
            R2.id.head_right_tv
    })
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.head_left_iv) {
           killMyself();
        } else if (id == R.id.tv_time_sort) {
            //按时间排序
            setDrawableRight(true, mTimeSort);
            setDrawableRight(false, mSimilaritySort);
            mPresenter.comparatorDate(mBeanList, mSortDateFlag);
            mSortDateFlag = !mSortDateFlag;
            mAdapter.notifyDataSetChanged();
        } else if (id == R.id.tv_similarity_sort) {
            setDrawableRight(false, mTimeSort);
            setDrawableRight(true, mSimilaritySort);
            mPresenter.comparatorScore(mBeanList, mSortSimilarityFlag);
            mSortSimilarityFlag = !mSortSimilarityFlag;
            mAdapter.notifyDataSetChanged();
        } else if (id == R.id.head_right_tv) {
            Intent mIntent = new Intent(this, TrackActivity.class);
            mIntent.putExtra(KEY_INTENT_PIC, mPicPath);
            launchActivity(mIntent);
        }
    }

    // 设置drawableRight
    private void setDrawableRight(boolean pressed, TextView textView) {
        Drawable drawable = null;
        if (pressed) {
            drawable = getResources().getDrawable(R.drawable.ic_sort_pressed);
            textView.setTextColor(getResources().getColor(R.color.color_2D87F9));
        } else {
            drawable = getResources().getDrawable(R.drawable.ic_sort);
            textView.setTextColor(getResources().getColor(R.color.color_989FB0));
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, null, drawable, null);
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

    @Override
    public void imageSearchSuccess(List<ImgSearchResponseEntity> entity) {
        List<ImgSearchResponseEntity> list = entity;
        if (null != list && list.size() > 0) {
            mPicPath = mData.getStringExtra(KEY_INTENT_PIC);
            showPic(mPicPath);
            for (int i = 0; i < list.size(); i++) {
                showNoDataPage(false);
                ImgSearchResponseEntity.DataBean bean = list.get(i).getData();
                if (TYPE_CAR.equals(bean.get_type())) {//如果是车辆信息的处理
                    String picUrl = TujieImageUtils.getDisplayUrl(bean.getSceneImg(),
                            bean.getLocation());
                    bean.setTraitImg(picUrl);
                    bean.setDeviceName(bean.getPlateNumber());
                }
                bean.setSelect(false);
                mBeanList.add(bean);
            }
            mAdapter.notifyDataSetChanged();
           mPresenter.addInsterData(mBeanList);
        } else {
            showNoDataPage(true);
        }
    }

    @Override
    public void imageSearchuploadImageFail() {
        showNoDataPage(true);
    }

    //显示无数据页面
    private void showNoDataPage(boolean flag) {
        if (flag) {
            mNoContentLayout.setVisibility(View.VISIBLE);
            mContainerLayout.setVisibility(View.GONE);
            mNoContentImg.setImageResource(R.drawable.ic_no_comparison_result);
            mRightTv.setVisibility(View.GONE);
        } else {
            mNoContentLayout.setVisibility(View.GONE);
            mContainerLayout.setVisibility(View.VISIBLE);
            mRightTv.setVisibility(View.VISIBLE);
        }
    }

    private void showPic(String path) {
        mImageLoader = ArmsUtils.obtainAppComponentFromContext(this).imageLoader();
        mImageLoader.loadImage(this,
                ImageConfigImpl
                        .builder()
                        .placeholder(R.drawable.ic_image_default)
                        .errorPic(R.drawable.ic_image_load_failed)
                        .url(path)
                        .imageView(mRIVimg)
                        .build());

    }
}
