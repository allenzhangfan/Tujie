package com.netposa.component.gzt.mvp.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.core.RouterHub;
import com.netposa.common.log.Log;
import com.netposa.common.utils.SystemUtil;
import com.netposa.component.gzt.R2;
import com.netposa.component.gzt.di.component.DaggerGztComponent;
import com.netposa.component.gzt.di.module.GztModule;
import com.netposa.component.gzt.mvp.contract.GztContract;
import com.netposa.component.gzt.mvp.model.entity.GztEntity;
import com.netposa.component.gzt.mvp.presenter.GztPresenter;
import com.netposa.component.gzt.R;
import com.netposa.component.gzt.mvp.ui.adapter.GztAdapter;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.common.constant.GlobalConstants.REQUEST_CODE_CLCX;
import static com.netposa.common.constant.GlobalConstants.REQUEST_CODE_RLTK;
import static com.netposa.common.constant.GlobalConstants.REQUEST_CODE_SFJB;
import static com.netposa.common.constant.GlobalConstants.REQUEST_CODE_SPJK;
import static com.netposa.common.constant.GlobalConstants.REQUEST_CODE_YTST;

public class GztFragment extends BaseFragment<GztPresenter> implements GztContract.View {

    @BindView(R2.id.gzt_rv)
    RecyclerView mRvGzt;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.ItemAnimator mItemAnimator;
    @Inject
    RecyclerView.ItemDecoration mItemDecoration;
    @Inject
    List<GztEntity> mBeanList;
    @Inject
    GztAdapter mAdapter;
    @Inject
    SystemUtil mSystemUtil;

    public static GztFragment newInstance() {
        GztFragment fragment = new GztFragment();
        return fragment;
    }

    // Required empty public constructor
    public GztFragment() {
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerGztComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .gztModule(new GztModule(getActivity(), this))
                .build()
                .inject(this);
    }

    @Override
    public View initContentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gzt, container, false);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        //recyclerview
        mRvGzt.setLayoutManager(mLayoutManager);
        mRvGzt.setItemAnimator(mItemAnimator);
        mRvGzt.addItemDecoration(mItemDecoration);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRvGzt.setHasFixedSize(true);
        mRvGzt.setAdapter(mAdapter);

        GztEntity entity = new GztEntity();
        entity.setId(R.id.id_spjk);
        entity.setResId(R.drawable.ic_video_surveillance);
        entity.setName(getString(R.string.spjk));
        mBeanList.add(entity);

        entity = new GztEntity();
        entity.setId(R.id.id_ytst);
        entity.setResId(R.drawable.ic_search_img);
        entity.setName(getString(R.string.ytst));
        mBeanList.add(entity);

        entity = new GztEntity();
        entity.setId(R.id.id_sfjb);
        entity.setResId(R.drawable.ic_identification);
        entity.setName(getString(R.string.sfjb));
        mBeanList.add(entity);

        entity = new GztEntity();
        entity.setId(R.id.id_clcx);
        entity.setResId(R.drawable.ic_car_inquiry);
        entity.setName(getString(R.string.clcx));
        mBeanList.add(entity);

        entity = new GztEntity();
        entity.setId(R.id.id_rltk);
        entity.setResId(R.drawable.ic_face_gallery);
        entity.setName(getString(R.string.rltk));
        mBeanList.add(entity);

        mAdapter.setNewData(mBeanList);
        initListener();
    }

    private void initListener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            GztEntity gztEntity = (GztEntity) adapter.getItem(position);
            int id = gztEntity.getId();
            if (id == R.id.id_sfjb) {
                if (!mSystemUtil.isFastDoubleClick()) {
                    requestPermissions(new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA},
                            REQUEST_CODE_SFJB);
                }
            } else if (id == R.id.id_ytst) {
                if (!mSystemUtil.isFastDoubleClick()) {
                    requestPermissions(new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA},
                            REQUEST_CODE_YTST);
                }
            } else if (id == R.id.id_clcx) {
                if (!mSystemUtil.isFastDoubleClick()) {
                    requestPermissions(new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_CLCX);
                }
            } else if (id == R.id.id_spjk) {
                if (!mSystemUtil.isFastDoubleClick()) {
                    requestPermissions(new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_SPJK);
                }
            } else if (id == R.id.id_rltk) {
                if (!mSystemUtil.isFastDoubleClick()) {
                    requestPermissions(new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_RLTK);
                }
            }
        });
    }

    @Override
    public void setData(@Nullable Object data) {

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

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "requestCode:" + requestCode + ",permissions:" +
                Arrays.toString(permissions) + ",grantResults:" +
                Arrays.toString(grantResults));
        if (requestCode == REQUEST_CODE_SPJK
                && grantResults[0] == PERMISSION_GRANTED) {
            ARouter.getInstance().build(RouterHub.SPJK_ACTIVITY).navigation(getActivity());
        } else if (requestCode == REQUEST_CODE_YTST
                && grantResults[0] == PERMISSION_GRANTED
                && grantResults[1] == PERMISSION_GRANTED) {
            ARouter.getInstance().build(RouterHub.YTST_PICTURE_SEARCH_ACTIVITY).navigation(getActivity());
        } else if (requestCode == REQUEST_CODE_SFJB
                && grantResults[0] == PERMISSION_GRANTED
                && grantResults[1] == PERMISSION_GRANTED) {
            ARouter.getInstance().build(RouterHub.CAMERA_FACE_LOGIN_ACTIVITY).navigation(getActivity());
        } else if (requestCode == REQUEST_CODE_CLCX
                && grantResults[0] == PERMISSION_GRANTED) {
            ARouter.getInstance().build(RouterHub.CLCX_QUERY_CAR_ACTIVITY).navigation(getActivity());
        } else if (requestCode == REQUEST_CODE_RLTK
                && grantResults[0] == PERMISSION_GRANTED) {
            ARouter.getInstance().build(RouterHub.RLTK_FACE_LIBRARY_ACTIVITY).navigation(getActivity());
        }
    }
}
