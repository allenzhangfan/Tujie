package com.netposa.commonres.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.netposa.commonres.R;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * Created by yexiaokang on 2018/10/16.
 */
public class CameraFrameView extends View {

    private RectF mRectF = new RectF();
    private Paint mPaint;
    @SuppressWarnings("FieldCanBeLocal")
    private int mSplit = 3;

    public CameraFrameView(Context context) {
        this(context, null);
    }

    public CameraFrameView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraFrameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
        mPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.5f,
                getResources().getDisplayMetrics()));
        float strokeWidth = mPaint.getStrokeWidth();

        float widthTotal = getWidth() - paddingLeft - paddingRight - strokeWidth * (mSplit + 1);
        float heightTotal = getHeight() - paddingTop - paddingBottom - strokeWidth * (mSplit + 1);

        float widthAverage = widthTotal / mSplit;
        float heightAverage = heightTotal / mSplit;

        float startX, startY, stopX, stopY;

        canvas.save();

        // rect

        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
        mPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.5f,
                getResources().getDisplayMetrics()));

        mRectF.left = paddingLeft;
        mRectF.top = paddingTop;
        mRectF.right = getWidth() - paddingRight - strokeWidth;
        mRectF.bottom = getHeight() - paddingBottom - strokeWidth;
        canvas.drawRect(mRectF, mPaint);


        // middle line color
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.white_alpha20));

        // vertical
        for (int i = 1; i < mSplit; i++) {
            startX = paddingLeft + widthAverage * i + strokeWidth * i;
            startY = paddingTop;
            stopX = startX;
            stopY = getHeight() - paddingBottom - strokeWidth;
            canvas.drawLine(startX, startY, stopX, stopY, mPaint);
        }

        // horizontal
        for (int i = 1; i < mSplit; i++) {
            startX = paddingLeft;
            startY = paddingTop + heightAverage * i + strokeWidth * i;
            stopX = getWidth() - paddingRight - strokeWidth;
            stopY = startY;
            canvas.drawLine(startX, startY, stopX, stopY, mPaint);
        }

        // corner attribute
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
        mPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f,
                getResources().getDisplayMetrics()));

        float cornerLength = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f,
                getResources().getDisplayMetrics());

        // left-top
        startX = paddingLeft;
        startY = paddingTop;
        stopX = startX + cornerLength;
        stopY = paddingTop;
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);
        stopX = startX;
        stopY = paddingTop + cornerLength;
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);

        // right-top
        startX = getWidth() - paddingRight;
        startY = paddingTop;
        stopX = startX - cornerLength;
        stopY = paddingTop;
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);
        stopX = startX;
        stopY = paddingTop + cornerLength;
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);

        // right-bottom
        startX = getWidth() - paddingRight;
        startY = getHeight() - paddingBottom;
        stopX = startX - cornerLength;
        stopY = startY;
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);
        stopX = startX;
        stopY = startY - cornerLength;
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);

        // left-bottom
        startX = paddingLeft;
        startY = getHeight() - paddingBottom;
        stopX = startX + cornerLength;
        stopY = startY;
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);
        stopX = startX;
        stopY = startY - cornerLength;
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);

        canvas.restore();
    }
}
