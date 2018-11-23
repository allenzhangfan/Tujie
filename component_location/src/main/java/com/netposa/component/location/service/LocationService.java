package com.netposa.component.location.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import com.netposa.common.utils.LocationUtils;
import com.netposa.component.location.BuildConfig;
import com.netposa.component.location.event.LocationEvent;
import org.greenrobot.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@SuppressLint("MissingPermission")
public class LocationService extends Service {

    private static final String TAG = "LocationService";
    private static final boolean LOG_DEBUG = BuildConfig.LOG_DEBUG;
    private static final Logger LOGGER = LoggerFactory.getLogger(TAG);

    /**
     * 位置信息更新周期，单位毫秒
     */
    private static final int MIN_TIME = 30 * 1000;
    /**
     * 位置变化最小距离：当位置距离变化超过此值时，将更新位置信息，单位米
     */
    private static final int MIN_DISTANCE = 100;

    private Location mLocation;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener = new GpsLocationListener();
    private boolean mRequestLocationUpdates = false;

    private BroadcastReceiver mReceiver = new LocationBroadcastReceiver();

    @Override
    public void onCreate() {
        super.onCreate();
        LOGGER.info("LocationService onCreate");
        // 监听GPS开关变化
        IntentFilter filter = new IntentFilter();
        filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        registerReceiver(mReceiver, filter);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        assert mLocationManager != null;
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (!mRequestLocationUpdates) {
                startRequestLocationUpdates();
            }
        } else {
            LOGGER.warn("GPS_PROVIDER is not enabled");
        }
    }

    private void startRequestLocationUpdates() {
        mLocation = getLastKnownLocation();
        LOGGER.info("getLastKnownLocation result :" + mLocation);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                MIN_TIME, MIN_DISTANCE, mLocationListener);
        mRequestLocationUpdates = true;
        EventBus.getDefault().postSticky(LocationEvent.obtain(mLocation));
    }

    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            LOGGER.info("getLastKnownLocation :" + provider);
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
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
        LOGGER.info("LocationService onDestroy");
        unregisterReceiver(mReceiver);
        if (mRequestLocationUpdates) {
            mLocationManager.removeUpdates(mLocationListener);
        }
        mRequestLocationUpdates = false;
        super.onDestroy();
    }

//    private static Criteria getCriteria() {
//        Criteria criteria = new Criteria();
//        // 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//        // 设置是否要求速度
//        criteria.setSpeedRequired(false);
//        // 设置是否允许运营商收费
//        criteria.setCostAllowed(false);
//        // 设置是否需要方位信息
//        criteria.setBearingRequired(false);
//        // 设置是否需要海拔信息
//        criteria.setAltitudeRequired(true);
//        // 设置对电源的需求
//        criteria.setPowerRequirement(Criteria.POWER_LOW);
//        return criteria;
//    }

    private class GpsLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            mLocation = location;
            if (LOG_DEBUG) {
                LOGGER.info("onLocationChanged Latitude = {}, Longitude = {}", location.getLatitude(), location.getLongitude());
                LOGGER.info("onLocationChanged = {}", location.toString());
                String address = LocationUtils.getStrAddress(location.getLatitude(), location.getLongitude());
                LOGGER.info("onLocationChanged address = {}", address);
            }
            EventBus.getDefault().postSticky(LocationEvent.obtain(mLocation));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            LOGGER.info("onProviderEnabled = {}", provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            LOGGER.info("onProviderDisabled = {}", provider);
        }
    }

    private class LocationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (LocationManager.PROVIDERS_CHANGED_ACTION.equals(intent.getAction())) {
                List<String> providers = mLocationManager.getProviders(true);
                LOGGER.info("PROVIDERS_CHANGED_ACTION: enabled providers = " + providers.toString());
                if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (!mRequestLocationUpdates) {
                        LOGGER.info("PROVIDERS_CHANGED_ACTION: startRequestLocationUpdates");
                        startRequestLocationUpdates();
                    }
                }
            }
        }
    }
}
