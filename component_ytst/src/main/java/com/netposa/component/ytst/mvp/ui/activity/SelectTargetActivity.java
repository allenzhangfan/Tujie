package com.netposa.component.ytst.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.button.MaterialButton;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.constant.GlobalConstants;
import com.netposa.common.log.Log;
import com.netposa.component.ytst.R;
import com.netposa.component.ytst.R2;
import com.netposa.component.ytst.di.component.DaggerSelectTargetComponent;
import com.netposa.component.ytst.di.module.SelectTargetModule;
import com.netposa.component.ytst.mvp.contract.SelectTargetContract;
import com.netposa.component.ytst.mvp.model.entity.ImgSearchRequestEntity;
import com.netposa.component.ytst.mvp.model.entity.PonitEntity;
import com.netposa.component.ytst.mvp.model.entity.UploadPicResponseEntity;
import com.netposa.component.ytst.mvp.presenter.SelectTargetPresenter;
import com.netposa.component.ytst.mvp.ui.adapter.SelectTargetAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.common.constant.GlobalConstants.KEY_DATA_KEY;
import static com.netposa.common.constant.GlobalConstants.KEY_DATA_TYPE;
import static com.netposa.common.constant.GlobalConstants.KEY_PICPATH;
import static com.netposa.common.constant.GlobalConstants.KEY_POSITION;
import static com.netposa.common.constant.GlobalConstants.KEY_SEESION;
import static com.netposa.common.core.RouterHub.YTST_SELECT_TARGET_ACTIVITY;
import static com.netposa.common.utils.TujieImageUtils.getPicUrl;
import static com.netposa.component.ytst.app.YtstConstants.KEY_IMGSEARCH;
import static com.netposa.component.ytst.app.YtstConstants.KEY_INTENT_PIC;


@Route(path = YTST_SELECT_TARGET_ACTIVITY)
public class SelectTargetActivity extends BaseActivity<SelectTargetPresenter> implements SelectTargetContract.View {

    @BindView(R2.id.title_tv)
    TextView mTitleTv;
    @BindView(R2.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R2.id.cl_no_content)
    ConstraintLayout mNoContentLayout;
    @BindView(R2.id.btn_sumit)
    MaterialButton mSumitBtn;
    @BindView(R2.id.iv_no_content)
    ImageView mNoContentImg;
    @BindView(R2.id.tv_no_content)
    TextView mNoContentText;


    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.ItemAnimator mItemAnimator;
    @Inject
    RecyclerView.ItemDecoration mItemDecoration;
    @Inject
    List<PonitEntity> mBeanList;
    @Inject
    SelectTargetAdapter mAdapter;
    @Inject
    ImgSearchRequestEntity imgSearchRequestEntity;

