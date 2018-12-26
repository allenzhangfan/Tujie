package com.netposa.component.ytst.mvp.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import butterknife.BindView;
import butterknife.OnClick;


import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.log.Log;
import com.netposa.commonres.widget.RoundImageView;
import com.netposa.component.room.entity.YtstCarAndPeopleEntity;
import com.netposa.component.ytst.R2;
import com.netposa.component.ytst.di.component.DaggerTrackComponent;
import com.netposa.component.ytst.di.module.TrackModule;
import com.netposa.component.ytst.mvp.contract.TrackContract;
import com.netposa.component.ytst.mvp.presenter.TrackPresenter;
import com.netposa.component.ytst.R;
import com.netposa.component.ytst.mvp.ui.adapter.TrackAdapter;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.component.ytst.app.YtstConstants.KEY_INTENT_PIC;

public class TrackActivity extends BaseActivity<TrackPresenter> implements TrackContract.View {

    @BindView(R2.id.title_tv)
    TextView mTitleTv;
    @BindView(R2.id.head_right_tv)
    TextView mRightTv;
    @BindView(R2.id.iv_captureImg)
    RoundImageView mRIVimg;
    @BindView(R2.id.rv_recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R2.id.tv_time_sort)
    TextView mTimeSort;
    @BindView(R2.id.tv_similarity_sort)
    TextView mSimilaritySort;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.ItemAnimator mItemAnimator;
    @Inject
    TrackAdapter mAdapter;
    @Inject
    List<YtstCarAndPeopleEntity> mBeanList;

    private ImageLoader mImageLoader;
    private String mPicPath;
    private boolean mSortDateFlag = false;
    private boolean mSortSimilarityFlag = false;
    private boolean isSelectAll = false;
    private int countIndex = 1;//默认初始为1
    private ArrayList<String> mSelectList = new ArrayList<>();


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerTrackComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .trackModule(new TrackModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_track; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        initTiteBar();
        getIntentData();
        initRecyclerView();
    }

    private void initTiteBar() {
        mTitleTv.setText(getString(R.string.guiji_analysis));
        mRightTv.setText(getString(R.string.sfjb_all_chose));
        mRightTv.setTextColor(getResources().getColor(R.color.color_2D87F9));
        mRightTv.setTextSize(15);
        mRightTv.setVisibility(View.VISIBLE);

    }

    private void getIntentData() {
        Intent intentData = this.getIntent();
        if (intentData == null) {
            Log.e(TAG, "intent mData is null,please check !");
            return;
        }
        mPicPath = intentData.getStringExtra(KEY_INTENT_PIC);
        mPresenter.getAllData();
    }

    private void initRecyclerView() {
        mRecyclerview.setLayoutManager(mLayoutManager);
        ((SimpleItemAnimator) mItemAnimator).setSupportsChangeAnimations(false);
        mRecyclerview.setItemAnimator(mItemAnimator);
//        mRecyclerview.addItemDecoration(mItemDecoration);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            YtstCarAndPeopleEntity bean = mBeanList.get(position);
            boolean isSelect = bean.isSelect();
            if (!isSelect) {
                countIndex++;
                mBeanList.get(position).setSelect(true);
                mSelectList.add(bean.getLongitude()+","+bean.getLatitude());
                if (countIndex == mBeanList.size()) {
                    isSelectAll = true;
                }
            } else {
                countIndex--;
                isSelectAll = false;
                mSelectList.remove(bean.getLongitude()+","+bean.getLatitude());
                mBeanList.get(position).setSelect(false);
            }
             mAdapter.notifyItemRangeChanged(0, mBeanList.size());
        });
    }


    @OnClick({R2.id.head_left_iv,
            R2.id.tv_time_sort,
            R2.id.tv_similarity_sort,
            R2.id.head_right_tv,
            R2.id.btn_cancel,
            R2.id.btn_sumit
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
            selectAll();
        } else if (id == R.id.btn_sumit) {
            if (mSelectList.size() > 0) {
               mPresenter.intentPathActivity(mSelectList,mBeanList,mPicPath);
            }
        } else if (id == R.id.btn_cancel) {
            for (int i = 0; i < mBeanList.size(); i++) {
                mBeanList.get(i).setSelect(false);
            }
            mSelectList.clear();
            mAdapter.notifyDataSetChanged();
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

    @Override
    public void showCarAndPepoleData(List<YtstCarAndPeopleEntity> entities) {
        showPic(mPicPath);
        mBeanList.addAll(entities);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showCarAndPepoleDataFailed() {

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

    /**
     * 全选和反选
     */
    private void selectAll() {
        if (mAdapter == null) return;
        mSelectList.clear();
        if (!isSelectAll) { // 全选
            for (int i = 0; i < mBeanList.size(); i++) {
                mBeanList.get(i).setSelect(true);
                mSelectList.add(mBeanList.get(i).getLongitude()+","+mBeanList.get(i).getLatitude());
            }
            countIndex = mBeanList.size();
            isSelectAll = true;
        } else { // 取消全选
            for (int i = 0; i < mBeanList.size(); i++) {
                mBeanList.get(i).setSelect(false);
            }
            countIndex = 1;
            isSelectAll = false;
        }
        mAdapter.notifyDataSetChanged();
    }
}
