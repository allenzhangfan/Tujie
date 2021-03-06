package com.netposa.component.spjk.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.log.Log;
import com.netposa.common.utils.TimeUtils;
import com.netposa.commonres.widget.CaptureTimeHelper;
import com.netposa.component.spjk.R;
import com.netposa.component.spjk.R2;
import com.netposa.component.spjk.di.component.DaggerHistoryVideoComponent;
import com.netposa.component.spjk.di.module.HistoryVideoModule;
import com.netposa.component.spjk.mvp.contract.HistoryVideoContract;
import com.netposa.component.spjk.mvp.model.entity.HistoryVideoRequestEntity;
import com.netposa.component.spjk.mvp.model.entity.HistoryVideoResponseEntity;
import com.netposa.component.spjk.mvp.presenter.HistoryVideoPresenter;
import com.netposa.component.spjk.mvp.ui.adapter.HistoryVideoAdapter;

import java.util.Calendar;
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
import static com.netposa.common.constant.GlobalConstants.CAMERA_QIANG_JI;
import static com.netposa.common.constant.GlobalConstants.PAGE_SIZE_DEFAULT;
import static com.netposa.common.utils.TimeUtils.FORMAT_PATTERN;
import static com.netposa.commonres.widget.CaptureTimeHelper.START_TIME;
import static com.netposa.component.spjk.app.SpjkConstants.KEY_HISTORY_PLAY_VIDEO;
import static com.netposa.component.spjk.app.SpjkConstants.KEY_HISTORY_VIDEO_DEVICES_NAME;
import static com.netposa.component.spjk.app.SpjkConstants.KEY_HISTORY_VIDEO_ORG_NAME;
import static com.netposa.component.spjk.app.SpjkConstants.KEY_SINGLE_CAMERA_HISTORY_VIDEO;
import static com.netposa.component.spjk.app.SpjkConstants.KEY_SINGLE_CAMERA_ID;
import static com.netposa.component.spjk.app.SpjkConstants.KEY_VIDEO_TYPE;

