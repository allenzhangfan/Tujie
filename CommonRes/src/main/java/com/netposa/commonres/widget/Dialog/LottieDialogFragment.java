package com.netposa.commonres.widget.Dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.airbnb.lottie.LottieAnimationView;
import com.netposa.common.log.Log;
import com.netposa.commonres.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by yexiaokang on 2018/12/14.
 */
@SuppressWarnings("WeakerAccess")
public abstract class LottieDialogFragment extends AppCompatDialogFragment {

    private LottieAnimationView mLottieAnimationView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AppCompatDialog(getContext(), R.style.LottieDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_lottie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLottieAnimationView = view.findViewById(R.id.lottie_animation_view);
        mLottieAnimationView.useHardwareAcceleration();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().setCanceledOnTouchOutside(false);
    }

    // show系列方法重载，主要解决以下问题：
    // java.lang.IllegalStateException: Fragment already added: XXDialogFragment

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            manager.beginTransaction().remove(this).commit();
            super.show(manager, tag);
        } catch (Exception e) {
            Log.e(tag, e);
        }
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        try {
            transaction.remove(this).commit();
            return super.show(transaction, tag);
        } catch (Exception e) {
            Log.e(tag, e);
        }
        return -1;
    }

    @Override
    public void showNow(FragmentManager manager, String tag) {
        try {
            manager.beginTransaction().remove(this).commit();
            super.showNow(manager, tag);
        } catch (Exception e) {
            Log.e(tag, e);
        }
    }

    public LottieAnimationView getLottieAnimationView() {
        return mLottieAnimationView;
    }
}
