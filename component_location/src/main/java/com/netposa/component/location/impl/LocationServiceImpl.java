package com.netposa.component.location.impl;

import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.netposa.common.core.RouterHub;
import com.netposa.common.log.Log;
import com.netposa.common.service.location.IntegratedLocation;
import com.netposa.common.service.location.LocationService;
import com.netposa.component.location.BuildConfig;
import com.netposa.component.location.event.LocationEvent;
import com.netposa.component.location.spi.LocationServiceProvider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * Created by yexiaokang on 2018/10/19.
 */

@Route(path = RouterHub.LOCATION_LOCATION_SERVICE, name = "定位服务")
public class LocationServiceImpl implements LocationService {

    private static final String TAG = "LocationServiceImpl";
    private final Map<String, Float> mAccuracyThreshold = new HashMap<>();
    private List<LocationServiceProvider> mServiceProviders;
    private Context mContext;
    private IntegratedLocation mLocation;
    private List<LocationListener> mLocationListeners = new ArrayList<>();
    private final Object mListenerLock = new Object();
    private LocationFilterStrategy mLocationFilterStrategy;
    private Intent mOnceLocationIntent;

    @Override
    public void startService() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
            for (LocationServiceProvider serviceProvider : mServiceProviders) {
                serviceProvider.startService(mContext);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onLocationEvent(LocationEvent event) {
        Log.i(TAG, "receive location event:" + event);
        IntegratedLocation location = event.getLocation();
        String method = location.getLocateMethod();
        if (mLocation == null) {
            if (mLocationFilterStrategy == null || mLocationFilterStrategy.accept(location)) {
                mLocation = location;
                Log.i(TAG, "dispatchLocationChanged for lastknown location: " + event);
                dispatchLocationChanged(mLocation);
            }
        } else {
            if (mLocationFilterStrategy == null || mLocationFilterStrategy.accept(location)) {
                Float threshold = mAccuracyThreshold.get(method);
                if (threshold == null || location.getAccuracy() <= threshold) {
                    mLocation = location;
                    Log.i(TAG, "dispatchLocationChanged for update location: " + event);
                    dispatchLocationChanged(mLocation);
                } else {
                    Log.w(TAG, String.format("Accuracy is too low = {%f}, location = {%s}", location.getAccuracy(), location.toString()));
                }
            }
        }
        event.recycle();
    }

    @Nullable
    @Override
    public IntegratedLocation getLocation() {
        return mLocation;
    }

    @Override
    public boolean addLocationListener(LocationListener listener) {
        synchronized (mListenerLock) {
            if (!mLocationListeners.contains(listener)) {
                mLocationListeners.add(listener);
                // 第一时间回调之前缓存的定位信息
                if (mLocation != null) {
                    listener.onLocationChanged(mLocation);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addLocationListenerBoundLifecycle(LocationListener listener,
                                                     @NonNull LifecycleOwner lifecycleOwner) {
        if (addLocationListener(listener)) {
            lifecycleOwner.getLifecycle().addObserver(new DefaultLifecycleObserver() {
                @Override
                public void onDestroy(@NonNull LifecycleOwner owner) {
                    owner.getLifecycle().removeObserver(this);
                    removeLocationListener(listener);
                }
            });
            return true;
        }
        return false;
    }

    @Override
    public void removeLocationListener(LocationListener listener) {
        synchronized (mListenerLock) {
            mLocationListeners.remove(listener);
        }
    }

    @Override
    public void updateLocationAccuracyThreshold(String method, float threshold) {
        mAccuracyThreshold.put(method, threshold);
    }

    @Override
    public void setLocationFilterStrategy(@Nullable LocationFilterStrategy strategy) {
        mLocationFilterStrategy = strategy;
    }

    @Override
    public void setLocationMethodEnabled(String method, boolean enabled) {
        for (LocationServiceProvider serviceProvider : mServiceProviders) {
            if (serviceProvider.getLocateMethod().equals(method)) {
                if (enabled) {
                    serviceProvider.startService(mContext);
                } else {
                    serviceProvider.stopService(mContext);
                }
            }
        }
    }

    @Override
    public void requestOnceLocation() {
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(mOnceLocationIntent);
    }

    @Override
    public void stopService() {
        if (EventBus.getDefault().isRegistered(this)) {
            for (LocationServiceProvider serviceProvider : mServiceProviders) {
                serviceProvider.stopService(mContext);
            }
            EventBus.getDefault().unregister(this);
            EventBus.getDefault().removeStickyEvent(LocationEvent.class);
            mLocationListeners.clear();
            mLocation = null;
        }
    }

    @Override
    public void init(Context context) {
        mContext = context;
        mAccuracyThreshold.put(IntegratedLocation.METHOD_GPS, 150f);
        mAccuracyThreshold.put(IntegratedLocation.METHOD_AMAP, 100f);
        ServiceLoader<LocationServiceProvider> serviceLoaders = ServiceLoader.load(LocationServiceProvider.class);
        mServiceProviders = new ArrayList<>();
        for (LocationServiceProvider serviceLoader : serviceLoaders) {
            mServiceProviders.add(serviceLoader);
        }
        mOnceLocationIntent = new Intent(LocationServiceProvider.ACTION_ONCE_LOCATION);
    }

    private void dispatchLocationChanged(IntegratedLocation location) {
        if (mLocationListeners.isEmpty()) {
            return;
        }
        LocationListener[] listeners = new LocationListener[0];
        synchronized (mListenerLock) {
            listeners = mLocationListeners.toArray(listeners);
        }
        for (LocationListener listener : listeners) {
            listener.onLocationChanged(location);
        }
    }
}
