package com.netposa.component.sfjb.mvp.ui.activity;

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
import android.widget.RadioButton;
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
import com.netposa.component.room.dao.DbHelper;
import com.netposa.component.room.entity.SfjbSearchHistoryEntity;
import com.netposa.component.room.entity.SpjkSearchHistoryEntity;
import com.netposa.component.sfjb.R;
import com.netposa.component.sfjb.R2;
import com.netposa.component.sfjb.di.component.DaggerSearchLibComponent;
import com.netposa.component.sfjb.di.module.SearchLibModule;
import com.netposa.component.sfjb.mvp.contract.SearchLibContract;
import com.netposa.component.sfjb.mvp.model.entity.SfjbSearchResultEntity;
import com.netposa.component.sfjb.mvp.presenter.SearchLibPresenter;
import com.netposa.component.sfjb.mvp.ui.adapter.SfjbSearchHistoryAdapter;
import com.netposa.component.sfjb.mvp.ui.adapter.SfjbSearchResulthAdapter;

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
import static com.netposa.component.sfjb.app.SfjbConstants.PAGE_SIZE;

public class SearchLibActivity extends BaseActivity<SearchLibPresenter> implements SearchLibContract.View, TextWatcher, TextView.OnEditorActionListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

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
    @BindView(R2.id.iv_nocontent)
    ImageView mIvNocontent;
    @BindView(R2.id.cl_no_content)
    ConstraintLayout mClNoContent;

    @Inject
    @Named("SearchSfjbHistory")
    RecyclerView.LayoutManager mSearchHistoryLayoutManager;
    @Inject
    @Named("SearchSfjbResult")
    RecyclerView.LayoutManager mSearchResultLayoutManager;
    @Inject
    @Named("SearchSfjbHistory")
    RecyclerView.ItemAnimator mSearchHistoryItemAnimator;
    @Inject
    @Named("SearchSfjbResult")
    RecyclerView.ItemAnimator mSearchResultItemAnimator;
    @Inject
    SfjbSearchHistoryAdapter mSearchHistoryAdapter;
    @Inject
    SfjbSearchResulthAdapter mSearchResultAdapter;
    @Inject
    List<SfjbSearchHistoryEntity> mSearchHistoryBeanList;
    @Inject
    List<SfjbSearchResultEntity> mSearchResultBeanList;
    @Inject
    LoadMoreView mLoadMoreView;

    private List<SfjbSearchHistoryEntity> historyEntityList = new ArrayList<>();
    private String mQuery;
    private int mCurrentPage = PAGE_SIZE;
    private boolean mkeyword = true;  //历史搜索的可见状态
    private boolean mResult = false;  //搜索结果的可见状态

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSearchLibComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .searchLibModule(new SearchLibModule(this,this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_search_lib; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
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
        mSearchResultAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            int id=view.getId();
            if(id==R.id.ly_single){
                RadioButton radioButton = view.findViewById(R.id.rb_single);
                boolean isChoose = radioButton.isChecked();
                radioButton.setChecked(!isChoose);
            }
        });
        //load more
        mSearchHistoryAdapter.setOnLoadMoreListener(this, mRvContentKeywords);
        mSearchHistoryAdapter.setLoadMoreView(mLoadMoreView);

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
     //   mPresenter.getOrganizeId();
        onRefresh();
        mOkclSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mOkclSearch.setOnEditorActionListener(this);
        mOkclSearch.addTextChangedListener(this);
    }

    private void insertData(String name) {
        historyEntityList = new ArrayList<>();
        SfjbSearchHistoryEntity entity = new SfjbSearchHistoryEntity();
        entity.setName(name);
        historyEntityList.add(entity);
        DbHelper
                .getInstance()
                .insertAllSfjbSearchHistories(historyEntityList)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }
    @Override
    public void onRefresh() {
        if (mResult) {
            mCurrentPage = PAGE_SIZE;
        //    mPresenter.getMatchData(mQuery, mCurrentPage, mOrganizeId);
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
        Log.i(TAG, "onLoadMoreRequested currentPage:" + mCurrentPage);
        mCurrentPage++;
     //   mPresenter.getMatchData(mQuery, mCurrentPage, mOrganizeId);
        mSearchResultAdapter.loadMoreEnd(true);
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
        noContentShow();
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
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_SEARCH) {
            mQuery = mOkclSearch.getText().toString().trim();
            if (TextUtils.isEmpty(mQuery)) {
                ToastUtils.showShort(R.string.query_hint);
                return false;
            }
            KeyboardUtils.hideSoftInput(this);
            insertData(mQuery);
      //      mPresenter.getMatchData(mQuery, mCurrentPage, mOrganizeId);
            showAndHideSearchHistoryLayout(true);
            initResultData();
        }
        return false;
    }

    private void initResultData() {
        List<SfjbSearchResultEntity> list=new ArrayList<>();
        for(int i=0;i<10;i++){
            SfjbSearchResultEntity entity=new SfjbSearchResultEntity();
            entity.setName("人像库"+i);
            list.add(entity);
        }
        mSrfl.setEnabled(true);
        mSrfl.setRefreshing(false);
        showAndHideSearchHistoryLayout(false);
        mSearchResultBeanList.addAll(list);
        mSearchResultAdapter.setMatchName(mQuery);
        //默认第一次加载会回调onLoadMoreRequested()加载更多这个方法,如果不需要可以配置如下:
        if (mSearchResultBeanList.size() < 10 && mSearchResultBeanList.size() > 0) {
            mSearchResultAdapter.disableLoadMoreIfNotFullPage();
        }
        mSrfl.setEnabled(false);

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mQuery = charSequence.toString();
        if (TextUtils.isEmpty(mQuery)) {
            showAndHideSearchHistoryLayout(true);
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
}
