package com.netposa.component.ytst.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class VehicleTrackRequestEntity implements Parcelable {

    /**
     * endTime : 1542613013000
     * first : 0
     * max : 999
     * plateColor :
     * plateNumber : 京A
     * startTime : 1542617015000
     * vehicleBrand :
     * vehicleColor : 0
     */

    private long endTime;
    private int first;
    private int max;
    private String plateColor;
    private String plateNumber;
    private long startTime;
    private String vehicleBrand;
    private String vehicleColor;

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getPlateColor() {
        return plateColor;
    }

    public void setPlateColor(String plateColor) {
        this.plateColor = plateColor;
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

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.endTime);
        dest.writeInt(this.first);
        dest.writeInt(this.max);
        dest.writeString(this.plateColor);
        dest.writeString(this.plateNumber);
        dest.writeLong(this.startTime);
        dest.writeString(this.vehicleBrand);
        dest.writeString(this.vehicleColor);
    }

    public VehicleTrackRequestEntity() {
    }

    protected VehicleTrackRequestEntity(Parcel in) {
        this.endTime = in.readLong();
        this.first = in.readInt();
        this.max = in.readInt();
        this.plateColor = in.readString();
        this.plateNumber = in.readString();
        this.startTime = in.readLong();
        this.vehicleBrand = in.readString();
        this.vehicleColor = in.readString();
    }

    public static final Creator<VehicleTrackRequestEntity> CREATOR = new Creator<VehicleTrackRequestEntity>() {
        @Override
        public VehicleTrackRequestEntity createFromParcel(Parcel source) {
            return new VehicleTrackRequestEntity(source);
        }

        @Override
        public VehicleTrackRequestEntity[] newArray(int size) {
            return new VehicleTrackRequestEntity[size];
        }
    };
}
