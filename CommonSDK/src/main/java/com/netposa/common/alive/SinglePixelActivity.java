package com.netposa.common.alive;

import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 锁屏后1像素保活Activity
 * 参考:https://www.jianshu.com/p/34e2d42815a2
 */
public class SinglePixelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window mWindow = getWindow();
        mWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams attrParams = mWindow.getAttributes();
        attrParams.x = 0;
        attrParams.y = 0;
        attrParams.height = 1;
        attrParams.width = 1;
        mWindow.setAttributes(attrParams);
        // 绑定SinglePixelActivity到SinglePixelUtil
        SinglePixelUtil.getInstance(this).setSingleActivity(this);
        checkScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkScreen();
    }

    /**
     * 检查屏幕状态  isScreenOn为true  屏幕“亮”结束该Activity
     */
    private void checkScreen() {
        PowerManager pm = (PowerManager) SinglePixelActivity.this.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        if (isScreenOn) {
            finish();
        }
    }
}
