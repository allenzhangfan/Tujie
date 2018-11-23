package com.netposa.component.spjk.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import androidx.annotation.Keep;

/**
 * Author：yeguoqiang
 * Created time：2018/11/12 21:09
 */
@Keep
public class OneKilometerCamerasResponseEntity implements Parcelable {

    /**
     * id : fxSJ000139MWIx2
     * name : 15楼集成研发
     * ability : traffic,face,body,camera,nonmotor
     * cameraType : 0
     * longitude : 114.40551454823738
     * latitude : 30.49415253207656
     * parentId : 095400497549783779
     * structureType : ["2"]
     * address : null
     */
    private String id;
    private String name;
    private String ability;
    private String cameraType;
    private String longitude;
    private String latitude;
    private String parentId;
    private String address;
    private List<String> structureType;

    public OneKilometerCamerasResponseEntity() {
    }

    protected OneKilometerCamerasResponseEntity(Parcel in) {
        id = in.readString();
        name = in.readString();
        ability = in.readString();
        cameraType = in.readString();
        longitude = in.readString();
        latitude = in.readString();
        parentId = in.readString();
        address = in.readString();
        structureType = in.createStringArrayList();
    }

    public static final Creator<OneKilometerCamerasResponseEntity> CREATOR = new Creator<OneKilometerCamerasResponseEntity>() {
        @Override
        public OneKilometerCamerasResponseEntity createFromParcel(Parcel in) {
            return new OneKilometerCamerasResponseEntity(in);
        }

        @Override
        public OneKilometerCamerasResponseEntity[] newArray(int size) {
            return new OneKilometerCamerasResponseEntity[size];
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

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public String getCameraType() {
        return cameraType;
    }

    public void setCameraType(String cameraType) {
        this.cameraType = cameraType;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getStructureType() {
        return structureType;
    }

    public void setStructureType(List<String> structureType) {
        this.structureType = structureType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OneKilometerCamerasResponseEntity{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", ability='").append(ability).append('\'');
        sb.append(", cameraType='").append(cameraType).append('\'');
        sb.append(", longitude='").append(longitude).append('\'');
        sb.append(", latitude='").append(latitude).append('\'');
        sb.append(", parentId='").append(parentId).append('\'');
        sb.append(", address=").append(address);
        sb.append(", structureType=").append(structureType);
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
        dest.writeString(ability);
        dest.writeString(cameraType);
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeString(parentId);
        dest.writeString(address);
        dest.writeStringList(structureType);
    }
}

