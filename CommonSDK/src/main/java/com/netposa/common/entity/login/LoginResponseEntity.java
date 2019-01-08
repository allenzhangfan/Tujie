package com.netposa.common.entity.login;

import com.netposa.common.modle.BaseBean;

/**
 * Author：yeguoqiang
 * Created time：2018/11/1 11:20
 */

public class LoginResponseEntity extends BaseBean {

    /**
     * videoOuterIp : 58.49.28.186:58287
     * tokenId : a216f64ba58661b94808057976d3d7bc
     * pictureOuterIp : 58.49.28.186:57199
     * mqttOuterIp : 192.168.101.10:1883
     * mqttInnerIp : 192.168.101.10:1883
     * videoInnerIp : 192.168.101.31:54
     * pictureInnerIp : 172.16.90.168:6551
     * user : {"id":"af2005b8-59df-4a71-a231-e0e6d232b60c","idCardNumber":"","gender":0,"admin":false,"phoneNo":"","userOrg":"b6cde976-1948-437c-a7ac-333ac52c55d1","orgName":"武汉","loginName":"admin","name":"管理员","registerTime":1542272131070,"policeNo":null,"status":"1"}
     */

    private String videoOuterIp;
    private String tokenId;
    private String pictureOuterIp;
    private String mqttOuterIp;
    private String mqttInnerIp;
    private String videoInnerIp;
    private String pictureInnerIp;
    private UserEntity user;

    public String getVideoOuterIp() {
        return videoOuterIp;
    }

    public void setVideoOuterIp(String videoOuterIp) {
        this.videoOuterIp = videoOuterIp;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getPictureOuterIp() {
        return pictureOuterIp;
    }

    public void setPictureOuterIp(String pictureOuterIp) {
        this.pictureOuterIp = pictureOuterIp;
    }

    public String getMqttOuterIp() {
        return mqttOuterIp;
    }

    public void setMqttOuterIp(String mqttOuterIp) {
        this.mqttOuterIp = mqttOuterIp;
    }

    public String getMqttInnerIp() {
        return mqttInnerIp;
    }

    public void setMqttInnerIp(String mqttInnerIp) {
        this.mqttInnerIp = mqttInnerIp;
    }

    public String getVideoInnerIp() {
        return videoInnerIp;
    }

    public void setVideoInnerIp(String videoInnerIp) {
        this.videoInnerIp = videoInnerIp;
    }

    public String getPictureInnerIp() {
        return pictureInnerIp;
    }

    public void setPictureInnerIp(String pictureInnerIp) {
        this.pictureInnerIp = pictureInnerIp;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public static class UserEntity {
        /**
         * id : af2005b8-59df-4a71-a231-e0e6d232b60c
         * idCardNumber :
         * gender : 0
         * admin : false
         * phoneNo :
         * userOrg : b6cde976-1948-437c-a7ac-333ac52c55d1
         * orgName : 武汉
         * loginName : admin
         * name : 管理员
         * registerTime : 1542272131070
         * policeNo : null
         * status : 1
         */

        private String id;
        private String idCardNumber;
        private int gender;
        private boolean admin;
        private String phoneNo;
        private String userOrg;
        private String orgName;
        private String loginName;
        private String name;
        private long registerTime;
        private String policeNo;
        private String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIdCardNumber() {
            return idCardNumber;
        }

        public void setIdCardNumber(String idCardNumber) {
            this.idCardNumber = idCardNumber;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public boolean isAdmin() {
            return admin;
        }

        public void setAdmin(boolean admin) {
            this.admin = admin;
        }

        public String getPhoneNo() {
            return phoneNo;
        }

        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }

        public String getUserOrg() {
            return userOrg;
        }

        public void setUserOrg(String userOrg) {
            this.userOrg = userOrg;
        }

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getRegisterTime() {
            return registerTime;
        }

        public void setRegisterTime(long registerTime) {
            this.registerTime = registerTime;
        }

        public String getPoliceNo() {
            return policeNo;
        }

        public void setPoliceNo(String policeNo) {
            this.policeNo = policeNo;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("UserEntity{");
            sb.append("id='").append(id).append('\'');
            sb.append(", idCardNumber='").append(idCardNumber).append('\'');
            sb.append(", gender=").append(gender);
            sb.append(", admin=").append(admin);
            sb.append(", phoneNo='").append(phoneNo).append('\'');
            sb.append(", userOrg='").append(userOrg).append('\'');
            sb.append(", orgName='").append(orgName).append('\'');
            sb.append(", loginName='").append(loginName).append('\'');
            sb.append(", name='").append(name).append('\'');
            sb.append(", registerTime=").append(registerTime);
            sb.append(", policeNo='").append(policeNo).append('\'');
            sb.append(", status='").append(status).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LoginResponseEntity{");
        sb.append("videoOuterIp='").append(videoOuterIp).append('\'');
        sb.append(", tokenId='").append(tokenId).append('\'');
        sb.append(", pictureOuterIp='").append(pictureOuterIp).append('\'');
        sb.append(", mqttOuterIp='").append(mqttOuterIp).append('\'');
        sb.append(", mqttInnerIp='").append(mqttInnerIp).append('\'');
        sb.append(", videoInnerIp='").append(videoInnerIp).append('\'');
        sb.append(", pictureInnerIp='").append(pictureInnerIp).append('\'');
        sb.append(", user=").append(user);
        sb.append('}');
        return sb.toString();
    }
}
