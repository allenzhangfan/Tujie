package com.netposa.commonres.widget.bottomsheet;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.netposa.common.log.Log;
import com.netposa.commonres.R;

import androidx.annotation.AnyThread;
import androidx.annotation.FloatRange;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by yexiaokang on 2018/11/6.
 */
@SuppressWarnings({"WeakerAccess", "ConstantConditions"})
public class BaseBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private static final String TAG = "BottomSheetDialog";

    private float mHeightPercent = 0.8f;
    private BottomSheetBehavior<FrameLayout> mBehavior;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(getContext(), R.style.BottomSheetDialog);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        if (view != null) {
            mBehavior = BottomSheetBehavior.from(((FrameLayout) view.getParent()));
            setBottomSheetDialog(view);
            mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    protected void setBottomSheetDialog(@NonNull View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (mHeightPercent > 0) {
            int height = (int) (getResources().getDisplayMetrics().heightPixels * mHeightPercent);
            params.height = height;
            mBehavior.setPeekHeight(height);
        }
        view.setLayoutParams(params);
    }

    protected void setFullBottomSheetDialog(@NonNull View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        int heightPixels = getResources().getDisplayMetrics().heightPixels;
        int statusBarHeight = 0;
        int resourceId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = Resources.getSystem().getDimensionPixelOffset(resourceId);
        }
        int height = heightPixels - statusBarHeight;
        params.height = height;
        mBehavior.setPeekHeight(height);
        view.setLayoutParams(params);
    }

    @MainThread
    public void setHeightPercent(@FloatRange(from = 0f, to = 1f) float heightPercent) {
        mHeightPercent = heightPercent;

        Dialog dialog = getDialog();
        if (dialog != null && dialog.isShowing()) {
            View view = getView();
            if (view != null && mHeightPercent > 0) {
                ViewGroup.LayoutParams params = view.getLayoutParams();
                int height = (int) (getResources().getDisplayMetrics().heightPixels * mHeightPercent);
                params.height = height;
                if (mBehavior != null) {
                    mBehavior.setPeekHeight(height);
                }
                view.setLayoutParams(params);
            }
        } else {
            Log.i(TAG, "setHeightPercent: heightPercent = " + heightPercent);
        }
    }

    @AnyThread
    public float getHeightPercent() {
        return mHeightPercent;
    }

    public BottomSheetBehavior<FrameLayout> getBehavior() {
        return mBehavior;
    }
}
