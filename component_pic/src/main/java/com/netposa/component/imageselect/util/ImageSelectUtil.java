package com.netposa.component.imageselect.util;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.util.Log;

import com.netposa.component.pic.R;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.listener.OnCheckedListener;
import com.zhihu.matisse.listener.OnSelectedListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * Author：yeguoqiang
 * Created time：2018/9/24 17:40
 * 参考:
 * https://blog.csdn.net/qiaoshi96_bk/article/details/76164913
 * https://github.com/zhihu/Matisse.git
 */
public class ImageSelectUtil {

    public static final int REQUEST_CODE_CHOOSE = 0xffff;

    public static void selectImages(Activity activity, int maxSelectable) {
        Matisse.from(activity)
                .choose(MimeType.ofImage(), false)//过滤视频
                .countable(true)
                .capture(true)
                .captureStrategy(
                        new CaptureStrategy(true, "com.netposa.tujie.fileprovider"))
                .maxSelectable(maxSelectable)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(
                        activity.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new Glide4Engine())    // for glide-V4
                .setOnSelectedListener(new OnSelectedListener() {
                    @Override
                    public void onSelected(
                            @NonNull List<Uri> uriList, @NonNull List<String> pathList) {
                        // DO SOMETHING IMMEDIATELY HERE
                        Log.e("onSelected", "onSelected: pathList=" + pathList);

                    }
                })
                .originalEnable(true)
                .maxOriginalSize(10)
                .autoHideToolbarOnSingleTap(true)
                .setOnCheckedListener(new OnCheckedListener() {
                    @Override
                    public void onCheck(boolean isChecked) {
                        // DO SOMETHING IMMEDIATELY HERE
                        Log.e("isChecked", "onCheck: isChecked=" + isChecked);
                    }
                })
                .theme(R.style.MatisseTheme)
                .forResult(REQUEST_CODE_CHOOSE);
    }

    public static void selectImages(Fragment fragment, int maxSelectable) {
        Matisse.from(fragment)
                .choose(MimeType.ofAll(), false)
                .countable(true)
                .capture(true)
                .captureStrategy(
                        new CaptureStrategy(true, "com.netposa.tujie.fileprovider"))
                .maxSelectable(maxSelectable)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(
                        fragment.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new Glide4Engine())    // for glide-V4
                .setOnSelectedListener(new OnSelectedListener() {
                    @Override
                    public void onSelected(
                            @NonNull List<Uri> uriList, @NonNull List<String> pathList) {
                        // DO SOMETHING IMMEDIATELY HERE
                        Log.e("onSelected", "onSelected: pathList=" + pathList);

                    }
                })
                .originalEnable(true)
                .maxOriginalSize(10)
                .autoHideToolbarOnSingleTap(true)
                .setOnCheckedListener(new OnCheckedListener() {
                    @Override
                    public void onCheck(boolean isChecked) {
                        // DO SOMETHING IMMEDIATELY HERE
                        Log.e("isChecked", "onCheck: isChecked=" + isChecked);
                    }
                })
                .theme(R.style.MatisseTheme)
                .forResult(REQUEST_CODE_CHOOSE);
    }
}
