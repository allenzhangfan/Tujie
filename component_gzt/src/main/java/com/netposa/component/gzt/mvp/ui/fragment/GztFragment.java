package com.netposa.component.gzt.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

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
import com.netposa.common.utils.ToastUtils;
import com.netposa.component.gzt.R2;
import com.netposa.component.gzt.di.component.DaggerGztComponent;
import com.netposa.component.gzt.di.module.GztModule;
import com.netposa.component.gzt.mvp.contract.GztContract;
import com.netposa.component.gzt.mvp.model.entity.GztEntity;
import com.netposa.component.gzt.mvp.presenter.GztPresenter;
import com.netposa.component.gzt.R;
import com.netposa.component.gzt.mvp.ui.adapter.GztAdapter;

import java.util.List;

import javax.inject.Inject;

import static com.jess.arms.utils.Preconditions.checkNotNull;

public class GztFragment extends BaseFragment<GztPresenter> implements GztContract.View {

    @BindView(R2.id.gzt_rv)
    RecyclerView mRvGzt;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.ItemAnimator mItemAnimator;
    @Inject
    List<GztEntity> mBeanList;
    @Inject
    GztAdapter mAdapter;

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
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRvGzt.setHasFixedSize(true);
        mRvGzt.setAdapter(mAdapter);

        GztEntity entity = new GztEntity();
        entity.setId(R.id.id_sfjb);
        entity.setResId(R.drawable.ic_identification);
        entity.setName(getString(R.string.sfjb));
        mBeanList.add(entity);

        entity = new GztEntity();
        entity.setId(R.id.id_ytst);
        entity.setResId(R.drawable.ic_search_img);
        entity.setName(getString(R.string.ytst));
        mBeanList.add(entity);

        entity = new GztEntity();
        entity.setId(R.id.id_clcx);
        entity.setResId(R.drawable.ic_car_inquiry);
        entity.setName(getString(R.string.clcx));
        mBeanList.add(entity);

        entity = new GztEntity();
        entity.setId(R.id.id_spjk);
        entity.setResId(R.drawable.ic_video_surveillance);
        entity.setName(getString(R.string.spjk));
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
                ToastUtils.showShort(R.string.sfjb);
             //   ARouter.getInstance().build(RouterHub.SFJB_GET_IMAGE_ACTIVITY).navigation(getActivity());
                ARouter.getInstance().build(RouterHub.SFJB_CHOSE_LIB_ACTIVITY).navigation(getActivity());
            } else if (id == R.id.id_ytst) {
                ToastUtils.showShort(R.string.ytst);
            } else if (id == R.id.id_clcx) {
                ARouter.getInstance().build(RouterHub.CLCX_QUERY_CAR_ACTIVITY).navigation(getActivity());
            } else if (id == R.id.id_spjk) {
                ARouter.getInstance().build(RouterHub.SPJK_ACTIVITY).navigation(getActivity());
//                ARouter.getInstance().build(RouterHub.HISTORY_VIDEO_ACTIVITY).navigation(getActivity());
            } else if (id == R.id.id_rltk) {
                ToastUtils.showShort(R.string.rltk);
            }
        });
    }

    /**
     * 通过此方法可以使 Fragment 能够与外界做一些交互和通信, 比如说外部的 Activity 想让自己持有的某个 Fragment 对象执行一些方法,
     * 建议在有多个需要与外界交互的方法时, 统一传 {@link Message}, 通过 what 字段来区分不同的方法, 在 {@link #setData(Object)}
     * 方法中就可以 {@code switch} 做不同的操作, 这样就可以用统一的入口方法做多个不同的操作, 可以起到分发的作用
     * <p>
     * 调用此方法时请注意调用时 Fragment 的生命周期, 如果调用 {@link #setData(Object)} 方法时 {@link Fragment#onCreate(Bundle)} 还没执行
     * 但在 {@link #setData(Object)} 里却调用了 Presenter 的方法, 是会报空的, 因为 Dagger 注入是在 {@link Fragment#onCreate(Bundle)} 方法中执行的
     * 然后才创建的 Presenter, 如果要做一些初始化操作,可以不必让外部调用 {@link #setData(Object)}, 在 {@link #initData(Bundle)} 中初始化就可以了
     * <p>
     * Example usage:
     * <pre>
     * public void setData(@Nullable Object data) {
     *     if (data != null && data instanceof Message) {
     *         switch (((Message) data).what) {
     *             case 0:
     *                 loadData(((Message) data).arg1);
     *                 break;
     *             case 1:
     *                 refreshUI();
     *                 break;
     *             default:
     *                 //do something
     *                 break;
     *         }
     *     }
     * }
     *
     * // call setData(Object):
     * Message data = new Message();
     * data.what = 0;
     * data.arg1 = 1;
     * fragment.setData(data);
     * </pre>
     *
     * @param data 当不需要参数时 {@code data} 可以为 {@code null}
     */
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
}
