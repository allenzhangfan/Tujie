package com.netposa.component.clcx.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.log.Log;
import com.netposa.component.clcx.R;
import com.netposa.component.clcx.R2;
import com.netposa.component.clcx.di.component.DaggerQueryResultComponent;
import com.netposa.component.clcx.di.module.QueryResultModule;
import com.netposa.component.clcx.mvp.contract.QueryResultContract;
import com.netposa.component.clcx.mvp.model.entity.QueryCarSearchEntity;
import com.netposa.component.clcx.mvp.model.entity.QueryCarSearchResponseEntity;
import com.netposa.component.clcx.mvp.presenter.QueryResultPresenter;
import com.netposa.component.clcx.mvp.ui.adapter.QueryResultAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.common.constant.GlobalConstants.PAGE_SIZE_DEFAULT;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_CAR_DETAIL;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_CURRENT_PAGE;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_DICTIONARY_TYPE_END_TIME;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_DICTIONARY_TYPE_START_TIME;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_END_TIME;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_PAGE_SIZE;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_PIC_PATH;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_PLATE_COLORS;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_PLATE_NUMBER;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_PLATE_TYPES;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_POSITION;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_START_TIME;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_VEHICLE_COLORS;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_VEHICLE_TYPES;

public class QueryResultActivity extends BaseActivity<QueryResultPresenter> implements QueryResultContract.View, SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R2.id.head_left_iv)
    ImageButton mHeadLeftIv;
    @BindView(R2.id.title_tv)
    TextView mTitleTv;
    @BindView(R2.id.ll_title)
    LinearLayout llTitle;
    @BindView(R2.id.line_view)
    View lineView;
    @BindView(R2.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R2.id.srfl)
    SwipeRefreshLayout mSrfl;
    @BindView(R2.id.cl_no_content)
    ConstraintLayout mClNoContent;
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
    QueryResultAdapter mAdapter;
    @Inject
    LoadMoreView mLoadMoreView;
    @Inject
    List<QueryCarSearchResponseEntity> mBeanList;
    @Inject
    QueryCarSearchEntity mRequestEntity;

    private int mCurrentPage = 1;
    private String mRecordId;
    private long mAbsTime;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerQueryResultComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .queryResultModule(new QueryResultModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_query_result; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        Intent data = getIntent();
        if (data == null) {
            Log.e(TAG,"intent data is null !");
            return;
        }
        mTvNoCOntent.setText(R.string.no_query_car);
        mIvNoContent.setImageResource(R.drawable.ic_no_content);
        long startTime = data.getLongExtra(KEY_START_TIME, 0);
        long endTime = data.getLongExtra(KEY_END_TIME, 0);
        int currentPage = data.getIntExtra(KEY_CURRENT_PAGE, 0);
        int pageSize = data.getIntExtra(KEY_PAGE_SIZE, 0);
        String plateNumber = data.getStringExtra(KEY_PLATE_NUMBER);
        ArrayList<String> plate_color_list = data.getStringArrayListExtra(KEY_PLATE_COLORS);
        ArrayList<String> plate_type_list = data.getStringArrayListExtra(KEY_PLATE_TYPES);
        ArrayList<String> car_color_list = data.getStringArrayListExtra(KEY_VEHICLE_COLORS);
        ArrayList<String> car_type_list = data.getStringArrayListExtra(KEY_VEHICLE_TYPES);

        mRequestEntity.setStartTime(startTime);
        mRequestEntity.setEndTime(endTime);
        mRequestEntity.setCurrentPage(currentPage);
        mRequestEntity.setPageSize(pageSize);
        mRequestEntity.setPlateNumber(plateNumber);
        mRequestEntity.setPlateColors(plate_color_list);
        mRequestEntity.setPlateTypes(plate_type_list);
        mRequestEntity.setVehicleColors(car_color_list);
        mRequestEntity.setVehicleTypes(car_type_list);

        mTitleTv.setText(R.string.clcx_title);
        mRvContent.setLayoutManager(mLayoutManager);
        mRvContent.setItemAnimator(mItemAnimator);
        mRvContent.addItemDecoration(mItemDecoration);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRvContent.setHasFixedSize(true);
        mRvContent.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(this, mRvContent);
        mAdapter.setLoadMoreView(mLoadMoreView);
        mSrfl.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mSrfl.setOnRefreshListener(this);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            QueryCarSearchResponseEntity item = (QueryCarSearchResponseEntity) adapter.getItem(position);
            Intent intent = new Intent(this, CarRecordActivity.class);
            intent.putExtra(KEY_CAR_DETAIL, item.getRecordId());
            intent.putExtra(KEY_DICTIONARY_TYPE_START_TIME, mRequestEntity.getStartTime());
            intent.putExtra(KEY_DICTIONARY_TYPE_END_TIME, mRequestEntity.getEndTime());
            intent.putExtra(KEY_POSITION,item.getLocation());
            intent.putExtra(KEY_PIC_PATH, item.getSceneImg());
            launchActivity(intent);
        });

        // 自动下拉刷新
        mRvContent.postDelayed(() -> {
            mSrfl.setRefreshing(true);
            mCurrentPage = 1;
            mRequestEntity.setLastPassTime(null);
            mRequestEntity.setLastPassId(null);
            mRequestEntity.setPageSize(PAGE_SIZE_DEFAULT);//固定20条数据
            mRequestEntity.setCurrentPage(mCurrentPage);
            mPresenter.getResultList(mRequestEntity);
        }, 100);
    }

    @Override
    public void getListSuccese(List<QueryCarSearchResponseEntity> videoList) {
        mSrfl.setRefreshing(false);
        mAdapter.setEnableLoadMore(true);
        Log.d(TAG, "currentPage:" + mCurrentPage + ",dataList:" + videoList.size());
        if (videoList.size() > 0) {
            if (mCurrentPage == 1) {
                showNoDatePage(false);
                mBeanList.clear();
            } else {
                mSrfl.setEnabled(true);
            }
            mRecordId = videoList.get(videoList.size() - 1).getRecordId();
            mAbsTime = videoList.get(videoList.size() - 1).getAbsTime();
            mBeanList.addAll(videoList);
            mAdapter.notifyDataSetChanged();
            if (videoList.size() < PAGE_SIZE_DEFAULT) {
                mAdapter.loadMoreEnd(false);
            } else {
                mAdapter.loadMoreComplete();
            }
        } else {
            showNoDatePage(true);
            mSrfl.setRefreshing(false);
        }
    }

    @Override
    public void getListFailed() {
        showNoDatePage(true);
    }

    @Override
    public void refreshData() {
        mAdapter.notifyDataSetChanged();
        mAdapter.loadMoreEnd(true);
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        mRequestEntity.setLastPassTime(null);
        mRequestEntity.setLastPassId(null);
        mRequestEntity.setCurrentPage(mCurrentPage);
        mPresenter.getResultList(mRequestEntity);
        Log.i(TAG, "onRefresh:" + mCurrentPage);
    }

    @Override
    public void onLoadMoreRequested() {
        mCurrentPage++;
        mRequestEntity.setLastPassId(mRecordId);
        mRequestEntity.setLastPassTime(mAbsTime);
        mRequestEntity.setCurrentPage(mCurrentPage);
        mPresenter.getResultList(mRequestEntity);
        Log.i(TAG, "onLoadMoreRequested:" + mCurrentPage);
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

    private void showNoDatePage(boolean show) {
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

    @OnClick({R2.id.head_left_iv, R2.id.title_tv})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.head_left_iv) {
            killMyself();
        }
    }
}
