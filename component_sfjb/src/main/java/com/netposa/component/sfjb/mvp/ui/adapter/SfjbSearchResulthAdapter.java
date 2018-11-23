package com.netposa.component.sfjb.mvp.ui.adapter;
import android.text.Html;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netposa.commonres.utils.TextMark;
import com.netposa.component.sfjb.R;
import com.netposa.component.sfjb.mvp.model.entity.SfjbSearchResultEntity;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import androidx.annotation.Nullable;

/**
 * Author：yeguoqiang
 * Created time：2018/10/29 14:31
 */
public class SfjbSearchResulthAdapter extends BaseQuickAdapter<SfjbSearchResultEntity, BaseViewHolder> {
    private String matchName;
    private List<String> keyList = new ArrayList<>();

    @Inject
    public SfjbSearchResulthAdapter(@Nullable List<SfjbSearchResultEntity> data) {
        super(R.layout.item_sfjb_matc, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SfjbSearchResultEntity item) {
        String name = item.getName();
        StringBuffer str = new StringBuffer("");
        keyList.add(getMatchName());
        str = TextMark.addChild(name, keyList, str);
        helper.setText(R.id.tv_name, Html.fromHtml(str.toString()));
        helper.addOnClickListener(R.id.iv_address);
        helper.setChecked(R.id.rb_single, false);
        helper.addOnClickListener(R.id.ly_single);
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public String getMatchName() {
        return matchName;
    }
}

