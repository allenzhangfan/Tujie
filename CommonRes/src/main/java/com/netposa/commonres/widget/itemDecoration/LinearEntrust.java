package com.netposa.commonres.widget.itemDecoration;

import android.graphics.Canvas;
import android.graphics.Rect;

import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * 请不要直接调用此类
 * 参考:https://github.com/hzl123456/SpacesItemDecoration.git
 */
public class LinearEntrust extends SpacesItemDecorationEntrust {


    public LinearEntrust(@Dimension(unit = Dimension.DP) int leftRight,
                       @Dimension(unit = Dimension.DP) int topBottom,
                       @Dimension(unit = Dimension.DP) int leftRightPadding,
                       @Dimension(unit = Dimension.DP) int topBottomPadding,
                       @ColorInt int color) {
        super(leftRight, topBottom, leftRightPadding, topBottomPadding, color);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        //没有子view或者没有没有颜色直接return
        if (mDivider == null || layoutManager.getChildCount() == 0) {
            return;
        }
        int left;
        int right;
        int top;
        int bottom;
        final int childCount = parent.getChildCount();
        if (layoutManager.getOrientation() == RecyclerView.VERTICAL) {
            for (int i = 0; i < childCount - 1; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                //将有颜色的分割线处于中间位置
                final float center = (layoutManager.getTopDecorationHeight(child) + 1 - mTopBottom) / 2;
                //计算下边的
                left = layoutManager.getLeftDecorationWidth(child);
                right = parent.getWidth() - layoutManager.getLeftDecorationWidth(child);
                top = (int) (child.getBottom() + center);
                bottom = top + mTopBottom;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        } else {
            for (int i = 0; i < childCount - 1; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                //将有颜色的分割线处于中间位置
                final float center = (layoutManager.getLeftDecorationWidth(child) + 1 - mLeftRight) / 2;
                //计算右边的
                left = (int) (child.getRight() + center);
                right = left + mLeftRight;
                top = layoutManager.getTopDecorationHeight(child);
                bottom = parent.getHeight() - layoutManager.getTopDecorationHeight(child);
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        //竖直方向的
        if (layoutManager.getOrientation() == RecyclerView.VERTICAL) {
            //最后一项需要 bottom
            if (parent.getChildAdapterPosition(view) == layoutManager.getItemCount() - 1) {
                outRect.bottom = mTopBottom;
            }
            outRect.top = mTopBottom;
            outRect.left = mLeftRight;
            outRect.right = mLeftRight;
        } else {
            //最后一项需要right
            if (parent.getChildAdapterPosition(view) == layoutManager.getItemCount() - 1) {
                outRect.right = mLeftRight;
            }
            outRect.top = mTopBottom;
            outRect.left = mLeftRight;
            outRect.bottom = mTopBottom;
        }
    }
}
