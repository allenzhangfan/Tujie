package com.netposa.component.jq.mvp.model.entity;

import java.util.List;

import androidx.annotation.Keep;

@Keep
public class ProcessAlarmResponseEntity {
        /**
         * entity : {"id":"6b589337-10c3-4635-916f-a8ee894f5ea2","absTime":1543988277202,"alarmTime":1543988277299,"alarmType":"2","eventType":null,"deviceId":"fxSJ000139MWIx1M","alarmAddress":"大门36","latitude":null,"longitude":null,"alarmStatus":"2","status":"1","alarmLevel":"1","imageUrl":"http://172.16.90.168:6551/DownLoadFile?filename=PFSB:/viasbimg/bimg/data/20181205/13_0/a73db965d73ab9ef3e097aa4d3b534fa","userId":"af2005b8-59df-4a71-a231-e0e6d232b60c","userName":"管理员","taskId":"e443fb44-baa6-491f-b300-cd8079c3af13","taskName":null,"taskReason":null,"source":"vdt","note":"当前警情有效","ext":"[{\"birthday\":\"\",\"libId\":\"517697470927470592\",\"nation\":\"未知\",\"sex\":\"0\",\"libName\":\"\",\"remark\":\"\",\"targetImage\":\"PFSB:/viasbimg/bimg/data/20181204/14_0/d1065061a3c4fd28957dcf22ba883c65\",\"score\":57,\"createTime\":\"2018-12-04 14:07:21\",\"idcard\":\"\",\"census\":\"\",\"name\":\"111\",\"personId\":\"519515057676091392\"}]","location":null,"carAlarmType":null,"loginId":"74534477-9089-46a0-9cec-2b63381f5a08,af2005b8-59df-4a71-a231-e0e6d232b60c,175ebc50-9794-43cc-810f-cb34cdbdfec9","libId":"517697470927470592","libName":"","score":57,"recordId":"6d6e0887-0f47-4604-9cc0-ad04ef765179","targetImage":null,"name":"111","sex":null,"adminList":[],"idCard":"","threshold":null,"realAlarm":-1,"faceId":null,"vehicleId":null,"wifiId":null,"rfidId":null,"number":0,"faceIds":null,"carIds":null,"wifiIds":null,"rfidIds":null}
         */

        private EntityBean entity;

        public EntityBean getEntity() {
            return entity;
        }

        public void setEntity(EntityBean entity) {
            this.entity = entity;
        }

        public static class EntityBean {
            /**
             * id : 6b589337-10c3-4635-916f-a8ee894f5ea2
             * absTime : 1543988277202
             * alarmTime : 1543988277299
             * alarmType : 2
             * eventType : null
             * deviceId : fxSJ000139MWIx1M
             * alarmAddress : 大门36
             * latitude : null
             * longitude : null
             * alarmStatus : 2
             * status : 1
             * alarmLevel : 1
             * imageUrl : http://172.16.90.168:6551/DownLoadFile?filename=PFSB:/viasbimg/bimg/data/20181205/13_0/a73db965d73ab9ef3e097aa4d3b534fa
             * userId : af2005b8-59df-4a71-a231-e0e6d232b60c
             * userName : 管理员
             * taskId : e443fb44-baa6-491f-b300-cd8079c3af13
             * taskName : null
             * taskReason : null
             * source : vdt
             * note : 当前警情有效
             * ext : [{"birthday":"","libId":"517697470927470592","nation":"未知","sex":"0","libName":"","remark":"","targetImage":"PFSB:/viasbimg/bimg/data/20181204/14_0/d1065061a3c4fd28957dcf22ba883c65","score":57,"createTime":"2018-12-04 14:07:21","idcard":"","census":"","name":"111","personId":"519515057676091392"}]
             * location : null
             * carAlarmType : null
             * loginId : 74534477-9089-46a0-9cec-2b63381f5a08,af2005b8-59df-4a71-a231-e0e6d232b60c,175ebc50-9794-43cc-810f-cb34cdbdfec9
             * libId : 517697470927470592
             * libName :
             * score : 57
             * recordId : 6d6e0887-0f47-4604-9cc0-ad04ef765179
             * targetImage : null
             * name : 111
             * sex : null
             * adminList : []
             * idCard :
             * threshold : null
             * realAlarm : -1
             * faceId : null
             * vehicleId : null
             * wifiId : null
             * rfidId : null
             * number : 0
             * faceIds : null
             * carIds : null
             * wifiIds : null
             * rfidIds : null
             */

            private String id;
            private long absTime;
            private long alarmTime;
            private String alarmType;
            private Object eventType;
            private String deviceId;
            private String alarmAddress;
            private Object latitude;
            private Object longitude;
            private String alarmStatus;
            private String status;
            private String alarmLevel;
            private String imageUrl;
            private String userId;
            private String userName;
            private String taskId;
            private Object taskName;
            private Object taskReason;
            private String source;
            private String note;
            private String ext;
            private Object location;
            private Object carAlarmType;
            private String loginId;
            private String libId;
            private String libName;
            private int score;
            private String recordId;
            private Object targetImage;
            private String name;
            private Object sex;
            private String idCard;
            private Object threshold;
            private int realAlarm;
            private Object faceId;
            private Object vehicleId;
            private Object wifiId;
            private Object rfidId;
            private int number;
            private Object faceIds;
            private Object carIds;
            private Object wifiIds;
            private Object rfidIds;
            private List<?> adminList;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public long getAbsTime() {
                return absTime;
            }

