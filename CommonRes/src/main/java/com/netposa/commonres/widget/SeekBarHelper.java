package com.netposa.commonres.widget;

import android.content.Context;
import android.view.ViewTreeObserver;
import android.widget.SeekBar;
import android.widget.TextView;

import com.netposa.commonres.R;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

/**
 * Created by yexiaokang on 2018/11/13.
 */
public class SeekBarHelper {

    public static void setupWithTextView(@NonNull SeekBar seekBar, @NonNull TextView textView) {
        seekBar.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        int width = seekBar.getWidth();
                        if (width > 0) {
                            seekBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            int progress = seekBar.getProgress();
                            int max = seekBar.getMax();
                            setTextViewPadding(textView, width, progress, max);
                        }
                    }
                });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int width = seekBar.getWidth();
                int max = seekBar.getMax();
                setTextViewPadding(textView, width, progress, max);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @UiThread
    private static void setTextViewPadding(@NonNull TextView textView, int width, int progress, int max) {
        float percent = progress * 1.0f / max;
        final Context context = textView.getContext();
        String text = context.getString(R.string.text_label_percent, progress);
        float content = textView.getPaint().measureText(text);
        int paddingLeft = (int) ((width - content) * percent);
        textView.setPadding(paddingLeft, 0, 0, 0);
        textView.setText(text);
    }
}
