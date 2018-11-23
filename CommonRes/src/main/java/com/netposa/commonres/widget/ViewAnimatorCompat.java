package com.netposa.commonres.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ViewAnimator;

import com.netposa.commonres.R;

import androidx.annotation.AnimRes;

/**
 * Created by yexiaokang on 2018/9/15.
 */
public class ViewAnimatorCompat extends FrameLayout {

    int mWhichChild = 0;
    boolean mFirstTime = true;

    boolean mAnimateFirstTime = true;

    Animation mInAnimation;
    Animation mOutAnimation;

    public ViewAnimatorCompat(Context context) {
        super(context);
        initViewAnimator(context, null);
    }

    public ViewAnimatorCompat(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewAnimatorCompat);
        int resource = a.getResourceId(R.styleable.ViewAnimatorCompat_android_inAnimation, 0);
        if (resource > 0) {
            setInAnimation(context, resource);
        }

        resource = a.getResourceId(R.styleable.ViewAnimatorCompat_android_outAnimation, 0);
        if (resource > 0) {
            setOutAnimation(context, resource);
        }

        boolean flag = a.getBoolean(R.styleable.ViewAnimatorCompat_android_animateFirstView, true);
        setAnimateFirstView(flag);

        a.recycle();

        initViewAnimator(context, attrs);
    }

    /**
     * Initialize this {@link ViewAnimator}, possibly setting
     * {@link #setMeasureAllChildren(boolean)} based on {@link FrameLayout} flags.
     */
    private void initViewAnimator(Context context, AttributeSet attrs) {
        if (attrs == null) {
            // For compatibility, always measure children when undefined.
            setMeasureAllChildren(true);
            return;
        }

        // For compatibility, default to measure children, but allow XML
        // attribute to override.
        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ViewAnimatorCompat);
        final boolean measureAllChildren = a.getBoolean(
                R.styleable.ViewAnimatorCompat_android_measureAllChildren, true);
        setMeasureAllChildren(measureAllChildren);
        a.recycle();
    }

    /**
     * Sets which child view will be displayed.
     *
     * @param whichChild the index of the child view to display
     */
    public void setDisplayedChild(int whichChild) {
        mWhichChild = whichChild;
        if (whichChild >= getChildCount()) {
            mWhichChild = 0;
        } else if (whichChild < 0) {
            mWhichChild = getChildCount() - 1;
        }
        boolean hasFocus = getFocusedChild() != null;
        // This will clear old focus if we had it
        showOnly(mWhichChild);
        if (hasFocus) {
            // Try to retake focus if we had it
            requestFocus(FOCUS_FORWARD);
        }
    }

    /**
     * Sets which child view will be displayed.
     *
     * @param tag the tag of the child view to display, require not null
     * @see View#getTag()
     * @see View#setTag(Object)
     */
    public void setDisplayedChild(Object tag) {
        final int childCount = getChildCount();
        int index = -1;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            Object obj = child.getTag();
            if (tag != null && tag.equals(obj)) {
                index = i;
                break;
            }
        }
        if (index >= 0) {
            setDisplayedChild(index);
        } else {
            android.util.Log.e("ViewAnimatorCompat",
                    "can't find the child view with the tag of " + tag);
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                child.setVisibility(GONE);
            }
        }
    }

    /**
     * Returns the index of the currently displayed child view.
     */
    public int getDisplayedChild() {
        return mWhichChild;
    }

    /**
     * Manually shows the next child.
     */
    public void showNext() {
        setDisplayedChild(mWhichChild + 1);
    }

    /**
     * Manually shows the previous child.
     */
    public void showPrevious() {
        setDisplayedChild(mWhichChild - 1);
    }

    /**
     * Shows only the specified child. The other displays Views exit the screen,
     * optionally with the with the {@link #getOutAnimation() out animation} and
     * the specified child enters the screen, optionally with the
     * {@link #getInAnimation() in animation}.
     *
     * @param childIndex The index of the child to be shown.
     * @param animate    Whether or not to use the in and out animations, defaults
     *                   to true.
     */
    void showOnly(int childIndex, boolean animate) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (i == childIndex) {
                if (animate && mInAnimation != null) {
                    child.startAnimation(mInAnimation);
                }
                child.setVisibility(View.VISIBLE);
                mFirstTime = false;
            } else {
                if (animate && mOutAnimation != null && child.getVisibility() == View.VISIBLE) {
                    child.startAnimation(mOutAnimation);
                } else if (child.getAnimation() == mInAnimation)
                    child.clearAnimation();
                child.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Shows only the specified child. The other displays Views exit the screen
     * with the {@link #getOutAnimation() out animation} and the specified child
     * enters the screen with the {@link #getInAnimation() in animation}.
     *
     * @param childIndex The index of the child to be shown.
     */
    void showOnly(int childIndex) {
        final boolean animate = (!mFirstTime || mAnimateFirstTime);
        showOnly(childIndex, animate);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (getChildCount() == 1) {
            child.setVisibility(View.VISIBLE);
        } else {
            child.setVisibility(View.GONE);
        }
        if (mWhichChild == index) {
            // ViewAnimatorCompat changed: fit for ViewStub or ViewStubCompat
            setDisplayedChild(mWhichChild);
        }
    }

    @Override
    public void removeAllViews() {
        super.removeAllViews();
        mWhichChild = 0;
        mFirstTime = true;
    }

    @Override
    public void removeView(View view) {
        final int index = indexOfChild(view);
        if (index >= 0) {
            removeViewAt(index);
        }
    }

    @Override
    public void removeViewAt(int index) {
        super.removeViewAt(index);
        final int childCount = getChildCount();
        if (childCount == 0) {
            mWhichChild = 0;
            mFirstTime = true;
        }
        // ViewAnimatorCompat changed: remove extra display operation
    }

    public void removeViewInLayout(View view) {
        removeView(view);
    }

    public void removeViews(int start, int count) {
        super.removeViews(start, count);
        if (getChildCount() == 0) {
            mWhichChild = 0;
            mFirstTime = true;
        }
        // ViewAnimatorCompat changed: remove extra display operation
    }

    public void removeViewsInLayout(int start, int count) {
        removeViews(start, count);
    }

    /**
     * Returns the View corresponding to the currently displayed child.
     *
     * @return The View currently displayed.
     * @see #getDisplayedChild()
     */
    public View getCurrentView() {
        return getChildAt(mWhichChild);
    }

    /**
     * Returns the current animation used to animate a View that enters the screen.
     *
     * @return An Animation or null if none is set.
     * @see #setInAnimation(Animation)
     * @see #setInAnimation(Context, int)
     */
    public Animation getInAnimation() {
        return mInAnimation;
    }

    /**
     * Specifies the animation used to animate a View that enters the screen.
     *
     * @param inAnimation The animation started when a View enters the screen.
     * @see #getInAnimation()
     * @see #setInAnimation(Context, int)
     */
    public void setInAnimation(Animation inAnimation) {
        mInAnimation = inAnimation;
    }

    /**
     * Returns the current animation used to animate a View that exits the screen.
     *
     * @return An Animation or null if none is set.
     * @see #setOutAnimation(Animation)
     * @see #setOutAnimation(Context, int)
     */
    public Animation getOutAnimation() {
        return mOutAnimation;
    }

    /**
     * Specifies the animation used to animate a View that exit the screen.
     *
     * @param outAnimation The animation started when a View exit the screen.
     * @see #getOutAnimation()
     * @see #setOutAnimation(Context, int)
     */
    public void setOutAnimation(Animation outAnimation) {
        mOutAnimation = outAnimation;
    }

    /**
     * Specifies the animation used to animate a View that enters the screen.
     *
     * @param context    The application's environment.
     * @param resourceID The resource id of the animation.
     * @see #getInAnimation()
     * @see #setInAnimation(Animation)
     */
    public void setInAnimation(Context context, @AnimRes int resourceID) {
        setInAnimation(AnimationUtils.loadAnimation(context, resourceID));
    }

    /**
     * Specifies the animation used to animate a View that exit the screen.
     *
     * @param context    The application's environment.
     * @param resourceID The resource id of the animation.
     * @see #getOutAnimation()
     * @see #setOutAnimation(Animation)
     */
    public void setOutAnimation(Context context, @AnimRes int resourceID) {
        setOutAnimation(AnimationUtils.loadAnimation(context, resourceID));
    }

    /**
     * Returns whether the current View should be animated the first time the ViewAnimator
     * is displayed.
     *
     * @return true if the current View will be animated the first time it is displayed,
     * false otherwise.
     * @see #setAnimateFirstView(boolean)
     */
    public boolean getAnimateFirstView() {
        return mAnimateFirstTime;
    }

    /**
     * Indicates whether the current View should be animated the first time
     * the ViewAnimator is displayed.
     *
     * @param animate True to animate the current View the first time it is displayed,
     *                false otherwise.
     */
    public void setAnimateFirstView(boolean animate) {
        mAnimateFirstTime = animate;
    }

    @Override
    public int getBaseline() {
        return (getCurrentView() != null) ? getCurrentView().getBaseline() : super.getBaseline();
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return ViewAnimator.class.getName();
    }
}
