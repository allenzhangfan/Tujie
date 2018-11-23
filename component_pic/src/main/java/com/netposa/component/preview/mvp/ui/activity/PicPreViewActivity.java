package com.netposa.component.preview.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.core.RouterHub;
import com.netposa.component.pic.R;
import com.netposa.component.pic.R2;
import com.netposa.component.preview.di.component.DaggerPicPreViewActivityComponent;
import com.netposa.component.preview.di.module.PicPreViewActivityModule;
import com.netposa.component.preview.mvp.contract.PicPreViewActivityContract;
import com.netposa.component.preview.mvp.presenter.PicPreViewActivityPresenter;
import com.netposa.component.preview.mvp.ui.adapter.PicPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

@Route(path = RouterHub.PIC_PREVIEW_ACTIVITY)
public class PicPreViewActivity extends BaseActivity<PicPreViewActivityPresenter> implements PicPreViewActivityContract.View, ViewPager.OnPageChangeListener {

    public static final String KEY_PIC_SELECTED_INDEX = "pic_selected_index";
    public static final String KEY_PIC_SELECTED_PATH_LIST = "pic_selected_path_list";

    @BindView(R2.id.viewpager)
    ViewPager mViewPager;
    @BindView(R2.id.head_right_iv)
    ImageView mIvHeadRight;
    @BindView(R2.id.head_left_iv)
    ImageView mIvHeadLeft;
    @BindView(R2.id.title_tv)
    TextView mTVtTitle;
    @BindView(R2.id.ll_title)
    LinearLayout mLlTtitle;

    @Inject
    PicPagerAdapter mPicPagerAdapter;
    @Inject
    List<View> mViewList;

    private ImageLoader mImageLoader;
    private int mPosition;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPicPreViewActivityComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .picPreViewActivityModule(new PicPreViewActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_pic_pre_view; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle bundle) {
        mImmersionBar.statusBarColor(R.color.black)
                .statusBarDarkFont(false, 0.2f)
                .init();
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mLlTtitle.setBackgroundColor(getResources().getColor(R.color.black));
        mIvHeadLeft.setImageResource(R.drawable.ic_back_white);
        mViewPager.setAdapter(mPicPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        //必须从Intent中去获取Bundle
        int index = extras.getInt(KEY_PIC_SELECTED_INDEX, 0);
        ArrayList<String> pathList = extras.getStringArrayList(KEY_PIC_SELECTED_PATH_LIST);
        mImageLoader = ArmsUtils.obtainAppComponentFromContext(this).imageLoader();
        mPosition = index;
        if (pathList != null && !pathList.isEmpty()) {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            for (int i = 0; i < pathList.size(); i++) {
                View view = layoutInflater.inflate(R.layout.layout_pic_preview, null);
                ImageView mIvContent = (ImageView) view.findViewById(R.id.iv_content);
                mImageLoader.loadImage(this, ImageConfigImpl
                        .builder()
                        .cacheStrategy(0)
                        .placeholder(R.drawable.ic_image_default)
                        .url(pathList.get(i))
                        .imageView(mIvContent)
                        .build());
                mViewList.add(view);
            }
            setTtitle(mPosition + 1);
            mPicPagerAdapter.setViews(mViewList);
            mViewPager.setCurrentItem(mPosition);
        }
    }

    private void setTtitle(int index) {
        StringBuilder sb = new StringBuilder();
        sb.append(index)
                .append("/")
                .append(mViewList.size())
                .toString();
        mTVtTitle.setTextColor(getResources().getColor(R.color.white));
        mTVtTitle.setText(sb);
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

    @OnClick({R2.id.head_right_iv, R2.id.head_left_iv})
    public void onClickView(View v) {
        int id = v.getId();
        if (id == R.id.head_right_iv) {
            int currentItem = mViewPager.getCurrentItem();
            if (mViewList.size() == 2) {
                mIvHeadRight.setVisibility(View.GONE);
            }
            mViewList.remove(currentItem);
            setTtitle(mPosition + 1);
            mPicPagerAdapter.setViews(mViewList);
        } else if (id == R.id.head_left_iv) {
            super.onBackPressed();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setTtitle(position + 1);
        mPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
