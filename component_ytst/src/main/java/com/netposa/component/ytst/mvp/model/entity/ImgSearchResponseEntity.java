package com.netposa.component.ytst.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

@Keep
public class ImgSearchResponseEntity  {

    /**
     * data : {"gender":0,"traitImg":"http://172.16.90.168:6551/DownLoadFile?filename=PFSB:/viasbimg/bimg/data/20181219/17_0/667b7ca8ce28f779cf0370dc303e4677","latitude":30.494255570819995,"source":9,"deviceId":"fxSJ000139MWIx1N","deviceName":"15楼集成研发","traitLocation":null,"smile":-1,"recordId":"536ea1d4-823a-4674-9960-33cc56294e30","sceneImg":"http://172.16.90.168:6551/DownLoadFile?filename=PFSB:/viasbimg/bimg/data/20181219/17_0/211cd1ae3be0c15b1093bb6dddbe402d","eyeGlass":1,"startTime":1545210192749,"saveTime":1545210192760,"pushTime":1545210307031,"mask":-1,"longitude":114.40570015462814,"sunGlass":0,"attractive":-1,"expression":-1,"confidence":0,"nodeType":"camera","absTime":1545210192749,"cameraType":"0","location":"1552.548.1606.602","endTime":1545210192749,"age":31,"_type":"face","relation_record":"{}","_recordId":"536ea1d4-823a-4674-9960-33cc56294e30","_absTime":1545210192749,"score":94}
     * requestIdMap : {}
     */

    private DataBean data;
    private RequestIdMapBean requestIdMap;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public RequestIdMapBean getRequestIdMap() {
        return requestIdMap;
    }

    public void setRequestIdMap(RequestIdMapBean requestIdMap) {
        this.requestIdMap = requestIdMap;
    }

    public static class DataBean implements Parcelable {
        /**
         * gender : 0
         * traitImg : http://172.16.90.168:6551/DownLoadFile?filename=PFSB:/viasbimg/bimg/data/20181219/17_0/667b7ca8ce28f779cf0370dc303e4677
         * latitude : 30.494255570819995
         * source : 9
         * deviceId : fxSJ000139MWIx1N
         * deviceName : 15楼集成研发
         * traitLocation : null
         * smile : -1
         * recordId : 536ea1d4-823a-4674-9960-33cc56294e30
         * sceneImg : http://172.16.90.168:6551/DownLoadFile?filename=PFSB:/viasbimg/bimg/data/20181219/17_0/211cd1ae3be0c15b1093bb6dddbe402d
         * eyeGlass : 1
         * startTime : 1545210192749
         * saveTime : 1545210192760
         * pushTime : 1545210307031
         * mask : -1
         * longitude : 114.40570015462814
         * sunGlass : 0
         * attractive : -1
         * expression : -1
         * confidence : 0
         * nodeType : camera
         * absTime : 1545210192749
         * cameraType : 0
         * location : 1552.548.1606.602
         * endTime : 1545210192749
         * age : 31
         * _type : face
         * relation_record : {}
         * _recordId : 536ea1d4-823a-4674-9960-33cc56294e30
         * _absTime : 1545210192749
         * score : 94
         */

        private int gender;
        private String traitImg;
        private double latitude;
        private int source;
        private String deviceId;
        private String deviceName;
        private String traitLocation;
        private int smile;
        private String recordId;
        private String sceneImg;
        private int eyeGlass;
        private Long startTime;
        private Long saveTime;
        private Long pushTime;
        private String mask;
        private double longitude;
        private int sunGlass;
        private String attractive;

        private String expression;
        private int confidence;
        private String nodeType;
        private long absTime;

        private String cameraType;
        private String location;
        private long endTime;
        private int age;

        private String _type;
        private String relation_record;
        private String _recordId;
        private long _absTime;
        private int score;
        private String plateNumber;
        private boolean select;

