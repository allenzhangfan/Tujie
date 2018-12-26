package com.netposa.component.mqtt.event;

import android.os.Bundle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.core.util.Pools;

/**
 * Created by yexiaokang on 2018/9/28.
 */
@SuppressWarnings("WeakerAccess")
public class MqttEvent {

    private static final int MAX_POOL_SIZE = 5;
    private static Pools.Pool<MqttEvent> sPool = new Pools.SynchronizedPool<>(MAX_POOL_SIZE);


    public static final int TYPE_INIT = 0;
    public static final int TYPE_OVER = 1;
    public static final int TYPE_ACTIVATE = 2;

    @IntDef({
            TYPE_INIT,
            TYPE_OVER,
            TYPE_ACTIVATE
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface EventType {
    }

    private int eventType;
    private Bundle mExtra;

    private MqttEvent(@EventType int eventType) {
        this.eventType = eventType;
        mExtra = new Bundle();
    }

    @EventType
    public int getEventType() {
        return eventType;
    }

    public void setEventType(@EventType int eventType) {
        this.eventType = eventType;
    }

    @NonNull
    public Bundle getExtra() {
        return mExtra;
    }

    public void recycle() {
        mExtra.clear();
        sPool.release(this);
    }

    public static MqttEvent obtain(@EventType int eventType) {
        MqttEvent event = sPool.acquire();
        if (event == null) {
            event = new MqttEvent(eventType);
        } else {
            event.setEventType(eventType);
        }
        return event;
    }
}
