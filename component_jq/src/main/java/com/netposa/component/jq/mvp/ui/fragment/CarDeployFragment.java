package com.netposa.component.jq.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.log.Log;
import com.netposa.component.jq.R2;
import com.netposa.component.jq.di.component.DaggerCarDeployComponent;
import com.netposa.component.jq.di.module.CarDeployModule;
import com.netposa.component.jq.mvp.contract.CarDeployContract;
import com.netposa.component.jq.mvp.model.entity.AlarmListRequestEntity;
import com.netposa.common.entity.push.JqItemEntity;
import com.netposa.component.jq.mvp.presenter.CarDeployPresenter;
import com.netposa.component.jq.R;
import com.netposa.component.jq.mvp.ui.activity.AlarmDetailsActivity;
import com.netposa.component.jq.mvp.ui.adapter.JqAdapter;

import java.util.List;

import javax.inject.Inject;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.common.constant.GlobalConstants.KEY_JQ_ITEM;
import static com.netposa.common.constant.GlobalConstants.PAGE_SIZE_DEFAULT;
import static com.netposa.common.constant.GlobalConstants.TYPE_CAR_DEPLOY;
import static com.netposa.component.jq.app.JqConstants.CAR_TO_DETAIL_REQUESTCODE;
import static com.netposa.component.jq.app.JqConstants.CAR_TO_DETAIL_RESULTCODE;

public class CarDeployFragment extends BaseFragment<CarDeployPresenter> implements
        CarDeployContract.View,
        SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R2.id.rv_content)
    RecyclerView mRvContent;
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
    List<JqItemEntity> mBeanList;
    @Inject
    JqAdapter mAdapter;
    @Inject
    AlarmListRequestEntity mRequest;
    @Inject
    LoadMoreView mLoadMoreView;

    private int mCurrentPage = 1;

    public static CarDeployFragment newInstance() {
        Log.d("FaceDeployFragment","newInstance");
        CarDeployFragment fragment = new CarDeployFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerCarDeployComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .carDeployModule(new CarDeployModule(this, getActivity()))
                .build()
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public View initContentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_face_and_car_deploy, container, false);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        showNoDataPage(true);
        mIvNoContent.setImageResource(R.drawable.ic_no_clue);
        mTvNoContent.setText(R.string.receive_no_message_deploy_car);
        //recyclerview
        mRvContent.setLayoutManager(mLayoutManager);
        mRvContent.setItemAnimator(mItemAnimator);
        mRvContent.setAdapter(mAdapter);

        mRequest.setCurrentPage(1);
        mRequest.setScore(0);
        mRequest.setPageSize(PAGE_SIZE_DEFAULT);
        mRequest.setAlarmType(String.valueOf(TYPE_CAR_DEPLOY));

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            JqItemEntity jqItemEntity = mBeanList.get(position);
            Intent intent = new Intent(getActivity(), AlarmDetailsActivity.class);
            intent.putExtra(KEY_JQ_ITEM, jqItemEntity.getId());//传递ID，下个界面重新请求接口
            startActivityForResult(intent, CAR_TO_DETAIL_REQUESTCODE);
        });

        //SwipeRefreshLayout
        mAdapter.setOnLoadMoreListener(this, mRvContent);
        mAdapter.setLoadMoreView(mLoadMoreView);
        mSrfl.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mSrfl.setOnRefreshListener(this);

        // 自动下拉刷新
        mRvContent.postDelayed(() -> {
            mSrfl.setRefreshing(true);
            mPresenter.getlistAlarmInfo();
        }, 100);
    }

    @Override
    public void setData(@Nullable Object data) {

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

    }

    @Override
    public void getlistAlarmInfoSuccess(List<JqItemEntity> dataList) {
        Log.i(TAG, "getlistAlarmInfoSuccess");
        //移除刷新圈
        mSrfl.setRefreshing(false);
        mAdapter.setEnableLoadMore(true);
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
    public void getlistAlarmInfoFail() {
        Log.i(TAG, "getlistAlarmInfoFail");
        showNoDataPage(true);
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        mRequest.setCurrentPage(mCurrentPage);
        mPresenter.getlistAlarmInfo();
        Log.i(TAG, "onRefresh:" + mCurrentPage);
    }

    @Override
    public void onLoadMoreRequested() {
        mCurrentPage++;
        mRequest.setCurrentPage(mCurrentPage);
        mPresenter.getlistAlarmInfo();
        Log.i(TAG, "onLoadMoreRequested:" + mCurrentPage);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CAR_TO_DETAIL_RESULTCODE && data != null) {
            Log.i(TAG, "car");
            mPresenter.getlistAlarmInfo();
        }
    }
}
