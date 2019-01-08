package com.netposa.component.location.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import com.google.auto.service.AutoService;
import com.netposa.common.log.Log;
import com.netposa.common.service.location.IntegratedLocation;
import com.netposa.common.utils.LocationUtils;
import com.netposa.component.location.event.LocationEvent;
import com.netposa.component.location.spi.LocationServiceProvider;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

@SuppressLint("MissingPermission")
@AutoService(LocationServiceProvider.class)
public class GpsLocationService extends Service implements LocationServiceProvider {

    private static final String TAG = "GpsLocationService";

    /**
     * 位置信息更新周期，单位毫秒
     */
    private static final int MIN_TIME = 30 * 1000;
    /**
     * 位置变化最小距离：当位置距离变化超过此值时，将更新位置信息，单位米
     */
    private static final int MIN_DISTANCE = 100;

    private Intent mService;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener = new GpsLocationListener();
    private boolean mRequestLocationUpdates = false;

    private BroadcastReceiver mReceiver = new LocationBroadcastReceiver();
    private BroadcastReceiver mOnceLocationReceiver = new OnceLocationBroadcastReceiver();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "GpsLocationService onCreate");
        // 监听GPS开关变化
        IntentFilter filter = new IntentFilter();
        filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        registerReceiver(mReceiver, filter);
        // 注册本地广播
        filter = new IntentFilter();
        filter.addAction(ACTION_ONCE_LOCATION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mOnceLocationReceiver, filter);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        assert mLocationManager != null;
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (!mRequestLocationUpdates) {
                startRequestLocationUpdates();
            }
        } else {
            Log.w(TAG, "GPS_PROVIDER is not enabled");
        }
    }

    /**
     * 开始请求位置更新
     */
    private void startRequestLocationUpdates() {
//        String bestProvider = mLocationManager.getBestProvider(getCriteria(), true);
        List<String> allProviders = mLocationManager.getAllProviders();
        Log.i(TAG, String.format("allProviders = {%s}", allProviders));
        String bestProvider;
        if (allProviders.contains(LocationManager.NETWORK_PROVIDER)) {
            bestProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            bestProvider = LocationManager.GPS_PROVIDER;
        }
        mLocationManager.requestLocationUpdates(bestProvider,
                MIN_TIME, MIN_DISTANCE, mLocationListener);
        mRequestLocationUpdates = true;
        Log.i(TAG, String.format("bestProvider = {%s}", bestProvider));
        Location location = getLastKnownLocation();
        if (location != null) {
            //历史位置信息
            Log.i(TAG, String.format("lastKnownLocation = {%s}", location.toString()));
            Log.i(TAG, String.format("lastKnownLocation = {%s}", LocationUtils.getAddress(location.getLatitude(), location.getLongitude())));
        }
    }

    /**
     * 遍历所有使能的定位提供者，获取最优的定位位置，可能为空
     *
     * @return 最后的已知位置，可能为空
     */
    private Location getLastKnownLocation() {
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "GpsLocationService onDestroy");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mOnceLocationReceiver);
        unregisterReceiver(mReceiver);
        if (mRequestLocationUpdates) {
            mLocationManager.removeUpdates(mLocationListener);
        }
        mRequestLocationUpdates = false;
        super.onDestroy();
    }

    @Override
    public void startService(Context context) {
        mService = new Intent(context, GpsLocationService.class);
        context.startService(mService);
    }

    @Override
    public void stopService(Context context) {
        if (mService == null) {
            throw new IllegalStateException("service is null");
        }
        context.stopService(mService);
    }

    @NonNull
    @Override
    public String getLocateMethod() {
        return IntegratedLocation.METHOD_GPS;
    }

    /**
     * 获取指定条件下的定位标准
     *
     * @return 定位标准
     */
    private static Criteria getCriteria() {
        Criteria criteria = new Criteria();
        // 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        // 设置是否需要方位信息
        criteria.setBearingRequired(false);
        // 设置是否需要海拔信息
        criteria.setAltitudeRequired(true);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

    private class GpsLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            updateLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i(TAG, String.format("onProviderEnabled = {%s}", provider));
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i(TAG, String.format("onProviderDisabled = {%s}", provider));
        }
    }

    private void updateLocation(Location location) {
        IntegratedLocation integratedLocation = new IntegratedLocation(location);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        String address = LocationUtils.getStrAddress(latitude, longitude);
        integratedLocation.setAddress(address);
        Log.i(TAG, String.format("latitude:%f,longitude:%f", latitude, longitude));
        Log.i(TAG, String.format("onLocationChanged Accuracy = {%f}", integratedLocation.getAccuracy()));
        Log.i(TAG, String.format("onLocationChanged = {%s}", integratedLocation.toString()));
        EventBus.getDefault().postSticky(LocationEvent.obtain(integratedLocation));
    }

    private class LocationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (LocationManager.PROVIDERS_CHANGED_ACTION.equals(intent.getAction())) {
                List<String> providers = mLocationManager.getProviders(true);
                Log.i(TAG, "PROVIDERS_CHANGED_ACTION: enabled providers = " + providers.toString());
                if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (!mRequestLocationUpdates) {
                        Log.i(TAG, "PROVIDERS_CHANGED_ACTION: startRequestLocationUpdates");
                        startRequestLocationUpdates();
                    }
                }
            }
        }
    }

    private class OnceLocationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // keep empty implementor
            Log.i(TAG, "OnceLocationBroadcastReceiver: startLocation{empty implementor}");
        }
    }
}
