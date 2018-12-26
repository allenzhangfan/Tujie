package com.netposa.component.rltk.mvp.model.entity;

/**
 * Author：yeguoqiang
 * Created time：2018/11/30 13:48
 */
public class FilterItemEntity {

    private boolean isSelect;
    private String content;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FilterItemEntity{");
        sb.append("isSelect=").append(isSelect);
        sb.append(", content='").append(content).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
