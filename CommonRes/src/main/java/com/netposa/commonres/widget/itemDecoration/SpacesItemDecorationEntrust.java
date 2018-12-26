package com.netposa.commonres.widget.itemDecoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * 请不要直接调用此类
 * 参考:https://github.com/hzl123456/SpacesItemDecoration.git
 */
public abstract class SpacesItemDecorationEntrust {

    //color的传入方式是resouce.getcolor
    protected Drawable mDivider;

    protected int mLeftRight;
    protected int mTopBottom;
    protected int mLeftRightPadding;
    protected int mTopBottomPadding;

    public SpacesItemDecorationEntrust(@Dimension(unit = Dimension.DP) int leftRight,
                                       @Dimension(unit = Dimension.DP) int topBottom,
                                       @Dimension(unit = Dimension.DP) int leftRightPadding,
                                       @Dimension(unit = Dimension.DP) int topBottomPadding,
                                       @ColorInt int color) {
        this.mLeftRight = leftRight;
        this.mTopBottom = topBottom;
        this.mLeftRightPadding = leftRightPadding;
        this.mTopBottomPadding = topBottomPadding;
        if (color != 0) {
            mDivider = new ColorDrawable(color);
        }
    }

    abstract void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state);

    abstract void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state);

}
