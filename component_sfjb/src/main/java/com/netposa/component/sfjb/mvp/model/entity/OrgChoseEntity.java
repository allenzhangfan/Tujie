package com.netposa.component.sfjb.mvp.model.entity;


public class OrgChoseEntity {

    private String id;  //org
    private String deviceId; //device
    private String organizationDesc;
    private int organizationType;
    private String treeDesc;
    private boolean choose;

    public OrgChoseEntity() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getOrganizationDesc() {
        return organizationDesc;
    }

    public void setOrganizationDesc(String organizationDesc) {
        this.organizationDesc = organizationDesc;
    }

    public int getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(int organizationType) {
        this.organizationType = organizationType;
    }

    public String getTreeDesc() {
        return treeDesc;
    }

    public void setTreeDesc(String treeDesc) {
        this.treeDesc = treeDesc;
    }

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

}
