package com.netposa.component.location.impl;

import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.netposa.common.core.RouterHub;
import com.netposa.common.log.Log;
import com.netposa.common.service.location.LocationService;
import com.netposa.component.location.event.LocationEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.Nullable;

/**
 * Created by yexiaokang on 2018/10/19.
 */

@Route(path = RouterHub.LOCATION_LOCATION_SERVICE, name = "定位服务")
public class LocationServiceImpl implements LocationService {

    private static final String TAG = LocationServiceImpl.class.getSimpleName();
    private Context mContext;
    private Intent mService;
    private Location mLocation;
    private LocationEvent mLocationEvent;

    @Override
    public void startService() {
        Log.d(TAG, "startService----->");
        EventBus.getDefault().register(this);
        mContext.startService(mService);
    }


    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onLocationEvent(LocationEvent event) {
        mLocationEvent = event;
        mLocation = event.getLocation();
        event.recycle();
    }

    @Nullable
    @Override
    public Location getLocation() {
        Log.d(TAG, "getLocation location----->" + mLocation);
        return mLocation;
    }

    @Override
    public void stopService() {
        Log.d(TAG, "stopService----->");
        mContext.stopService(mService);
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().removeStickyEvent(LocationEvent.class);
        mLocation = null;
    }

    @Override
    public void init(Context context) {
        Log.d(TAG, "start to init----->");
        mContext = context;
        mService = new Intent(context, com.netposa.component.location.service.LocationService.class);
    }
}
