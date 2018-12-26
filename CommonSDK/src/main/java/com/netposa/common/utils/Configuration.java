package com.netposa.common.utils;

import android.os.Environment;

import com.netposa.common.constant.GlobalConstants;

import java.io.File;

/**
 * @author RocXu
 * @class Configuration
 * 负责管理应用程序的存储目录
 */
public class Configuration {

    /**
     * 获取应用程序存储根目录目录
     */
    public static String getRootPath() {
        return GlobalConstants.ROOT_PATH;
    }

    /**
     * 获取缓存目录
     *
     * @return 成功则返回获取到的路径，失败则返回null
     */
    public static String getCacheDirectoryPath() {
        String path = GlobalConstants.CACHE_PATH;
        if (FileUtils.createOrExistsDir(path)) {
            return path;
        } else {
            return null;
        }
    }

    /**
     * 获取App包名相关的缓存储存目录.
     *
     * @return 成功则返回获取到的路径，失败则返回null
     */
    public static String getAppCacheDirectoryPath() {
        File cacheDir = null;
        if (DeviceUtil.isSDCardEnable()) {
            cacheDir = Utils.getContext().getExternalCacheDir();
        } else {
            cacheDir = Utils.getContext().getCacheDir();
        }
        if (!FileUtils.createOrExistsDir(cacheDir)) {
            return null;
        }
        String path = cacheDir.getAbsolutePath();
        if (!path.endsWith("/")) {
            path += "/";
        }
        return path;
    }

    /**
     * 获取头像目录
     *
     * @return 成功则返回获取到的路径，失败则返回null
     */
    public static String getAvatarDirectoryPath() {
        String path = GlobalConstants.AVATAR_PATH;
        if (FileUtils.createOrExistsDir(path)) {
            return path;
        } else {
            return null;
        }
    }

    /**
     * 获取拍照存储目录
     *
     * @return 成功则返回获取到的路径，失败则返回null
     */
    public static String getPictureDirectoryPath() {
        String path = GlobalConstants.PICTURE_PATH;
        if (FileUtils.createOrExistsDir(path)) {
            return path;
        } else {
            return null;
        }
    }

    /**
     * 获取下载目录
     *
     * @return 成功则返回获取到的路径，失败则返回null
     */
    public static String getDownloadDirectoryPath() {
        String path = GlobalConstants.DOWNLOAD_PATH;
        if (FileUtils.createOrExistsDir(path)) {
            return path;
        } else {
            return null;
        }
    }

    /**
     * 获取视频目录
     *
     * @return 成功则返回获取到的路径，失败则返回null
     */
    public static String getVideoDirectoryPath() {
        String path = GlobalConstants.VIDEO_PATH;
        if (FileUtils.createOrExistsDir(path)) {
            return path;
        } else {
            return null;
        }
    }

    /**
     * 获取APK存放目录
     *
     * @return 成功则返回获取到的路径，失败则返回null
     */
    public static String getAPKDirectoryPath() {
        String path = GlobalConstants.APK_PATH;
        if (FileUtils.createOrExistsDir(path)) {
            return path;
        } else {
            return null;
        }
    }

    /**
     * 获取日志目录
     *
     * @return 成功则返回获取到的路径，失败则返回null
     */
    public static String getLogDirectoryPath() {
        String path = GlobalConstants.LOG_PATH;
        if (FileUtils.createOrExistsDir(path)) {
            return path;
        } else {
            return null;
        }
    }

    public static String getExternalROOTPath() {
        return getExternalStorage() + GlobalConstants.MAIN_PATH;
    }

    public static String getInternalROOTPath() {
        return getInternalFilesDir();
    }

    /**
     * 获取内部存放文件的目录
     *
     * @detail /data/data/包名/files/
     */
    public static String getInternalFilesDir() {
        File dir = Utils.getContext().getFilesDir();
        if (dir == null || !dir.exists()) {
            dir = new File(getInternalStorage(), "files");
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
        String filesDir = dir.getAbsolutePath();
        if (!filesDir.endsWith("/")) {
            filesDir += "/";
        }
        return filesDir;
    }

    /**
     * @return 内部存储区数据目录，目录以"/"结尾
     * 获取应用程序内部存储区的数据目录
     */
    public static String getInternalStorage() {
        String internalPath = Utils.getContext().getApplicationInfo().dataDir;
        if (internalPath == null || "".equals(internalPath.trim())) {
            internalPath = "/data/data/" + Utils.getContext().getPackageName() + "/";
        } else if (!internalPath.endsWith("/")) {
            internalPath += "/";
        }
        return internalPath;
    }

    /**
     * @return 应用程序外部存储区目录，目录以"/"结尾
     * 获取应用程序外部存储区的目录
     */
    public static String getExternalStorage() {
        String externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        if (externalPath == null || "".equals(externalPath.trim()) || !FileUtils.isFileExists(externalPath)) {
            externalPath = GlobalConstants.DEFAULT_EXTERNAL_PATH;
        } else if (!externalPath.endsWith("/")) {
            externalPath += "/";
        }
        return externalPath;
    }
}
