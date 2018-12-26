package com.netposa.component.rltk.mvp.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.material.button.MaterialButton;
import com.netposa.component.rltk.R;
import com.netposa.component.rltk.mvp.model.entity.FilterItemEntity;
import java.util.List;
import androidx.annotation.Nullable;

/**
 * Author：yeguoqiang
 * Created time：2018/11/29 20:24
 */
public class FilterItemAdapter extends BaseQuickAdapter<FilterItemEntity, BaseViewHolder> {

    public FilterItemAdapter(@Nullable List<FilterItemEntity> data) {
        super(R.layout.item_filter, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FilterItemEntity item) {
        MaterialButton btFilter = helper.getView(R.id.bt_filter);
        btFilter.setText(item.getContent());
        if (item.isSelect()) {
            btFilter.setSelected(true);
        } else {
            btFilter.setSelected(false);
        }
    }
}