package com.netposa.commonres.widget.spinner;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.lang.ref.WeakReference;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by yexiaokang on 2018/9/14.
 */
@SuppressWarnings({"WeakerAccess", "ClickableViewAccessibility"})
public class SpinnerTextView extends AppCompatTextView implements View.OnClickListener {

    private PopupWindow mPopupWindow;
    private ListView mListView;
    private OnClickListener mOnClickListener;
    private WeakReference<View> mAnchor;
    private OnSpinnerItemClickListener mOnSpinnerItemClickListener;
    private SpinnerTextViewAdapter mAdapter;
    private boolean mIsfollow = true;
    private boolean mShouldClose = true;

    public SpinnerTextView(Context context) {
        super(context);
        init(context);
    }

    public SpinnerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SpinnerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LinearLayout root = new LinearLayout(context);
        root.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        root.setBackgroundColor(0xaa000000);
        root.setOrientation(LinearLayout.VERTICAL);

        mPopupWindow = new MyPopupWindow(root, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT, true);

        mListView = new ListView(context);
        mListView.setDivider(new ColorDrawable(0xffd6d6d6));
        mListView.setDividerHeight(1);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView.setOnItemClickListener((parent, view, position, id) -> {
            if (mAdapter != null) {
                if (mIsfollow) {
                    setText(mAdapter.getItemText(position));
                }
            }
            if (mShouldClose) {
                closeWindow();
            }
            if (mOnSpinnerItemClickListener != null) {
                mOnSpinnerItemClickListener.onItemClick(parent, view, position, id);
            }
            setItemChecked(position);
        });

        root.addView(mListView);
        root.setFocusableInTouchMode(true);

        root.setOnTouchListener((v, event) -> {
            closeWindow();
            return true;
        });
        root.setOnKeyListener((v, keyCode, event) -> {
            closeWindow();
            return true;
        });
        super.setOnClickListener(this);
    }

    public ListView getListView() {
        return mListView;
    }

    public PopupWindow getPopupWindow() {
        return mPopupWindow;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mOnClickListener = l;
    }

    public void setOnSpinnerItemClickListener(OnSpinnerItemClickListener onSpinnerItemClickListener) {
        mOnSpinnerItemClickListener = onSpinnerItemClickListener;
    }

    public void setPopupWindowAnchor(View anchor) {
        mAnchor = new WeakReference<>(anchor);
    }

    @Override
    public void onClick(View v) {
        if (mPopupWindow.isShowing()) {
            closeWindow();
        } else {
            showWindow();
        }
        if (mOnClickListener != null) {
            mOnClickListener.onClick(v);
        }
    }

    public void setAdapter(SpinnerTextViewAdapter adapter) {
        mAdapter = adapter;
        if (mListView != null) {
            mListView.setAdapter(adapter);
        }
        if (adapter.getCount() > 0) {
            setItemChecked(0);
        }

    }

    public int getCheckedItemPosition() {
        if (mListView != null) {
            return mListView.getCheckedItemPosition();
        }
        return 0;
    }

    public Object getSelectItem() {
        if (mAdapter != null && mAdapter.getCount() > 0) {
            return mAdapter.getItem(getCheckedItemPosition());
        }
        return null;
    }

    public void setItemChecked(int position) {
        if (mListView != null && mListView.getCount() > 0) {
            mListView.setItemChecked(mListView.getCount() > position ? position : mListView.getCount() - 1, true);
        }
        if (mAdapter != null && mAdapter.getCount() > 0) {
            if (mIsfollow) {
                setText(mAdapter.getItemText(mAdapter.getCount() > position ? position : mAdapter.getCount() - 1));
            }
        }
    }

    private void showWindow() {
        if (mAnchor != null && mAnchor.get() != null) {
            mPopupWindow.showAsDropDown(mAnchor.get());
        } else {
            mPopupWindow.showAsDropDown(this);
        }
    }

    private void closeWindow() {
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    public interface OnSpinnerItemClickListener {

        void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }


    private class MyPopupWindow extends PopupWindow {

        public MyPopupWindow() {
        }

        public MyPopupWindow(View contentView) {
            super(contentView);
        }

        public MyPopupWindow(int width, int height) {
            super(width, height);
        }

        public MyPopupWindow(View contentView, int width, int height) {
            super(contentView, width, height);
        }

        public MyPopupWindow(View contentView, int width, int height, boolean focusable) {
            super(contentView, width, height, focusable);
        }

        @Override
        public void showAsDropDown(View anchor) {
            if (Build.VERSION.SDK_INT >= 24) {
                Rect rect = new Rect();
                anchor.getGlobalVisibleRect(rect);
                int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
                setHeight(h);
            }
            super.showAsDropDown(anchor);
        }
    }

    public void changeTitleAsSelected(boolean isfollow) {
        mIsfollow = isfollow;
    }

    public void closePopAsItemClicked(boolean shouldClose) {
        mShouldClose = shouldClose;
    }
}
