package com.netposa.component.my.mvp.model.entity;

import com.kyleduo.switchbutton.SwitchButton;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;

/**
 * Created by yexiaokang on 2018/9/23.
 */
@SuppressWarnings("WeakerAccess")
public class MenuEntity {

    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_TOGGLE = 2;


    @IntDef({
            TYPE_DEFAULT,
            TYPE_TEXT,
            TYPE_TOGGLE
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

    private final int id;

    private int type = TYPE_DEFAULT;
    private String title;
    @DrawableRes
    private int resId;

    // Used by clean type
    private String value;

    // Used by toggle type
    private boolean checked;
    private SwitchButton.OnCheckedChangeListener mOnCheckedChangeListener;

    public MenuEntity(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Type
    public int getType() {
        return type;
    }

    public void setType(@Type int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @DrawableRes
    public int getResId() {
        return resId;
    }

    public void setResId(@DrawableRes int resId) {
        this.resId = resId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public SwitchButton.OnCheckedChangeListener getOnCheckedChangeListener() {
        return mOnCheckedChangeListener;
    }

    public void setOnCheckedChangeListener(SwitchButton.OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListener = onCheckedChangeListener;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MenuEntity{");
        sb.append("id=").append(id);
        sb.append(", type=").append(type);
        sb.append(", title='").append(title).append('\'');
        sb.append(", resId=").append(resId);
        sb.append(", value='").append(value).append('\'');
        sb.append(", checked=").append(checked);
        sb.append('}');
        return sb.toString();
    }
}
