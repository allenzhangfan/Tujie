package com.netposa.component.login.mvp.model.entity;

import com.netposa.common.modle.BaseBean;

import androidx.annotation.Keep;

/**
 * Author：yeguoqiang
 * Created time：2018/11/1 11:20
 */

@Keep
public class LoginResponseEntity extends BaseBean {


    /**
     * tokenId : 21232f297a57a5a743894a0e4a801fc3
     * user : {"email":"","id":"af2005b8-59df-4a71-a231-e0e6d232b60c","isAdmin":false,"level":0,"loginName":"admin","name":"管理员","permitRepeatLogin":0,"personalSetting":false,"phoneNo":"","sex":0,"startTime":0,"status":1}
     */

    private String tokenId;
    private UserEntity user;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public static class UserEntity {
        /**
         * email :
         * id : af2005b8-59df-4a71-a231-e0e6d232b60c
         * isAdmin : false
         * level : 0
         * loginName : admin
         * name : 管理员
         * permitRepeatLogin : 0
         * personalSetting : false
         * phoneNo :
         * sex : 0
         * startTime : 0
         * status : 1
         */

        private String email;
        private String id;
        private boolean isAdmin;
        private int level;
        private String loginName;
        private String name;
        private int permitRepeatLogin;
        private boolean personalSetting;
        private String phoneNo;
        private int sex;
        private int startTime;
        private int status;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isIsAdmin() {
            return isAdmin;
        }

        public void setIsAdmin(boolean isAdmin) {
            this.isAdmin = isAdmin;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
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

        public int getPermitRepeatLogin() {
            return permitRepeatLogin;
        }

        public void setPermitRepeatLogin(int permitRepeatLogin) {
            this.permitRepeatLogin = permitRepeatLogin;
        }

        public boolean isPersonalSetting() {
            return personalSetting;
        }

        public void setPersonalSetting(boolean personalSetting) {
            this.personalSetting = personalSetting;
        }

        public String getPhoneNo() {
            return phoneNo;
        }

        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getStartTime() {
            return startTime;
        }

        public void setStartTime(int startTime) {
            this.startTime = startTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
