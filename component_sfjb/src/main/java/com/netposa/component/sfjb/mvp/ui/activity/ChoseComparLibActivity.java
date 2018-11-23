package com.netposa.component.sfjb.mvp.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.core.RouterHub;
import com.netposa.common.log.Log;
import com.netposa.common.utils.SizeUtils;
import com.netposa.commonres.modle.LoadingDialog;
import com.netposa.component.sfjb.R;
import com.netposa.component.sfjb.R2;
import com.netposa.component.sfjb.di.component.DaggerChoseComparLibComponent;
import com.netposa.component.sfjb.di.module.ChoseComparLibModule;
import com.netposa.component.sfjb.mvp.contract.ChoseComparLibContract;
import com.netposa.component.sfjb.mvp.model.entity.OrgChoseEntity;
import com.netposa.component.sfjb.mvp.presenter.ChoseComparLibPresenter;
import com.netposa.component.sfjb.mvp.ui.adapter.ChoseLibAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.component.sfjb.mvp.model.entity.OrgChoseEntity.TYPE_CAMERA;
import static com.netposa.component.sfjb.mvp.model.entity.OrgChoseEntity.TYPE_GROUP;

@Route(path = RouterHub.SFJB_CHOSE_LIB_ACTIVITY)
public class ChoseComparLibActivity extends BaseActivity<ChoseComparLibPresenter> implements ChoseComparLibContract.View {
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
    LoadingDialog mLoadingDialog;
    @BindView(R2.id.ll_org_main)
    LinearLayout mLlOrgMain;
    @BindView(R2.id.head_left_back_iv)
    ImageButton headLeftBackIv;
    @BindView(R2.id.iv_all_single)
    RadioButton mIvAllSingle;
    @BindView(R2.id.ly_all_chose)
    LinearLayout mLyAllChose;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.ItemAnimator mItemAnimator;
    @Inject
    ChoseLibAdapter mAdapter;
    @Inject
    List<OrgChoseEntity> mOrgChoseEntities;

