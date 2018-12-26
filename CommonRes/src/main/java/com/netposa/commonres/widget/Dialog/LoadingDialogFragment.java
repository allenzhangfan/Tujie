package com.netposa.commonres.widget.Dialog;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by yexiaokang on 2018/12/14.
 */
public class LoadingDialogFragment extends LottieDialogFragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // material_loading.json 是透明背景的加载动画，适用于特殊的场景下的背景渲染
        // material_wave_loading.json 是背景为白色的加载动画，适用大部分的场景
    }
}
