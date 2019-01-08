package com.netposa.component.location.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.TextUtils;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.auto.service.AutoService;
import com.netposa.common.log.Log;
import com.netposa.common.service.location.IntegratedLocation;
import com.netposa.common.utils.LocationUtils;
import com.netposa.component.location.BuildConfig;
import com.netposa.component.location.event.LocationEvent;
import com.netposa.component.location.spi.LocationServiceProvider;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * Created by yexiaokang on 2018/11/23.
 */
@AutoService(LocationServiceProvider.class)
public class AMapLocationService extends Service implements LocationServiceProvider {

    private static final String TAG = "AMapLocationService";

    private Intent mService;
    private AMapLocationClient mAMapLocationClient;
    private BroadcastReceiver mOnceLocationReceiver = new OnceLocationBroadcastReceiver();

    private AMapLocationListener mAMapLocationListener = aMapLocation -> {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                // 模式为仅设备模式(Device_Sensors)，需要手动获取地址，其他模式不需要
                IntegratedLocation integratedLocation = new IntegratedLocation(aMapLocation);
                if (TextUtils.isEmpty(integratedLocation.getAddress())) {
                    String address = LocationUtils.getStrAddress(integratedLocation.getLatitude(),
                            integratedLocation.getLongitude());
                    integratedLocation.setAddress(address);
                }
                Log.i(TAG, "latitude:" + aMapLocation.getLatitude() + ",longitude:" + aMapLocation.getLongitude());
                Log.i(TAG, String.format("onLocationChanged Accuracy = {%f}", integratedLocation.getAccuracy()));
                Log.i(TAG, String.format("onLocationChanged = {%s}", integratedLocation.toString()));
                EventBus.getDefault().postSticky(LocationEvent.obtain(integratedLocation));
            } else {
                Log.e(TAG, String.format("AMapLocationError, ErrCode: {%d}, errInfo: {%s}", aMapLocation.getErrorCode(), aMapLocation.getErrorInfo()));
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "AMapLocationService onCreate");
        // 注册本地广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_ONCE_LOCATION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mOnceLocationReceiver, filter);
        // 初始化高德SDK定位客户端
        mAMapLocationClient = new AMapLocationClient(getApplicationContext());
        mAMapLocationClient.setLocationListener(mAMapLocationListener);
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        option.setGeoLanguage(AMapLocationClientOption.GeoLanguage.ZH);
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //true表示启动单次定位，false表示使用默认的连续定位策略
        option.setOnceLocation(true);
        //true表示获取最近3s内精度最高的一次定位结果；false表示使用默认的连续定位策略
        option.setOnceLocationLatest(true);
        //设置定位间隔,单位毫秒,默认为2000ms，最低800ms。
        option.setInterval(30 * 1000);
        //设置是否返回地址信息（默认返回地址信息）
        option.setNeedAddress(true);
        //设置是否允许模拟位置,默认为true，允许模拟位置
        option.setMockEnable(true);
        //设置是否启用缓存策略，SDK将在设备位置未改变时返回之前相同位置的缓存结果,默认为true
        option.setLocationCacheEnable(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        option.setHttpTimeOut(20000);
        mAMapLocationClient.setLocationOption(option);
        //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
        mAMapLocationClient.stopLocation();
        mAMapLocationClient.startLocation();
        Log.i(TAG, String.format("AMapLocationClient Version = {%s}", mAMapLocationClient.getVersion()));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "AMapLocationService onDestroy");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mOnceLocationReceiver);
        // 停止定位后，本地定位服务并不会被销毁
        mAMapLocationClient.stopLocation();
        // 销毁定位客户端，同时销毁本地定位服务。
        mAMapLocationClient.onDestroy();
        super.onDestroy();
    }

    @Override
    public void startService(Context context) {
        mService = new Intent(context, AMapLocationService.class);
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
        return IntegratedLocation.METHOD_AMAP;
    }

    private class OnceLocationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "OnceLocationBroadcastReceiver: startLocation");
            mAMapLocationClient.startLocation();
        }
    }
}
