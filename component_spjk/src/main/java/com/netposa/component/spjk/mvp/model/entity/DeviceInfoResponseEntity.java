package com.netposa.component.spjk.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

@Keep
public class DeviceInfoResponseEntity implements Parcelable {
    /**
     * id : fxSJ000139MWIx2
     * name : 15楼集成研发
     * type : camera
     * orgid : 095400497549783779
     * orgname : 汉阳区分局
     * longitude : 114.40551454823738
     * latitude : 30.49415253207656
     * isOnline : null
     * ability : traffic,face,body,camera,nonmotor
     * deviceId : null
     * cameraType : 0
     * direction : null
     * angle : 0
     * repairunit : null
     * linkman : null
     * linkno : null
     * manufacturer : null
     * playUrl : rtsp://192.168.15.82:554/PVG/live/?PVG=172.16.90.151:2100/admin/admin/av/ONVIF/海康237
     * address : null
     */
    private String id;
    private String name;
    private String type;
    private String orgid;
    private String orgname;
    private String longitude;
    private String latitude;
    private String isOnline;
    private String ability;
    private String deviceId;
    private String cameraType;
    private String direction;
    private String angle;
    private String repairunit;
    private String linkman;
    private String linkno;
    private String manufacturer;
    private String playUrl;
    private String address;

    public DeviceInfoResponseEntity(){}

    protected DeviceInfoResponseEntity(Parcel in) {
        id = in.readString();
        name = in.readString();
        type = in.readString();
        orgid = in.readString();
        orgname = in.readString();
        longitude = in.readString();
        latitude = in.readString();
        ability = in.readString();
        cameraType = in.readString();
        angle = in.readString();
        playUrl = in.readString();
    }

    public static final Creator<DeviceInfoResponseEntity> CREATOR = new Creator<DeviceInfoResponseEntity>() {
        @Override
        public DeviceInfoResponseEntity createFromParcel(Parcel in) {
            return new DeviceInfoResponseEntity(in);
        }

        @Override
        public DeviceInfoResponseEntity[] newArray(int size) {
            return new DeviceInfoResponseEntity[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCameraType() {
        return cameraType;
    }

    public void setCameraType(String cameraType) {
        this.cameraType = cameraType;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getAngle() {
        return angle;
    }

    public void setAngle(String angle) {
        this.angle = angle;
    }

    public String getRepairunit() {
        return repairunit;
    }

    public void setRepairunit(String repairunit) {
        this.repairunit = repairunit;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getLinkno() {
        return linkno;
    }

    public void setLinkno(String linkno) {
        this.linkno = linkno;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DeviceInfoResponseEntity{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", orgid='").append(orgid).append('\'');
        sb.append(", orgname='").append(orgname).append('\'');
        sb.append(", longitude='").append(longitude).append('\'');
        sb.append(", latitude='").append(latitude).append('\'');
        sb.append(", isOnline=").append(isOnline);
        sb.append(", ability='").append(ability).append('\'');
        sb.append(", deviceId=").append(deviceId);
        sb.append(", cameraType='").append(cameraType).append('\'');
        sb.append(", direction=").append(direction);
        sb.append(", angle='").append(angle).append('\'');
        sb.append(", repairunit=").append(repairunit);
        sb.append(", linkman=").append(linkman);
        sb.append(", linkno=").append(linkno);
        sb.append(", manufacturer=").append(manufacturer);
        sb.append(", playUrl='").append(playUrl).append('\'');
        sb.append(", address=").append(address);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(orgid);
        dest.writeString(orgname);
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeString(ability);
        dest.writeString(cameraType);
        dest.writeString(angle);
        dest.writeString(playUrl);
    }
}
