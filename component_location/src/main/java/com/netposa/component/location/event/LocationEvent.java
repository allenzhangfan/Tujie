package com.netposa.component.location.event;

import com.netposa.common.service.location.IntegratedLocation;

import androidx.core.util.Pools;

/**
 * Created by yexiaokang on 2018/10/19.
 */
public class LocationEvent {

    private static final int MAX_POOL_SIZE = 5;
    private static Pools.Pool<LocationEvent> sPool = new Pools.SynchronizedPool<>(MAX_POOL_SIZE);

    private IntegratedLocation mLocation;

    private LocationEvent(IntegratedLocation location) {
        mLocation = location;
    }

    public IntegratedLocation getLocation() {
        return mLocation;
    }

    public void recycle() {
        mLocation = null;
        sPool.release(this);
    }

    public static LocationEvent obtain(IntegratedLocation location) {
        LocationEvent event = sPool.acquire();
        if (event == null) {
            event = new LocationEvent(location);
        } else {
            event.mLocation = location;
        }
        return event;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LocationEvent{");
        sb.append("LocateMethod:").append(mLocation.getLocateMethod());
        sb.append('}');
        return sb.toString();
    }
}
