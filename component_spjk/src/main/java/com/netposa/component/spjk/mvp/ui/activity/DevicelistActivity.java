package com.netposa.component.spjk.mvp.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.log.Log;
import com.netposa.common.utils.SizeUtils;
import com.netposa.commonres.widget.Dialog.LottieDialogFragment;
import com.netposa.component.spjk.R;
import com.netposa.component.spjk.R2;
import com.netposa.component.spjk.app.SpjkConstants;
import com.netposa.component.spjk.di.component.DaggerDevicelistComponent;
import com.netposa.component.spjk.di.module.DevicelistModule;
import com.netposa.component.spjk.mvp.contract.DevicelistContract;
import com.netposa.component.spjk.mvp.model.entity.SpjkListDeviceResponseEntity;
import com.netposa.component.spjk.mvp.presenter.DevicelistPresenter;
import com.netposa.component.spjk.mvp.ui.adapter.DeviceParentAdapter;

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
import static com.netposa.common.constant.GlobalConstants.KEY_SINGLE_CAMERA_LOCATION_LATITUDE;
import static com.netposa.common.constant.GlobalConstants.KEY_SINGLE_CAMERA_LOCATION_LONGITUDE;
import static com.netposa.component.spjk.app.SpjkConstants.KEY_SINGLE_CAMERA_ID;

public class DevicelistActivity extends BaseActivity<DevicelistPresenter> implements DevicelistContract.View {

    @BindView(R2.id.head_left_iv)
    ImageButton mHeadLeftIv;
    @BindView(R2.id.title_tv)
    TextView mTitleTv;
    @BindView(R2.id.head_right_iv)
    ImageView mHeadRightIv;
    @BindView(R2.id.ll_title)
    LinearLayout mLlTitle;
    @BindView(R2.id.divider_line)
    View mDividerLine;
    @BindView(R2.id.parent_main_hz_scrollview)
    HorizontalScrollView mParentMainHzScrollview;
    @BindView(R2.id.org_recyview)
    RecyclerView mOrgRecyview;
    @BindView(R2.id.srl)
    SwipeRefreshLayout mSrl;
    @BindView(R2.id.ll_org_main)
    LinearLayout mLlOrgMain;
    @BindView(R2.id.head_left_back_iv)
    ImageButton headLeftBackIv;
    @BindView(R2.id.iv_no_content)
    ImageView mIvNoContent;
    @BindView(R2.id.tv_no_content)
    TextView mTvNoContent;
    @BindView(R2.id.view_divider)
    View mViewDivider;
    @BindView(R2.id.cl_no_content)
    ConstraintLayout mClNoContent;

    @Inject
    LottieDialogFragment mLoadingDialogFragment;
    @Inject
    DeviceParentAdapter mAdapter;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.ItemAnimator mItemAnimator;
    @Inject
    List<SpjkListDeviceResponseEntity.DeviceTreeListBean> mOrgMainEntities;


