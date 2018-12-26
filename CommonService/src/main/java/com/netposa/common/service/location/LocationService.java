package com.netposa.common.service.location;

import android.location.Location;

import com.alibaba.android.arouter.facade.template.IProvider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

/**
 * Created by yexiaokang on 2018/10/19.
 */
public interface LocationService extends IProvider {

    /**
     * 开始定位服务
     */
    void startService();

    /**
     * 手动获取当前的定位信息，可能为空
     *
     * @return 当前的定位信息，可能为空
     */
    @Nullable
    IntegratedLocation getLocation();

    /**
     * 添加位置更新回调接口
     *
     * @param listener 位置更新回调接口
     * @return {@code true}为添加接口成功，否则为{@code false}
     * @see #addLocationListenerBoundLifecycle(LocationListener, LifecycleOwner)
     */
    boolean addLocationListener(LocationListener listener);

    /**
     * 添加位置更新回调接口，并且绑定生命周期，生命周期结束时自动移除该监听器
     *
     * @param listener       位置更新回调接口
     * @param lifecycleOwner 生命周期拥有者
     * @return {@code true}为添加接口成功，否则为{@code false}
     * @see #addLocationListener(LocationListener)
     * @see androidx.lifecycle.Lifecycle
     */
    boolean addLocationListenerBoundLifecycle(LocationListener listener,
                                              @NonNull LifecycleOwner lifecycleOwner);

    /**
     * 移除位置更新回调接口
     *
     * @param listener 位置更新回调接口
     */
    void removeLocationListener(LocationListener listener);

    /**
     * 更新定位准确度阈值
     *
     * @param method    定位方案
     * @param threshold 准确度阈值
     * @see IntegratedLocation.LocateMethod
     * @see Location#getAccuracy()
     */
    void updateLocationAccuracyThreshold(@IntegratedLocation.LocateMethod String method, float threshold);

    /**
     * 设置定位的过滤策略
     *
     * @param strategy 定位过滤策略
     */
    void setLocationFilterStrategy(@Nullable LocationFilterStrategy strategy);

    /**
     * 设置是否使能某个定位方案
     *
     * @param method  定位方案
     * @param enabled 是否使能，{@code true}为使能，否则为{@code false}
     */
    void setLocationMethodEnabled(@IntegratedLocation.LocateMethod String method, boolean enabled);

    /**
     * 请求一次定位结果
     */
    void requestOnceLocation();

    /**
     * 关闭定位服务
     */
    void stopService();

    /**
     * copy from {@link android.location.LocationListener}
     */
    interface LocationListener {

        /**
         * Called when the location has changed.
         *
         * <p> There are no restrictions on the use of the supplied Location object.
         *
         * @param location The new location, as a Location object.
         */
        void onLocationChanged(@NonNull IntegratedLocation location);
    }

    /**
     * 定位过滤策略
     */
    interface LocationFilterStrategy {

        /**
         * 是否接受某个的定位方案提供的定位信息，某些情况下只根据定位准确度的过滤可能不够，
         * 此时需要启用一些自定义的过滤规则
         *
         * @param location 定位信息
         * @return {@code true}为接收，否则为{@code false}
         */
        boolean accept(@NonNull IntegratedLocation location);
    }
}
