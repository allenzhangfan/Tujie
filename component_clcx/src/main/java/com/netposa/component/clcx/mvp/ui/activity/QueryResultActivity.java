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
import com.netposa.component.clcx.R;
import com.netposa.component.clcx.R2;
import com.netposa.component.clcx.di.component.DaggerQueryResultComponent;
import com.netposa.component.clcx.di.module.QueryResultModule;
import com.netposa.component.clcx.mvp.contract.QueryResultContract;
import com.netposa.component.clcx.mvp.model.entity.QueryResultEntity;
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

public class QueryResultActivity extends BaseActivity<QueryResultPresenter> implements QueryResultContract.View,SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener{

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
    @BindView(R2.id.iv_nocontent)
    ImageView mIvNocontent;
    @BindView(R2.id.cl_no_content)
    ConstraintLayout mClNoContent;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.ItemAnimator mItemAnimator;
    @Inject
    QueryResultAdapter mAdapter;
    @Inject
    LoadMoreView mLoadMoreView;
    @Inject
    List<QueryResultEntity> mBeanList;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerQueryResultComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .queryResultModule(new QueryResultModule(this,this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_query_result; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mTitleTv.setText(R.string.clcx_title);
        mRvContent.setLayoutManager(mLayoutManager);
        mRvContent.setItemAnimator(mItemAnimator);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRvContent.setHasFixedSize(true);
        mRvContent.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(this,mRvContent);
        mAdapter.setLoadMoreView(mLoadMoreView);
        mSrfl.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mSrfl.setOnRefreshListener(this);
        mSrfl.setRefreshing(true);
        //todo
        initData();
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            launchActivity(new Intent(this,CarRecordActivity.class));
        });
    }

    private void initData() {
        List<QueryResultEntity> mList=new ArrayList<>();
        for (int i = 0; i <25 ; i++) {
            QueryResultEntity entity=new QueryResultEntity();
            entity.setName("曙光南门摄像头"+i);
            entity.setDate("2018_03_11  11:20:26");
            entity.setNum("京A  3434"+i+"26");
            entity.setPic("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1542874343966&di=a69766d00e254ecb0726c9dcd14e29d4&imgtype=0&src=http%3A%2F%2Fimg.igeek.com.cn%2Fuploads%2F2017%2F09%2F07%2Fa1ef0d85fde04189990bdb49e8902bef.jpeg");
            mList.add(entity);
        }
        if(mList.size()>0){
            mBeanList.addAll(mList);
            refreshData();
            if (mBeanList.size() < 10 && mBeanList.size() > 0) {
                mAdapter.disableLoadMoreIfNotFullPage();
            }
            showNoDatePage(false);
        }else{
            showNoDatePage(true);
        }
    }

    @Override
    public void refreshData() {
    }

    @Override
    public void onRefresh() {
        // todo
        initData();
        mAdapter.loadMoreEnd(true);
    }

    @Override
    public void onLoadMoreRequested() {

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

    private void showNoDatePage(boolean show) {
        if (show){
            mSrfl.setVisibility(View.GONE);
            mClNoContent.setVisibility(View.VISIBLE);
        }else {
            if (mSrfl.getVisibility()==View.GONE){
                mSrfl.setVisibility(View.VISIBLE);
            }
            mClNoContent.setVisibility(View.GONE);
        }
        mSrfl.setEnabled(false);
    }

    @OnClick({R2.id.head_left_iv, R2.id.title_tv})
    public void onViewClicked(View view) {
        int id=view.getId();
        if(id==R.id.head_left_iv){
            killMyself();
        }

    }
}
