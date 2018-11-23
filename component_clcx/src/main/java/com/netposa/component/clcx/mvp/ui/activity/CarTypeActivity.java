package com.netposa.component.clcx.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.netposa.component.clcx.R2;
import com.netposa.component.clcx.di.component.DaggerCarTypeComponent;
import com.netposa.component.clcx.di.module.CarTypeModule;
import com.netposa.component.clcx.mvp.contract.CarTypeContract;
import com.netposa.component.clcx.mvp.model.entity.CarTypeEntry;
import com.netposa.component.clcx.mvp.presenter.CarTypePresenter;
import com.netposa.component.clcx.R;
import com.netposa.component.clcx.mvp.ui.adapter.CarTypeAdapter;

import java.util.List;

import javax.inject.Inject;

import static com.jess.arms.utils.Preconditions.checkNotNull;

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
    List<CarTypeEntry>  mAdapterList;

    private boolean isSelectAll = false;
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
        mTitle_txt.setText(this.getString(R.string.car_type));
        mTitle_txt.getPaint().setFakeBoldText(true);
        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setItemAnimator(mItemAnimator);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setAdapter(mAdapter);
        initData();
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            // 如果点击的第一个就是全选 和全部取消
            Log.e("postion=", position + "");
            if (position == 0) {
                selectAll();
                return;
            }
        });
    }
    /**
     * 全选和反选
     */
    private void selectAll() {
        if (mAdapter == null) return;
        List<CarTypeEntry> adapterList=mAdapter.getData();
        if (!isSelectAll) { // 全选
//            mSelectList.clear();
            for (int i = 0; i<adapterList.size(); i++) {
                adapterList.get(i).setSelect(true);
//                mSelectList.add(adapterList.get(i).getTitle());
            }
            isSelectAll = true;
//            mSelectList.remove(0);
        } else { // 取消全选
            for (int i = 0; i < adapterList.size();  i++) {
                mAdapter.getData().get(i).setSelect(false);
            }
            isSelectAll = false;
//            mSelectList.clear();
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initData() {
        String[] source = getResources().getStringArray(R.array.car_type_item);
        for (int i = 0; i < source.length; i++) {
            CarTypeEntry myLiveList = new CarTypeEntry();
            myLiveList.setTitle(source[i]);
            mAdapterList.add(myLiveList);
        }
    }

    @OnClick({R2.id.head_left_iv})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.head_left_iv) {
            killMyself();
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
}
