package com.netposa.component.spjk.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.log.Log;
import com.netposa.common.utils.KeyboardUtils;
import com.netposa.common.utils.ToastUtils;
import com.netposa.commonres.widget.OneKeyClearEditText;
import com.netposa.component.room.DbHelper;
import com.netposa.component.room.entity.SpjkSearchHistoryEntity;
import com.netposa.component.spjk.R;
import com.netposa.component.spjk.R2;
import com.netposa.component.spjk.di.component.DaggerSpjkSearchComponent;
import com.netposa.component.spjk.di.module.SpjkSearchModule;
import com.netposa.component.spjk.mvp.contract.SpjkSearchContract;
import com.netposa.component.spjk.mvp.model.entity.SpjkSearchResponseEntity;
import com.netposa.component.spjk.mvp.presenter.SpjkSearchPresenter;
import com.netposa.component.spjk.mvp.ui.adapter.SpjkSearchHistoryAdapter;
import com.netposa.component.spjk.mvp.ui.adapter.SpjkSearchResulthAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.schedulers.Schedulers;

import static android.view.inputmethod.InputMethodManager.RESULT_UNCHANGED_SHOWN;
import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.common.constant.GlobalConstants.KEY_SINGLE_CAMERA_LOCATION_LATITUDE;
import static com.netposa.common.constant.GlobalConstants.KEY_SINGLE_CAMERA_LOCATION_LONGITUDE;
import static com.netposa.component.spjk.app.SpjkConstants.KEY_SINGLE_CAMERA_ID;

