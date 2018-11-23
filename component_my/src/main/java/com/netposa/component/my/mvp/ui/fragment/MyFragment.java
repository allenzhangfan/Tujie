package com.netposa.component.my.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
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
    private boolean mIsOpen;

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
                .myModule(new MyModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initContentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mIsOpen=SPUtils.getInstance().getBoolean(GlobalConstants.HAS_FACE, false);
        MenuEntity menuEntity = new MenuEntity(R.id.menu_face);
        menuEntity.setType(MenuEntity.TYPE_TOGGLE);
        menuEntity.setResId(R.drawable.ic_face);
        menuEntity.setTitle(getString(R.string.face_login));
        menuEntity.setOnCheckedChangeListener(this);
        if(mIsOpen){
            menuEntity.setChecked(true);
        }else {
            menuEntity.setChecked(false);
        }
        mBeanList.add(menuEntity);

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
                launchActivity(new Intent(getActivity(), AboutActivity.class));
            } else if (entity.getId() == R.id.menu_copy_db) {
                if (mPresenter != null) {
                    mPresenter.copyDbToSdCard(getActivity());
                }
            }
        });

//        String username = SPUtils.getInstance().getString(GlobalConstants.USER_NAME);
//        mNameTv.setText(username);
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.i(TAG, "onCheckedChanged: isChecked = " + isChecked);
        SPUtils.getInstance().put(GlobalConstants.HAS_FACE, isChecked);
    }

    @OnClick({R2.id.rl_go})
    public void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.rl_go) {
            launchActivity(new Intent(getActivity(), PersonInfoActivity.class));
        }
    }
}
