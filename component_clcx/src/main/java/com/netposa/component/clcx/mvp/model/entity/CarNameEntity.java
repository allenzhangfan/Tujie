package com.netposa.component.clcx.mvp.model.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class CarNameEntity implements MultiItemEntity {
    public static final int TYPE_ITEM_NAME = 0x1;
    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getItemType() {
        return TYPE_ITEM_NAME;
    }
}
