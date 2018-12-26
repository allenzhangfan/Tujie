package com.netposa.component.spjk.mvp.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netposa.component.room.entity.SpjkSearchHistoryEntity;
import com.netposa.component.spjk.R;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;

/**
 * Author：yeguoqiang
 * Created time：2018/10/29 14:31
 */
public class SpjkSearchHistoryAdapter extends BaseQuickAdapter<SpjkSearchHistoryEntity, BaseViewHolder> {

    @Inject
    public SpjkSearchHistoryAdapter(@Nullable List<SpjkSearchHistoryEntity> data) {
        super(R.layout.item_history, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SpjkSearchHistoryEntity item) {
        String name = item.getName();
        helper.setText(R.id.tv_name, name);
        helper.addOnClickListener(R.id.iv_delete);
    }
}
