package com.netposa.component.rltk.mvp.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.log.Log;
import com.netposa.common.utils.SizeUtils;
import com.netposa.component.rltk.R;
import com.netposa.component.rltk.R2;
import com.netposa.component.rltk.di.component.DaggerChooseDeviceComponent;
import com.netposa.component.rltk.di.module.ChooseDeviceModule;
import com.netposa.component.rltk.mvp.contract.ChooseDeviceContract;
import com.netposa.component.rltk.mvp.model.entity.OrgChoseEntity;
import com.netposa.component.rltk.mvp.model.entity.SearchDeviceResponseEntity;
import com.netposa.component.rltk.mvp.presenter.ChooseDevicePresenter;
import com.netposa.component.rltk.mvp.ui.adapter.ChooseDeviceAdapter;
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
import static com.netposa.component.rltk.app.RltkConstants.CHILD_COUNT;
import static com.netposa.component.rltk.app.RltkConstants.KEY_TO_CHOSE_DEVICE;
import static com.netposa.component.rltk.app.RltkConstants.KEY_TO_FACE_LIB_DEVICE;
import static com.netposa.component.rltk.app.RltkConstants.KEY_TO_FACE_LIB_ORG;
import static com.netposa.component.rltk.app.RltkConstants.ORG;

public class ChooseDeviceActivity extends BaseActivity<ChooseDevicePresenter> implements ChooseDeviceContract.View {
    @BindView(R2.id.head_left_iv)
    ImageButton mHeadLeftIv;
    @BindView(R2.id.title_tv)
    TextView mTitleTv;
    @BindView(R2.id.divider_line)
    View mDividerLine;
    @BindView(R2.id.parent_main_hz_scrollview)
    HorizontalScrollView mParentMainHzScrollview;
    @BindView(R2.id.org_recyview)
    RecyclerView mOrgRecyview;
    @BindView(R2.id.ll_org_main)
    LinearLayout mLlOrgMain;
    @BindView(R2.id.head_left_back_iv)
    ImageButton headLeftBackIv;
    @BindView(R2.id.iv_all_single)
    RadioButton mIvAllSingle;
    @BindView(R2.id.ly_all_chose)
    LinearLayout mLyAllChose;
    @BindView(R2.id.srl)
    SwipeRefreshLayout mSrl;
    @BindView(R2.id.cl_no_content)
    ConstraintLayout mClNoContent;
    @BindView(R2.id.bt_compare)
    MaterialButton mBt_compare;
    @BindView(R2.id.iv_no_content)
    ImageView mIvNoContent;
    @BindView(R2.id.tv_no_content)
    TextView mTvNoCOntent;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.ItemAnimator mItemAnimator;
    @Inject
    ChooseDeviceAdapter mAdapter;
    @Inject
    List<OrgChoseEntity> mOrgChoseEntities;

