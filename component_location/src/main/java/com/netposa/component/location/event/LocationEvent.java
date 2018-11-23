package com.netposa.component.location.event;

import android.location.Location;

import androidx.core.util.Pools;

/**
 * Created by yexiaokang on 2018/10/19.
 */
public class LocationEvent {

    private static final int MAX_POOL_SIZE = 5;
    private static Pools.Pool<LocationEvent> sPool = new Pools.SynchronizedPool<>(MAX_POOL_SIZE);

    private Location mLocation;

    private LocationEvent(Location location) {
        mLocation = location;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void recycle() {
        mLocation = null;
        sPool.release(this);
    }

    public static LocationEvent obtain(Location location) {
        LocationEvent event = sPool.acquire();
        if (event == null) {
            event = new LocationEvent(location);
        } else {
            event.mLocation = location;
        }
        return event;
    }
}
