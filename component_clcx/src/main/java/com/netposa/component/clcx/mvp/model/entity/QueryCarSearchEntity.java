package com.netposa.component.clcx.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import androidx.annotation.Keep;

@Keep
public class QueryCarSearchEntity implements Parcelable {

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
    private List<String> plateColors;
    private List<String> plateTypes;
    private List<String> vehicleColors;
    private List<String> vehicleTypes;

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

    public List<?> getPlateColors() {
        return plateColors;
    }

    public void setPlateColors(List<String> plateColors) {
        this.plateColors = plateColors;
    }

    public List<String> getPlateTypes() {
        return plateTypes;
    }

    public void setPlateTypes(List<String> plateTypes) {
        this.plateTypes = plateTypes;
    }

    public List<String> getVehicleColors() {
        return vehicleColors;
    }

    public void setVehicleColors(List<String> vehicleColors) {
        this.vehicleColors = vehicleColors;
    }

    public List<?> getVehicleTypes() {
        return vehicleTypes;
    }

    public void setVehicleTypes(List<String> vehicleTypes) {
        this.vehicleTypes = vehicleTypes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.currentPage);
        dest.writeLong(this.endTime);
        dest.writeInt(this.pageSize);
        dest.writeString(this.plateNumber);
        dest.writeLong(this.startTime);
        dest.writeStringList(this.plateColors);
        dest.writeStringList(this.plateTypes);
        dest.writeStringList(this.vehicleColors);
        dest.writeStringList(this.vehicleTypes);
    }

    public QueryCarSearchEntity() {
    }

    protected QueryCarSearchEntity(Parcel in) {
        this.currentPage = in.readInt();
        this.endTime = in.readLong();
        this.pageSize = in.readInt();
        this.plateNumber = in.readString();
        this.startTime = in.readLong();
        this.plateColors = in.createStringArrayList();
        this.plateTypes = in.createStringArrayList();
        this.vehicleColors = in.createStringArrayList();
        this.vehicleTypes = in.createStringArrayList();
    }

    public static final Creator<QueryCarSearchEntity> CREATOR = new Creator<QueryCarSearchEntity>() {
        @Override
        public QueryCarSearchEntity createFromParcel(Parcel source) {
            return new QueryCarSearchEntity(source);
        }

        @Override
        public QueryCarSearchEntity[] newArray(int size) {
            return new QueryCarSearchEntity[size];
        }
    };
}
