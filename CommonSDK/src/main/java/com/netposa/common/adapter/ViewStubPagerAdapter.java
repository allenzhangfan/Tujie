package com.netposa.common.adapter;

import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

/**
 * Created by fine on 2017/12/1.
 */

public class ViewStubPagerAdapter extends PagerAdapter {

    private static final String TAG = "ViewStubPagerAdapter";
    private static final boolean LOG_DEBUG = false;

    private List<ViewStub> mViewStubs;
    private List<String> mTitles;
    private SparseArray<View> mViews = new SparseArray<>();
    private View mCurrentView;

    public ViewStubPagerAdapter(List<ViewStub> viewStubs, List<String> titles) {
        if (viewStubs == null || titles == null) {
            throw new NullPointerException();
        }
        if (viewStubs.size() != titles.size()) {
            throw new IllegalArgumentException("views size must be equal with titles size");
        }
        mViewStubs = viewStubs;
        mTitles = titles;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        String viewTag;
        View view = mViews.get(position);
        if (view != null) {
            viewTag = makeViewTag(container.getId(), position);
            view.setTag(viewTag);
            container.addView(view);
        } else {
            ViewStub viewStub = mViewStubs.get(position);
            container.addView(viewStub);
            view = viewStub.inflate();
            mViews.put(position, view);
            viewTag = makeViewTag(container.getId(), position);
            view.setTag(viewTag);
        }
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
    public int getCount() {
        return mViewStubs.size();
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
