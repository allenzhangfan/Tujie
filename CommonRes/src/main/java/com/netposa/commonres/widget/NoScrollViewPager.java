package com.netposa.commonres.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * <p>可以禁止滑动翻页的ViewPager </p>
 *
 * @author 张华洋 2017/9/27 10:10
 * @version V1.1
 * @name NoScrollViewPager
 */
public class NoScrollViewPager extends ViewPager {

    private boolean isPagingEnabled = true;

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }

    public void setPagerEnabled(boolean b) {
        this.isPagingEnabled = b;
    }

}
