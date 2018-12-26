package com.netposa.component.jq.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.utils.KeyboardUtils;
import com.netposa.common.utils.ToastUtils;
import com.netposa.commonres.widget.OneKeyClearEditText;
import com.netposa.component.jq.R2;
import com.netposa.component.jq.di.component.DaggerJqSearchComponent;
import com.netposa.component.jq.di.module.JqSearchModule;
import com.netposa.component.jq.mvp.contract.JqSearchContract;
import com.netposa.common.entity.push.JqItemEntity;
import com.netposa.component.jq.mvp.presenter.JqSearchPresenter;
import com.netposa.component.jq.mvp.ui.adapter.JqAdapter;
import com.netposa.component.room.entity.JqSearchHistoryEntity;
import com.netposa.component.jq.R;
import com.netposa.component.jq.mvp.ui.adapter.JqSearchHistoryAdapter;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.common.constant.GlobalConstants.TYPE_CAR_DEPLOY;
import static com.netposa.common.constant.GlobalConstants.TYPE_INVALID;
import static com.netposa.common.constant.GlobalConstants.TYPE_FACE_DEPLOY;
import static com.netposa.common.constant.GlobalConstants.TYPE_SUSPENDING;

public class JqSearchActivity extends BaseActivity<JqSearchPresenter> implements JqSearchContract.View, TextWatcher, TextView.OnEditorActionListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R2.id.tv_cancel)
    TextView mTvCancel;
    @BindView(R2.id.rv_content_keywords)
    RecyclerView mRvContentKeyWords;
    @BindView(R2.id.rv_content_search_result)
    RecyclerView mRvContentSearchResult;
    @BindView(R2.id.okcl_search)
    OneKeyClearEditText mOneKeyClearEditText;
    @BindView(R2.id.rl_search_keywords)
    RelativeLayout mRlSearchKeywords;
    @BindView(R2.id.srfl)
    SwipeRefreshLayout mSrfl;

    @Inject
    @Named("SearchHistory")
    RecyclerView.LayoutManager mSearchHistoryLayoutManager;
    @Inject
    @Named("SearchResult")
    RecyclerView.LayoutManager mSearchResultLayoutManager;
    @Inject
    @Named("SearchHistory")
    RecyclerView.ItemAnimator mSearchHistoryItemAnimator;
    @Inject
    @Named("SearchResult")
    RecyclerView.ItemAnimator mSearchResultItemAnimator;
    @Inject
    List<JqSearchHistoryEntity> mSearchHistoryBeanList;
    @Inject
    List<JqItemEntity> mSearchResultBeanList;
    @Inject
    JqSearchHistoryAdapter mSearchHistoryAdapter;
    @Inject
    JqAdapter mSearchResultAdapter;
    @Inject
    LoadMoreView mLoadMoreView;

    private String mQuery;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerJqSearchComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .jqSearchModule(new JqSearchModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_jq_search; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        //搜索历史recyclerview
        mRvContentKeyWords.setLayoutManager(mSearchHistoryLayoutManager);
        mRvContentKeyWords.setItemAnimator(mSearchHistoryItemAnimator);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRvContentKeyWords.setHasFixedSize(true);
        mRvContentKeyWords.setAdapter(mSearchHistoryAdapter);
        mSearchHistoryAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.iv_delete) {
                mPresenter.deleteSigleHistory(position);
            }
        });

        //搜索结果recyclerview
        mRvContentSearchResult.setLayoutManager(mSearchResultLayoutManager);
        mRvContentSearchResult.setItemAnimator(mSearchResultItemAnimator);
        mRvContentSearchResult.setAdapter(mSearchResultAdapter);

        //load more
        mSearchHistoryAdapter.setOnLoadMoreListener(this, mRvContentKeyWords);
        mSearchHistoryAdapter.setLoadMoreView(mLoadMoreView);

        mSearchResultAdapter.setOnLoadMoreListener(this, mRvContentSearchResult);
        mSearchResultAdapter.setLoadMoreView(mLoadMoreView);

        //SwipeRefreshLayout
        mSrfl.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mSrfl.setOnRefreshListener(this);
        mSrfl.setRefreshing(true);

        onRefresh();
        mOneKeyClearEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mOneKeyClearEditText.setOnEditorActionListener(this);
        mOneKeyClearEditText.addTextChangedListener(this);

        //TODO test data here
        for (int i = 0; i < 20; i++) {
            JqItemEntity entity1 = new JqItemEntity();
            entity1.setItemType(TYPE_FACE_DEPLOY);
            entity1.setItemHandleType(TYPE_INVALID);
            entity1.setCaptureLibName("武汉市2月6日城市人脸布控");
            entity1.setAlarmTime("2018-10-11 12:31:33");
            entity1.setCameraLocation("保利国际中心1楼正门摄像机");
            entity1.setSimilarity(94);
            mSearchResultBeanList.add(entity1);

            JqItemEntity entity2 = new JqItemEntity();
            entity2.setItemHandleType(TYPE_SUSPENDING);
            entity2.setItemType(TYPE_CAR_DEPLOY);
            entity2.setCarNumber("陕A 11L003");
            entity2.setCaptureLibName("武汉市2月6日城市人脸布控");
            entity2.setAlarmTime("2018-10-11 12:31:33");
            entity2.setCameraLocation("保利国际中心1楼正门摄像机");
            mSearchResultBeanList.add(entity2);
        }
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

    private int count = 0;//清空一次以后第二次点击不做任何操作

    @OnClick({R2.id.iv_delete_all, R2.id.tv_cancel})
    public void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_delete_all) {
            if (count == 1 || mSearchHistoryBeanList.size() == 0) {
                return;
            }
            mSearchHistoryBeanList.clear();
            mPresenter.deleteAll();
            refreshData();
            count++;
        } else if (id == R.id.tv_cancel) {
            killMyself();
        }
    }

    @Override
    public void refreshData() {
        mSearchHistoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadDataForFirstTime() {
        if (mSearchHistoryBeanList.size() == 0) {
            mRlSearchKeywords.setVisibility(View.GONE);
            return;
        }
        refreshData();
    }

    /**
     * 搜索事件响应
     **/
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mQuery = s.toString();
        if (TextUtils.isEmpty(mQuery)) {
            showAndHideSearchHistoryLayout(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            mQuery = mOneKeyClearEditText.getText().toString().trim();
            if (TextUtils.isEmpty(mQuery)) {
                showMessage(getString(R.string.query_hint));
                return false;
            }
            KeyboardUtils.hideSoftInput(this);
            showAndHideSearchHistoryLayout(false);
            //TODO 开始搜索
            mSrfl.setEnabled(true);
        }
        return false;
    }

    @Override
    public void onRefresh() {
        mPresenter.getAll();
    }

    @Override
    public void onLoadMoreRequested() {
        //TODO
        new Handler().postDelayed(() -> {
            mSearchHistoryAdapter.loadMoreEnd(true);
            mSrfl.setRefreshing(false);
            mSrfl.setEnabled(false);
        }, 100);
    }

    private void showAndHideSearchHistoryLayout(boolean show) {
        if (show) {
            mRlSearchKeywords.setVisibility(View.VISIBLE);
            mRvContentKeyWords.setVisibility(View.VISIBLE);
            mRvContentSearchResult.setVisibility(View.GONE);
        } else {
            mRlSearchKeywords.setVisibility(View.GONE);
            mRvContentKeyWords.setVisibility(View.GONE);
            mRvContentSearchResult.setVisibility(View.VISIBLE);
        }
    }
}
