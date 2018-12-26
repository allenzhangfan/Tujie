package com.netposa.commonres.widget.itemDecoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * 请不要直接调用此类
 * 参考:https://github.com/hzl123456/SpacesItemDecoration.git
 * 目前仅修改了垂直的item间距，水平的item间距还未修改
 */
public class GridEntrust extends SpacesItemDecorationEntrust {

    private static final String TAG = GridEntrust.class.getSimpleName();

    public GridEntrust(@Dimension(unit = Dimension.DP) int leftRight,
                       @Dimension(unit = Dimension.DP) int topBottom,
                       @Dimension(unit = Dimension.DP) int leftRightPadding,
                       @Dimension(unit = Dimension.DP) int topBottomPadding,
                       @ColorInt int color) {
        super(leftRight, topBottom, leftRightPadding, topBottomPadding, color);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        final GridLayoutManager.SpanSizeLookup lookup = layoutManager.getSpanSizeLookup();

        if (mDivider == null || layoutManager.getChildCount() == 0) {
            return;
        }
        //判断总的数量是否可以整除
        int spanCount = layoutManager.getSpanCount();
        int left, right, top, bottom;
        final int childCount = parent.getChildCount();
        if (layoutManager.getOrientation() == RecyclerView.VERTICAL) {
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                //将带有颜色的分割线处于中间位置
                final float centerLeft = ((float) (layoutManager.getLeftDecorationWidth(child) + layoutManager.getRightDecorationWidth(child))
                        * spanCount / (spanCount + 1) + 1 - mLeftRight) / 2;
                final float centerTop = (layoutManager.getBottomDecorationHeight(child) + 1 - mTopBottom) / 2;
                //得到它在总数里面的位置
                final int position = parent.getChildAdapterPosition(child);
                //获取它所占有的比重
                final int spanSize = lookup.getSpanSize(position);
                //获取每排的位置
                final int spanIndex = lookup.getSpanIndex(position, layoutManager.getSpanCount());
                //判断是否为第一排
                boolean isFirst = layoutManager.getSpanSizeLookup().getSpanGroupIndex(position, spanCount) == 0;
                //画上边的，第一排不需要上边的,只需要在最左边的那项的时候画一次就好
                if (!isFirst && spanIndex == 0) {
                    left = layoutManager.getLeftDecorationWidth(child);
                    right = parent.getWidth() - layoutManager.getLeftDecorationWidth(child);
                    top = (int) (child.getTop() - centerTop) - mTopBottom;
                    bottom = top + mTopBottom;
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
                //最右边的一排不需要右边的
                boolean isRight = spanIndex + spanSize == spanCount;
                if (!isRight) {
                    //计算右边的
                    left = (int) (child.getRight() + centerLeft);
                    right = left + mLeftRight;
                    top = child.getTop();
                    if (!isFirst) {
                        top -= centerTop;
                    }
                    bottom = (int) (child.getBottom() + centerTop);
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
            }
        } else {
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                //将带有颜色的分割线处于中间位置
                final float centerLeft = (layoutManager.getRightDecorationWidth(child) + 1 - mLeftRight) / 2;
                final float centerTop = ((float) (layoutManager.getTopDecorationHeight(child) + layoutManager.getBottomDecorationHeight(child))
                        * spanCount / (spanCount + 1) - mTopBottom) / 2;
                //得到它在总数里面的位置
                final int position = parent.getChildAdapterPosition(child);
                //获取它所占有的比重
                final int spanSize = lookup.getSpanSize(position);
                //获取每排的位置
                final int spanIndex = lookup.getSpanIndex(position, layoutManager.getSpanCount());
                //判断是否为第一列
                boolean isFirst = layoutManager.getSpanSizeLookup().getSpanGroupIndex(position, spanCount) == 0;
                //画左边的，第一排不需要左边的,只需要在最上边的那项的时候画一次就好
                if (!isFirst && spanIndex == 0) {
                    left = (int) (child.getLeft() - centerLeft) - mLeftRight;
                    right = left + mLeftRight;
                    top = layoutManager.getRightDecorationWidth(child);
                    bottom = parent.getHeight() - layoutManager.getTopDecorationHeight(child);
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
                //最下的一排不需要下边的
                boolean isRight = spanIndex + spanSize == spanCount;
                if (!isRight) {
                    //计算右边的
                    left = child.getLeft();
                    if (!isFirst) {
                        left -= centerLeft;
                    }
                    right = (int) (child.getRight() + centerTop);
                    top = (int) (child.getBottom() + centerLeft);
                    bottom = top + mLeftRight;
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        final GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        final int position = parent.getChildAdapterPosition(view);
        final int spanCount = layoutManager.getSpanCount();
        int mMaxSpanGroupIndex = layoutManager.getSpanSizeLookup()
                .getSpanGroupIndex(parent.getAdapter().getItemCount() - 1, spanCount);//最后一行
        GridLayoutManager.SpanSizeLookup spanSizeLookup = layoutManager.getSpanSizeLookup();
        int spanGroupIndex = spanSizeLookup.getSpanGroupIndex(position, spanCount);//每一排的index(排数)
        int spanIndex = spanSizeLookup.getSpanIndex(position, spanCount);//每一排item的index
        int spanSize = lp.getSpanSize();
//        Log.d(TAG, "position:" + position + ",spanIndex:" + spanIndex + ",spanGroupIndex:" + spanGroupIndex + ",spanSize:" + spanSize + ",mMaxSpanGroupIndex:" + mMaxSpanGroupIndex
//                + ",leftRightPadding:" + mLeftRightPadding + ",leftRight:" + mLeftRight + ",topBottomPadding:" + mTopBottomPadding + ",topBottom:" + mTopBottom);
        if (layoutManager.getOrientation() == RecyclerView.VERTICAL) {
            outRect.bottom = mTopBottom;
            //判断是否在第一排
            if (spanGroupIndex == 0) {//第一排的需要上面
                outRect.top = mTopBottomPadding;
            } else if (spanGroupIndex == mMaxSpanGroupIndex) {//最后一排需要下面
                outRect.bottom = mTopBottomPadding;
            }
            //这里忽略和合并项的问题，只考虑占满和单一的问题
            if (spanSize == spanCount) {//占满
                outRect.left = mLeftRightPadding;
                outRect.right = mLeftRightPadding;
            } else {
                outRect.left = (spanCount * mLeftRightPadding + (mLeftRight - 2 * mLeftRightPadding) * lp.getSpanIndex()) / spanCount;
                outRect.right = (mLeftRightPadding * 2 + mLeftRight * (spanCount - 1)) / spanCount - outRect.left;
            }
        } else {
            if (spanGroupIndex == 0) {//第一排的需要left
                outRect.left = mLeftRight;
            }
            outRect.right = mLeftRight;
            //这里忽略和合并项的问题，只考虑占满和单一的问题
            if (spanSize == spanCount) {//占满
                outRect.top = mTopBottom;
                outRect.bottom = mTopBottom;
            } else {
                outRect.top = (int) (((float) (spanCount - lp.getSpanIndex())) / spanCount * mTopBottom);
                outRect.bottom = (int) (((float) mTopBottom * (spanCount + 1) / spanCount) - outRect.top);
            }
        }
    }
}