package com.netposa.common.core;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.log4j.LogConfigurator;
import android.util.Log;

import com.jess.arms.base.delegate.AppLifecycles;
import com.netposa.common.BuildConfig;
import com.netposa.common.utils.FileUtils;

import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import androidx.annotation.NonNull;

import static com.netposa.common.constant.GlobalConstants.LOG_FILE_NAME;
import static com.netposa.common.constant.GlobalConstants.LOG_PATH;


/**
 * Created by yexiaokang on 2018/9/27.
 */
public class Log4jAppLifecycle implements AppLifecycles, Thread.UncaughtExceptionHandler {

    private static final String TAG = "Log4jAppLifecycle";

    private Logger mLogger;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private HandlerThread mHandlerThread = new HandlerThread(TAG);

    @Override
    public void attachBaseContext(@NonNull Context base) {
        mHandlerThread.start();
    }

    @Override
    public void onCreate(@NonNull Application application) {
        Handler handler = new Handler(mHandlerThread.getLooper());
        handler.post(() -> {
            if (Build.VERSION.SDK_INT >= 23) {
                if (application.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.os.Process.myPid(),
                        android.os.Process.myUid()) == PackageManager.PERMISSION_GRANTED) {
                    log4jConfigure(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED));
                } else {
                    Log.w(TAG, "onCreate: No WRITE_EXTERNAL_STORAGE Permission");
                    log4jConfigure(false);
                }
            } else {
                log4jConfigure(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED));
            }
            mLogger = LoggerFactory.getLogger(TAG);
            mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(Log4jAppLifecycle.this);
            mHandlerThread.quitSafely();
        });
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (mLogger != null) {
            mLogger.error("uncaughtException", e);
        }
        mDefaultHandler.uncaughtException(t, e);
    }

    /**
     * %m 输出代码中指定的消息<br/>
     * %p 输出优先级，即DEBUG，INFO，WARN，ERROR，FATAL；5表示宽度为5（即输出结果以5个字符宽度对齐），负号表示左对齐<br/>
     * %r 输出自应用启动到输出该log信息耗费的毫秒数<br/>
     * %c 输出所属的类目，通常就是所在类的全名<br/>
     * %t 输出产生该日志事件的线程名<br/>
     * %n 输出一个回车换行符，Windows平台为“rn”，Unix平台为“n”<br/>
     * %d 输出日志时间点的日期或时间，默认格式为ISO8601，也可以在其后指定格式，比如：%d{yyyy MMM dd HH:mm:ss,SSS}，输出类似：2002年10月18日 22：10：28，921<br/>
     * %l 输出日志事件的发生位置，包括类目名、发生的线程，以及在代码中的行数。<br/>
     *
     * @param useFileAppender useFileAppender
     */
    public static void log4jConfigure(boolean useFileAppender) {
        FileUtils.createOrExistsDir(LOG_PATH);
        String fileName = LOG_PATH + LOG_FILE_NAME;
        try {
            LogConfigurator logConfigurator = new LogConfigurator();
            logConfigurator.setResetConfiguration(true);
            logConfigurator.setFileName(fileName);
            logConfigurator.setRootLevel(Level.DEBUG);
            logConfigurator.setLevel("org.apache", Level.ERROR);
//            logConfigurator.setFilePattern("%d{yyyy-MM-dd HH:mm:ss.SSS} %-5p [%t] [%c{2}]-[%L] %m%n");
            logConfigurator.setFilePattern("%d{yyyy-MM-dd HH:mm:ss.SSS} %-5p [%c{1}] %m%n");
            logConfigurator.setLogCatPattern("%m%n");
            logConfigurator.setMaxFileSize(1024 * 1024 * 5);
            logConfigurator.setUseFileAppender(useFileAppender);
            logConfigurator.setUseLogCatAppender(BuildConfig.DEBUG);
            logConfigurator.setImmediateFlush(true);
            logConfigurator.setMaxBackupDays(5);
            logConfigurator.setMaxBackupSize(10);
            logConfigurator.setAsyncFileAppender(true);
            logConfigurator.configure();
        } catch (Exception e) {
            Log.e(TAG, "log4jConfigure", e);
        }
    }
}
