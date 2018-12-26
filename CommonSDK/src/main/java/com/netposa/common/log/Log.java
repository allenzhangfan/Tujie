package com.netposa.common.log;

import android.text.TextUtils;

import com.jess.arms.BuildConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.netposa.common.log.ObjectUtil.objectToString;

public class Log {
    private static final int INVALID = -1;
    private static final String DEFAULTTAG = "TuJie";
    private static final String APPLICATIONID = "com.netposa.tujie";
    private static boolean DEBUG = BuildConfig.LOG_DEBUG;
    //是否在release版本显示大于d等级的log,如log.i,log.w,log.e
    private static boolean SHOW_HIGH_PRIORITY = BuildConfig.LOG_SHOW_HIGH_PRIORITY;
    private static Logger sLogger;

    public static void init() {
        sLogger = LoggerFactory.getLogger(DEFAULTTAG);
    }

    private static String getClassName() {
        StackTraceElement[] sElements = new Throwable().getStackTrace();
        if (sElements != null && sElements.length >= 4) {
            String className = sElements[3].getFileName().replace(".java", "");
            return className;
        }
        return DEFAULTTAG;
    }

    private static void debugAllLog(String tag, String msg) {
        // for debug , set to true
        if (false) {
            android.util.Log.d(DEFAULTTAG, tag + ":" + msg);
        }
    }

    private static int E(String tag, String msg) {
        debugAllLog(tag, msg);
        return android.util.Log.e(tag, msg);
    }

    private static int W(String tag, String msg) {
        debugAllLog(tag, msg);
        return android.util.Log.w(tag, msg);
    }

    private static int I(String tag, String msg) {
        debugAllLog(tag, msg);
        return android.util.Log.i(tag, msg);
    }

    private static int D(String tag, String msg) {
        debugAllLog(tag, msg);
        return android.util.Log.d(tag, msg);
    }

    private static int V(String tag, String msg) {
        debugAllLog(tag, msg);
        return android.util.Log.v(tag, msg);
    }

    public static int e(Object msg) {
        if (DEBUG || SHOW_HIGH_PRIORITY) {
            return E(getTag(null), getMsg(msg));
        }
        return INVALID;
    }

    public static int w(Object msg) {
        if (DEBUG || SHOW_HIGH_PRIORITY) {
            return W(getTag(null), getMsg(msg));
        }
        return INVALID;
    }

    public static int i(Object msg) {
        if (DEBUG || SHOW_HIGH_PRIORITY) {
            return I(getTag(null), getMsg(msg));
        }
        return INVALID;
    }

    public static int d(Object msg) {
        if (DEBUG) {
            return D(getTag(null), getMsg(msg));
        }
        return INVALID;
    }

    public static int v(Object msg) {
        if (DEBUG || SHOW_HIGH_PRIORITY) {
            return V(getTag(null), getMsg(msg));
        }
        return INVALID;
    }

    public static int e() {
        if (DEBUG || SHOW_HIGH_PRIORITY) {
            return E(getTag(null), getStackTraceString(new Throwable()));
        }
        return INVALID;
    }

    public static int w() {
        if (DEBUG || SHOW_HIGH_PRIORITY) {
            return W(getTag(null), getStackTraceString(new Throwable()));
        }
        return INVALID;
    }

    public static int i() {
        if (DEBUG || SHOW_HIGH_PRIORITY) {
            return I(getTag(null), getStackTraceString(new Throwable()));
        }
        return INVALID;
    }

    public static int d() {
        if (DEBUG || SHOW_HIGH_PRIORITY) {
            return D(getTag(null), getStackTraceString(new Throwable()));
        }
        return INVALID;
    }

    public static int v() {
        if (DEBUG || SHOW_HIGH_PRIORITY) {
            return V(getTag(null), getStackTraceString(new Throwable()));
        }
        return INVALID;
    }

    public static int e(String tag, String msg, Throwable tr) {
        return E(getTag(tag), msg + '\n' + getStackTraceString(tr));
    }

    public static int w(String tag, String msg, Throwable tr) {
        return W(getTag(tag), msg + '\n' + getStackTraceString(tr));
    }

