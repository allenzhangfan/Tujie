package com.netposa.component.jq.mvp.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netposa.component.jq.R;
import com.netposa.component.room.entity.JqSearchHistoryEntity;

import java.util.List;
import javax.inject.Inject;
import androidx.annotation.Nullable;

/**
 * Author：yeguoqiang
 * Created time：2018/10/29 14:31
 */
public class JqSearchHistoryAdapter extends BaseQuickAdapter<JqSearchHistoryEntity, BaseViewHolder> {

    @Inject
    public JqSearchHistoryAdapter(@Nullable List<JqSearchHistoryEntity> data) {
        super(R.layout.item_history, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, JqSearchHistoryEntity item) {
        String name = item.getName();
        helper.setText(R.id.tv_name,name);
        helper.addOnClickListener(R.id.iv_delete);
    }
}
