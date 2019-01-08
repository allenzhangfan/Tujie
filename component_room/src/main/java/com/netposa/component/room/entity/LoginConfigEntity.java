package com.netposa.component.room.entity;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Author：yeguoqiang
 * Created time：2018/10/22 16:09
 */
@Entity(tableName = "LoginConfig")
public class LoginConfigEntity {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    private String uuid;

    /**
     * 用户名
     */
    @ColumnInfo(name = "username")
    private String username;

    /**
     * 密码
     */
    @ColumnInfo(name = "password")
    private String password;

    /**
     * 用户id
     */
    @ColumnInfo(name = "id")
    private String id;
    @ColumnInfo(name = "idCardNumber")
    private String idCardNumber;
    /**
     * 0男性 1女性
     */
    @ColumnInfo(name = "gender")
    private int gender;
    @ColumnInfo(name = "admin")
    private boolean admin;
    @ColumnInfo(name = "phoneNo")
    private String phoneNo;
    @ColumnInfo(name = "userOrg")
    private String userOrg;
    /**
     * 昵称
     */
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "orgName")
    private String orgName;
    @ColumnInfo(name = "registerTime")
    private long registerTime;
    @ColumnInfo(name = "policeNo")
    private String policeNo;
    @ColumnInfo(name = "status")
    private String status;

    /**
     * 是否开启人脸登录开关
     */
    @ColumnInfo(name = "faceLoginOpened")
    private boolean faceLoginOpened;

    public LoginConfigEntity() {
        uuid = UUID.randomUUID().toString();
    }

    @NonNull
    public String getUuid() {
        return uuid;
    }

    public void setUuid(@NonNull String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public boolean isFaceLoginOpened() {
        return faceLoginOpened;
    }

    public void setFaceLoginOpened(boolean faceLoginOpened) {
        this.faceLoginOpened = faceLoginOpened;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LoginConfigEntity{");
        sb.append("uuid='").append(uuid).append('\'');
        sb.append(", username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", id='").append(id).append('\'');
        sb.append(", idCardNumber='").append(idCardNumber).append('\'');
        sb.append(", gender=").append(gender);
        sb.append(", admin=").append(admin);
        sb.append(", phoneNo='").append(phoneNo).append('\'');
        sb.append(", userOrg='").append(userOrg).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", orgName='").append(orgName).append('\'');
        sb.append(", registerTime=").append(registerTime);
        sb.append(", policeNo='").append(policeNo).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append(", faceLoginOpened=").append(faceLoginOpened);
        sb.append('}');
        return sb.toString();
    }
}