    private List<Integer> mChooseBeanList = new ArrayList<>();// 选择的处理
    private String mSessionKey;
    private String mPicPath;
    private ArrayList<UploadPicResponseEntity.DetectResultMapEntity> mPointList;
    private List<String> excludedDataKeys = new ArrayList<>();
    private String mDataType;
    private String mDataKey;
    private String mPosition;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSelectTargetComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .selectTargetModule(new SelectTargetModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_select_target; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mTitleTv.setText(getString(R.string.select_target));
        Intent data = getIntent();
        if (data == null) {
            Log.e(TAG, "intent data is null,please check !");
            return;
        }
        mDataKey = data.getStringExtra(KEY_DATA_KEY);
        if (!TextUtils.isEmpty(mDataKey)) {
            mDataType = data.getStringExtra(KEY_DATA_TYPE);
//            mDataKey = data.getStringExtra(KEY_DATA_KEY);
            mPosition=data.getStringExtra(KEY_POSITION);
            mPicPath=data.getStringExtra(KEY_PICPATH);
            mSessionKey=data.getStringExtra(KEY_SEESION);
            UploadPicResponseEntity.DetectResultMapEntity detectResultMapEntity = new UploadPicResponseEntity.DetectResultMapEntity();
            detectResultMapEntity.setDataKey(mDataKey);
            detectResultMapEntity.setDataType(mDataType);
            detectResultMapEntity.setPosition(mPosition);
            mPointList = new ArrayList<>();
            mPointList.add(detectResultMapEntity);
        } else  {
            mSessionKey = data.getStringExtra(KEY_SEESION);
            mPicPath = data.getStringExtra(KEY_PICPATH);
            mPointList = data.getParcelableArrayListExtra(KEY_POSITION);
        }
        //recyclerview
        mRvContent.setLayoutManager(mLayoutManager);
        ((SimpleItemAnimator) mItemAnimator).setSupportsChangeAnimations(false);
        mRvContent.setItemAnimator(mItemAnimator);
        mRvContent.addItemDecoration(mItemDecoration);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRvContent.setHasFixedSize(true);
        mRvContent.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mChooseBeanList.size() > 0) {
                if (position == mChooseBeanList.get(0)) { //如果同一个位置
                    mBeanList.get(position).setSelected(false);
                    mChooseBeanList.clear();
                } else {
                    mBeanList.get(mChooseBeanList.get(0)).setSelected(false);
                    mBeanList.get(position).setSelected(true);
                    mChooseBeanList.clear();
                    mChooseBeanList.add(position);
                }
            } else {// 第一次点击
                mChooseBeanList.add(position);
                mBeanList.get(mChooseBeanList.get(0)).setSelected(true);
            }
            mAdapter.notifyItemRangeChanged(0, mBeanList.size());
        });

        if (mPointList.size()>0){
            hideOrShowView(false);
            for (int i = 0; i < mPointList.size(); i++) {
                // 只显示DataType 是face 和 VEHICLE 车的
                String dataType = mPointList.get(i).getDataType();
                if (GlobalConstants.FACE_TYPE.equals(dataType) || GlobalConstants.VEHICLE_TYPE.equals(dataType)) {
                    PonitEntity entity = new PonitEntity();
                    entity.setImgPath(getPicUrl(mPicPath, mPointList.get(i).getPosition()));
//                    entity.setPosition(TujieImageUtils.getPicUrl());
                    entity.setSelected(false);
                    entity.setDataKey(mPointList.get(i).getDataKey());
                    entity.setDataType(mPointList.get(i).getDataType());
                    mBeanList.add(entity);
                } else {
                    excludedDataKeys.add(mPointList.get(i).getDataKey());
                }
            }
        }else{
            hideOrShowView(true);
        }
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

    @OnClick({
            R2.id.head_left_iv,
            R2.id.btn_sumit
    })
    public void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.head_left_iv) {
            killMyself();
        } else if (id == R.id.btn_sumit) {
            if (mChooseBeanList.size() > 0) {// 选择了才跳转
                String includeDataKey = null;
                for (int i = 0; i < mBeanList.size(); i++) {
                    if (mBeanList.get(i).isSelected()) {
                        includeDataKey = mBeanList.get(i).getDataKey();
                    } else {
                        excludedDataKeys.add(mBeanList.get(i).getDataKey());
                    }
                }
                imgSearchRequestEntity.setExcludedDataKeys(excludedDataKeys);
                imgSearchRequestEntity.setIncludeDataKey(includeDataKey);
                imgSearchRequestEntity.setSessionKey(mSessionKey);
                Intent mIntent = new Intent(this, SearchResultActivity.class);
                mIntent.putExtra(KEY_IMGSEARCH, imgSearchRequestEntity);
                mIntent.putExtra(KEY_INTENT_PIC, mBeanList.get(mChooseBeanList.get(0)).getImgPath());
                launchActivity(mIntent);
            } else {
                showMessage(getString(R.string.choose_aim));
            }
        }
    }
    private void  hideOrShowView(boolean flag){
        if (flag){
            mSumitBtn.setVisibility(View.GONE);
            mNoContentLayout.setVisibility(View.VISIBLE);
            mRvContent.setVisibility(View.GONE);
            mNoContentImg.setImageResource(R.drawable.ic_no_comparison_result);
            mNoContentText.setText(R.string.not_discern);
        }else {
            mSumitBtn.setVisibility(View.VISIBLE);
            mNoContentLayout.setVisibility(View.GONE);
            mRvContent.setVisibility(View.VISIBLE);
        }
    }
}
