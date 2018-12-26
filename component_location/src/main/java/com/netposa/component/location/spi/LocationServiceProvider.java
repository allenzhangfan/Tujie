package com.netposa.component.location.spi;

import android.content.Context;

import com.netposa.common.service.location.IntegratedLocation;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

/**
 * Created by yexiaokang on 2018/11/23.
 */
@Keep
public interface LocationServiceProvider {

    String ACTION_ONCE_LOCATION = "android.intent.action.ONCE_LOCATION";

    void startService(Context context);

    void stopService(Context context);

    @NonNull
    @IntegratedLocation.LocateMethod
    String getLocateMethod();
}
