package com.netposa.commonres.widget.Dialog;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by yexiaokang on 2018/12/14.
 */
public class ComparisonDialogFragment extends LottieDialogFragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // triangle_loading.json 是专门针对比对操作挑选的动画
        getLottieAnimationView().setAnimation("triangle_loading.json");
    }
}
