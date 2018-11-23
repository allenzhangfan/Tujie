package com.netposa.component.jq.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.schedulers.Schedulers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.log.Log;
import com.netposa.component.jq.R2;
import com.netposa.component.jq.di.component.DaggerJqComponent;
import com.netposa.component.jq.di.module.JqModule;
import com.netposa.component.jq.mvp.contract.JqContract;
import com.netposa.component.jq.mvp.model.entity.JqItemEntity;
import com.netposa.component.jq.mvp.presenter.JqPresenter;
import com.netposa.component.jq.R;
import com.netposa.component.jq.mvp.ui.activity.AlarmDetailsActivity;
import com.netposa.component.jq.mvp.ui.activity.JqSearchActivity;
import com.netposa.component.jq.mvp.ui.adapter.JqAdapter;
import com.netposa.component.room.dao.DbHelper;
import com.netposa.component.room.entity.JqSearchHistoryEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.component.jq.app.JqConstants.KEY_JQ_ITEM;
import static com.netposa.component.jq.mvp.model.entity.JqItemEntity.TYPE_CAR;
import static com.netposa.component.jq.mvp.model.entity.JqItemEntity.TYPE_INVALID;
import static com.netposa.component.jq.mvp.model.entity.JqItemEntity.TYPE_PEOPLE;
import static com.netposa.component.jq.mvp.model.entity.JqItemEntity.TYPE_SUSPENDING;
import static com.netposa.component.jq.mvp.model.entity.JqItemEntity.TYPE_VALID;

public class JqFragment extends BaseFragment<JqPresenter> implements JqContract.View {

    @BindView(R2.id.title_tv)
    TextView mTVtTitle;
    @BindView(R2.id.head_left_iv)
    ImageView mIvHeadLeft;
    @BindView(R2.id.head_right_iv)
    ImageView mIvHeadRight;
    @BindView(R2.id.rv_content)
    RecyclerView mRvContent;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.ItemAnimator mItemAnimator;
    @Inject
    List<JqItemEntity> mBeanList;
    @Inject
    JqAdapter mAdapter;

    public static JqFragment newInstance() {
        JqFragment fragment = new JqFragment();
        return fragment;
    }

    // Required empty public constructor
    public JqFragment() {
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerJqComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .jqModule(new JqModule(getActivity(), this))
                .build()
                .inject(this);
    }

