package com.netposa.component.rltk.mvp.model.entity;

import androidx.annotation.Keep;

public class OrgChoseEntity {

    public static final int TYPE_GROUP = 0x1;
    public static final int TYPE_CAMERA = 0X2;
    public static final int TYPE_CAMERA_CIRCLE=0X3;

    private String parentId;
    private String ChildId;
    private String organizationDesc;
    private String organizationType;
    private String treeDesc;
    private boolean mIsRootOrg;
    private boolean choose;
    private int childcount;
    private String cameraType;

    public OrgChoseEntity() {

    }

    public boolean isRootOrg() {
        return mIsRootOrg;
    }

    public void setRootOrg(boolean rootOrg) {
        this.mIsRootOrg = rootOrg;
    }

    public Object getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getChildId() {
        return ChildId;
    }

    public void setChildId(String childId) {
        ChildId = childId;
    }

    public boolean ismIsRootOrg() {
        return mIsRootOrg;
    }

    public void setmIsRootOrg(boolean mIsRootOrg) {
        this.mIsRootOrg = mIsRootOrg;
    }

    public String getOrganizationDesc() {
        return organizationDesc;
    }

    public void setOrganizationDesc(String organizationDesc) {
        this.organizationDesc = organizationDesc;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
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

    public int getChildcount() {
        return childcount;
    }

    public void setChildcount(int childcount) {
        this.childcount = childcount;
    }

    public String getCameraType() {
        return cameraType;
    }

    public void setCameraType(String cameraType) {
        this.cameraType = cameraType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrgMainEntity{");
        sb.append("parentId='").append(parentId).append('\'');
        sb.append(", ChildId='").append(ChildId).append('\'');
        sb.append(", organizationDesc='").append(organizationDesc).append('\'');
        sb.append(", organizationType=").append(organizationType).append('\'');
        sb.append(", treeDesc='").append(treeDesc).append('\'');
        sb.append(", mIsRootOrg=").append(mIsRootOrg);
        sb.append('}');
        return sb.toString();
    }
}
