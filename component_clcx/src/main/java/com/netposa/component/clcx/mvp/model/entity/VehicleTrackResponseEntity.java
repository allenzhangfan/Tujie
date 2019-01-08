package com.netposa.component.clcx.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class VehicleTrackResponseEntity implements Parcelable {
    /**
     * count : 10
     * list : [{"monitorId":"001z48vu3WkC9W1u","monitorName":"连云港人体视频_5","id":"8c1f35f7-f1d3-4c7e-ac6d-0cf24db341d0","passTime":1531202601000,"y":34.2257951591,"x":108.9644172497,"sceneImg":"PFSB:/viasbimg/bimg/data/20180710/14_0/d52ad22ca0cd745d2064c89b98d7d705","location":"1301.525.1891.950"},{"monitorId":"001z48vu3WkC9W1u","monitorName":"连云港人体视频_5","id":"0f1cdace-d87d-4ccd-869b-60f33c560a92","passTime":1531206299000,"y":34.2257951591,"x":108.9644172497,"sceneImg":"PFSB:/viasbimg/bimg/data/20180710/15_0/640886e0d14ec0a9aaa1e85f2053e98e","location":"1490.496.1916.852"}]
     */

    private int count;
    private List<ListBean> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Parcelable {
        /**
         * monitorId : 001z48vu3WkC9W1u
         * monitorName : 连云港人体视频_5
         * id : 8c1f35f7-f1d3-4c7e-ac6d-0cf24db341d0
         * passTime : 1531202601000
         * y : 34.2257951591
         * x : 108.9644172497
         * sceneImg : PFSB:/viasbimg/bimg/data/20180710/14_0/d52ad22ca0cd745d2064c89b98d7d705
         * location : 1301.525.1891.950
         */

        private String monitorId;
        private String monitorName;
        private String id;
        private long passTime;
        private double y;
        private double x;
        private String sceneImg;
        private String location;

        public String getMonitorId() {
            return monitorId;
        }

        public void setMonitorId(String monitorId) {
            this.monitorId = monitorId;
        }

        public String getMonitorName() {
            return monitorName;
        }

        public void setMonitorName(String monitorName) {
            this.monitorName = monitorName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public long getPassTime() {
            return passTime;
        }

        public void setPassTime(long passTime) {
            this.passTime = passTime;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public String getSceneImg() {
            return sceneImg;
        }

        public void setSceneImg(String sceneImg) {
            this.sceneImg = sceneImg;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.monitorId);
            dest.writeString(this.monitorName);
            dest.writeString(this.id);
            dest.writeLong(this.passTime);
            dest.writeDouble(this.y);
            dest.writeDouble(this.x);
            dest.writeString(this.sceneImg);
            dest.writeString(this.location);
        }

        public ListBean() {
        }

        protected ListBean(Parcel in) {
            this.monitorId = in.readString();
            this.monitorName = in.readString();
            this.id = in.readString();
            this.passTime = in.readLong();
            this.y = in.readDouble();
            this.x = in.readDouble();
            this.sceneImg = in.readString();
            this.location = in.readString();
        }

        public static final Creator<ListBean> CREATOR = new Creator<ListBean>() {
            @Override
            public ListBean createFromParcel(Parcel source) {
                return new ListBean(source);
            }

            @Override
            public ListBean[] newArray(int size) {
                return new ListBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.count);
        dest.writeList(this.list);
    }

    public VehicleTrackResponseEntity() {
    }

    protected VehicleTrackResponseEntity(Parcel in) {
        this.count = in.readInt();
        this.list = new ArrayList<ListBean>();
        in.readList(this.list, ListBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<VehicleTrackResponseEntity> CREATOR = new Parcelable.Creator<VehicleTrackResponseEntity>() {
        @Override
        public VehicleTrackResponseEntity createFromParcel(Parcel source) {
            return new VehicleTrackResponseEntity(source);
        }

        @Override
        public VehicleTrackResponseEntity[] newArray(int size) {
            return new VehicleTrackResponseEntity[size];
        }
    };
}
