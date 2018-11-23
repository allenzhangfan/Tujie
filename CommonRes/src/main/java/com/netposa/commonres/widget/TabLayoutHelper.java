package com.netposa.commonres.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.tabs.TabLayout;
import com.netposa.commonres.R;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.annotation.RestrictTo;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by yexiaokang on 2018/11/11.
 */
@SuppressWarnings({"WeakerAccess", "SameParameterValue"})
public class TabLayoutHelper {

    private static final String TAG = "TabLayoutHelper";

    public static void setTabLayoutIndicatorWidth(@NonNull TabLayout tabLayout, @Nullable ViewPager viewPager) {
        setTabLayoutIndicatorWidth(tabLayout, viewPager, Indicator.MIN_INDICATOR_WIDTH);
    }

    public static void setTabLayoutIndicatorWidth(@NonNull TabLayout tabLayout, @Nullable ViewPager viewPager,
                                                  @Dimension(unit = Dimension.DP) float indicatorWidth) {

        final Context context = tabLayout.getContext();
        int widthInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorWidth,
                context.getResources().getDisplayMetrics());

        ViewGroup parent = (ViewGroup) tabLayout.getParent();
        int tabLayoutId = tabLayout.getId();
        ConstraintLayout constraintLayout;
        if (parent instanceof ConstraintLayout) {
            constraintLayout = (ConstraintLayout) parent;
        } else {
            int index = parent.indexOfChild(tabLayout);
            ViewGroup.LayoutParams params = tabLayout.getLayoutParams();
            parent.removeViewAt(index);

            // add constraintLayout
            constraintLayout = new ConstraintLayout(context);
            parent.addView(constraintLayout, index, params);

            // add tabLayout
            ConstraintLayout.LayoutParams params1 = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params1.leftToLeft = 0;
            params1.rightToRight = 0;
            constraintLayout.addView(tabLayout, params1);
        }