        protected DataBean(Parcel in) {
            gender = in.readInt();
            traitImg = in.readString();
            latitude = in.readDouble();
            source = in.readInt();
            deviceId = in.readString();
            deviceName = in.readString();
            smile = in.readInt();
            recordId = in.readString();
            sceneImg = in.readString();
            eyeGlass = in.readInt();
            if (in.readByte() == 0) {
                startTime = null;
            } else {
                startTime = in.readLong();
            }
            if (in.readByte() == 0) {
                saveTime = null;
            } else {
                saveTime = in.readLong();
            }
            if (in.readByte() == 0) {
                pushTime = null;
            } else {
                pushTime = in.readLong();
            }
            mask = in.readString();
            longitude = in.readDouble();
            sunGlass = in.readInt();
            attractive = in.readString();
            expression = in.readString();
            confidence = in.readInt();
            nodeType = in.readString();
            absTime = in.readLong();
            cameraType = in.readString();
            location = in.readString();
            endTime = in.readLong();
            age = in.readInt();
            _type = in.readString();
            relation_record = in.readString();
            _recordId = in.readString();
            _absTime = in.readLong();
            score = in.readInt();
            plateNumber = in.readString();
            select = in.readByte() != 0;
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel in) {
                return new DataBean(in);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

        public String getSceneImg() {
            return sceneImg;
        }

        public void setSceneImg(String sceneImg) {
            this.sceneImg = sceneImg;
        }

        public int getEyeGlass() {
            return eyeGlass;
        }

        public void setEyeGlass(int eyeGlass) {
            this.eyeGlass = eyeGlass;
        }

        public Long getStartTime() {
            return startTime;
        }

        public void setStartTime(Long startTime) {
            this.startTime = startTime;
        }

        public Long getSaveTime() {
            return saveTime;
        }

        public void setSaveTime(Long saveTime) {
            this.saveTime = saveTime;
        }

        public Long getPushTime() {
            return pushTime;
        }

        public void setPushTime(Long pushTime) {
            this.pushTime = pushTime;
        }

        public String getMask() {
            return mask;
        }

        public void setMask(String mask) {
            this.mask = mask;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public int getSunGlass() {
            return sunGlass;
        }

        public void setSunGlass(int sunGlass) {
            this.sunGlass = sunGlass;
        }

        public String getAttractive() {
            return attractive;
        }

        public void setAttractive(String attractive) {
            this.attractive = attractive;
        }

        public String getExpression() {
            return expression;
        }

        public void setExpression(String expression) {
            this.expression = expression;
        }

        public int getConfidence() {
            return confidence;
        }

        public void setConfidence(int confidence) {
            this.confidence = confidence;
        }

        public String getNodeType() {
            return nodeType;
        }

        public void setNodeType(String nodeType) {
            this.nodeType = nodeType;
        }

        public long getAbsTime() {
            return absTime;
        }

        public void setAbsTime(long absTime) {
            this.absTime = absTime;
        }

        public String getCameraType() {
            return cameraType;
        }

        public void setCameraType(String cameraType) {
            this.cameraType = cameraType;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String get_type() {
            return _type;
        }

        public void set_type(String _type) {
            this._type = _type;
        }

        public String getRelation_record() {
            return relation_record;
        }

        public void setRelation_record(String relation_record) {
            this.relation_record = relation_record;
        }

        public String get_recordId() {
            return _recordId;
        }

        public void set_recordId(String _recordId) {
            this._recordId = _recordId;
        }

        public long get_absTime() {
            return _absTime;
        }

        public void set_absTime(long _absTime) {
            this._absTime = _absTime;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getPlateNumber() {
            return plateNumber;
        }

        public void setPlateNumber(String plateNumber) {
            this.plateNumber = plateNumber;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public String getTraitImg() {
            return traitImg;
        }

        public void setTraitImg(String traitImg) {
            this.traitImg = traitImg;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public int getSource() {
            return source;
        }

        public void setSource(int source) {
            this.source = source;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getTraitLocation() {
            return traitLocation;
        }

        public void setTraitLocation(String traitLocation) {
            this.traitLocation = traitLocation;
        }

        public int getSmile() {
            return smile;
        }

        public void setSmile(int smile) {
            this.smile = smile;
        }

        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(gender);
            parcel.writeString(traitImg);
            parcel.writeDouble(latitude);
            parcel.writeInt(source);
            parcel.writeString(deviceId);
            parcel.writeString(deviceName);
            parcel.writeInt(smile);
            parcel.writeString(recordId);
            parcel.writeString(sceneImg);
            parcel.writeInt(eyeGlass);
            if (startTime == null) {
                parcel.writeByte((byte) 0);
            } else {
                parcel.writeByte((byte) 1);
                parcel.writeLong(startTime);
            }
            if (saveTime == null) {
                parcel.writeByte((byte) 0);
            } else {
                parcel.writeByte((byte) 1);
                parcel.writeLong(saveTime);
            }
            if (pushTime == null) {
                parcel.writeByte((byte) 0);
            } else {
                parcel.writeByte((byte) 1);
                parcel.writeLong(pushTime);
            }
            parcel.writeString(mask);
            parcel.writeDouble(longitude);
            parcel.writeInt(sunGlass);
            parcel.writeString(attractive);
            parcel.writeString(expression);
            parcel.writeInt(confidence);
            parcel.writeString(nodeType);
            parcel.writeLong(absTime);
            parcel.writeString(cameraType);
            parcel.writeString(location);
            parcel.writeLong(endTime);
            parcel.writeInt(age);
            parcel.writeString(_type);
            parcel.writeString(relation_record);
            parcel.writeString(_recordId);
            parcel.writeLong(_absTime);
            parcel.writeInt(score);
            parcel.writeString(plateNumber);
            parcel.writeByte((byte) (select ? 1 : 0));
        }
    }

    public static class RequestIdMapBean {
    }
}
