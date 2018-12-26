package com.netposa.commonres.widget.itemDecoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.netposa.common.utils.SizeUtils;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author：yeguoqiang
 * Created time：2018/12/10 08:48
 * 网格布局分割线
 * RecyclerView之利用ItemDecoration实现万能分割线
 * 参考:https://blog.csdn.net/k_bb_666/article/details/80840837
 *     https://blog.csdn.net/HZHAboom/article/details/78629937
 */
public class GovAffMatrixItemDecoration extends RecyclerView.ItemDecoration {
    
    private Paint mPaint; //如果需要用画笔手绘
    private Drawable mDrawableDivider; //如果需要绘制给定的drawable
    private int mPaintDividerLength = 1; //分割线宽度或高度
    private DrawType drawType; //用画笔或者特定的drawable绘制
    private int spanCount = 1; //网格布局的列数或行数，默认为一
    
    /**
     * 注意：列表的方向
     * RecyclerView.VERTICAL或RecyclerView.HORIZONTAL
     */
    private int mOrientation;
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider}; //系统默认的分割线

    /**
     * 自定义分割线
     *
     * @param orientation  列表方向
     * @param dividerColor 分割线颜色
     */
    public GovAffMatrixItemDecoration(int orientation, @ColorInt int dividerColor) {
        this(orientation, -100, dividerColor);
    }

    /**
     * @param context     上下文
     * @param orientation 列表方向
     */
    public GovAffMatrixItemDecoration(Context context, int orientation) {
        this(context, orientation, -100);
    }

    /**
     * 自定义分割线
     *
     * @param orientation   列表方向
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    public GovAffMatrixItemDecoration(int orientation, int dividerHeight, @ColorInt int dividerColor) {
        if (orientation != RecyclerView.VERTICAL && orientation != RecyclerView.HORIZONTAL) {
            throw new IllegalArgumentException("请输入正确的参数！");
        }
        mOrientation = orientation;
        if (dividerHeight != -100) {
            //分割线高度
            mPaintDividerLength = dividerHeight;
        }
        //创建特定画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(dividerColor);
        mPaint.setStyle(Paint.Style.FILL);
        //表明绘制用paint
        drawType = DrawType.USEPAINT;
    }

    /**
     * 自定义分割线
     *
     * @param context     上下文
     * @param orientation 列表方向
     * @param drawableId  分割线图片
     */
    public GovAffMatrixItemDecoration(Context context, int orientation, @DrawableRes int drawableId) {
        if (orientation != RecyclerView.VERTICAL && orientation != RecyclerView.HORIZONTAL) {
            throw new IllegalArgumentException("请输入正确的参数！");
        }
        mOrientation = orientation;
        if (drawableId == -100) {
            final TypedArray a = context.obtainStyledAttributes(ATTRS); //获取系统的样式
            mDrawableDivider = a.getDrawable(0);
            a.recycle();
        } else {
            mDrawableDivider = ContextCompat.getDrawable(context, drawableId);
        }
        drawType = DrawType.USEDRAWABLE; //表明使用特点的drawable绘制
    }

    /**
     * get Item Offsets：获得item的偏移量。
     * 通过方法名得知此方法用来控制item的偏移
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
            spanCount = gridLayoutManager.getSpanCount();
        }
        /**
         * 列表的方向为横向，画分割线就是纵向的，需要确定的是child的右边偏移值
         * 留出空间画分割线
         */
        if (this.mOrientation == RecyclerView.HORIZONTAL)
            switch (drawType) {
                case USEPAINT:
                    outRect.set(0, 0, mPaintDividerLength, 0);
                    break;
                case USEDRAWABLE:
                    outRect.set(0, 0, mDrawableDivider.getIntrinsicWidth(), 0);
                    break;
            }
        /**
         * 列表的方向为纵向，画分割线就是横向的，需要确定的是child的下边偏移值
         * 留出空间画分割线
         */
        else if (this.mOrientation == RecyclerView.VERTICAL)
            switch (drawType) {
                case USEPAINT:
                    outRect.set(mPaintDividerLength, 0, 0, mPaintDividerLength);
                    break;
                case USEDRAWABLE:
                    outRect.set(mDrawableDivider.getIntrinsicHeight(), 0, 0, mDrawableDivider.getIntrinsicHeight());
                    break;
            }
    }

    /**
     * 绘制分割线
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mOrientation == RecyclerView.VERTICAL) {
            //列表是纵向的，需要绘制横向的分割线
            drawHorizontal(c, parent);
            drawVertical(c, parent);
        } else {
            //列表是横向的，需要绘制纵向的分割线
            drawVertical(c, parent);
        }
    }

    /**
     * 绘制横向 item 分割线。左、上、右都是可计算的，下需要获取给定的高度值
     *
     * @param canvas
     * @param parent
     */
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        //左边：到父容器的left内间距位置值
        final int left = parent.getPaddingLeft();
        //右边：到父容器的right内间距位置值
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        final int childSize = parent.getChildCount();
        //循环绘制每条分割线
        //因为每次绘制会以父布局的宽为长度，所以每行只需绘制一次，在此计算出需要绘制的次数
        int drawSize = (childSize / spanCount) + ((childSize % spanCount > 0) ? 1 : 0);
        for (int i = 1; i < childSize; i++) {
            if (i % spanCount == 0) {
                final View child = parent.getChildAt(i - 1);
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
                //上边：具体的某条分割线的左边以child的(bottom+bottomMargin)位置值
                final int top = child.getBottom() + layoutParams.bottomMargin;
                //下边：根据类型判断
                int bottom;
                switch (drawType) {
                    case USEPAINT://构造方法声明使用画笔绘制
                        //下边：top加上分割线的高度
                        bottom = top + mPaintDividerLength;
                        if (mPaint != null) {
                            canvas.drawRect(left, top, right, bottom, mPaint);
                        }
                        break;
                    case USEDRAWABLE://构造方法声明使用drawable
                        if (mDrawableDivider != null) {
                            //下边：top加上分割线的高度
                            bottom = top + mDrawableDivider.getIntrinsicHeight();
                            mDrawableDivider.setBounds(left, top, right, bottom);
                            mDrawableDivider.draw(canvas);
                        }
                        break;
                }
            }
        }
    }

    /**
     * 绘制纵向 item 分割线。上、下、左都是可计算的，右侧需要获取给定的宽度值
     *
     * @param canvas
     * @param parent
     */
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        //上边：到父容器的top内间距位置值
//        final int top = parent.getPaddingTop();
//        //下边：到父容器的bottom内间距位置值
//        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
        final int childSize = parent.getChildCount();
        //循环绘制每条分割线
        for (int i = 0; i < childSize - 1; i++) {
            if (i % spanCount == 0 || i % spanCount == 1) {
                final View child = parent.getChildAt(i);
                //获取child的布局参数
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();

                final int top = child.getTop() + SizeUtils.dp2px(8);
                final int bottom = child.getBottom() - SizeUtils.dp2px(8);
                //左边：具体的某条分割线的左边以child的(right+rightMargin)位置值
                final int left = child.getRight() + layoutParams.rightMargin;
                //右边：根据类型判断
                int right;
                switch (drawType) {
                    case USEPAINT://构造方法声明使用画笔绘制
                        //右边：left加上分割线的宽度
                        right = left + mPaintDividerLength;
                        if (mPaint != null) {
                            canvas.drawRect(left, top, right, bottom, mPaint);
                        }
                        break;
                    case USEDRAWABLE://构造方法声明使用drawable
                        if (mDrawableDivider != null) {
                            //右边：left加上分割线的宽度
                            right = left + mDrawableDivider.getIntrinsicWidth();
                            mDrawableDivider.setBounds(left, top, right, bottom);
                            mDrawableDivider.draw(canvas);
                        }
                        break;
                }
            }
        }

    }

    public static enum DrawType {
        USEPAINT(1),//用自定义颜色的画笔画
        USEDRAWABLE(2); //用特定的drawable画

        private final int type;

        DrawType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }
}