public class SpjkSearchActivity extends BaseActivity<SpjkSearchPresenter> implements SpjkSearchContract.View, TextWatcher, TextView.OnEditorActionListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R2.id.okcl_search)
    OneKeyClearEditText mOkclSearch;
    @BindView(R2.id.tv_cancel)
    TextView mTvCancel;
    @BindView(R2.id.ll_search)
    LinearLayout mLlSearch;
    @BindView(R2.id.iv_delete_all)
    ImageView mIvDeleteAll;
    @BindView(R2.id.rl_search_keywords)
    RelativeLayout mRlSearchKeywords;
    @BindView(R2.id.rv_content_keywords)
    RecyclerView mRvContentKeywords;
    @BindView(R2.id.rv_content_search_result)
    RecyclerView mRvContentSearchResult;
    @BindView(R2.id.srfl)
    SwipeRefreshLayout mSrfl;
    @BindView(R2.id.cl_no_content)
    ConstraintLayout mClNoContent;
    @BindView(R2.id.iv_no_content)
    ImageView mIvNoContent;
    @BindView(R2.id.tv_no_content)
    TextView mTvNoCOntent;

    @Inject
    @Named("SearchSpjkHistory")
    RecyclerView.LayoutManager mSearchHistoryLayoutManager;
    @Inject
    @Named("SearchSpjkResult")
    RecyclerView.LayoutManager mSearchResultLayoutManager;
    @Inject
    @Named("SearchSpjkHistory")
    RecyclerView.ItemAnimator mSearchHistoryItemAnimator;
    @Inject
    @Named("SearchSpjkResult")
    RecyclerView.ItemAnimator mSearchResultItemAnimator;
    @Inject
    List<SpjkSearchHistoryEntity> mSearchHistoryBeanList;
    @Inject
    List<SpjkSearchResponseEntity.ListBean> mSearchResultBeanList;
    @Inject
    SpjkSearchHistoryAdapter mSearchHistoryAdapter;
    @Inject
    SpjkSearchResulthAdapter mSearchResultAdapter;
    @Inject
    LoadMoreView mLoadMoreView;

    private String mOrganizeId;
    private String mQuery;
    List<SpjkSearchHistoryEntity> historyEntityList = new ArrayList<>();
    private int mCurrentPage = 1;
    private boolean mkeyword = true;  //历史搜索的可见状态
    private boolean mResult = false;  //搜索结果的可见状态

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSpjkSearchComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .spjkSearchModule(new SpjkSearchModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_search_result; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mTvNoCOntent.setText(R.string.no_search_result);
        mIvNoContent.setImageResource(R.drawable.ic_no_follow);
        //搜索历史recyclerview
        mRvContentKeywords.setLayoutManager(mSearchHistoryLayoutManager);
        mRvContentKeywords.setItemAnimator(mSearchHistoryItemAnimator);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRvContentKeywords.setHasFixedSize(true);
        mRvContentKeywords.setAdapter(mSearchHistoryAdapter);
        mSearchHistoryAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            mPresenter.deleteSigleHistory(position);
        });

        //搜索结果recyclerview
        mRvContentSearchResult.setLayoutManager(mSearchResultLayoutManager);
        mRvContentSearchResult.setItemAnimator(mSearchResultItemAnimator);
        mRvContentSearchResult.setAdapter(mSearchResultAdapter);

        //load more
        mSearchHistoryAdapter.setOnLoadMoreListener(this, mRvContentKeywords);
        mSearchHistoryAdapter.setLoadMoreView(mLoadMoreView);

        mSearchResultAdapter.setOnLoadMoreListener(this, mRvContentSearchResult);
        mSearchResultAdapter.setLoadMoreView(mLoadMoreView);

        //SwipeRefreshLayout
        mSrfl.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mSrfl.setOnRefreshListener(this);
        mSrfl.setRefreshing(true);

        //对于刚跳到一个新的界面就要弹出软键盘的情况上述代码可能由于界面为加载完全而无法弹出软键盘
        //此时应该适当的延迟弹出软键盘（保证界面的数据加载完成）
        new Timer()
                .schedule(
                        new TimerTask() {
                            public void run() {
                                InputMethodManager inputManager =
                                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputManager.showSoftInput(mOkclSearch, RESULT_UNCHANGED_SHOWN);
                            }

                        },
                        200);

        mPresenter.getOrganizeId();
        onRefresh();
        mOkclSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mOkclSearch.setOnEditorActionListener(this);
        mOkclSearch.addTextChangedListener(this);

        mSearchHistoryAdapter.setOnItemClickListener((adapter, view, position) -> {
            SpjkSearchHistoryEntity mHistory = (SpjkSearchHistoryEntity) adapter.getItem(position);
            mOkclSearch.setText(mHistory.getName());
            mCurrentPage = 1;
            mPresenter.getMatchData(mHistory.getName(), mCurrentPage, mOrganizeId);
        });
        mSearchResultAdapter.setOnItemClickListener((adapter, view, position) -> {
            SpjkSearchResponseEntity.ListBean item = (SpjkSearchResponseEntity.ListBean) adapter.getItem(position);
            Intent intent = new Intent(this, VideoPlayActivity.class);
            String cameraId = item.getId();
            intent.putExtra(KEY_SINGLE_CAMERA_ID, cameraId);
            launchActivity(intent);
        });
        mSearchResultAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            SpjkSearchResponseEntity.ListBean item = (SpjkSearchResponseEntity.ListBean) adapter.getItem(position);
            Intent dataIntent = new Intent(this, SingleCameraLocationActivity.class);
            String latitude = String.valueOf(item.getLatitude());
            String longitude = String.valueOf(item.getLongitude());
            dataIntent.putExtra(KEY_SINGLE_CAMERA_LOCATION_LATITUDE, latitude);
            dataIntent.putExtra(KEY_SINGLE_CAMERA_LOCATION_LONGITUDE, longitude);
            launchActivity(dataIntent);
        });
    }

    private void insertData(String name) {
        historyEntityList = new ArrayList<>();
        SpjkSearchHistoryEntity entity = new SpjkSearchHistoryEntity();
        entity.setName(name);
        historyEntityList.add(entity);
        DbHelper
                .getInstance()
                .insertAllSpjkSearchHistories(historyEntityList)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public void getOrganizeId(String id) {
        mOrganizeId = id;
    }

    @Override
    public void getListFailed() {
        noContentShow();
    }

    @Override
    public void getListSuccess(SpjkSearchResponseEntity responseEntity) {
        if (responseEntity.getList().size() > 0) {
            mSearchResultBeanList.clear();
            mSrfl.setEnabled(true);
            mSrfl.setRefreshing(false);
            showAndHideSearchHistoryLayout(false);
            mSearchResultBeanList.addAll(responseEntity.getList());
            for (int i = 0; i < mSearchResultBeanList.size(); i++) {
                responseEntity.getList().get(i).setMatchName(mQuery);
            }
//            mSearchResultAdapter.setMatchName(mQuery);
            //默认第一次加载会回调onLoadMoreRequested()加载更多这个方法,如果不需要可以配置如下:
            if (mSearchResultBeanList.size() < 10 && mSearchResultBeanList.size() > 0) {
                mSearchResultAdapter.disableLoadMoreIfNotFullPage();
            }
            contentShow();
            mSrfl.setEnabled(false);
        } else {
            noContentShow();
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

    @OnClick({R2.id.tv_cancel, R2.id.iv_delete_all})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.iv_delete_all) {
            if (count == 1 || mSearchHistoryBeanList.size() == 0) {
                return;
            }
            mSearchHistoryBeanList.clear();
            mPresenter.deleteAll();
            refreshData();
            count++;
            if (mRlSearchKeywords.getVisibility() == View.GONE && mSrfl.getVisibility() == View.GONE) {
                mRlSearchKeywords.setVisibility(View.VISIBLE);
                mSrfl.setVisibility(View.VISIBLE);
            }
        } else if (id == R.id.tv_cancel) {
            KeyboardUtils.hideSoftInput(this);
            killMyself();
        }
    }

    @Override
    public void onRefresh() {
        if (mResult) {
            mCurrentPage = 1;
            mPresenter.getMatchData(mQuery, mCurrentPage, mOrganizeId);
            mSearchResultAdapter.loadMoreEnd(true);
        } else if (mkeyword) {
            mPresenter.getAll();
        }
    }

    @Override
    public void refreshData() {
        if (mResult) {
            mSearchResultAdapter.notifyDataSetChanged();
            mSearchResultAdapter.loadMoreEnd(true);
        } else if (mkeyword) {
            mSearchHistoryAdapter.notifyDataSetChanged();
            mSearchHistoryAdapter.loadMoreEnd(true);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        Log.d(TAG, "onLoadMoreRequested currentPage:" + mCurrentPage);
        mCurrentPage++;
        mPresenter.getMatchData(mQuery, mCurrentPage, mOrganizeId);
        mSearchResultAdapter.loadMoreEnd(true);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_SEARCH) {
            mQuery = mOkclSearch.getText().toString().trim();
            if (TextUtils.isEmpty(mQuery)) {
                showMessage(getString(R.string.query_hint));
                return false;
            }
            KeyboardUtils.hideSoftInput(this);
            insertData(mQuery);
            mPresenter.getMatchData(mQuery, mCurrentPage, mOrganizeId);
        }
        return false;
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mQuery = charSequence.toString();
        if (TextUtils.isEmpty(mQuery)) {
            showAndHideSearchHistoryLayout(true);
            mSearchHistoryBeanList.clear();
            mPresenter.getAll();
            mClNoContent.setVisibility(View.GONE);
            mRlSearchKeywords.setVisibility(View.VISIBLE);
            mSrfl.setVisibility(View.VISIBLE);
            return;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    @Override
    public void loadDataForFirstTimeSuccess() {
        mSearchResultAdapter.loadMoreEnd();
        if (mSearchHistoryBeanList.size() == 0) {
            mRlSearchKeywords.setVisibility(View.GONE);
            return;
        }
        refreshData();
    }

    @Override
    public void loadDataForFirstTimeFail() {

    }

    private void showAndHideSearchHistoryLayout(boolean show) {
        if (show) {
            mkeyword = true;
            mResult = false;
            mRlSearchKeywords.setVisibility(View.VISIBLE);
            mRvContentKeywords.setVisibility(View.VISIBLE);
            mRvContentSearchResult.setVisibility(View.GONE);
        } else {
            mkeyword = false;
            mResult = true;
            mRlSearchKeywords.setVisibility(View.GONE);
            mRvContentKeywords.setVisibility(View.GONE);
            mRvContentSearchResult.setVisibility(View.VISIBLE);
        }
    }

    private void noContentShow() {
        mClNoContent.setVisibility(View.VISIBLE);
        mRlSearchKeywords.setVisibility(View.GONE);
        mSrfl.setVisibility(View.GONE);
    }

    private void contentShow() {
        mClNoContent.setVisibility(View.GONE);
        mRlSearchKeywords.setVisibility(View.GONE);
        mSrfl.setVisibility(View.VISIBLE);
    }
}
