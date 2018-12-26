package com.netposa.component.rltk.mvp.model.entity;

import androidx.annotation.Keep;

@Keep
public class FaceDetailResponseEntity {
        /**
         * recordId : d1f551a2-a38d-4ac5-bb64-0ed82fbcf696
         * confidence : 0
         * longitude : 114.40449144195695
         * latitude : 30.493912558923796
         * startTime : 1543994365470
         * endTime : 1543994365470
         * saveTime : 1543994365477
         * absTime : 1543994365470
         * sunGlass : 0
         * sceneImg : http://172.16.90.168:6551/DownLoadFile?filename=PFSB:/viasbimg/bimg/data/20181205/15_0/a7efb17c6f2c3e6c8657ddaa61e6d080
         * expression : -1
         * deviceName : 大门36
         * mask : -1
         * eyeGlass : 0
         * pushTime : 1543994484036
         * traitImg : http://172.16.90.168:6551/DownLoadFile?filename=PFSB:/viasbimg/bimg/data/20181205/15_0/a23abde3062d4cee6f6f64599aed47d3
         * attractive : -1
         * deviceId : fxSJ000139MWIx1M
         * age : 32
         * location : 1581.522.1641.582
         * gender : 1
         * cameraType : 0
         */

        private String recordId;
        private int confidence;
        private double longitude;
        private double latitude;
        private long startTime;
        private long endTime;
        private long saveTime;
        private long absTime;
        private int sunGlass;
        private String sceneImg;
        private int expression;
        private String deviceName;
        private int mask;
        private int eyeGlass;
        private long pushTime;
        private String traitImg;
        private int attractive;
        private String deviceId;
        private int age;
        private String location;
        private String gender;
        private String cameraType;

        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public int getConfidence() {
            return confidence;
        }

        public void setConfidence(int confidence) {
            this.confidence = confidence;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public long getSaveTime() {
            return saveTime;
        }

        public void setSaveTime(long saveTime) {
            this.saveTime = saveTime;
        }

        public long getAbsTime() {
            return absTime;
        }

        public void setAbsTime(long absTime) {
            this.absTime = absTime;
        }

        public int getSunGlass() {
            return sunGlass;
        }

        public void setSunGlass(int sunGlass) {
            this.sunGlass = sunGlass;
        }

        public String getSceneImg() {
            return sceneImg;
        }

        public void setSceneImg(String sceneImg) {
            this.sceneImg = sceneImg;
        }

        public int getExpression() {
            return expression;
        }

        public void setExpression(int expression) {
            this.expression = expression;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public int getMask() {
            return mask;
        }

        public void setMask(int mask) {
            this.mask = mask;
        }

        public int getEyeGlass() {
            return eyeGlass;
        }

        public void setEyeGlass(int eyeGlass) {
            this.eyeGlass = eyeGlass;
        }

        public long getPushTime() {
            return pushTime;
        }

        public void setPushTime(long pushTime) {
            this.pushTime = pushTime;
        }

        public String getTraitImg() {
            return traitImg;
        }

        public void setTraitImg(String traitImg) {
            this.traitImg = traitImg;
        }

        public int getAttractive() {
            return attractive;
        }

        public void setAttractive(int attractive) {
            this.attractive = attractive;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getCameraType() {
            return cameraType;
        }

        public void setCameraType(String cameraType) {
            this.cameraType = cameraType;
        }
}
