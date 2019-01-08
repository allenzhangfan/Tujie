package com.netposa.component.rltk.mvp.model.entity;

import java.util.List;

/**
 * Author：yeguoqiang
 * Created time：2018/12/3 14:14
 */
public class FaceLibraryRequestEntity {
    /**
     * currentPage : 1
     * deviceList : ["47883295999"]
     * endTime : 0
     * eyeGlass : 1
     * gender : 1
     * orgList : ["47883295999"]
     * pageSize : 20
     * startTime : 0
     */

    private int currentPage;
    private long endTime;
    private String eyeGlass;
    private String gender;
    private int pageSize;
    private long startTime;
    private List<String> deviceList;
    private List<String> orgList;
    private String requestId;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getEyeGlass() {
        return eyeGlass;
    }

    public void setEyeGlass(String eyeGlass) {
        this.eyeGlass = eyeGlass;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public List<String> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<String> deviceList) {
        this.deviceList = deviceList;
    }

    public List<String> getOrgList() {
        return orgList;
    }

    public void setOrgList(List<String> orgList) {
        this.orgList = orgList;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
