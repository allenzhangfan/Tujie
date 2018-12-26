package com.netposa.common.entity.push;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * Created by yexiaokang on 2018/9/29.
 */
@SuppressWarnings("WeakerAccess")
public class NotificationEvent {

    public static final int TYPE_DCJ = 0;
    public static final int TYPE_ALARM = 1;

    @IntDef({
            TYPE_DCJ,
            TYPE_ALARM
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface EventType {
    }


    private final int eventType;

    public NotificationEvent(@EventType int eventType) {
        this.eventType = eventType;
    }

    @EventType
    public int getEventType() {
        return eventType;
    }
}
