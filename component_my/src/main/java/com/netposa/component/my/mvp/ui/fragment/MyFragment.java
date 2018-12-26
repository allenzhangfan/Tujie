package com.netposa.component.my.mvp.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DeviceUtils;
import com.netposa.common.constant.GlobalConstants;
import com.netposa.common.log.Log;
import com.netposa.common.utils.SPUtils;
import com.netposa.component.my.BuildConfig;
import com.netposa.component.my.R2;
import com.netposa.component.my.di.component.DaggerMyComponent;
import com.netposa.component.my.di.module.MyModule;
import com.netposa.component.my.mvp.contract.MyContract;
import com.netposa.component.my.mvp.model.entity.MenuEntity;
import com.netposa.component.my.mvp.presenter.MyPresenter;
import com.netposa.component.my.R;
import com.netposa.component.my.mvp.ui.activity.AboutActivity;
import com.netposa.component.my.mvp.ui.activity.PersonInfoActivity;
import com.netposa.component.my.mvp.ui.adapter.MyMenuAdapter;
import java.util.List;

import javax.inject.Inject;

import static com.jess.arms.utils.Preconditions.checkNotNull;

@Keep
public class MyFragment extends BaseFragment<MyPresenter> implements MyContract.View, CompoundButton.OnCheckedChangeListener {

    @BindView(R2.id.tv_name)
    TextView mNameTv;
    @BindView(R2.id.menu_lv)
    ListView mMenuLv;

    @Inject
    List<MenuEntity> mBeanList;
    @Inject
    MyMenuAdapter mAdapter;
    @Inject
    RxErrorHandler mErrorHandler;

    private boolean mIsOpen;
    private Context mContext;

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    // Required empty public constructor
    public MyFragment() {
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerMyComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .myModule(new MyModule(getActivity(), this))
                .build()
                .inject(this);
    }

    @Override
    public View initContentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mIsOpen = SPUtils.getInstance().getBoolean(GlobalConstants.HAS_FACE, false);
        MenuEntity menuEntity = new MenuEntity(R.id.menu_face);
        menuEntity.setType(MenuEntity.TYPE_TOGGLE);
        menuEntity.setResId(R.drawable.ic_face);
        menuEntity.setTitle(getString(R.string.face_login));
        menuEntity.setOnCheckedChangeListener(this);
        if (mIsOpen) {
            menuEntity.setChecked(true);
        } else {
            menuEntity.setChecked(false);
        }
        mBeanList.add(menuEntity);
        mPresenter.getCacheSize();

        menuEntity = new MenuEntity(R.id.menu_clean);
        menuEntity.setType(MenuEntity.TYPE_TEXT);
        menuEntity.setResId(R.drawable.ic_clean);
        menuEntity.setTitle(getString(R.string.clear_cache));
        menuEntity.setValue("0.0M");
        mBeanList.add(menuEntity);

        menuEntity = new MenuEntity(R.id.menu_about);
        menuEntity.setType(MenuEntity.TYPE_DEFAULT);
        menuEntity.setResId(R.drawable.ic_about);
        menuEntity.setTitle(getString(R.string.about));
        mBeanList.add(menuEntity);

        if (BuildConfig.DEBUG) {
            menuEntity = new MenuEntity(R.id.menu_copy_db);
            menuEntity.setType(MenuEntity.TYPE_DEFAULT);
            menuEntity.setResId(R.drawable.ic_clean);
            menuEntity.setTitle("拷贝数据库");
            mBeanList.add(menuEntity);
        }
        mMenuLv.setAdapter(mAdapter);
        mMenuLv.setOnItemClickListener((parent, view, position, id) -> {
            MenuEntity entity = (MenuEntity) mAdapter.getItem(position);
            if (entity.getId() == R.id.menu_about) {
                launchActivity(new Intent(mContext, AboutActivity.class));
            } else if (entity.getId() == R.id.menu_copy_db) {
                if (mPresenter != null) {
                    mPresenter.copyDbToSdCard(mContext);
                }
            } else if (entity.getId() == R.id.menu_clean) {
                mPresenter.clearCache();
            }
        });
        String username = SPUtils.getInstance().getString(GlobalConstants.CONFIG_LAST_USER_NICKNAME);
        mNameTv.setText(username);
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.i(TAG, "onCheckedChanged: isChecked = " + isChecked);
        SPUtils.getInstance().put(GlobalConstants.HAS_FACE, isChecked);
    }

    @OnClick({R2.id.rl_go})
    public void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.rl_go) {
            launchActivity(new Intent(mContext, PersonInfoActivity.class));
        }
    }
}
