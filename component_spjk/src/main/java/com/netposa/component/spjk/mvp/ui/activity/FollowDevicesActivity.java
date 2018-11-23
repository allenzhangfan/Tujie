package com.netposa.component.spjk.mvp.ui.activity;

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
import com.netposa.component.room.entity.SpjkCollectionDeviceEntiry;
import com.netposa.component.spjk.R2;
import com.netposa.component.spjk.di.component.DaggerFollowDevicesComponent;
import com.netposa.component.spjk.di.module.FollowDevicesModule;
import com.netposa.component.spjk.mvp.contract.FollowDevicesContract;
import com.netposa.component.spjk.mvp.presenter.FollowDevicesPresenter;
import com.netposa.component.spjk.R;
import com.netposa.component.spjk.mvp.ui.adapter.FollowDevicesAdapter;

import java.util.List;
import javax.inject.Inject;
import static com.jess.arms.utils.Preconditions.checkNotNull;

public class FollowDevicesActivity extends BaseActivity<FollowDevicesPresenter> implements FollowDevicesContract.View {

    @BindView(R2.id.title_tv)
    TextView mTVtTitle;
    @BindView(R2.id.rv_content)
    RecyclerView mRvContent;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.ItemAnimator mItemAnimator;
    @Inject
    FollowDevicesAdapter mAdapter;
    @Inject
    List<SpjkCollectionDeviceEntiry> mBeanList;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerFollowDevicesComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .followDevicesModule(new FollowDevicesModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_follow_devices; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mTVtTitle.setText(R.string.subscribe);
        //recyclerview
        mRvContent.setLayoutManager(mLayoutManager);
        mRvContent.setItemAnimator(mItemAnimator);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRvContent.setHasFixedSize(true);
        mRvContent.setAdapter(mAdapter);
        mPresenter.getAll();

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
            R2.id.head_left_iv})
    public void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.head_left_iv) {
            killMyself();
        }
    }

    @Override
    public void loadDataSuccess(List<SpjkCollectionDeviceEntiry> response) {
        Log.e(TAG, "loadDataSuccess :" + response.toString());
        mAdapter.setNewData(response);
    }

    @Override
    public void loadDataFailed() {

    }
}
