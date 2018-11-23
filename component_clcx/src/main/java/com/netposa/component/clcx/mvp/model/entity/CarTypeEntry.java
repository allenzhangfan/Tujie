package com.netposa.component.clcx.mvp.model.entity;


public class CarTypeEntry {

    private String title;

    public boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CarTypeEntry{");
        sb.append("title='").append(title).append('\'');
        sb.append(", isSelect=").append(isSelect);
        sb.append('}');
        return sb.toString();
    }
}
