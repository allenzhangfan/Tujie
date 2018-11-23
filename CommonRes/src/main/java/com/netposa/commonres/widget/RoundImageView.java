package com.netposa.commonres.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;

import com.netposa.commonres.R;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.ViewCompat;

/**
 * Created by yexiaokang on 2018/11/15.
 */
public class RoundImageView extends AppCompatImageView {

    private int mLeftTopRadius;
    private int mRightTopRadius;
    private int mRightBottomRadius;
    private int mLeftBottomRadius;

    private Path mPath = new Path();

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        if (a.hasValue(R.styleable.RoundImageView_radius)) {
            int radius = a.getDimensionPixelOffset(R.styleable.RoundImageView_radius, 0);
            setLeftTopRadius(radius);
            setRightTopRadius(radius);
            setRightBottomRadius(radius);
            setLeftBottomRadius(radius);
        }
        if (a.hasValue(R.styleable.RoundImageView_left_top_radius)) {
            int radius = a.getDimensionPixelOffset(R.styleable.RoundImageView_left_top_radius, 0);
            setLeftTopRadius(radius);
        }
        if (a.hasValue(R.styleable.RoundImageView_right_top_radius)) {
            int radius = a.getDimensionPixelOffset(R.styleable.RoundImageView_right_top_radius, 0);
            setRightTopRadius(radius);
        }
        if (a.hasValue(R.styleable.RoundImageView_right_bottom_radius)) {
            int radius = a.getDimensionPixelOffset(R.styleable.RoundImageView_right_bottom_radius, 0);
            setRightBottomRadius(radius);
        }
        if (a.hasValue(R.styleable.RoundImageView_left_bottom_radius)) {
            int radius = a.getDimensionPixelOffset(R.styleable.RoundImageView_left_bottom_radius, 0);
            setLeftBottomRadius(radius);
        }
        a.recycle();
    }

    public int getLeftTopRadius() {
        return mLeftTopRadius;
    }

    public void setLeftTopRadius(int leftTopRadius) {
        if (mLeftTopRadius != leftTopRadius) {
            mLeftTopRadius = leftTopRadius;
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public int getRightTopRadius() {
        return mRightTopRadius;
    }

    public void setRightTopRadius(int rightTopRadius) {
        if (mRightTopRadius != rightTopRadius) {
            mRightTopRadius = rightTopRadius;
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public int getRightBottomRadius() {
        return mRightBottomRadius;
    }

    public void setRightBottomRadius(int rightBottomRadius) {
        if (mRightBottomRadius != rightBottomRadius) {
            mRightBottomRadius = rightBottomRadius;
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public int getLeftBottomRadius() {
        return mLeftBottomRadius;
    }

    public void setLeftBottomRadius(int leftBottomRadius) {
        if (mLeftBottomRadius != leftBottomRadius) {
            mLeftBottomRadius = leftBottomRadius;
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int maxLeft = Math.max(mLeftTopRadius, mLeftBottomRadius);
        int maxRight = Math.max(mRightTopRadius, mRightBottomRadius);
        int minWidth = maxLeft + maxRight;
        int maxTop = Math.max(mLeftTopRadius, mRightTopRadius);
        int maxBottom = Math.max(mLeftBottomRadius, mRightBottomRadius);
        int minHeight = maxTop + maxBottom;
        int width = getWidth();
        int height = getHeight();
        // 只有图片的宽高大于设置的圆角距离的时候才进行裁剪
        if (width >= minWidth && height >= minHeight) {
            mPath.reset();
            // 四个角：右上，右下，左下，左上
            mPath.moveTo(mLeftTopRadius, 0);
            mPath.lineTo(width - mRightTopRadius, 0);
            mPath.quadTo(width, 0, width, mRightTopRadius);

            mPath.lineTo(width, height - mRightBottomRadius);
            mPath.quadTo(width, height, width - mRightBottomRadius, height);

            mPath.lineTo(mLeftBottomRadius, height);
            mPath.quadTo(0, height, 0, height - mLeftBottomRadius);

            mPath.lineTo(0, mLeftTopRadius);
            mPath.quadTo(0, 0, mLeftTopRadius, 0);

            canvas.clipPath(mPath);
        }
        super.onDraw(canvas);
    }
}
