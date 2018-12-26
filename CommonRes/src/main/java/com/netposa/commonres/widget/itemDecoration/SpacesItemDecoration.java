package com.netposa.commonres.widget.itemDecoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * 参考:https://github.com/hzl123456/SpacesItemDecoration.git
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private SpacesItemDecorationEntrust mEntrust;
    private int mColor;//item间距的颜色
    private int mLeftRight;//每个item与左边以及右边的间距(如果需要设置每排item间距全部一样，需要mLeftRight=mLeftrightPadding)
    private int mTopBottom;//每个item与顶部以及顶部的间距(如果单独设置了mLeftrightPadding、mTopbottomPadding则此属性仅对item与item之间的间距生效)
    private int mLeftrightPadding;//整个RecyclerView与左边以及右边的间距
    private int mTopbottomPadding;//整个RecyclerView与顶部以及底部的间距

    /**
     * 如果需要设置水平均分每个item间距，请使用此方法
     *
     * @param leftRight
     * @param topBottom
     */
    public SpacesItemDecoration(@Dimension(unit = Dimension.DP) int leftRight,
                                @Dimension(unit = Dimension.DP) int topBottom) {
        this(leftRight, topBottom, leftRight, topBottom);
    }

    public SpacesItemDecoration(@Dimension(unit = Dimension.DP) int leftRight,
                                @Dimension(unit = Dimension.DP) int topBottom,
                                @ColorInt int mColor) {
        this(leftRight, topBottom);
        this.mColor = mColor;
    }

    /**
     * 如果整个recyclerview与上下左右的间距跟item与item之间的间距大小不一致请使用此方法
     *
     * @param leftRight
     * @param topBottom
     * @param topbottomPadding
     * @param leftrightPadding
     */
    public SpacesItemDecoration(@Dimension(unit = Dimension.DP) int leftRight,
                                @Dimension(unit = Dimension.DP) int topBottom,
                                @Dimension(unit = Dimension.DP) int leftrightPadding,
                                @Dimension(unit = Dimension.DP) int topbottomPadding) {
        this.mLeftRight = leftRight;
        this.mTopBottom = topBottom;
        this.mLeftrightPadding = leftrightPadding;
        this.mTopbottomPadding = topbottomPadding;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mEntrust == null) {
            mEntrust = getEntrust(parent.getLayoutManager());
        }
        mEntrust.onDraw(c, parent, state);
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mEntrust == null) {
            mEntrust = getEntrust(parent.getLayoutManager());
        }
        mEntrust.getItemOffsets(outRect, view, parent, state);
    }

    private SpacesItemDecorationEntrust getEntrust(RecyclerView.LayoutManager manager) {
        SpacesItemDecorationEntrust entrust = null;
        //要注意这边的GridLayoutManager是继承LinearLayoutManager，所以要先判断GridLayoutManager
        if (manager instanceof GridLayoutManager) {
            entrust = new GridEntrust(mLeftRight, mTopBottom, mLeftrightPadding, mTopbottomPadding, mColor);
        } else if (manager instanceof StaggeredGridLayoutManager) {
            entrust = new StaggeredGridEntrust(mLeftRight, mTopBottom, mLeftrightPadding, mTopbottomPadding, mColor);
        } else {//其他的都当做Linear来进行计算
            entrust = new LinearEntrust(mLeftRight, mTopBottom, mLeftrightPadding, mTopbottomPadding, mColor);
        }
        return entrust;
    }
}