    private Drawable mLeftDrawable;
    private List<List<SpjkListDeviceResponseEntity.DeviceTreeListBean>> mAllOrgMainEntities;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerDevicelistComponent
                //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .devicelistModule(new DevicelistModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_devicelist; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    int mIndex = 0;

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mAllOrgMainEntities = new ArrayList<>();
        Log.e(TAG, "initView: " + mAllOrgMainEntities.hashCode());
        mTitleTv.setText(R.string.list_item_chose);
        mIvNoContent.setImageResource(R.drawable.ic_no_follow);
        mTvNoContent.setText(R.string.no_choose_list);
        mHeadRightIv.setVisibility(View.VISIBLE);
        mHeadRightIv.setImageResource(R.drawable.ic_search_black);
        mOrgRecyview.setLayoutManager(mLayoutManager);
        mOrgRecyview.setItemAnimator(mItemAnimator);
        mOrgRecyview.setAdapter(mAdapter);
        mSrl.setEnabled(false);
        mSrl.setRefreshing(false);
        mSrl.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mLeftDrawable = getResources().getDrawable(R.drawable.ic_list_go_blue);
        mLeftDrawable.setBounds(0, 0, mLeftDrawable.getMinimumWidth(), mLeftDrawable.getMinimumHeight());
        mPresenter.getSpjkDevicelist("");
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            int id = view.getId();
            SpjkListDeviceResponseEntity.DeviceTreeListBean item = (SpjkListDeviceResponseEntity.DeviceTreeListBean) adapter.getItem(position);
            if (id == R.id.ly_detail) {
                if (mOrgMainEntities.get(position).getChildrenCount() > SpjkConstants.CHILD_COUNT) {
                    Log.d("请求参数" + item.getId());
                    mPresenter.getSpjkDevicelist(item.getId());
                    createAddTextView(item.getName(), mIndex);
                    mIndex++;
                } else {
                    Intent intent = new Intent(this, VideoPlayActivity.class);
                    String cameraId = item.getId();
                    intent.putExtra(KEY_SINGLE_CAMERA_ID, cameraId);
                    launchActivity(intent);
                }
            }
            if (id == R.id.iv_menu) {
                Intent dataIntent = new Intent(this, SingleCameraLocationActivity.class);
                String latitude = String.valueOf(item.getLatitude());
                String longitude = String.valueOf(item.getLongitude());
                dataIntent.putExtra(KEY_SINGLE_CAMERA_LOCATION_LATITUDE, latitude);
                dataIntent.putExtra(KEY_SINGLE_CAMERA_LOCATION_LONGITUDE, longitude);
                launchActivity(dataIntent);
            }
        });
    }

    @Override
    public void getListSuccess(SpjkListDeviceResponseEntity responseEntity) {
        List<SpjkListDeviceResponseEntity.DeviceTreeListBean> mAllEntities = new ArrayList<>();
        mOrgMainEntities.clear();
        if (responseEntity.getDeviceTreeList().size() > 0) {
            showNoDataPage(false);
            mOrgMainEntities.addAll(responseEntity.getDeviceTreeList());
            Log.d("mOrgMainEntities:" + responseEntity.toString());
            mAllEntities.addAll(mOrgMainEntities);
            mAllOrgMainEntities.add(mAllEntities);
            refreshData();
        } else {
            showNoDataPage(true);
        }
    }

    @Override
    public void getListFailed() {
        showNoDataPage(true);
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
    public void refreshData() {
        mAdapter.notifyDataSetChanged();
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

    @OnClick({R2.id.head_left_iv, R2.id.head_right_iv, R2.id.head_left_back_iv})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.head_left_iv) {
            onBackPressed();
        } else if (i == R.id.head_left_back_iv) {
            killMyself();
        } else if (i == R.id.head_right_iv) {
            launchActivity(new Intent(this, SpjkSearchActivity.class));
        }
    }

    private void createAddTextView(String name, int index) {
        TextView mTextView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.rightMargin = SizeUtils.dp2px(6);
        mTextView.setTextSize(12);
        mTextView.setTextColor(getResources().getColor(R.color.color_333333));
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setLayoutParams(params);
        mTextView.setMaxLines(1);
        mTextView.setText(name);
        if (index != 0) {
            mTextView.setCompoundDrawables(mLeftDrawable, null, null, null);
            mTextView.setCompoundDrawablePadding(SizeUtils.dp2px(3));
        }
        mTextView.setTag(index);
        mLlOrgMain.addView(mTextView);
        mParentMainHzScrollview.post(() ->
                mParentMainHzScrollview.fullScroll(HorizontalScrollView.FOCUS_RIGHT));
        //循环，然后让最后一个TextView变灰
        int childCount = mLlOrgMain.getChildCount();
        for (int i = 0; i < childCount; i++) {
            TextView tv = (TextView) mLlOrgMain.getChildAt(i);
            if (i == childCount - 1) {
                //最后一个，颜色恢复
                tv.setTextColor(getResources().getColor(R.color.color_333333));
            } else {
                headLeftBackIv.setVisibility(View.VISIBLE);
                tv.setTextColor(getResources().getColor(R.color.app_theme_color));
            }
        }
        checkLevel();
        mTextView.setOnClickListener(v -> {
            //每次点击一个textview，删除当前textview后面的textview
            deleteTvAndRefresh(mLlOrgMain, mTextView);
        });
    }

    private void deleteTvAndRefresh(LinearLayout mLlOrgMain, TextView textView) {
        int remainingChildCount = mLlOrgMain.getChildCount();
        int tvIndex = (int) textView.getTag();
        if (tvIndex == remainingChildCount - 1) {
            Log.e(TAG, "当前-->remainingChildCount:" + remainingChildCount + ",tvIndex:" + tvIndex + ",mAllOrgMainEntities:" + mAllOrgMainEntities.size() + ",mOrgMainEntities:" + mOrgMainEntities);
            return;
        }
        for (int i = remainingChildCount - 1; i > -1; i--) {
            TextView childTv = (TextView) mLlOrgMain.getChildAt(i);
            if (null != childTv && (int) childTv.getTag() == tvIndex) {
                for (int j = remainingChildCount - 1; j >= i; j--) {
                    if (tvIndex == j) {
                        if (tvIndex == 0) {
                            headLeftBackIv.setVisibility(View.GONE);
                        }
                        mOrgMainEntities.clear();
                        mOrgMainEntities.addAll(mAllOrgMainEntities.get(j + 1));
                        refreshData();
                        Log.e(TAG, "最后-->刷新前的remainingChildCount:" + remainingChildCount + ",tvIndex:" + tvIndex + ",i:" + i + ",j:" + j + ",mAllOrgMainEntities:" + mAllOrgMainEntities.size() + ",mOrgMainEntities:" + mOrgMainEntities);
                        return;
                    }
                    mAllOrgMainEntities.remove(j + 1);
                    Log.d(TAG, mAllOrgMainEntities.size());
                    TextView orgTv = (TextView) mLlOrgMain.getChildAt(j);
                    if (null != orgTv) {
                        mLlOrgMain.removeView(orgTv);
                        mIndex--;
                        ((TextView) (mLlOrgMain.getChildAt(mLlOrgMain.getChildCount() - 1))).setTextColor(getResources().getColor(R.color.color_333333));
                        Log.e(TAG, "循环-->:" + remainingChildCount + ",tvIndex:" + tvIndex + ",i:" + i + ",j:" + j + ",mAllOrgMainEntities:" + mAllOrgMainEntities.size() + ",mOrgMainEntities:" + mOrgMainEntities);
                    }
                }
            }
        }
        checkLevel();
    }

    private void checkLevel() {
        mDividerLine.setVisibility(mLlOrgMain.getChildCount() >= 1 ? View.VISIBLE : View.GONE);
        mParentMainHzScrollview.setVisibility(mLlOrgMain.getChildCount() >= 1 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (null != mLlOrgMain) {
            int childSize = mLlOrgMain.getChildCount();
            if (childSize > 1) {
                TextView textView = (TextView) mLlOrgMain.getChildAt(mIndex - 2);
                deleteTvAndRefresh(mLlOrgMain, textView);
            } else {
                killMyself();
            }
        }
    }

    private void showNoDataPage(boolean show) {
        if (show) {
            mViewDivider.setVisibility(View.GONE);
            mOrgRecyview.setVisibility(View.GONE);
            mClNoContent.setVisibility(View.VISIBLE);
        } else {
            mViewDivider.setVisibility(View.VISIBLE);
            mOrgRecyview.setVisibility(View.VISIBLE);
            mClNoContent.setVisibility(View.GONE);
        }
    }
}
