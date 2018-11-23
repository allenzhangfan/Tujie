package com.netposa.component.gzt.mvp.ui.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netposa.component.gzt.R;
import com.netposa.component.gzt.mvp.model.entity.GztEntity;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;

/**
 * Author：yeguoqiang
 * Created time：2018/10/25 14:29
 */
public class GztAdapter extends BaseQuickAdapter<GztEntity, BaseViewHolder> {

    @Inject
    public GztAdapter(@Nullable List<GztEntity> data) {
        super(R.layout.item_gzt, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GztEntity item) {
        helper.setText(R.id.name_tv, item.getName());
        ImageView iconIv = helper.getView(R.id.icon_iv);
        iconIv.setImageResource(item.getResId());
    }
}
