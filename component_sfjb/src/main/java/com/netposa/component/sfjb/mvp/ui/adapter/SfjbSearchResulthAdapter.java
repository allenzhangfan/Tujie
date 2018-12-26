package com.netposa.component.sfjb.mvp.ui.adapter;

import android.text.Html;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netposa.commonres.utils.TextMark;
import com.netposa.component.sfjb.R;
import com.netposa.component.sfjb.mvp.model.entity.SearchFaceLibResponseEntity;
import com.netposa.component.sfjb.mvp.model.entity.SfjbSearchResultEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;

/**
 * Author：yeguoqiang
 * Created time：2018/10/29 14:31
 */
public class SfjbSearchResulthAdapter extends BaseQuickAdapter<SearchFaceLibResponseEntity.ListBean, BaseViewHolder> {
    private List<String> keyList = new ArrayList<>();

    @Inject
    public SfjbSearchResulthAdapter(@Nullable List<SearchFaceLibResponseEntity.ListBean> data) {
        super(R.layout.item_sfjb_matc, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchFaceLibResponseEntity.ListBean item) {
        String name = item.getLibName();
        boolean choose=item.isChoose();
        StringBuffer str = new StringBuffer("");
        keyList.add(item.getMatchName());
        str = TextMark.addChild(name, keyList, str);
        helper.setText(R.id.tv_name, Html.fromHtml(str.toString()));
        helper.setChecked(R.id.rb_single, choose);
        helper.addOnClickListener(R.id.ly_single);
    }
}

