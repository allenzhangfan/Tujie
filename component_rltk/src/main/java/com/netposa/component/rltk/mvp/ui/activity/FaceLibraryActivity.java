package com.netposa.component.rltk.mvp.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.google.android.material.button.MaterialButton;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.core.RouterHub;
import com.netposa.common.log.Log;
import com.netposa.common.utils.SizeUtils;
import com.netposa.common.utils.TimeUtils;
import com.netposa.commonres.widget.itemDecoration.SpacesItemDecoration;
import com.netposa.component.rltk.R;
import com.netposa.component.rltk.R2;
import com.netposa.component.rltk.di.component.DaggerFaceLibraryComponent;
import com.netposa.component.rltk.di.module.FaceLibraryModule;
import com.netposa.component.rltk.mvp.contract.FaceLibraryContract;
import com.netposa.component.rltk.mvp.model.entity.FaceLibraryRequestEntity;
import com.netposa.component.rltk.mvp.model.entity.FaceLibraryResponseEntity;
import com.netposa.component.rltk.mvp.model.entity.FilterItemEntity;
import com.netposa.component.rltk.mvp.presenter.FaceLibraryPresenter;
import com.netposa.component.rltk.mvp.ui.adapter.FaceLibraryAdapter;
import com.netposa.component.rltk.mvp.ui.adapter.FilterItemAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.common.constant.GlobalConstants.PAGE_SIZE_DEFAULT;
import static com.netposa.common.constant.GlobalConstants.TYPE_ALL;
import static com.netposa.common.constant.GlobalConstants.TYPE_FAMALE;
import static com.netposa.common.constant.GlobalConstants.TYPE_MALE;
import static com.netposa.common.constant.GlobalConstants.TYPE_NO_WEAR_GLASS;
import static com.netposa.common.constant.GlobalConstants.TYPE_OTHER;
import static com.netposa.common.constant.GlobalConstants.TYPE_WEAR_GLASS;
import static com.netposa.component.rltk.app.RltkConstants.KEY_TO_CHOSE_DEVICE;
import static com.netposa.component.rltk.app.RltkConstants.KEY_TO_DETAIL;
import static com.netposa.component.rltk.app.RltkConstants.KEY_TO_FACE_LIB_DEVICE;
import static com.netposa.component.rltk.app.RltkConstants.KEY_TO_FACE_LIB_ORG;

@Route(path = RouterHub.RLTK_FACE_LIBRARY_ACTIVITY)
public class FaceLibraryActivity extends BaseActivity<FaceLibraryPresenter> implements FaceLibraryContract.View, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener {

    @BindView(R2.id.title_tv)
    TextView mTitleTv;
    @BindView(R2.id.head_right_iv)
    ImageView mIvHeadRight;
    @BindView(R2.id.tv_male)
    TextView mTvMale;
    @BindView(R2.id.tv_famale)
    TextView mTvFamale;
    @BindView(R2.id.tv_wear_glasses)
    TextView mTvWearGlasses;
    @BindView(R2.id.tv_no_wear_glasses)
    TextView mTvNoWearGlasses;
    @BindView(R2.id.tv_filter)
    TextView mTvFilter;
    @BindView(R2.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R2.id.rv_time)
    RecyclerView mRvTime;
    @BindView(R2.id.rv_sex)
    RecyclerView mRvSex;
    @BindView(R2.id.rv_glasses)
    RecyclerView mRvGlasses;
    @BindView(R2.id.ll_pop_filter)
    LinearLayout mLlPopFilter;
    @BindView(R2.id.cl_no_content)
    ConstraintLayout mClNoContent;
    @BindView(R2.id.srfl)
    SwipeRefreshLayout mSrfl;
    @BindView(R2.id.bt_reset)
    MaterialButton mBtReset;
    @BindView(R2.id.bt_confirm)
    MaterialButton mBtConfirm;
    @BindView(R2.id.view_bottom)
    View mViewBottom;
    @BindView(R2.id.iv_no_content)
    ImageView mIvNoContent;
    @BindView(R2.id.tv_no_content)
    TextView mTvNoCOntent;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.ItemAnimator mItemAnimator;
    @Inject
    RecyclerView.ItemDecoration mItemDecoration;
    @Inject
    List<FaceLibraryResponseEntity.ListEntity> mBeanList;
    @Inject
    FaceLibraryAdapter mAdapter;
    @Inject
    LoadMoreView mLoadMoreView;
    @Inject
    FaceLibraryRequestEntity mRequest;