    private List<List<OrgChoseEntity>> mAllOrgChoseEntities;
    private int mIndex = 0;
    private Drawable mLeftDrawable;
    private boolean mIsAllChose = false;
    private int countIndex = 0;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerChoseComparLibComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .choseComparLibModule(new ChoseComparLibModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_chose_compar_lib; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        mTitleTv.setText(R.string.sfjb_chose_compairlib);
        mHeadRightIv.setVisibility(View.VISIBLE);
        mHeadRightIv.setImageResource(R.drawable.ic_search_black);
        mOrgRecyview.setLayoutManager(mLayoutManager);
        mOrgRecyview.setItemAnimator(mItemAnimator);
        mOrgRecyview.setAdapter(mAdapter);

        mLeftDrawable = getResources().getDrawable(R.drawable.ic_list_go_blue);
        mLeftDrawable.setBounds(0, 0, mLeftDrawable.getMinimumWidth(), mLeftDrawable.getMinimumHeight());
        mLoadingDialog = new LoadingDialog(this);

        mIvAllSingle.setChecked(mIsAllChose);

        //TODO
        initData();
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Log.d(TAG, "OnItemChild click !!");
            int id = view.getId();
            if (id == R.id.ly_detail) {
                if (mOrgChoseEntities.get(position).getOrganizationType() == TYPE_GROUP) {
                    String mChildId = mOrgChoseEntities.get(position).getChildId();
                    if (!TextUtils.isEmpty(mChildId)) {
                        mIndex++;
                        mIsAllChose = false;
                        mIvAllSingle.setChecked(mIsAllChose);
                        //TODO
                        getChildData(mChildId);
                    } else {
                        showMessage("不能点击");
                    }
                }
            } else {
                RadioButton radioButton = view.findViewById(R.id.rb_single);
                boolean isChoose = radioButton.isChecked();
                radioButton.setChecked(!isChoose);
                if (isChoose) {
                    countIndex--;
                    mIsAllChose = !isChoose;
                    mIvAllSingle.setChecked(!isChoose);
                    for (OrgChoseEntity mOrgChoseEntity : mOrgChoseEntities) {
                        mOrgChoseEntity.setChoose(!isChoose);
                    }
                    Log.d(TAG, "isChoose:" + isChoose);
                } else {
                    countIndex++;
                    if (countIndex == mOrgChoseEntities.size()) {
                        mIsAllChose = !isChoose;
                        mIvAllSingle.setChecked(!isChoose);
                    }
                }
            }
        });
    }

    private void initData() {
        mAllOrgChoseEntities = new ArrayList<>();

        List<OrgChoseEntity> orgMainEntities = new ArrayList<>();
        OrgChoseEntity entity1 = new OrgChoseEntity();
        entity1.setTreeDesc("武汉市");
        entity1.setOrganizationDesc("洪山区");
        entity1.setParentId("1");
        entity1.setChildId("10");
        entity1.setChoose(false);
        entity1.setOrganizationType(TYPE_GROUP);
        mOrgChoseEntities.add(entity1);

        OrgChoseEntity entity2 = new OrgChoseEntity();
        entity2.setTreeDesc("武汉市");
        entity2.setOrganizationDesc("江夏区");
        entity2.setParentId("1");
        entity2.setChildId("11");
        entity2.setChoose(false);
        entity2.setOrganizationType(TYPE_GROUP);
        mOrgChoseEntities.add(entity2);

        OrgChoseEntity entity3 = new OrgChoseEntity();
        entity3.setTreeDesc("武汉市");
        entity3.setOrganizationDesc("汉阳区");
        entity3.setParentId("1");
        entity3.setChoose(false);
        entity3.setOrganizationType(TYPE_CAMERA);
        entity3.setChildId("12");
        mOrgChoseEntities.add(entity3);
        orgMainEntities.addAll(mOrgChoseEntities);
        mAllOrgChoseEntities.add(orgMainEntities);

        createAddTextView(true, mOrgChoseEntities.get(0), mIndex);
    }

    private void getChildData(String mChildId) {
        List<OrgChoseEntity> orgMainEntities = new ArrayList<>();
        if (mChildId.equals("10")) {
            mOrgChoseEntities.clear();
            OrgChoseEntity entity1 = new OrgChoseEntity();
            entity1.setTreeDesc("洪山区");
            entity1.setOrganizationDesc("嫌疑犯库");
            entity1.setChoose(false);
            entity1.setParentId("2");
            entity1.setOrganizationType(TYPE_GROUP);
            entity1.setChildId("10");
            mOrgChoseEntities.add(entity1);

            OrgChoseEntity entity2 = new OrgChoseEntity();
            entity2.setTreeDesc("洪山区");
            entity2.setOrganizationDesc("城市库");
            entity2.setParentId("2");
            entity2.setChoose(false);
            entity2.setOrganizationType(TYPE_GROUP);
            entity2.setChildId("10");
            mOrgChoseEntities.add(entity2);
            orgMainEntities.addAll(mOrgChoseEntities);
            mAllOrgChoseEntities.add(orgMainEntities);

            Log.e(TAG, "initChildData: " + mOrgChoseEntities.toString());
            Log.e(TAG, "initChildData: " + mAllOrgChoseEntities.toString() + ",addr:" + mAllOrgChoseEntities.hashCode());

            createAddTextView(true, mOrgChoseEntities.get(0), mIndex);
            refreshData();

        } else if (mChildId.equals("11")) {
            mOrgChoseEntities.clear();
            OrgChoseEntity entity1 = new OrgChoseEntity();
            entity1.setTreeDesc("江夏区");
            entity1.setOrganizationDesc("重点犯库");
            entity1.setParentId("3");
            entity1.setChoose(false);
            entity1.setOrganizationType(TYPE_GROUP);
            mOrgChoseEntities.add(entity1);

            OrgChoseEntity entity2 = new OrgChoseEntity();
            entity2.setTreeDesc("江夏区");
            entity2.setOrganizationDesc("城区");
            entity2.setParentId("3");
            entity2.setChoose(false);
            entity2.setOrganizationType(TYPE_GROUP);
            mOrgChoseEntities.add(entity2);
            orgMainEntities.addAll(mOrgChoseEntities);
            mAllOrgChoseEntities.add(orgMainEntities);

            createAddTextView(true, mOrgChoseEntities.get(0), mIndex);
            refreshData();

        } else if (mChildId.equals("12")) {
            mOrgChoseEntities.clear();
            OrgChoseEntity entity1 = new OrgChoseEntity();
            entity1.setTreeDesc("汉阳区");
            entity1.setOrganizationDesc("嫌疑人库");
            entity1.setParentId("4");
            entity1.setChoose(false);
            entity1.setOrganizationType(TYPE_CAMERA);
            mOrgChoseEntities.add(entity1);
            orgMainEntities.addAll(mOrgChoseEntities);
            mAllOrgChoseEntities.add(orgMainEntities);
            createAddTextView(true, mOrgChoseEntities.get(0), mIndex);
            refreshData();
        }
    }

    private void createAddTextView(boolean isRootClick, OrgChoseEntity orgChoseEntity, int mIndex) {
        TextView mTextView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.rightMargin = SizeUtils.dp2px(6);
        mTextView.setTextSize(12);
        mTextView.setTextColor(getResources().getColor(R.color.default_text_color));
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setLayoutParams(params);
        mTextView.setMaxLines(1);
        // textView.setEllipsize(TextUtils.TruncateAt.END);
        if (isRootClick) {
            mTextView.setText(orgChoseEntity.getTreeDesc());
        } else {
            mTextView.setText(orgChoseEntity.getOrganizationDesc());
        }
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
                tv.setTextColor(getResources().getColor(R.color.default_text_color));
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
                        mOrgChoseEntities.addAll(mAllOrgChoseEntities.get(j));
                        refreshData();
                        Log.e(TAG, "最后-->刷新前的remainingChildCount:" + remainingChildCount + ",tvIndex:" + tvIndex + ",i:" + i + ",j:" + j + ",mAllOrgMainEntities:" + mAllOrgChoseEntities.size() + ",mOrgMainEntities:" + mOrgChoseEntities);
                        return;
                    }
                    mAllOrgChoseEntities.remove(j);
                    TextView orgTv = (TextView) mLlOrgMain.getChildAt(j);
                    if (null != orgTv) {
                        mLlOrgMain.removeView(orgTv);
                        mIndex--;
                        ((TextView) (mLlOrgMain.getChildAt(mLlOrgMain.getChildCount() - 1))).setTextColor(getResources().getColor(R.color.default_text_color));
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
    public void showLoading(String message) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void refreshData() {
        mAdapter.notifyDataSetChanged();
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
            R2.id.head_right_iv,
            R2.id.head_left_back_iv,
            R2.id.ly_all_chose})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.head_left_iv) {
            onBackPressed();
        } else if (id == R.id.head_left_back_iv) {
            killMyself();
        } else if (id == R.id.bt_compare) {
            Log.e(TAG, "1111");
        } else if (id == R.id.head_right_iv) {
            launchActivity(new Intent(this, SearchLibActivity.class));
        } else if (id == R.id.ly_all_chose) {
            if (mIsAllChose) {
                mIvAllSingle.setChecked(false);
                mIsAllChose = false;
                for (int i = 0; i < mOrgChoseEntities.size(); i++) {
                    mOrgChoseEntities.get(i).setChoose(false);
                }
                refreshData();
            } else {
                mIvAllSingle.setChecked(true);
                mIsAllChose = true;
                for (int i = 0; i < mOrgChoseEntities.size(); i++) {
                    mOrgChoseEntities.get(i).setChoose(true);
                }
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
                TextView textView = (TextView) mLlOrgMain.getChildAt(mIndex - 1);
                deleteTvAndRefresh(mLlOrgMain, textView);
                setAllchose(mOrgChoseEntities);
            } else {
                killMyself();
            }
        }
    }
}
