package com.netposa.component.ytst.mvp.model.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class CarInfoEntity implements MultiItemEntity {
    public static final int TYPE_ITEM_INFO = 0x2;
    public String title;
    public String description;
    public boolean dividerVisable;
    private boolean gpsVisable;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDividerVisable() {
        return dividerVisable;
    }

    public void setDividerVisable(boolean dividerVisable) {
        this.dividerVisable = dividerVisable;
    }

    public boolean isGpsVisable() {
        return gpsVisable;
    }

    public void setGpsVisable(boolean gpsVisable) {
        this.gpsVisable = gpsVisable;
    }

    @Override
    public int getItemType() {
        return TYPE_ITEM_INFO;
    }
}