    public static int i(String tag, String msg, Throwable tr) {
        return I(getTag(tag), msg + '\n' + getStackTraceString(tr));
    }

    public static int d(String tag, String msg, Throwable tr) {
        return D(getTag(tag), msg + '\n' + getStackTraceString(tr));
    }

    public static int v(String tag, String msg, Throwable tr) {
        return V(getTag(tag), msg + '\n' + getStackTraceString(tr));
    }

    public static String getStackTraceString(Throwable tr) {
        return android.util.Log.getStackTraceString(tr);
    }

    public static int w(String tag, Throwable tr) {
        if (DEBUG || SHOW_HIGH_PRIORITY) {
            return android.util.Log.w(tag, tr);
        }
        return INVALID;
    }

    private static String getMessage(Object obj) {
        if (obj != null) {
            return objectToString(obj);
        }
        return "";
    }

    public static int e(Object tag, Object obj) {
        if (DEBUG || SHOW_HIGH_PRIORITY) {
            return E(getTag(tag), getMsg(obj));
        }
        return INVALID;
    }

    public static int w(Object tag, Object obj) {
        if (DEBUG || SHOW_HIGH_PRIORITY) {
            return W(getTag(tag), getMsg(obj));
        }
        return INVALID;
    }

    public static int i(Object tag, Object obj) {
        if (DEBUG || SHOW_HIGH_PRIORITY) {
            return I(getTag(tag), getMsg(obj));
        }
        return INVALID;
    }

    public static int d(Object tag, Object obj) {
        if (DEBUG || SHOW_HIGH_PRIORITY) {
            return D(getTag(tag), getMsg(obj));
        }
        return INVALID;
    }

    public static int v(Object tag, Object obj) {
        if (DEBUG || SHOW_HIGH_PRIORITY) {
            return V(getTag(tag), getMsg(obj));
        }
        return INVALID;
    }

    private static String getTag(Object tag) {
        String logTag = null;
        if (tag != null) {
            if (tag instanceof String) {
                logTag = (String) tag;
            } else {
                logTag = tag.getClass().getSimpleName();
            }
        }
        if (TextUtils.isEmpty(logTag)) {
            logTag = getClassName();
        }
        return logTag;
    }

    private static String getMsg(Object obj) {
        String msg = null;
        if (obj != null) {
            if (obj instanceof String) {
                msg = (String) obj;
            } else {
                msg = getMessage(obj);
            }
        }
        if (DEBUG) {
            msg = appendLogLoaction(msg);
        }
        return msg;
    }

    private static String appendLogLoaction(String msg) {
        StackTraceElement[] sElements = Thread.currentThread().getStackTrace();
        if (sElements != null && sElements.length >= 6) {
            int index = 5;
            String className = sElements[index].getFileName();
            //处理LogF调用
            if (!TextUtils.isEmpty(className) && className.equals("LogF.java")) {
                if (sElements.length >= 7) {
                    index = 6;
                    className = sElements[index].getFileName();
                } else {
                    return msg;
                }
            }
            String methodName = sElements[index].getMethodName();
            int lineNumber = sElements[index].getLineNumber();
            String methodNameShort = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[ (").append(className).append(":").append(lineNumber).append(")#").append(methodNameShort).append(" ] ");
            if (!TextUtils.isEmpty(msg)) {
                stringBuilder.append(msg);
            }
            return stringBuilder.toString();
        }
        return msg;
    }

    //写入sd卡
    public static void i(String tag, String msg) {
        if (TextUtils.isEmpty(msg)) {
            msg = "";
        }
        if (DEBUG) {
            I(getTag(tag), getMsg(msg));
        } else {
            sLogger.info(tag.concat(" : ").concat(msg));
        }
    }

    //写入sd卡
    public static void e(String tag, String msg) {
        if (TextUtils.isEmpty(msg)) {
            msg = "";
        }
        if (DEBUG) {
            E(getTag(tag), getMsg(msg));
        } else {
            sLogger.info(tag.concat(" : ").concat(msg));
        }
    }
}