    List<FilterItemEntity> mFiltertimeBeanList, mFilterSexBeanList, mFilterGlassesBeanList;
    FilterItemAdapter mFiltertimeAdapter, mFilterSexAdapter, mFilterGlassesAdapter;

    private int mCheckedViewcolor, mDefaultViewcolor;
    private Drawable mIcFilterSelected, mIcFilterDefault;
    private Animation mTopIn, mTopOut;
    private List<TextView> mTvTitles;
    private int mCurrentPage = 1;
    private ArrayList<String> mdeviceIds;
    private ArrayList<String> mOrgIds;
    private long mStartTime = TimeUtils.getBeforHour(-24);// 默认当前时间的前24小时;
    private String mRequestId;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerFaceLibraryComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .faceLibraryModule(new FaceLibraryModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_face_library; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mTitleTv.setText(getString(R.string.rltk));
        mIvHeadRight.setVisibility(View.VISIBLE);
        mTvNoCOntent.setText(R.string.lib_no_data);
        mIvNoContent.setImageResource(R.drawable.ic_no_follow);
        mIvHeadRight.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_tree));
        mIcFilterDefault = ContextCompat.getDrawable(this, R.drawable.ic_filter);
        mIcFilterSelected = ContextCompat.getDrawable(this, R.drawable.ic_filter_blue);
        mIcFilterSelected.setBounds(0, 0, mIcFilterSelected.getMinimumWidth(), mIcFilterSelected.getMinimumHeight());
        mIcFilterDefault.setBounds(0, 0, mIcFilterDefault.getMinimumWidth(), mIcFilterDefault.getMinimumHeight());
        mCheckedViewcolor = ContextCompat.getColor(this, R.color.color_2D87F9);
        mDefaultViewcolor = ContextCompat.getColor(this, R.color.color_697084);
        mTvTitles = new ArrayList<>();
        mTvTitles.add(mTvMale);
        mTvTitles.add(mTvFamale);
        mTvTitles.add(mTvWearGlasses);
        mTvTitles.add(mTvNoWearGlasses);
        mTvTitles.add(mTvFilter);

        //recyclerview
        mRvContent.setLayoutManager(mLayoutManager);
        mRvContent.setItemAnimator(mItemAnimator);
        mRvContent.addItemDecoration(mItemDecoration);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRvContent.setHasFixedSize(true);
        mRvContent.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            FaceLibraryResponseEntity.ListEntity entity = (FaceLibraryResponseEntity.ListEntity) adapter.getItem(position);
            String recordId = entity.getRecordId();
            Log.i(TAG, recordId);
            if (!TextUtils.isEmpty(recordId)) {
                Intent intent = new Intent(this, PhotoInformationActivity.class);
                intent.putExtra(KEY_TO_DETAIL, recordId);
                launchActivity(intent);
            } else {
                showMessage(getString(R.string.no_recordId));
            }
        });

        //SwipeRefreshLayout
        mAdapter.setOnLoadMoreListener(this, mRvContent);
        mAdapter.setLoadMoreView(mLoadMoreView);
        mSrfl.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mSrfl.setOnRefreshListener(this);

        //筛选框
        initFilterWinow();

        // 自动下拉刷新
        mRvContent.postDelayed(() -> {
            mSrfl.setRefreshing(true);
            resetRequest();
            mPresenter.getFaceList();
        }, 100);
    }

    private void initFilterWinow() {
        mTopIn = AnimationUtils.loadAnimation(this, R.anim.top_in);
        mTopOut = AnimationUtils.loadAnimation(this, R.anim.top_out);

        //时间
        int itemMargin = SizeUtils.dp2px(16); // 每个item与左上右下的间距
        int rvMargin = SizeUtils.dp2px(16);//整个RecyclerView与上下左右两侧的间距
        GridLayoutManager filterLayoutManager = new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false);
        SpacesItemDecoration itemDecoration = new SpacesItemDecoration(itemMargin, itemMargin, rvMargin, rvMargin);
        mRvTime.setLayoutManager(filterLayoutManager);
        mRvTime.setItemAnimator(mItemAnimator);
        mRvTime.addItemDecoration(itemDecoration);

        mFiltertimeBeanList = new ArrayList<>();
        FilterItemEntity timeEntity = new FilterItemEntity();
        timeEntity.setContent(getString(R.string.twenty_four_hours));
        mFiltertimeBeanList.add(timeEntity);
        timeEntity = new FilterItemEntity();
        timeEntity.setContent(getString(R.string.three_days));
        mFiltertimeBeanList.add(timeEntity);
        timeEntity = new FilterItemEntity();
        timeEntity.setContent(getString(R.string.seven_days));
        mFiltertimeBeanList.add(timeEntity);
        mFiltertimeAdapter = new FilterItemAdapter(mFiltertimeBeanList);
        mRvTime.setAdapter(mFiltertimeAdapter);
        mFiltertimeAdapter.setOnItemClickListener((adapter, view, position) -> {
            for (int i = 0; i < mFiltertimeBeanList.size(); i++) {
                FilterItemEntity entity = mFiltertimeBeanList.get(i);
                if (position == i) {
                    entity.setSelect(true);
                } else {
                    entity.setSelect(false);
                }
            }
            mFiltertimeAdapter.notifyDataSetChanged();
        });

        //性别
        mFilterSexBeanList = new ArrayList<>();
        filterLayoutManager = new GridLayoutManager(this, 3);
        mRvSex.setLayoutManager(filterLayoutManager);
        mRvSex.setItemAnimator(mItemAnimator);
        mRvSex.addItemDecoration(itemDecoration);

        FilterItemEntity sexEntity = new FilterItemEntity();
        sexEntity.setContent(getString(R.string.all));
        mFilterSexBeanList.add(sexEntity);
        sexEntity = new FilterItemEntity();
        sexEntity.setContent(getString(R.string.male));
        mFilterSexBeanList.add(sexEntity);
        sexEntity = new FilterItemEntity();
        sexEntity.setContent(getString(R.string.famale));
        mFilterSexBeanList.add(sexEntity);
        mFilterSexAdapter = new FilterItemAdapter(mFilterSexBeanList);
        mRvSex.setAdapter(mFilterSexAdapter);
        mFilterSexAdapter.setOnItemClickListener((adapter, view, position) -> {
            for (int i = 0; i < mFilterSexBeanList.size(); i++) {
                FilterItemEntity entity = mFilterSexBeanList.get(i);
                if (position == i) {
                    entity.setSelect(true);
                } else {
                    if (position == 0) {
                        entity.setSelect(true);
                    } else {
                        entity.setSelect(false);
                    }
                }
            }
            mFilterSexAdapter.notifyDataSetChanged();
        });

        //有无眼镜
        mFilterGlassesBeanList = new ArrayList<>();
        filterLayoutManager = new GridLayoutManager(this, 3);
        mRvGlasses.setLayoutManager(filterLayoutManager);
        mRvGlasses.setItemAnimator(mItemAnimator);
        mRvGlasses.addItemDecoration(itemDecoration);

        FilterItemEntity glassesEntity = new FilterItemEntity();
        glassesEntity.setContent(getString(R.string.wear_glasses));
        mFilterGlassesBeanList.add(glassesEntity);
        glassesEntity = new FilterItemEntity();
        glassesEntity.setContent(getString(R.string.no_wear_glasses));
        mFilterGlassesBeanList.add(glassesEntity);
        glassesEntity = new FilterItemEntity();
        glassesEntity.setContent(getString(R.string.other));
        mFilterGlassesBeanList.add(glassesEntity);
        mFilterGlassesAdapter = new FilterItemAdapter(mFilterGlassesBeanList);
        mRvGlasses.setAdapter(mFilterGlassesAdapter);
        mFilterGlassesAdapter.setOnItemClickListener((adapter, view, position) -> {
            for (int i = 0; i < mFilterGlassesBeanList.size(); i++) {
                FilterItemEntity entity = mFilterGlassesBeanList.get(i);
                if (position == i) {
                    entity.setSelect(true);
                } else {
                    entity.setSelect(false);
                }
            }
            mFilterGlassesAdapter.notifyDataSetChanged();
        });

        mBtReset.setOnClickListener(this);
        mBtConfirm.setOnClickListener(this);
        mViewBottom.setOnClickListener(this);
    }

    @Override
    public void showLoading(String message) {
        mSrfl.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mSrfl.setRefreshing(false);
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

    @OnClick({
            R2.id.head_left_iv,
            R2.id.head_right_iv,
            R2.id.tv_male,
            R2.id.tv_famale,
            R2.id.tv_wear_glasses,
            R2.id.tv_no_wear_glasses,
            R2.id.ll_filter
    })
    public void onViewClick(View view) {
        int id = view.getId();
        boolean selected;
        boolean sexAllSelected;//性别全选
        if (id == R.id.head_left_iv) {
            killMyself();
        } else if (id == R.id.head_right_iv) {
            if (mLlPopFilter.getVisibility() == View.VISIBLE) {
                mLlPopFilter.startAnimation(mTopOut);
                mLlPopFilter.setVisibility(View.GONE);
                return;
            }
            Intent intent = new Intent(this, ChooseDeviceActivity.class);
            startActivityForResult(intent, KEY_TO_CHOSE_DEVICE);
            hidePopFilterLayout();
        } else if (id == R.id.tv_male) {
            selected = !mTvMale.isSelected();
            sexAllSelected = selected && mTvFamale.isSelected();
            mFilterSexBeanList.get(0).setSelect(sexAllSelected);
            mFilterSexBeanList.get(1).setSelect(selected);
            mFilterSexBeanList.get(2).setSelect(mTvFamale.isSelected());
            mFilterSexAdapter.notifyDataSetChanged();
            setTvState(mTvMale, selected);
            setTvState(mTvFilter, false);
            getFaceList();
            hidePopFilterLayout();
        } else if (id == R.id.tv_famale) {
            selected = !mTvFamale.isSelected();
            sexAllSelected = selected && mTvMale.isSelected();
            mFilterSexBeanList.get(0).setSelect(sexAllSelected);
            mFilterSexBeanList.get(1).setSelect(mTvMale.isSelected());
            mFilterSexBeanList.get(2).setSelect(selected);
            mFilterSexAdapter.notifyDataSetChanged();
            setTvState(mTvFamale, selected);
            setTvState(mTvFilter, false);
            getFaceList();
            hidePopFilterLayout();
        } else if (id == R.id.tv_wear_glasses) {
            selected = !mTvWearGlasses.isSelected();
            mFilterGlassesBeanList.get(0).setSelect(selected);
            mFilterGlassesBeanList.get(1).setSelect(!selected);
            mFilterGlassesAdapter.notifyDataSetChanged();
            setTvState(mTvWearGlasses, selected);
            setTvState(mTvNoWearGlasses, false);
            setTvState(mTvFilter, false);
            getFaceList();
            hidePopFilterLayout();
        } else if (id == R.id.tv_no_wear_glasses) {
            selected = !mTvNoWearGlasses.isSelected();
            mFilterGlassesBeanList.get(0).setSelect(!selected);
            mFilterGlassesBeanList.get(1).setSelect(selected);
            mFilterGlassesAdapter.notifyDataSetChanged();
            setTvState(mTvNoWearGlasses, selected);
            setTvState(mTvWearGlasses, false);
            setTvState(mTvFilter, false);
            getFaceList();
            hidePopFilterLayout();
        } else if (id == R.id.ll_filter) {
            selected = true;
            setTvState(mTvFilter, selected);
            if (mLlPopFilter.getVisibility() == View.GONE) {
                mLlPopFilter.startAnimation(mTopIn);
                mLlPopFilter.setVisibility(View.VISIBLE);
            } else {
                mLlPopFilter.startAnimation(mTopOut);
                mLlPopFilter.setVisibility(View.GONE);
            }
        }
    }

    private void getFaceList() {
        mCurrentPage = 1;
        mRequest.setCurrentPage(mCurrentPage);
        mRequest.setRequestId(null);
        String gender = "";
        if (mTvMale.isSelected()) {
            gender = TYPE_MALE;
        }
        if (mTvFamale.isSelected()) {
            gender = TYPE_FAMALE;
        }
        if (mTvMale.isSelected() && mTvFamale.isSelected()) {
            gender = TYPE_ALL;
        }
        String eyeGlass = "";
        if (mTvWearGlasses.isSelected()) {
            eyeGlass = TYPE_WEAR_GLASS;
        }
        if (mTvNoWearGlasses.isSelected()) {
            eyeGlass = TYPE_NO_WEAR_GLASS;
        }
        mRequest.setGender(gender);
        mRequest.setEyeGlass(eyeGlass);
        mRequest.setStartTime(mStartTime);
        mRequest.setEndTime(TimeUtils.getBeforHour(0));//当前时间
        mPresenter.getFaceList();
    }

    public void resetRequest() {
        mRequest.setRequestId(null);
        mCurrentPage = 1;
        mRequest.setCurrentPage(mCurrentPage);
        mRequest.setGender(TYPE_ALL);
        mRequest.setEyeGlass(TYPE_ALL);
        mRequest.setStartTime(TimeUtils.getBeforHour(-24));// 当前时间的前24小时
        mRequest.setEndTime(TimeUtils.getBeforHour(0));//当前时间
    }

    /**
     * 设置textview选中与否的状态
     *
     * @param tv
     * @param select
     */
    private void setTvState(TextView tv, boolean select) {
        if (select) {
            tv.setSelected(true);
            tv.getPaint().setFakeBoldText(true);
            tv.setTextColor(mCheckedViewcolor);
            if (tv.getId() == R.id.tv_filter) {
                tv.setCompoundDrawables(mIcFilterSelected, null, null, null);
            }
        } else {
            tv.setSelected(false);
            tv.getPaint().setFakeBoldText(false);
            tv.setTextColor(mDefaultViewcolor);
            if (tv.getId() == R.id.tv_filter) {
                tv.setCompoundDrawables(mIcFilterDefault, null, null, null);
            }
        }
    }

    @Override
    public void getDataSuccess(FaceLibraryResponseEntity response) {
        //上拉加载更多的时候需要带上requestId,第一次不需要
        mRequestId = response.getRequestId();
        //移除刷新圈
        mSrfl.setRefreshing(false);
        mAdapter.setEnableLoadMore(true);
        List<FaceLibraryResponseEntity.ListEntity> dataList = response.getList();
        Log.d(TAG, "currentPage:" + mCurrentPage + ",dataList:" + dataList.size());
        if (dataList.size() > 0) {
            if (mCurrentPage == 1) {
                showNoDataPage(false);
                mBeanList.clear();
            } else {
                mSrfl.setEnabled(true);
            }
            mBeanList.addAll(dataList);
            mAdapter.notifyDataSetChanged();
            if (dataList.size() < PAGE_SIZE_DEFAULT) {
                mAdapter.loadMoreEnd(false);
            } else {
                mAdapter.loadMoreComplete();
            }
        } else {
            showNoDataPage(true);
            mSrfl.setRefreshing(false);
        }
    }

    @Override
    public void getDataFailed() {
        showNoDataPage(true);
    }

    private void showNoDataPage(boolean show) {
        if (show) {
            mSrfl.setVisibility(View.GONE);
            mClNoContent.setVisibility(View.VISIBLE);
            mSrfl.setEnabled(false);
        } else {
            if (mSrfl.getVisibility() == View.GONE) {
                mSrfl.setVisibility(View.VISIBLE);
            }
            mClNoContent.setVisibility(View.GONE);
            mSrfl.setEnabled(true);
        }
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        mRequest.setRequestId(null);
        mRequest.setCurrentPage(mCurrentPage);
        mPresenter.getFaceList();
        Log.i(TAG, "onRefresh:" + mCurrentPage);
    }

    @Override
    public void onLoadMoreRequested() {
        mCurrentPage++;
        mRequest.setRequestId(mRequestId);
        mRequest.setCurrentPage(mCurrentPage);
        mPresenter.getFaceList();
        Log.i(TAG, "onLoadMoreRequested:" + mCurrentPage);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_reset) {
            for (int i = 0; i < mFiltertimeBeanList.size(); i++) {
                mFiltertimeBeanList.get(i).setSelect(false);
            }
            mFiltertimeAdapter.notifyDataSetChanged();
            for (int i = 0; i < mFilterSexBeanList.size(); i++) {
                mFilterSexBeanList.get(i).setSelect(false);
            }
            mFilterSexAdapter.notifyDataSetChanged();
            for (int i = 0; i < mFilterGlassesBeanList.size(); i++) {
                mFilterGlassesBeanList.get(i).setSelect(false);
            }
            mFilterGlassesAdapter.notifyDataSetChanged();
            setTvState(mTvMale,false);
            setTvState(mTvFamale,false);
            setTvState(mTvWearGlasses, false);
            setTvState(mTvNoWearGlasses, false);
            hidePopFilterLayout();
            //重置之后重新请求一下
            resetRequest();
            mPresenter.getFaceList();
        } else if (id == R.id.bt_confirm) {
            mRequest.setRequestId(null);
            mCurrentPage = 1;
            mRequest.setCurrentPage(mCurrentPage);
            //时间
            for (int i = 0; i < mFiltertimeBeanList.size(); i++) {
                if (mFiltertimeBeanList.get(i).isSelect()) {
                    if (i == 0) {
                        mStartTime = TimeUtils.getBeforHour(-24);// 当前时间的前24小时
                    } else if (i == 1) {
                        mStartTime = TimeUtils.getBeforHour(-24 * 3);// 当前时间的前3天
                    } else if (i == 2) {
                        mStartTime = TimeUtils.getBeforHour(-24 * 7);// 当前时间的前7天
                    }
                }
            }
            long endTime = TimeUtils.getBeforHour(0);// 当前时间
            mRequest.setStartTime(mStartTime);
            mRequest.setEndTime(endTime);
            //性别
            for (int i = 0; i < mFilterSexBeanList.size(); i++) {
                if (mFilterSexBeanList.get(i).isSelect()) {
                    if (i == 0) {//全部
                        mRequest.setGender(TYPE_ALL);
                        setTvState(mTvMale, true);
                        setTvState(mTvFamale, true);
                        break;
                    } else if (i == 1) {//男性
                        mRequest.setGender(TYPE_MALE);
                        setTvState(mTvMale, true);
                        setTvState(mTvFamale, false);
                    } else if (i == 2) {//女性
                        mRequest.setGender(TYPE_FAMALE);
                        setTvState(mTvMale, false);
                        setTvState(mTvFamale, true);
                    }
                }
            }
            //眼镜
            mRequest.setEyeGlass(TYPE_ALL);//默认全选
            for (int i = 0; i < mFilterGlassesBeanList.size(); i++) {
                if (mFilterGlassesBeanList.get(i).isSelect()) {
                    if (i == 0) {//戴眼镜 1
                        setTvState(mTvWearGlasses, true);
                        setTvState(mTvNoWearGlasses, false);
                        mRequest.setEyeGlass(TYPE_WEAR_GLASS);
                    } else if (i == 1) {//未戴眼镜 0
                        mRequest.setEyeGlass(TYPE_NO_WEAR_GLASS);
                        setTvState(mTvWearGlasses, false);
                        setTvState(mTvNoWearGlasses, true);
                    } else if (i == 2) {//其它 -1
                        mRequest.setEyeGlass(TYPE_OTHER);
                    }
                }
            }
            mPresenter.getFaceList();
            hidePopFilterLayout();
        } else if (id == R.id.view_bottom) {
            hidePopFilterLayout();
        }
    }

    private void hidePopFilterLayout() {
        if (mLlPopFilter.getVisibility() == View.VISIBLE) {
            mLlPopFilter.startAnimation(mTopOut);
            mLlPopFilter.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case KEY_TO_CHOSE_DEVICE:
                mdeviceIds = data.getStringArrayListExtra(KEY_TO_FACE_LIB_DEVICE);
                mOrgIds = data.getStringArrayListExtra(KEY_TO_FACE_LIB_ORG);
                resetRequest();
                mRequest.setDeviceList(mdeviceIds);
                mRequest.setOrgList(mOrgIds);
                mPresenter.getFaceList();
                break;
        }
    }
}
