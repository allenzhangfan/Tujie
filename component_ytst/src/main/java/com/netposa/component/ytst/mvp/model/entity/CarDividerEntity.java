package com.netposa.component.ytst.mvp.model.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class CarDividerEntity implements MultiItemEntity {
    public static final int TYPE_ITEM = 0x3;

    @Override
    public int getItemType() {
        return TYPE_ITEM;
    }
}
