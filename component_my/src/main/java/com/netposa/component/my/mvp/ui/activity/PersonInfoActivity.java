package com.netposa.component.my.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.AppManager;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.core.RouterHub;
import com.netposa.commonres.modle.LoadingDialog;
import com.netposa.commonres.widget.Dialog.SweetDialog;
import com.netposa.component.my.R2;
import com.netposa.component.my.di.component.DaggerPersonInfoComponent;
import com.netposa.component.my.di.module.PersonInfoModule;
import com.netposa.component.my.mvp.contract.PersonInfoContract;
import com.netposa.component.my.mvp.model.entity.PersonInfoEntity;
import com.netposa.component.my.mvp.model.entity.PersonInfoDividerEntity;
import com.netposa.component.my.mvp.presenter.PersonInfoPresenter;
import com.netposa.component.my.R;
import com.netposa.component.my.mvp.ui.adapter.PersonInfoAdapter;

import java.util.List;

import javax.inject.Inject;

import static com.jess.arms.utils.Preconditions.checkNotNull;

public class PersonInfoActivity extends BaseActivity<PersonInfoPresenter> implements PersonInfoContract.View {

    @BindView(R2.id.title_tv)
    TextView mTVtTitle;
    @BindView(R2.id.rv_person_info)
    RecyclerView mRvPersonInfo;

    @Inject
    LoadingDialog mLoadingDialog;
    @Inject
    SweetDialog mSweetDialog;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.ItemAnimator mItemAnimator;
    @Inject
    List<MultiItemEntity> mBeanList;
    @Inject
    PersonInfoAdapter mPersonInfoAdapter;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPersonInfoComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .personInfoModule(new PersonInfoModule(this,this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_person_info; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mTVtTitle.setText(R.string.person_information);
        mRvPersonInfo.setLayoutManager(mLayoutManager);
        mRvPersonInfo.setItemAnimator(mItemAnimator);
        mRvPersonInfo.setAdapter(mPersonInfoAdapter);

        //TODO test data here
        PersonInfoDividerEntity entity1 = new PersonInfoDividerEntity();
        mBeanList.add(entity1);

        PersonInfoEntity entity2 = new PersonInfoEntity();
        entity2.setDividerVisable(true);
        entity2.setTitle(getString(R.string.user_name));
        entity2.setDescription("jingyuan2018");
        mBeanList.add(entity2);

        PersonInfoEntity entity3 = new PersonInfoEntity();
        entity3.setDividerVisable(true);
        entity3.setTitle(getString(R.string.name));
        entity3.setDescription("屈宏盛");
        mBeanList.add(entity3);

        PersonInfoEntity entity4 = new PersonInfoEntity();
        entity4.setDividerVisable(true);
        entity4.setTitle(getString(R.string.sex));
        entity4.setDescription("男");
        mBeanList.add(entity4);

        PersonInfoEntity entity5 = new PersonInfoEntity();
        entity5.setDividerVisable(false);
        entity5.setTitle(getString(R.string.organization_related));
        entity5.setDescription("南昌市青山区派出所");
        mBeanList.add(entity5);

        PersonInfoDividerEntity entity6 = new PersonInfoDividerEntity();
        mBeanList.add(entity6);

        PersonInfoEntity entity7 = new PersonInfoEntity();
        entity7.setDividerVisable(true);
        entity7.setTitle(getString(R.string.police_number));
        entity7.setDescription("232313411113");
        mBeanList.add(entity7);

        PersonInfoEntity entity8 = new PersonInfoEntity();
        entity8.setDividerVisable(false);
        entity8.setTitle(getString(R.string.phone_number));
        entity8.setDescription("154122414515");
        mBeanList.add(entity8);
    }

    @Override
    public void showLoading(String message) {
        mLoadingDialog.show();
    }

    @Override
    public void hideLoading() {
        mLoadingDialog.dismiss();
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

    @OnClick({R2.id.head_left_iv,R2.id.btn_logout})
    public void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.head_left_iv) {
            killMyself();
        } else if (id == R.id.btn_logout) {
            logout();
        }
    }

    /**
     * 登出界面
     */
    private void logout() {
        mSweetDialog.setTitle(getString(R.string.sure_to_logout));
        mSweetDialog.setPositive(getString(R.string.logout));
        mSweetDialog.setPositiveListener(v -> {
            mSweetDialog.dismiss();
            mPresenter.logOut();
        });
        mSweetDialog.setNegativeListener(null);
        mSweetDialog.show();
    }

    @Override
    public void logOutSuccess() {
        exitToLogin();
    }

    @Override
    public void logOutFail() {
        exitToLogin();
    }

    private void exitToLogin(){
        // 退出登录时，下次不再自动登录
        AppManager.getAppManager().killAll("LoginActivity");
        ARouter.getInstance().build(RouterHub.LOGIN_LOGIN_ACTIVITY).navigation(this);
    }
}
