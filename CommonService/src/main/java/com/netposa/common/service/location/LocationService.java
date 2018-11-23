package com.netposa.common.service.location;

import android.location.Location;

import com.alibaba.android.arouter.facade.template.IProvider;

import androidx.annotation.Nullable;

/**
 * Created by yexiaokang on 2018/10/19.
 */
public interface LocationService extends IProvider {

    void startService();

    @Nullable
    Location getLocation();


    void stopService();
}
