package com.netposa.common.mqtt.util;

import android.os.Build;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

/**
 * Created by yexiaokang on 2018/9/6.
 */
@SuppressWarnings("WeakerAccess")
public final class MqttLoggerFactory {

    private static MqttLoggerFactory sMqttLoggerFactory;

    private final CopyOnWriteArrayList<Handler> mHandlers = new CopyOnWriteArrayList<>();
    private final Map<String, Logger> mLoggers = new HashMap<>();


    public static MqttLoggerFactory getInstance() {
        if (sMqttLoggerFactory == null) {
            synchronized (MqttLoggerFactory.class) {
                if (sMqttLoggerFactory == null) {
                    sMqttLoggerFactory = new MqttLoggerFactory();
                }
            }
        }
        return sMqttLoggerFactory;
    }

    private MqttLoggerFactory() {
        if (isAndroid()) {
            addHandler(new AndroidLogHandler());
        } else {
            addHandler(new ConsoleHandler());
        }
    }

    public Logger getLogger(String name) {
        if (TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("logger name is null");
        }
        Logger logger = mLoggers.get(name);
        if (logger != null) {
            return logger;
        } else {
            logger = Logger.getLogger(name);
            logger.setUseParentHandlers(false);
            for (Handler handler : mHandlers) {
                logger.addHandler(handler);
            }
            mLoggers.put(name, logger);
            return logger;
        }
    }

    public void addHandler(Handler handler) {
        if (handler == null) {
            return;
        }
        mHandlers.add(handler);
    }

    public void removeHandler(Handler handler) {
        if (handler == null) {
            return;
        }
        mHandlers.remove(handler);
    }

    public void removeAllHandlers() {
        mHandlers.clear();
    }

    private static boolean isAndroid() {
        try {
            Class.forName("android.os.Build");
            if (Build.VERSION.SDK_INT != 0) {
                return true;
            }
        } catch (ClassNotFoundException ignored) {
        }
        return false;
    }
}
