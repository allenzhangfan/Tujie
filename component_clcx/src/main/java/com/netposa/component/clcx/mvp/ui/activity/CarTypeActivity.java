package com.netposa.component.clcx.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.log.Log;
import com.netposa.common.utils.ToastUtils;
import com.netposa.component.clcx.R2;
import com.netposa.component.clcx.di.component.DaggerCarTypeComponent;
import com.netposa.component.clcx.di.module.CarTypeModule;
import com.netposa.component.clcx.mvp.contract.CarTypeContract;
import com.netposa.component.clcx.mvp.model.entity.CarTypeEntry;
import com.netposa.component.clcx.mvp.presenter.CarTypePresenter;
import com.netposa.component.clcx.R;
import com.netposa.component.clcx.mvp.ui.adapter.CarTypeAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_LIST_TYPE;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_QUANBU;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_SELECT_RESULT;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_SINGLE_TYPE;
import static com.netposa.component.clcx.app.ClcxConstants.REQUESTCODE_CAR_PLATE;
import static com.netposa.component.clcx.app.ClcxConstants.REQUESTCODE_CAR_TYPE;
import static com.netposa.component.clcx.app.ClcxConstants.mType_car;
import static com.netposa.component.clcx.app.ClcxConstants.mType_plate;

public class CarTypeActivity extends BaseActivity<CarTypePresenter> implements CarTypeContract.View {

    @BindView(R2.id.recyclerview)
    RecyclerView mRecyclerview;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.ItemAnimator mItemAnimator;
    @BindView(R2.id.title_tv)
    TextView mTitle_txt;

    @Inject
    CarTypeAdapter mAdapter;
    @Inject
    ArrayList<CarTypeEntry> mAdapterList;
    @Inject
    List<String> mSelectList;

    private boolean isSelectAll = false;
    private String mType;//类型
    private String[] source;//类型资源
    private int countIndex = 1;//默认初始为1

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCarTypeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .carTypeModule(new CarTypeModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_car_type; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        Intent data = getIntent();
        if (data == null) {
            Log.i(TAG, "intent data is null,please check !");
            return;
        }
        mType = data.getStringExtra(KEY_SINGLE_TYPE);
        mAdapterList= data.getParcelableArrayListExtra(KEY_LIST_TYPE);
        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setItemAnimator(mItemAnimator);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setAdapter(mAdapter);
        if (mAdapterList.size()>0){
            if (mType.equals(mType_plate)) {
                mTitle_txt.setText(this.getString(R.string.plate_type));
            }
            if (mType.equals(mType_car)) {
                mTitle_txt.setText(this.getString(R.string.car_type));
            }
            for (int i=0;i<mAdapterList.size();i++){
                if (mAdapterList.get(i).isSelect){
                    mSelectList.add(mAdapterList.get(i).getTitle());
                }
            }
            mAdapter.addData(mAdapterList);
        }else {
            if (mType.equals(mType_plate)) {
                source = getResources().getStringArray(R.array.plate_type_item);
                mTitle_txt.setText(this.getString(R.string.plate_type));
            }
            if (mType.equals(mType_car)) {
                source = getResources().getStringArray(R.array.car_type_item);
                mTitle_txt.setText(this.getString(R.string.car_type));
            }
            initData();
        }
        mTitle_txt.getPaint().setFakeBoldText(true);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            // 如果点击的第一个就是全选 和全部取消
            if (position == 0) {
                selectAll();
                return;
            }
            CarTypeEntry entity = mAdapterList.get(position);
            boolean isSelect = entity.isSelect;
            if (!isSelect) {
                countIndex++;
                entity.setSelect(true);
                mSelectList.add(entity.getTitle());
                if (countIndex == mAdapterList.size()) {
                    mAdapterList.get(0).setSelect(true);
                    isSelectAll = true;
                }
            } else {
                countIndex--;
                entity.setSelect(false);
                isSelectAll = false;
                mSelectList.remove(entity.getTitle());
                mAdapterList.get(0).setSelect(false);
            }
            mAdapter.notifyDataSetChanged();
        });
    }

    /**
     * 全选和反选
     */
    private void selectAll() {
        if (mAdapter == null) return;
        mSelectList.clear();
        if (!isSelectAll) { // 全选
            for (int i = 0; i < mAdapterList.size(); i++) {
                mAdapterList.get(i).setSelect(true);
                mSelectList.add(mAdapterList.get(i).getTitle());
            }
            countIndex = mAdapterList.size();
            isSelectAll = true;
        } else { // 取消全选
            for (int i = 0; i < mAdapterList.size(); i++) {
                mAdapterList.get(i).setSelect(false);
            }
            countIndex = 1;
            isSelectAll = false;
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initData() {
        for (int i = 0; i < source.length; i++) {
            CarTypeEntry myLiveList = new CarTypeEntry();
            myLiveList.setTitle(source[i]);
            myLiveList.setSelect(false);
            mAdapterList.add(myLiveList);
        }
        mAdapter.addData(mAdapterList);
    }

    @OnClick({R2.id.head_left_iv,
            R2.id.btn_sure})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.head_left_iv) {
            killMyself();
        } else if (id == R.id.btn_sure) {
            if (mSelectList.size()>0){
                goBackQueryCarActivity(mType);
            }else {
                // 没有做选择提示
                if (mType.equals(mType_plate)){
                    showMessage(getString(R.string.choose_plate_type));
                }else if (mType.equals(mType_car)){
                    showMessage(getString(R.string.choose_car_type));
                }
            }
        }
    }

    private void goBackQueryCarActivity(String type) {
        Intent intent = new Intent(this, QueryCarActivity.class);
        intent.putParcelableArrayListExtra(KEY_SELECT_RESULT, mAdapterList);
        if (type.equals(mType_plate)) {
            this.setResult(REQUESTCODE_CAR_PLATE, intent);
        } else if (type.equals(mType_car)) {
            this.setResult(REQUESTCODE_CAR_TYPE, intent);
        }
        killMyself();
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
}
