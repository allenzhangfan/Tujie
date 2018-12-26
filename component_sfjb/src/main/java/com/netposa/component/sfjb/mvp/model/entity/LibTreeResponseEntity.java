package com.netposa.component.sfjb.mvp.model.entity;

import java.util.List;

import androidx.annotation.Keep;

@Keep
public class LibTreeResponseEntity {
    private List<DirectlyLibraryListBean> directlyLibraryList;
    private List<OrgListBean> orgList;

    public List<DirectlyLibraryListBean> getDirectlyLibraryList() {
        return directlyLibraryList;
    }

    public void setDirectlyLibraryList(List<DirectlyLibraryListBean> directlyLibraryList) {
        this.directlyLibraryList = directlyLibraryList;
    }

    public List<OrgListBean> getOrgList() {
        return orgList;
    }

    public void setOrgList(List<OrgListBean> orgList) {
        this.orgList = orgList;
    }

    public static class DirectlyLibraryListBean {
        /**
         * libId : 517290720051068928
         * libName : 人员布控库02
         * libType : 2
         * orgId : b6cde976-1948-437c-a7ac-333ac52c55d1
         * remark : null
         * virtualLib : 0
         * creatorId : af2005b8-59df-4a71-a231-e0e6d232b60c
         * creatorName : admin
         * pcount : 0
         * receiverIds : null
         * receiverNames : null
         * createTime : 1543373318457
         * pinyin : renyuanbukongku02
         */

        private String libId;
        private String libName;
        private String libType;
        private String orgId;
        private Object remark;
        private int virtualLib;
        private String creatorId;
        private String creatorName;
        private int pcount;
        private Object receiverIds;
        private Object receiverNames;
        private long createTime;
        private String pinyin;

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

        public String getLibType() {
            return libType;
        }

        public void setLibType(String libType) {
            this.libType = libType;
        }

        public String getOrgId() {
            return orgId;
        }

        public void setOrgId(String orgId) {
            this.orgId = orgId;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public int getVirtualLib() {
            return virtualLib;
        }

        public void setVirtualLib(int virtualLib) {
            this.virtualLib = virtualLib;
        }

        public String getCreatorId() {
            return creatorId;
        }

        public void setCreatorId(String creatorId) {
            this.creatorId = creatorId;
        }

        public String getCreatorName() {
            return creatorName;
        }

        public void setCreatorName(String creatorName) {
            this.creatorName = creatorName;
        }

        public int getPcount() {
            return pcount;
        }

        public void setPcount(int pcount) {
            this.pcount = pcount;
        }

        public Object getReceiverIds() {
            return receiverIds;
        }

        public void setReceiverIds(Object receiverIds) {
            this.receiverIds = receiverIds;
        }

        public Object getReceiverNames() {
            return receiverNames;
        }

        public void setReceiverNames(Object receiverNames) {
            this.receiverNames = receiverNames;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }
    }

    public static class OrgListBean {
        /**
         * id : b66bffb5-0365-4afe-8480-e2eb9b213578
         * name : 洪山区
         * parentId : b6cde976-1948-437c-a7ac-333ac52c55d1
         * acronym : hsq
         * sortNo : null
         * description : null
         * pinyin : hongshanqu
         * orgCode : null
         * status : 1
         * created : 1543473800206
         * isChild : 2
         */

        private String id;
        private String name;
        private String parentId;
        private String acronym;
        private Object sortNo;
        private Object description;
        private String pinyin;
        private Object orgCode;
        private int status;
        private long created;
        private int isChild;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getAcronym() {
            return acronym;
        }

        public void setAcronym(String acronym) {
            this.acronym = acronym;
        }

        public Object getSortNo() {
            return sortNo;
        }

        public void setSortNo(Object sortNo) {
            this.sortNo = sortNo;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public Object getOrgCode() {
            return orgCode;
        }

        public void setOrgCode(Object orgCode) {
            this.orgCode = orgCode;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getCreated() {
            return created;
        }

        public void setCreated(long created) {
            this.created = created;
        }

        public int getIsChild() {
            return isChild;
        }

        public void setIsChild(int isChild) {
            this.isChild = isChild;
        }
    }
}
