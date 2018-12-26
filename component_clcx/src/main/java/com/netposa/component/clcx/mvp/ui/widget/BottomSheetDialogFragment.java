package com.netposa.component.clcx.mvp.ui.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.netposa.commonres.widget.bottomsheet.BaseBottomSheetDialogFragment;
import com.netposa.component.clcx.R;
import com.netposa.component.clcx.R2;
import com.netposa.component.clcx.mvp.model.entity.PlateSelectEntity;
import com.netposa.component.clcx.mvp.ui.adapter.PlateSelectAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author：yeguoqiang
 * Created time：2018/11/22 14:23
 */
public class BottomSheetDialogFragment extends BaseBottomSheetDialogFragment {

    @BindView(R2.id.rv_content)
    RecyclerView mRvContent;

    private static final int APP_SPAN_COUNT = 4;
    private BottomSheetDialogListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.plate_select_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        GridLayoutManager layoutManager = new GridLayoutManager(view.getContext(), APP_SPAN_COUNT);
        List<PlateSelectEntity> beanList = new ArrayList<>();
        mRvContent.setLayoutManager(layoutManager);
        mRvContent.setItemAnimator(new DefaultItemAnimator());
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRvContent.setHasFixedSize(true);

        List<String> datas = Arrays.asList(
                "京", "津", "冀", "晋", "蒙", "辽", "吉", "黑", "沪",
                "苏", "浙", "皖", "闽", "赣", "鲁", "豫", "鄂", "湘",
                "粤", "桂", "琼", "渝", "川", "黔", "滇", "藏", "陕",
                "甘", "青", "宁", "新", "台", "港", "澳");
        for (String data : datas) {
            PlateSelectEntity entity = new PlateSelectEntity();
            entity.setName(data);
            beanList.add(entity);
        }
        PlateSelectAdapter selectAdapter = new PlateSelectAdapter(beanList);
        mRvContent.setAdapter(selectAdapter);

        selectAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PlateSelectEntity item = (PlateSelectEntity) adapter.getItem(position);
                mListener.setSelectResult(item.getName());
                getBehavior().setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
    }

    public interface BottomSheetDialogListener {
        void setSelectResult(String name);
    }

    public void setBottomSheetDialogListener(BottomSheetDialogListener listener) {
        mListener = listener;
    }

    public void removeListener() {
        if (mListener != null) {
            mListener = null;
        }
    }
}
