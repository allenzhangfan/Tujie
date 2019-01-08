package com.netposa.component.clcx.mvp.model.entity;

import java.util.ArrayList;

public class QueryCarSearchEntity {

    /**
     * currentPage : 1
     * endTime : 1542961264122
     * pageSize : 20
     * plateColors : []
     * plateNumber : string
     * plateTypes : []
     * startTime : 1542442864122
     * vehicleColors : []
     * vehicleTypes : []
     */

    private int currentPage;
    private long endTime;
    private int pageSize;
    private String plateNumber;
    private long startTime;
    private ArrayList<String> plateColors;
    private ArrayList<String> plateTypes;
    private ArrayList<String> vehicleColors;
    private ArrayList<String> vehicleTypes;
    private String lastPassId;
    private Long lastPassTime;

    public QueryCarSearchEntity() {
    }

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

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public ArrayList<String> getPlateColors() {
        return plateColors;
    }

    public void setPlateColors(ArrayList<String> plateColors) {
        this.plateColors = plateColors;
    }

    public ArrayList<String> getPlateTypes() {
        return plateTypes;
    }

    public void setPlateTypes(ArrayList<String> plateTypes) {
        this.plateTypes = plateTypes;
    }

    public ArrayList<String> getVehicleColors() {
        return vehicleColors;
    }

    public void setVehicleColors(ArrayList<String> vehicleColors) {
        this.vehicleColors = vehicleColors;
    }

    public ArrayList<String> getVehicleTypes() {
        return vehicleTypes;
    }

    public void setVehicleTypes(ArrayList<String> vehicleTypes) {
        this.vehicleTypes = vehicleTypes;
    }

    public String getLastPassId() {
        return lastPassId;
    }

    public void setLastPassId(String lastPassId) {
        this.lastPassId = lastPassId;
    }

    public Long getLastPassTime() {
        return lastPassTime;
    }

    public void setLastPassTime(Long lastPassTime) {
        this.lastPassTime = lastPassTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("QueryCarSearchEntity{");
        sb.append("currentPage=").append(currentPage);
        sb.append(", endTime=").append(endTime);
        sb.append(", pageSize=").append(pageSize);
        sb.append(", plateNumber='").append(plateNumber).append('\'');
        sb.append(", startTime=").append(startTime);
        sb.append(", plateColors=").append(plateColors);
        sb.append(", plateTypes=").append(plateTypes);
        sb.append(", vehicleColors=").append(vehicleColors);
        sb.append(", vehicleTypes=").append(vehicleTypes);
        sb.append(", lastPassId='").append(lastPassId).append('\'');
        sb.append(", lastPassTime=").append(lastPassTime);
        sb.append('}');
        return sb.toString();
    }
}
