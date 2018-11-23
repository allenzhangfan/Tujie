package com.netposa.component.sfjb.mvp.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netposa.component.sfjb.R;
import com.netposa.component.room.entity.SfjbSearchHistoryEntity;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;

public class SfjbSearchHistoryAdapter extends BaseQuickAdapter<SfjbSearchHistoryEntity, BaseViewHolder> {
    @Inject
    public SfjbSearchHistoryAdapter(@Nullable List<SfjbSearchHistoryEntity> data) {
        super(R.layout.item_history, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SfjbSearchHistoryEntity item) {
        String name = item.getName();
        helper.setText(R.id.tv_name, name);
        helper.addOnClickListener(R.id.iv_delete);
    }
}
