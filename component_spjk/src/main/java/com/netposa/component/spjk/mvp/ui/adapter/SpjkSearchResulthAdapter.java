package com.netposa.component.spjk.mvp.ui.adapter;

import android.text.Html;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netposa.common.utils.Utils;
import com.netposa.commonres.utils.TextMark;
import com.netposa.component.spjk.R;
import com.netposa.component.spjk.mvp.model.entity.SpjkItemEntity;
import com.netposa.component.spjk.mvp.model.entity.SpjkSearchResponseEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;

/**
 * Author：yeguoqiang
 * Created time：2018/10/29 14:31
 */
public class SpjkSearchResulthAdapter extends BaseQuickAdapter<SpjkSearchResponseEntity.ListBean, BaseViewHolder> {
    private String matchName;
    private List<String> keyList = new ArrayList<>();

    @Inject
    public SpjkSearchResulthAdapter(@Nullable List<SpjkSearchResponseEntity.ListBean> data) {
        super(R.layout.item_spjk_matc, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SpjkSearchResponseEntity.ListBean item) {
        String name = item.getName();
        StringBuffer str = new StringBuffer("");
        keyList.add(getMatchName());
        str = TextMark.addChild(name, keyList, str);
        helper.setText(R.id.tv_name, Html.fromHtml(str.toString()));
        helper.addOnClickListener(R.id.iv_address);
        if (item.getLatitude() == 0 && item.getLatitude() == 0) {
            helper.setBackgroundRes(R.id.iv_address, R.drawable.ic_gps).setVisible(R.id.iv_address, false);
        }else{
            helper.setBackgroundRes(R.id.iv_address, R.drawable.ic_gps).setVisible(R.id.iv_address, true);
        }
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public String getMatchName() {
        return matchName;
    }
}