public class HistoryVideoActivity extends BaseActivity<HistoryVideoPresenter> implements
        HistoryVideoContract.View,
        View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener,
        CaptureTimeHelper.Listener {

    @BindView(R2.id.title_tv)
    TextView mTVtTitle;
    @BindView(R2.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R2.id.rl_begin_time)
    RelativeLayout mRlBeginTime;
    @BindView(R2.id.rl_end_time)
    RelativeLayout mRlEndTime;
    @BindView(R2.id.tv_begin_time)
    TextView mTvBeginTime;
    @BindView(R2.id.tv_end_time)
    TextView mTvEndTime;
    @BindView(R2.id.iv_begin_time)
    ImageView mIvBeginTime;
    @BindView(R2.id.iv_end_time)
    ImageView mIvEndTime;
    @BindView(R2.id.srfl)
    SwipeRefreshLayout mSrfl;
    @BindView(R2.id.cl_no_content)
    ConstraintLayout mClNoContent;
    @BindView(R2.id.iv_no_content)
    ImageView mIvNoContent;
    @BindView(R2.id.tv_no_content)
    TextView mTvNoContent;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.ItemAnimator mItemAnimator;
    @Inject
    List<HistoryVideoResponseEntity.VideoListEntity> mBeanList;
    @Inject
    HistoryVideoAdapter mAdapter;
    @Inject
    HistoryVideoRequestEntity mRequestEntity;
    @Inject
    LoadMoreView mLoadMoreView;
    @Inject
    CaptureTimeHelper mCaptureTimeHelper;

    private int mCurrentPage = 1;
    private String mCameraId;
    private long mStartTime;
    private long mEndTime;
    private String mOrgName;
    private int mCameraTypeInt;
    private String mDeviceName;


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerHistoryVideoComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .historyVideoModule(new HistoryVideoModule(this, this, getSupportFragmentManager()))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_history_video; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        Intent data = getIntent();
        if (data == null) {
            Log.e(TAG, "intent data is null,please check !");
            return;
        }
        mCameraId = data.getStringExtra(KEY_SINGLE_CAMERA_HISTORY_VIDEO);
        mOrgName = data.getStringExtra(KEY_HISTORY_VIDEO_ORG_NAME);
        mDeviceName = data.getStringExtra(KEY_HISTORY_VIDEO_DEVICES_NAME);
        mCameraTypeInt = data.getIntExtra(KEY_VIDEO_TYPE, CAMERA_QIANG_JI);
        mTVtTitle.setText(R.string.history_video);
        mIvNoContent.setImageResource(R.drawable.ic_no_follow);
        mTvNoContent.setText(R.string.no_history_video);
        mTVtTitle.getPaint().setFakeBoldText(true);

        //recyclerview
        mRvContent.setLayoutManager(mLayoutManager);
        mRvContent.setItemAnimator(mItemAnimator);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRvContent.setHasFixedSize(true);
        mRvContent.setAdapter(mAdapter);

        mAdapter.setOnLoadMoreListener(this, mRvContent);
        mAdapter.setLoadMoreView(mLoadMoreView);

        //SwipeRefreshLayout
        mSrfl.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mSrfl.setOnRefreshListener(this);
        mAdapter.setOnItemClickListener(((adapter, view, position) -> {
            HistoryVideoResponseEntity.VideoListEntity item = (HistoryVideoResponseEntity.VideoListEntity) adapter.getItem(position);
            Intent intent = new Intent(this, HistoryVideoPlayActivity.class);
            String playUrl = item.getPlayUrl();
            intent.putExtra(KEY_HISTORY_PLAY_VIDEO, playUrl);
            intent.putExtra(KEY_HISTORY_VIDEO_ORG_NAME, mOrgName);
            intent.putExtra(KEY_HISTORY_VIDEO_DEVICES_NAME, mDeviceName);
            intent.putExtra(KEY_SINGLE_CAMERA_ID, mCameraId);
            intent.putExtra(KEY_VIDEO_TYPE, mCameraTypeInt);
            launchActivity(intent);
        }));

        mRlBeginTime.setOnClickListener(this);
        mRlEndTime.setOnClickListener(this);

        //时间选择有关初始化
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        mCaptureTimeHelper.init(startCalendar, endCalendar, this);
        // 出始默认请求此时之前的2小时
        mStartTime = TimeUtils.getBeforHour(-2);// 当前时间的前2小时
        mEndTime = TimeUtils.getBeforHour(0);//当前时间
        // 自动下拉刷新
        mRvContent.postDelayed(() -> {
            mSrfl.setRefreshing(true);
            mPresenter.getCameraHistoryList(mStartTime, mEndTime, mCameraId, 1);
        }, 100);
    }

    @Override
    public void onRefresh() {
        mPresenter.getCameraHistoryList(mStartTime, mEndTime, mCameraId, 1);
        mAdapter.setEnableLoadMore(false);
    }

    @Override
    public void onLoadMoreRequested() {
        mCurrentPage++;
        mSrfl.setEnabled(false);
        mPresenter.getCameraHistoryList(mStartTime, mEndTime, mCameraId, mCurrentPage);
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
    public void refreshData() {
        mAdapter.notifyDataSetChanged();
        mAdapter.loadMoreEnd(true);
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
            R2.id.head_left_iv})
    public void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.head_left_iv) {
            killMyself();
        }
    }

    @Override
    public void getVideoSucess(HistoryVideoResponseEntity entity) {
        Log.d(TAG, entity.toString());
        mSrfl.setRefreshing(false);
        mAdapter.setEnableLoadMore(true);
        List<HistoryVideoResponseEntity.VideoListEntity> videoList = entity.getVideoList();
        Log.d(TAG, "currentPage:" + mCurrentPage + ",dataList:" + videoList.size());
        if (videoList.size() > 0) {
            if (mCurrentPage == 1) {
                showNoDataPage(false);
                mBeanList.clear();
            } else {
                mSrfl.setEnabled(true);
            }
            mBeanList.addAll(videoList);
            mAdapter.notifyDataSetChanged();
            if (videoList.size() < PAGE_SIZE_DEFAULT) {
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
    public void getVideoFAiled() {
        showNoDataPage(true);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.rl_begin_time) {
            mCaptureTimeHelper.showStartDateDialog();
        } else if (id == R.id.rl_end_time) {
            mCaptureTimeHelper.showEndDateDialog();
        }
    }

    private void showNoDataPage(boolean show) {
        if (show) {
            mSrfl.setVisibility(View.GONE);
            mClNoContent.setVisibility(View.VISIBLE);
        } else {
            if (mSrfl.getVisibility() == View.GONE) {
                mSrfl.setVisibility(View.VISIBLE);
            }
            mClNoContent.setVisibility(View.GONE);
        }
        mSrfl.setEnabled(false);
    }

    @Override
    public void setCaptureTime(long time, int selectTag) {
        if (selectTag == START_TIME) {
            mStartTime = time;
            String startTimeStr = TimeUtils.millis2String(time, FORMAT_PATTERN);
            mTvBeginTime.setText(startTimeStr);
            mIvBeginTime.setVisibility(View.GONE);
        } else {
            mEndTime = time;
            String endTimeStr = TimeUtils.millis2String(time, FORMAT_PATTERN);
            mTvEndTime.setText(endTimeStr);
            mIvEndTime.setVisibility(View.GONE);
            //开始查询历史视频
            mPresenter.getCameraHistoryList(mStartTime, mEndTime, mCameraId, 1);
        }
    }
}
