package com.netposa.common.entity.push;

/**
 * Author：yeguoqiang
 * Created time：2018/12/10 19:46
 */
public class AlarmPeopleDetailResponseForExt {

    /**
     * birthday :
     * libId : 517697470927470592
     * nation : 未知
     * sex : 0
     * libName : 人脸布控库001
     * remark :
     * targetImage : PFSB:/viasbimg/bimg/data/20181204/14_0/1dd24a2fb1ae3d170e685abc406ed4ea
     * score : 59
     * createTime : 2018-12-04 14:07:36
     * idcard :
     * census :
     * name : 222
     * personId : 519515118116012032
     */

    private String birthday;
    private String libId;
    private String nation;
    private String sex;
    private String libName;
    private String remark;
    private String targetImage;
    private int score;
    private String createTime;
    private String idcard;
    private String census;
    private String name;
    private String personId;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLibId() {
        return libId;
    }

    public void setLibId(String libId) {
        this.libId = libId;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLibName() { return libName; }

    public void setLibName(String libName) {
        this.libName = libName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTargetImage() {
        return targetImage;
    }

    public void setTargetImage(String targetImage) {
        this.targetImage = targetImage;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIdcard() { return idcard; }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getCensus() {
        return census;
    }

    public void setCensus(String census) {
        this.census = census;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
}
