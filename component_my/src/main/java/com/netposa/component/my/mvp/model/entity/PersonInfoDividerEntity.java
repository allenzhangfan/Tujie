package com.netposa.component.my.mvp.model.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Author：yeguoqiang
 * Created time：2018/10/27 13:33
 */
public class PersonInfoDividerEntity implements MultiItemEntity {

    public static final int TYPE_DIVIDER = 0x2;

    @Override
    public int getItemType() {
        return TYPE_DIVIDER;
    }

}
