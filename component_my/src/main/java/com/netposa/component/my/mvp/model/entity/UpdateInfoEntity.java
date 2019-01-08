package com.netposa.component.my.mvp.model.entity;

import com.google.gson.annotations.SerializedName;

public class UpdateInfoEntity {
    /**
     * forceUpdate : false
     * intervalTimes : -1
     * minVersion : 0
     * relativePath : /ia/app/updateVersion
     * totalTimes : -1
     * updateJson : false
     * versionDescription : 该升级啦!
     * versionName : v1.0.0
     * versionNumber : 1
     */
    @SerializedName(value = "forceUpdate")
    private boolean forceUpdate;
    @SerializedName(value = "intervalTimes")
    private int intervalTimes;
    @SerializedName(value = "minVersion")
    private int minVersion;
    @SerializedName(value = "relativePath")
    private String relativePath;
    @SerializedName(value = "totalTimes")
    private int totalTimes;
    @SerializedName(value = "updateJson")
    private boolean updateJson;
    @SerializedName(value = "versionDescription")
    private String versionDescription;
    @SerializedName(value = "versionName")
    private String versionName;
    @SerializedName(value = "versionNumber")
    private int versionNumber;

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public int getIntervalTimes() {
        return intervalTimes;
    }

    public void setIntervalTimes(int intervalTimes) {
        this.intervalTimes = intervalTimes;
    }

    public int getMinVersion() {
        return minVersion;
    }

    public void setMinVersion(int minVersion) {
        this.minVersion = minVersion;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public int getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(int totalTimes) {
        this.totalTimes = totalTimes;
    }

    public boolean isUpdateJson() {
        return updateJson;
    }

    public void setUpdateJson(boolean updateJson) {
        this.updateJson = updateJson;
    }

    public String getVersionDescription() {
        return versionDescription;
    }

    public void setVersionDescription(String versionDescription) {
        this.versionDescription = versionDescription;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }
}