        // add indicator
        ConstraintLayout.LayoutParams params2 = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ConstraintLayout.LayoutParams.MATCH_CONSTRAINT);
        params2.leftToLeft = tabLayoutId;
        params2.rightToRight = tabLayoutId;
        params2.topToTop = tabLayoutId;
        params2.bottomToBottom = tabLayoutId;

        Drawable tabSelectedIndicator = tabLayout.getTabSelectedIndicator();
        int tabIndicatorGravity = tabLayout.getTabIndicatorGravity();
        Indicator indicator = new Indicator(context);
        indicator.setupWithTabLayout(tabLayout);
        indicator.setIndicator(tabSelectedIndicator, tabIndicatorGravity);
        indicator.setIndicatorWidth(widthInPx);
        indicator.setSelectedIndicatorColor(ContextCompat.getColor(context, R.color.app_theme_color));
        constraintLayout.addView(indicator, params2);

        // remove tabLayout indicator
        tabLayout.setSelectedTabIndicator(0);

        // get tabLayout tabIndicatorAnimationDuration
        int tabIndicatorAnimationDuration = getReflectFieldIntValue(tabLayout,
                "tabIndicatorAnimationDuration", 300);

        if (viewPager != null) {
            IndicatorOnPageChangeListener indicatorOnPageChangeListener =
                    new IndicatorOnPageChangeListener(indicator, tabIndicatorAnimationDuration);
            indicatorOnPageChangeListener.reset();
            viewPager.addOnPageChangeListener(indicatorOnPageChangeListener);
        } else {
            IndicatorOnTabSelectedListener indicatorOnTabSelectedListener =
                    new IndicatorOnTabSelectedListener(indicator, tabIndicatorAnimationDuration);
            tabLayout.addOnTabSelectedListener(indicatorOnTabSelectedListener);
        }
    }

    private static int getReflectFieldIntValue(Object obj, String name, int defValue) {
        try {
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.getInt(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return defValue;
        }
    }

    /**
     * A {@link ViewPager.OnPageChangeListener} class which contains the necessary calls back to the
     * provided {@link Indicator} so that the tab position is kept in sync.
     *
     * <p>This class stores the provided Indicator weakly, meaning that you can use {@link
     * ViewPager#addOnPageChangeListener(ViewPager.OnPageChangeListener)
     * addOnPageChangeListener(OnPageChangeListener)} without removing the listener and not cause a
     * leak.
     */
    private static class IndicatorOnPageChangeListener implements ViewPager.OnPageChangeListener {

        private final int tabIndicatorAnimationDuration;
        private final WeakReference<Indicator> indicatorRef;
        private int previousScrollState;
        private int scrollState;

        public IndicatorOnPageChangeListener(Indicator indicator, int tabIndicatorAnimationDuration) {
            this.indicatorRef = new WeakReference<>(indicator);
            this.tabIndicatorAnimationDuration = tabIndicatorAnimationDuration;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            final Indicator indicator = indicatorRef.get();
            if (indicator != null) {
                // Update the indicator if we're not settling after being idle. This is caused
                // from a setCurrentItem() call and will be handled by an animation from
                // onPageSelected() instead.
                final boolean updateIndicator =
                        !(scrollState == ViewPager.SCROLL_STATE_SETTLING
                                && previousScrollState == ViewPager.SCROLL_STATE_IDLE);
                if (updateIndicator) {
                    indicator.setIndicatorPositionFromTabPosition(position, positionOffset);
                }
            }
        }

        @Override
        public void onPageSelected(int position) {
            final Indicator indicator = indicatorRef.get();
            if (indicator != null) {
                // Select the tab, only updating the indicator if we're not being dragged/settled
                // (since onPageScrolled will handle that).
                final boolean updateIndicator =
                        scrollState == ViewPager.SCROLL_STATE_IDLE
                                || (scrollState == ViewPager.SCROLL_STATE_SETTLING
                                && previousScrollState == ViewPager.SCROLL_STATE_IDLE);
                if (updateIndicator) {
                    indicator.animateIndicatorToPosition(position, tabIndicatorAnimationDuration);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            previousScrollState = scrollState;
            scrollState = state;
        }

        void reset() {
            previousScrollState = scrollState = ViewPager.SCROLL_STATE_IDLE;
        }
    }

    private static class IndicatorOnTabSelectedListener implements TabLayout.OnTabSelectedListener {

        private final int tabIndicatorAnimationDuration;
        private final WeakReference<Indicator> indicatorRef;

        public IndicatorOnTabSelectedListener(Indicator indicator, int tabIndicatorAnimationDuration) {
            this.indicatorRef = new WeakReference<>(indicator);
            this.tabIndicatorAnimationDuration = tabIndicatorAnimationDuration;
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            final Indicator indicator = indicatorRef.get();
            if (indicator != null) {
                final int position = tab.getPosition();
                indicator.animateIndicatorToPosition(position, tabIndicatorAnimationDuration);
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }

    /**
     * copy from <tt>TabLayout.SlidingTabIndicator</tt>
     */
    @SuppressLint("RestrictedApi")
    @SuppressWarnings({"unused", "SameParameterValue"})
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public static class Indicator extends View {

        private static final boolean LOG_DEBUG = false;
        private static final String TAG = "Indicator";

        @Dimension(unit = Dimension.DP)
        static final int MIN_INDICATOR_WIDTH = 24;

        private final RectF tabViewContentBounds;
        private final Paint selectedIndicatorPaint;
        private final GradientDrawable defaultSelectionIndicator;

        private int selectedIndicatorHeight = -1;
        private Drawable tabSelectedIndicator;
        private int tabIndicatorGravity;

        private int selectedPosition = -1;
        private float selectionOffset;

        private int indicatorLeft = -1;
        private int indicatorRight = -1;
        private int indicatorWidth = -1;
        private ValueAnimator indicatorAnimator;

        private Method getContentWidthMethod;
        private WeakReference<TabLayout> tabLayoutRef;

        public Indicator(Context context) {
            this(context, null);
        }

        public Indicator(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public Indicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            tabViewContentBounds = new RectF();
            selectedIndicatorPaint = new Paint();
            defaultSelectionIndicator = new GradientDrawable();
        }

        public void setSelectedIndicatorColor(@ColorInt int color) {
            if (selectedIndicatorPaint.getColor() != color) {
                selectedIndicatorPaint.setColor(color);
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }

        public void setSelectedIndicatorHeight(@Px int height) {
            if (selectedIndicatorHeight != height) {
                selectedIndicatorHeight = height;
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }

        public void setIndicatorWidth(@Px int indicatorWidth) {
            if (this.indicatorWidth != indicatorWidth) {
                this.indicatorWidth = indicatorWidth;
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }

        public void setIndicator(@Nullable Drawable tabSelectedIndicator,
                                 @TabLayout.TabIndicatorGravity int tabIndicatorGravity) {
            if (this.tabSelectedIndicator != tabSelectedIndicator
                    || this.tabIndicatorGravity != tabIndicatorGravity) {
                this.tabSelectedIndicator = tabSelectedIndicator;
                this.tabIndicatorGravity = tabIndicatorGravity;
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }

        public void setupWithTabLayout(TabLayout tabLayout) {
            if (tabLayoutRef == null || tabLayoutRef.get() != tabLayout) {
                this.tabLayoutRef = new WeakReference<>(tabLayout);
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }

        public void setIndicatorPositionFromTabPosition(int selectedPosition, float selectionOffset) {
            this.selectedPosition = selectedPosition;
            this.selectionOffset = selectionOffset;
            updateIndicatorPosition();
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            super.onLayout(changed, l, t, r, b);

            if (indicatorAnimator != null && indicatorAnimator.isRunning()) {
                // If we're currently running an animation, lets cancel it and start a
                // new animation with the remaining duration
                indicatorAnimator.cancel();
                final long duration = indicatorAnimator.getDuration();
                animateIndicatorToPosition(
                        selectedPosition,
                        Math.round((1f - indicatorAnimator.getAnimatedFraction()) * duration));
            } else {
                // If we've been layed out, update the indicator position
                updateIndicatorPosition();
            }
        }

        private void updateIndicatorPosition() {
            TabLayout tabLayout = tabLayoutRef != null ? tabLayoutRef.get() : null;
            if (tabLayout == null) {
                return;
            }
            LinearLayout slidingTabIndicator = (LinearLayout) tabLayout.getChildAt(0);
            View selectedTitle = slidingTabIndicator.getChildAt(selectedPosition);
            int left;
            int right;
            if (selectedTitle != null && selectedTitle.getWidth() > 0) {
                left = selectedTitle.getLeft();
                right = selectedTitle.getRight();
                if (!tabLayout.isTabIndicatorFullWidth()) {
                    calculateTabViewContentBounds(selectedTitle, tabViewContentBounds);
                    left = (int) tabViewContentBounds.left;
                    right = (int) tabViewContentBounds.right;
                }
                if (selectionOffset > 0f && selectedPosition < slidingTabIndicator.getChildCount() - 1) {
                    // Draw the selection partway between the tabs
                    View nextTitle = slidingTabIndicator.getChildAt(selectedPosition + 1);
                    int nextTitleLeft = nextTitle.getLeft();
                    int nextTitleRight = nextTitle.getRight();

                    if (!tabLayout.isTabIndicatorFullWidth()) {
                        calculateTabViewContentBounds(nextTitle, tabViewContentBounds);
                        nextTitleLeft = (int) tabViewContentBounds.left;
                        nextTitleRight = (int) tabViewContentBounds.right;
                    }

                    left = (int) (selectionOffset * nextTitleLeft + (1.0f - selectionOffset) * left);
                    right = (int) (selectionOffset * nextTitleRight + (1.0f - selectionOffset) * right);
                }
            } else {
                left = right = -1;
            }

            setIndicatorPosition(left, right);
        }

        private void setIndicatorPosition(int left, int right) {
            if (left != indicatorLeft || right != indicatorRight) {
                // If the indicator's left/right has changed, invalidate
                indicatorLeft = left;
                indicatorRight = right;
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }

        public void animateIndicatorToPosition(final int position, int duration) {
            if (indicatorAnimator != null && indicatorAnimator.isRunning()) {
                indicatorAnimator.cancel();
            }

            TabLayout tabLayout = tabLayoutRef != null ? tabLayoutRef.get() : null;
            if (tabLayout == null) {
                return;
            }
            LinearLayout slidingTabIndicator = (LinearLayout) tabLayout.getChildAt(0);

            final View targetView = slidingTabIndicator.getChildAt(position);
            if (targetView == null) {
                // If we don't have a view, just update the position now and return
                updateIndicatorPosition();
                return;
            }
            int targetLeft = targetView.getLeft();
            int targetRight = targetView.getRight();
            if (!tabLayout.isTabIndicatorFullWidth()) {
                calculateTabViewContentBounds(targetView, tabViewContentBounds);
                targetLeft = (int) tabViewContentBounds.left;
                targetRight = (int) tabViewContentBounds.right;
            }

            final int finalTargetLeft = targetLeft;
            final int finalTargetRight = targetRight;

            final int startLeft = indicatorLeft;
            final int startRight = indicatorRight;
            if (startLeft != finalTargetLeft || startRight != finalTargetRight) {
                ValueAnimator animator = indicatorAnimator = new ValueAnimator();
                animator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
                animator.setDuration(duration);
                animator.setFloatValues(0, 1);
                //noinspection Convert2Lambda
                animator.addUpdateListener(
                        new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animator) {
                                final float fraction = animator.getAnimatedFraction();
                                setIndicatorPosition(
                                        AnimationUtils.lerp(startLeft, finalTargetLeft, fraction),
                                        AnimationUtils.lerp(startRight, finalTargetRight, fraction));
                            }
                        });
                animator.addListener(
                        new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animator) {
                                selectedPosition = position;
                                selectionOffset = 0f;
                            }
                        });
                animator.start();
            }
        }

        /**
         * Given a {@code TabView}, calculate the left and right bounds of its content.
         *
         * <p>If only text label is present, calculates the width of the text label. If only icon is
         * present, calculates the width of the icon. If both are present, the text label bounds take
         * precedence. If both are present and inline mode is enabled, the sum of the bounds of the both
         * the text label and icon are calculated. If neither are present or if the calculated
         * difference between the left and right bounds is less than 24dp, then left and right bounds
         * are adjusted such that the difference between them is equal to 24dp.
         *
         * @param tabView {@code TabView} for which to calculate left and right content bounds.
         */
        private void calculateTabViewContentBounds(View tabView, RectF contentBounds) {
            int tabViewContentWidth = getContentWidth(tabView);
            if (tabViewContentWidth < dpToPx(MIN_INDICATOR_WIDTH)) {
                tabViewContentWidth = dpToPx(MIN_INDICATOR_WIDTH);
            }

            int tabViewCenter = (tabView.getLeft() + tabView.getRight()) / 2;
            int contentLeftBounds = tabViewCenter - (tabViewContentWidth / 2);
            int contentRightBounds = tabViewCenter + (tabViewContentWidth / 2);

            contentBounds.set(contentLeftBounds, 0, contentRightBounds, 0);
        }

        @Override
        public void draw(Canvas canvas) {
            int indicatorHeight = 0;
            if (tabSelectedIndicator != null) {
                indicatorHeight = tabSelectedIndicator.getIntrinsicHeight();
            }
            if (selectedIndicatorHeight >= 0) {
                indicatorHeight = selectedIndicatorHeight;
            }

            int indicatorTop = 0;
            int indicatorBottom = 0;

            switch (tabIndicatorGravity) {
                case TabLayout.INDICATOR_GRAVITY_BOTTOM:
                    indicatorTop = getHeight() - indicatorHeight;
                    indicatorBottom = getHeight();
                    break;
                case TabLayout.INDICATOR_GRAVITY_CENTER:
                    indicatorTop = (getHeight() - indicatorHeight) / 2;
                    indicatorBottom = (getHeight() + indicatorHeight) / 2;
                    break;
                case TabLayout.INDICATOR_GRAVITY_TOP:
                    indicatorTop = 0;
                    indicatorBottom = indicatorHeight;
                    break;
                case TabLayout.INDICATOR_GRAVITY_STRETCH:
                    indicatorTop = 0;
                    indicatorBottom = getHeight();
                    break;
                default:
                    break;
            }

            if (LOG_DEBUG) {
                Log.i(TAG, "draw: indicatorHeight = " + indicatorHeight);
                Log.i(TAG, "draw: indicatorTop = " + indicatorTop + " indicatorBottom = "
                        + indicatorBottom + " indicatorLeft = " + indicatorLeft + " indicatorRight = " + indicatorRight);
            }

            // Draw the selection indicator on top of tab item backgrounds
            if (indicatorLeft >= 0 && indicatorRight > indicatorLeft) {
                Drawable selectedIndicator;
                selectedIndicator =
                        DrawableCompat.wrap(
                                tabSelectedIndicator != null ? tabSelectedIndicator : defaultSelectionIndicator);
                selectedIndicator.setBounds(indicatorLeft, indicatorTop, indicatorRight, indicatorBottom);
                if (selectedIndicatorPaint != null) {
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                        // Drawable doesn't implement setTint in API 21
                        selectedIndicator.setColorFilter(
                                selectedIndicatorPaint.getColor(), PorterDuff.Mode.SRC_IN);
                    } else {
                        DrawableCompat.setTint(selectedIndicator, selectedIndicatorPaint.getColor());
                    }
                }
                selectedIndicator.draw(canvas);
            }

            // Draw the tab item contents (icon and label) on top of the background + indicator layers
            super.draw(canvas);
        }


        private int getContentWidth(View tabView) {
            if (indicatorWidth > 0) {
                return indicatorWidth;
            }
            if (getContentWidthMethod == null) {
                try {
                    getContentWidthMethod = tabView.getClass().getDeclaredMethod("getContentWidth");
                    getContentWidthMethod.setAccessible(true);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            if (getContentWidthMethod != null) {
                try {
                    return (int) getContentWidthMethod.invoke(tabView);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            return -1;
        }

        @Px
        private int dpToPx(@Dimension(unit = Dimension.DP) int dps) {
            return Math.round(this.getResources().getDisplayMetrics().density * (float) dps);
        }
    }
}
