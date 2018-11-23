package com.netposa.component.spjk.mvp.model.entity;

public class OrgMainEntity{

    public static final int TYPE_GROUP=0x1;
    public static final int TYPE_CAMERA=0X2;

    private String parentId;
    private String ChildId;
    private String organizationDesc;
    private int organizationType;
    private String treeDesc;
    private boolean mIsRootOrg;

    public OrgMainEntity(){

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrgMainEntity{");
        sb.append("parentId='").append(parentId).append('\'');
        sb.append(", ChildId='").append(ChildId).append('\'');
        sb.append(", organizationDesc='").append(organizationDesc).append('\'');
        sb.append(", organizationType=").append(organizationType);
        sb.append(", treeDesc='").append(treeDesc).append('\'');
        sb.append(", mIsRootOrg=").append(mIsRootOrg);
        sb.append('}');
        return sb.toString();
    }
}
