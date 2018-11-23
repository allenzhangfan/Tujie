package com.netposa.common.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

/**
 * Created by yexiaokang on 2017/11/7.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private static final String TAG = "ViewPagerAdapter";
    private static final boolean LOG_DEBUG = false;

    private List<View> mViews;
    private List<String> mTitles;
    private View mCurrentView;

    public ViewPagerAdapter(List<View> views, List<String> titles) {
        if (views == null || titles == null) {
            throw new NullPointerException();
        }
        if (views.size() != titles.size()) {
            throw new IllegalArgumentException("views size must be equal with titles size");
        }
        mViews = views;
        mTitles = titles;
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = mViews.get(position);
        String viewTag = makeViewTag(container.getId(), position);
        view.setTag(viewTag);
        container.addView(view);
        if (LOG_DEBUG) {
            Log.i(TAG, "instantiateItem: addView childCount = " + container.getChildCount() +
                    " viewTag = " + viewTag);
        }
        return viewTag;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = mViews.get(position);
        container.removeView(view);
        if (LOG_DEBUG) {
            Log.i(TAG, "destroyItem: removeView childCount = " + container.getChildCount() +
                    " viewTag = " + object.toString());
        }
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = mViews.get(position);
        if (view != mCurrentView) {
            view.setVisibility(View.VISIBLE);
            mCurrentView = view;
        }
        if (LOG_DEBUG) {
            Log.i(TAG, "setPrimaryItem: childCount = " + container.getChildCount() +
                    " viewTag = " + object.toString());
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object.toString().equals(view.getTag());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    private static String makeViewTag(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }
}