    private List<List<OrgChoseEntity>> mAllOrgChoseEntities;
    private int mIndex = 0;
    private Drawable mLeftDrawable;
    private boolean mIsAllChose = false;
    private int countIndex = 0;
    private String mChildId;
    private ArrayList<String> mdeviceIds;
    private ArrayList<String> morgIds;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerChooseDeviceComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .chooseDeviceModule(new ChooseDeviceModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_choose_device; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mAllOrgChoseEntities = new ArrayList<>();
        mTitleTv.setText(R.string.rltk_choce_device);
        mTvNoCOntent.setText(R.string.no_device_choose);
        mIvNoContent.setImageResource(R.drawable.ic_no_follow);
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

        mIvAllSingle.setChecked(mIsAllChose);
        mAllOrgChoseEntities = new ArrayList<>();
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            int id = view.getId();
            if (id == R.id.ly_detail) {
                if (mOrgChoseEntities.get(position).getOrganizationType().equals(ORG)) {
                    String mName = mOrgChoseEntities.get(position).getOrganizationDesc();
                    mChildId = mOrgChoseEntities.get(position).getChildId();
                    if (!TextUtils.isEmpty(mChildId)) {
                        mIsAllChose = false;
                        mIvAllSingle.setChecked(mIsAllChose);
                        mPresenter.getChoseDevicelist(mChildId);
                        createAddTextView(mName, mIndex);
                        mIndex++;
                    } else {
                        showMessage("不能点击");
                    }
                }
            } else {
                OrgChoseEntity entity = mOrgChoseEntities.get(position);
                boolean choose = entity.isChoose();
                if (choose) {
                    countIndex--;
                    entity.setChoose(!choose);
                    mIsAllChose = !choose;
                    mIvAllSingle.setChecked(mIsAllChose);
                } else {
                    countIndex++;
                    entity.setChoose(true);
                    if (countIndex == mOrgChoseEntities.size()) {
                        mIsAllChose = true;
                        mIvAllSingle.setChecked(mIsAllChose);
                    }
                }
                refreshData();
            }
        });
        // 自动下拉刷新
        mOrgRecyview.postDelayed(() -> {
            mSrl.setRefreshing(true);
            mPresenter.getChoseDevicelist("");
        }, 100);
    }

    @Override
    public void getListSuccess(SearchDeviceResponseEntity entity) {
        mSrl.setEnabled(false);
        mSrl.setRefreshing(false);
        List<OrgChoseEntity> mAllEntities = new ArrayList<>();
        mOrgChoseEntities.clear();
        OrgChoseEntity mOrgChoseEntity;
        Log.d("mOrgMainEntities:" + entity.toString());
        for (SearchDeviceResponseEntity.DeviceTreeListBean mOrgEntity : entity.getDeviceTreeList()) {
            mOrgChoseEntity = new OrgChoseEntity();
            mOrgChoseEntity.setChoose(false);
            mOrgChoseEntity.setChildId(mOrgEntity.getId());
            mOrgChoseEntity.setOrganizationDesc(mOrgEntity.getName());
            mOrgChoseEntity.setOrganizationType(mOrgEntity.getNodeType());// org组织；camera 相机
            mOrgChoseEntity.setChildcount(mOrgEntity.getChildrenCount());
            mOrgChoseEntity.setCameraType(mOrgEntity.getCameraType());
            mOrgChoseEntities.add(mOrgChoseEntity);
        }
        mAllEntities.addAll(mOrgChoseEntities);
        mAllOrgChoseEntities.add(mAllEntities);
        refreshData();
    }

    @Override
    public void getListFailed() {
        mSrl.setEnabled(false);
        mSrl.setRefreshing(false);
        showNoDataPage(true);
    }

    private void createAddTextView(String name, int mIndex) {
        TextView mTextView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.rightMargin = SizeUtils.dp2px(6);
        mTextView.setTextSize(12);
        mTextView.setTextColor(getResources().getColor(R.color.color_333333));
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setLayoutParams(params);
        mTextView.setMaxLines(1);
        mTextView.setText(name);
        // textView.setEllipsize(TextUtils.TruncateAt.END);
        if (mIndex != 0) {
            mTextView.setCompoundDrawables(mLeftDrawable, null, null, null);
            mTextView.setCompoundDrawablePadding(SizeUtils.dp2px(3));
        }
        mTextView.setTag(mIndex);
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

    private void deleteTvAndRefresh(LinearLayout mLlOrgMain, TextView mTextView) {
        int remainingChildCount = mLlOrgMain.getChildCount();
        int tvIndex = (int) mTextView.getTag();
        if (tvIndex == remainingChildCount - 1) {
            Log.e(TAG, "当前-->remainingChildCount:" + remainingChildCount + ",tvIndex:" + tvIndex + ",mAllOrgMainEntities:" + mAllOrgChoseEntities.size() + ",mOrgMainEntities:" + mOrgChoseEntities);
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
                        mOrgChoseEntities.clear();
                        mOrgChoseEntities.addAll(mAllOrgChoseEntities.get(j + 1));
                        refreshData();
                        Log.e(TAG, "最后-->刷新前的remainingChildCount:" + remainingChildCount + ",tvIndex:" + tvIndex + ",i:" + i + ",j:" + j + ",mAllOrgMainEntities:" + mAllOrgChoseEntities.size() + ",mOrgMainEntities:" + mOrgChoseEntities);
                        return;
                    }
                    mAllOrgChoseEntities.remove(j + 1);
                    Log.d(TAG, mAllOrgChoseEntities.size());
                    TextView orgTv = (TextView) mLlOrgMain.getChildAt(j);
                    if (null != orgTv) {
                        mLlOrgMain.removeView(orgTv);
                        mIndex--;
                        ((TextView) (mLlOrgMain.getChildAt(mLlOrgMain.getChildCount() - 1))).setTextColor(getResources().getColor(R.color.color_333333));
                        Log.e(TAG, "循环-->:" + remainingChildCount + ",tvIndex:" + tvIndex + ",i:" + i + ",j:" + j + ",mAllOrgMainEntities:" + mAllOrgChoseEntities.size() + ",mOrgMainEntities:" + mOrgChoseEntities);
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
    public void refreshData() {
        mAdapter.notifyDataSetChanged();
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

    @OnClick({R2.id.bt_compare,
            R2.id.head_left_iv,
            R2.id.head_left_back_iv,
            R2.id.ly_all_chose})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.head_left_iv) {
            onBackPressed();
        } else if (id == R.id.head_left_back_iv) {
            killMyself();
        } else if (id == R.id.bt_compare) {
            mdeviceIds = new ArrayList<>();
            morgIds = new ArrayList<>();
            for (int i = 0; i < mOrgChoseEntities.size(); i++) {
                if (mOrgChoseEntities.get(i).getChildcount() > CHILD_COUNT) {
                    if (mOrgChoseEntities.get(i).isChoose()) {
                        morgIds.add(mOrgChoseEntities.get(i).getChildId());
                    }
                } else if (mOrgChoseEntities.get(i).getChildcount() == CHILD_COUNT) {
                    mdeviceIds.add(mOrgChoseEntities.get(i).getChildId());
                }

            }
            if (mdeviceIds.size() > 0 || morgIds.size() > 0) {
                goBackFaceLibActivity();
            } else {
                showMessage(getString(R.string.no_deviceId));
            }
        } else if (id == R.id.ly_all_chose) {
            if (mIsAllChose) { // 取消全选
                mIvAllSingle.setChecked(false);
                mIsAllChose = false;
                for (int i = 0; i < mOrgChoseEntities.size(); i++) {
                    mOrgChoseEntities.get(i).setChoose(false);
                }
                countIndex = 0;
                refreshData();
            } else { //全选
                mIvAllSingle.setChecked(true);
                mIsAllChose = true;
                for (int i = 0; i < mOrgChoseEntities.size(); i++) {
                    mOrgChoseEntities.get(i).setChoose(true);
                }
                countIndex = mOrgChoseEntities.size();
                refreshData();
            }
        }
    }

    private void setAllchose(List<OrgChoseEntity> mOrgChoseEntities) {
        for (int i = 0; i < mOrgChoseEntities.size(); i++) {
            boolean ischose = mOrgChoseEntities.get(i).isChoose();
            mIvAllSingle.setChecked(ischose);
            mIsAllChose = ischose;
        }
    }

    @Override
    public void onBackPressed() {
        if (null != mLlOrgMain) {
            int childSize = mLlOrgMain.getChildCount();
            if (childSize > 1) {
                TextView textView = (TextView) mLlOrgMain.getChildAt(mIndex - 2);
                deleteTvAndRefresh(mLlOrgMain, textView);
                setAllchose(mOrgChoseEntities);
            } else {
                killMyself();
            }
        }
    }

    private void showNoDataPage(boolean show) {
        if (show) {
            mSrl.setVisibility(View.GONE);
            mClNoContent.setVisibility(View.VISIBLE);
            mLyAllChose.setVisibility(View.GONE);
            mBt_compare.setVisibility(View.GONE);
            mSrl.setEnabled(false);
        } else {
            if (mSrl.getVisibility() == View.GONE) {
                mSrl.setVisibility(View.VISIBLE);
            }
            mClNoContent.setVisibility(View.GONE);
            mBt_compare.setVisibility(View.VISIBLE);
            mLyAllChose.setVisibility(View.VISIBLE);
            mSrl.setEnabled(true);
        }
    }

    private void goBackFaceLibActivity() {
        Intent intent = new Intent(this, FaceLibraryActivity.class);
        intent.putStringArrayListExtra(KEY_TO_FACE_LIB_DEVICE, mdeviceIds);
        intent.putStringArrayListExtra(KEY_TO_FACE_LIB_ORG, morgIds);
        Log.i(TAG,"mdeviceIds="+mdeviceIds.size()+"morgIds="+morgIds.size());
        this.setResult(KEY_TO_CHOSE_DEVICE, intent);
        killMyself();
    }
}