    @Override
    public View initContentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_jq, container, false);
    }

    int count = 0;

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mTVtTitle.setText(R.string.jq);
        mIvHeadLeft.setVisibility(View.GONE);
        mIvHeadRight.setVisibility(View.VISIBLE);
        mIvHeadRight.setImageResource(R.drawable.ic_search);
        //recyclerview
        mRvContent.setLayoutManager(mLayoutManager);
        mRvContent.setItemAnimator(mItemAnimator);
        mRvContent.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) ->
                launchActivity(new Intent(getActivity(), AlarmDetailsActivity.class)));

        //TODO test data here
        for (int i = 0; i < 3; i++) {
            JqItemEntity entity2 = new JqItemEntity();
            entity2.setItemHandleType(TYPE_SUSPENDING);
            entity2.setItemType(TYPE_CAR);
            entity2.setCarNumber("陕A 11L001");
            entity2.setAddress("武汉市2月7日城市车辆布控");
            entity2.setTime("2018-10-11 12:31:33");
            entity2.setCameraLocation("保利国际中心1楼正门摄像机");
            mBeanList.add(entity2);

            JqItemEntity entity3 = new JqItemEntity();
            entity3.setItemHandleType(TYPE_VALID);
            entity3.setItemType(TYPE_CAR);
            entity3.setCarNumber("陕A 11L002");
            entity3.setAddress("武汉市2月8日城市车辆布控");
            entity3.setTime("2018-10-11 12:31:33");
            entity3.setCameraLocation("保利国际中心1楼正门摄像机");
            mBeanList.add(entity3);

            JqItemEntity entity4 = new JqItemEntity();
            entity4.setItemHandleType(TYPE_INVALID);
            entity4.setItemType(TYPE_CAR);
            entity4.setCarNumber("陕A 11L003");
            entity4.setAddress("武汉市2月9日城市车辆布控");
            entity4.setTime("2018-10-11 12:31:33");
            entity4.setCameraLocation("保利国际中心1楼正门摄像机");
            mBeanList.add(entity4);

            JqItemEntity entity1 = new JqItemEntity();
            entity1.setItemType(TYPE_PEOPLE);
            entity1.setItemHandleType(TYPE_INVALID);
            entity1.setAddress("武汉市2月6日城市人脸布控");
            entity1.setTime("2018-10-11 12:31:33");
            entity1.setCameraLocation("保利国际中心1楼正门摄像机");
            entity1.setSimilarity(94);
            mBeanList.add(entity1);

            JqItemEntity entity5 = new JqItemEntity();
            entity5.setItemType(TYPE_PEOPLE);
            entity5.setItemHandleType(TYPE_VALID);
            entity5.setAddress("武汉市2月7日城市人脸布控");
            entity5.setTime("2018-10-11 12:31:33");
            entity5.setCameraLocation("保利国际中心1楼正门摄像机");
            entity5.setSimilarity(94);
            mBeanList.add(entity5);

            JqItemEntity entity6 = new JqItemEntity();
            entity6.setItemType(TYPE_PEOPLE);
            entity6.setItemHandleType(TYPE_SUSPENDING);
            entity6.setAddress("武汉市2月8日城市人脸布控");
            entity6.setTime("2018-10-11 12:31:33");
            entity6.setCameraLocation("保利国际中心1楼正门摄像机");
            entity6.setSimilarity(94);
            mBeanList.add(entity6);
        }

        //TODO test data here
        List<JqSearchHistoryEntity> historyEntityList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            JqSearchHistoryEntity entity = new JqSearchHistoryEntity();
            entity.setName("宫本武藏:" + i);
            historyEntityList.add(entity);
        }

        if (count == 0) {
            DbHelper
                    .getInstance()
                    .insertAllSearchHistories(historyEntityList)
                    .subscribeOn(Schedulers.io())
                    .subscribe();
        }
        count++;

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            JqItemEntity jqItemEntity = mBeanList.get(position);
            Intent intent = new Intent(getActivity(), AlarmDetailsActivity.class);
            intent.putExtra(KEY_JQ_ITEM, jqItemEntity);
            launchActivity(intent);
        });
    }

    /**
     * 通过此方法可以使 Fragment 能够与外界做一些交互和通信, 比如说外部的 Activity 想让自己持有的某个 Fragment 对象执行一些方法,
     * 建议在有多个需要与外界交互的方法时, 统一传 {@link Message}, 通过 what 字段来区分不同的方法, 在 {@link #setData(Object)}
     * 方法中就可以 {@code switch} 做不同的操作, 这样就可以用统一的入口方法做多个不同的操作, 可以起到分发的作用
     * <p>
     * 调用此方法时请注意调用时 Fragment 的生命周期, 如果调用 {@link #setData(Object)} 方法时 {@link Fragment#onCreate(Bundle)} 还没执行
     * 但在 {@link #setData(Object)} 里却调用了 Presenter 的方法, 是会报空的, 因为 Dagger 注入是在 {@link Fragment#onCreate(Bundle)} 方法中执行的
     * 然后才创建的 Presenter, 如果要做一些初始化操作,可以不必让外部调用 {@link #setData(Object)}, 在 {@link #initView(Bundle)} 中初始化就可以了
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

    @OnClick({R2.id.head_right_iv})
    public void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.head_right_iv) {
            if (isFastDoubleClick()) {
                Log.d(TAG, "onViewClick :double click");
            } else {
                launchActivity(new Intent(getActivity(), JqSearchActivity.class));
            }
        }
    }

    //判断是否是快速点击(双击)，保证多次点击只响应一次点击事件
    private long lastClickTime = 0L; //上一次点击的时间

    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (timeD < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