            public void setAbsTime(long absTime) {
                this.absTime = absTime;
            }

            public long getAlarmTime() {
                return alarmTime;
            }

            public void setAlarmTime(long alarmTime) {
                this.alarmTime = alarmTime;
            }

            public String getAlarmType() {
                return alarmType;
            }

            public void setAlarmType(String alarmType) {
                this.alarmType = alarmType;
            }

            public Object getEventType() {
                return eventType;
            }

            public void setEventType(Object eventType) {
                this.eventType = eventType;
            }

            public String getDeviceId() {
                return deviceId;
            }

            public void setDeviceId(String deviceId) {
                this.deviceId = deviceId;
            }

            public String getAlarmAddress() {
                return alarmAddress;
            }

            public void setAlarmAddress(String alarmAddress) {
                this.alarmAddress = alarmAddress;
            }

            public Object getLatitude() {
                return latitude;
            }

            public void setLatitude(Object latitude) {
                this.latitude = latitude;
            }

            public Object getLongitude() {
                return longitude;
            }

            public void setLongitude(Object longitude) {
                this.longitude = longitude;
            }

            public String getAlarmStatus() {
                return alarmStatus;
            }

            public void setAlarmStatus(String alarmStatus) {
                this.alarmStatus = alarmStatus;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getAlarmLevel() {
                return alarmLevel;
            }

            public void setAlarmLevel(String alarmLevel) {
                this.alarmLevel = alarmLevel;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getTaskId() {
                return taskId;
            }

            public void setTaskId(String taskId) {
                this.taskId = taskId;
            }

            public Object getTaskName() {
                return taskName;
            }

            public void setTaskName(Object taskName) {
                this.taskName = taskName;
            }

            public Object getTaskReason() {
                return taskReason;
            }

            public void setTaskReason(Object taskReason) {
                this.taskReason = taskReason;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public String getNote() {
                return note;
            }

            public void setNote(String note) {
                this.note = note;
            }

            public String getExt() {
                return ext;
            }

            public void setExt(String ext) {
                this.ext = ext;
            }

            public Object getLocation() {
                return location;
            }

            public void setLocation(Object location) {
                this.location = location;
            }

            public Object getCarAlarmType() {
                return carAlarmType;
            }

            public void setCarAlarmType(Object carAlarmType) {
                this.carAlarmType = carAlarmType;
            }

            public String getLoginId() {
                return loginId;
            }

            public void setLoginId(String loginId) {
                this.loginId = loginId;
            }

            public String getLibId() {
                return libId;
            }

            public void setLibId(String libId) {
                this.libId = libId;
            }

            public String getLibName() {
                return libName;
            }

            public void setLibName(String libName) {
                this.libName = libName;
            }

            public int getScore() {
                return score;
            }

            public void setScore(int score) {
                this.score = score;
            }

            public String getRecordId() {
                return recordId;
            }

            public void setRecordId(String recordId) {
                this.recordId = recordId;
            }

            public Object getTargetImage() {
                return targetImage;
            }

            public void setTargetImage(Object targetImage) {
                this.targetImage = targetImage;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Object getSex() {
                return sex;
            }

            public void setSex(Object sex) {
                this.sex = sex;
            }

            public String getIdCard() {
                return idCard;
            }

            public void setIdCard(String idCard) {
                this.idCard = idCard;
            }

            public Object getThreshold() {
                return threshold;
            }

            public void setThreshold(Object threshold) {
                this.threshold = threshold;
            }

            public int getRealAlarm() {
                return realAlarm;
            }

            public void setRealAlarm(int realAlarm) {
                this.realAlarm = realAlarm;
            }

            public Object getFaceId() {
                return faceId;
            }

            public void setFaceId(Object faceId) {
                this.faceId = faceId;
            }

            public Object getVehicleId() {
                return vehicleId;
            }

            public void setVehicleId(Object vehicleId) {
                this.vehicleId = vehicleId;
            }

            public Object getWifiId() {
                return wifiId;
            }

            public void setWifiId(Object wifiId) {
                this.wifiId = wifiId;
            }

            public Object getRfidId() {
                return rfidId;
            }

            public void setRfidId(Object rfidId) {
                this.rfidId = rfidId;
            }

            public int getNumber() {
                return number;
            }

            public void setNumber(int number) {
                this.number = number;
            }

            public Object getFaceIds() {
                return faceIds;
            }

            public void setFaceIds(Object faceIds) {
                this.faceIds = faceIds;
            }

            public Object getCarIds() {
                return carIds;
            }

            public void setCarIds(Object carIds) {
                this.carIds = carIds;
            }

            public Object getWifiIds() {
                return wifiIds;
            }

            public void setWifiIds(Object wifiIds) {
                this.wifiIds = wifiIds;
            }

            public Object getRfidIds() {
                return rfidIds;
            }

            public void setRfidIds(Object rfidIds) {
                this.rfidIds = rfidIds;
            }

            public List<?> getAdminList() {
                return adminList;
            }

            public void setAdminList(List<?> adminList) {
                this.adminList = adminList;
            }
        }
}
