package com.netposa.commonres.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Dimension;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by yexiaokang on 2018/10/10.
 */
@SuppressWarnings("ALL")
public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private static final float SPACE = 20f;

    private Context mContext;
    private int mSpanCount;

    private float mHorizontalSpacing;
    private float mVerticalSpacing;
    private float mTopPadding;

    public GridItemDecoration(Context context, @IntRange(from = 2) int spanCount) {
        mContext = context;
        mSpanCount = spanCount;

        mHorizontalSpacing = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                SPACE, mContext.getResources().getDisplayMetrics());
        mVerticalSpacing = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                SPACE, mContext.getResources().getDisplayMetrics());
    }

    public GridItemDecoration(Context context, @IntRange(from = 2) int spanCount,
                              @Dimension(unit = Dimension.DP) float horizontalSpacing,
                              @Dimension(unit = Dimension.DP) float verticalSpacing) {
        mContext = context;
        mSpanCount = spanCount;
        mHorizontalSpacing = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                horizontalSpacing, mContext.getResources().getDisplayMetrics());
        mVerticalSpacing = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                verticalSpacing, mContext.getResources().getDisplayMetrics());
    }

    public void setTopPadding(@Dimension(unit = Dimension.DP) float topPadding) {
        mTopPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                topPadding, mContext.getResources().getDisplayMetrics());
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent.getAdapter() == null) {
            return;
        }
        final int position = parent.getChildAdapterPosition(view);
        final int verticalSpacing = (int) mVerticalSpacing;
        final int horizontalSpacingOneThird = (int) (mHorizontalSpacing / 3);
        final int horizontalSpacingTwoThirds = (int) (mHorizontalSpacing * 2 / 3);
        final int horizontalSpacingHalf = (int) (mHorizontalSpacing / 2);
        final int topPadding = (int) mTopPadding;

        if (position != RecyclerView.NO_POSITION) {
            int offset = position % mSpanCount;
            boolean firstRow = position < mSpanCount;

            /*final int itemCount = parent.getAdapter().getItemCount();
            int value = itemCount % mSpanCount;
            int count = (value == 0) ? itemCount : (itemCount + mSpanCount - value);
            boolean lastRow = (count - position - 1) < mSpanCount;*/

            outRect.top = firstRow ? topPadding : verticalSpacing;

            if (mSpanCount == 2) {
                if (offset == 0) {
                    outRect.left = 0;
                    outRect.right = horizontalSpacingHalf;
                } else {
                    outRect.left = horizontalSpacingHalf;
                    outRect.right = 0;
                }
            } else {
                if (offset == 0) {
                    outRect.left = 0;
                    outRect.right = horizontalSpacingTwoThirds;
                } else if (offset == (mSpanCount - 1)) {
                    outRect.left = horizontalSpacingTwoThirds;
                    outRect.right = 0;
                } else {
                    outRect.left = horizontalSpacingOneThird;
                    outRect.right = horizontalSpacingOneThird;
                }
            }
        }
    }
}
