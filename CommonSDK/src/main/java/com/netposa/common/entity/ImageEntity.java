package com.netposa.common.entity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * Created by yexiaokang on 2018/9/17.
 * <h4>图片类实体</h4>
 * <p>主要用来配合<tt>Adapter</tt>来展示图片，主要用于视频身份和实口管控的图片联合搜索布局</p>
 */
@SuppressWarnings("WeakerAccess")
public class ImageEntity {

    public static final int LIMIT = 3;

    /**
     * 图片类型，索引一定要按照从0开始的顺序
     *
     * @see android.widget.Adapter#getItemViewType(int)
     */

    public static final int TYPE_IMAGE = 0;     // 图片类型
    public static final int TYPE_ADD = 1;       // 添加图片
    public static final int TYPE_DEL = 2;       // 删除图片

    @IntDef({
            TYPE_IMAGE,
            TYPE_ADD,
            TYPE_DEL
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ImageType {
    }

    @ImageType
    private int type;
    private String path;

    public ImageEntity(@ImageType int type) {
        this.type = type;
    }

    @ImageType
    public int getType() {
        return type;
    }

    public void setType(@ImageType int type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
