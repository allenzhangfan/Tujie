package com.netposa.component.sfjb.mvp.model.entity;

import java.util.List;

import androidx.annotation.Keep;

@Keep
public class SearchFaceLibResponseEntity {

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * libId : 507241843663044608
         * creatorId : 8242539a-f1fc-4da0-b54a-a00d34790fcc
         * creatorName : admin
         * libName : 武昌分局人员布控库01
         * libType : 2
         * pcount : 0
         * remark : 武昌分局人员布控库01
         * createTime : 1540977479628
         * orgId : 9ab98e8a-00fe-4f1a-9ae3-44c5ecd547ae
         * virtualLib : 0
         * pinyin : wuchangfenjurenyuanbukongku01
         * receiverNames : null
         * receiverIds : null
         */
        private boolean choose; //是否选择
        private String matchName; //标记匹配
        private String libId;
        private String creatorId;
        private String creatorName;
        private String libName;
        private int libType;
        private int pcount;
        private String remark;
        private long createTime;
        private String orgId;
        private int virtualLib;
        private String pinyin;
        private Object receiverNames;
        private Object receiverIds;

        public boolean isChoose() {
            return choose;
        }

        public void setChoose(boolean choose) {
            this.choose = choose;
        }

        public String getMatchName() {
            return matchName;
        }

        public void setMatchName(String matchName) {
            this.matchName = matchName;
        }

        public String getLibId() {
            return libId;
        }

        public void setLibId(String libId) {
            this.libId = libId;
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

        public String getLibName() {
            return libName;
        }

        public void setLibName(String libName) {
            this.libName = libName;
        }

        public int getLibType() {
            return libType;
        }

        public void setLibType(int libType) {
            this.libType = libType;
        }

        public int getPcount() {
            return pcount;
        }

        public void setPcount(int pcount) {
            this.pcount = pcount;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getOrgId() {
            return orgId;
        }

        public void setOrgId(String orgId) {
            this.orgId = orgId;
        }

        public int getVirtualLib() {
            return virtualLib;
        }

        public void setVirtualLib(int virtualLib) {
            this.virtualLib = virtualLib;
        }

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public Object getReceiverNames() {
            return receiverNames;
        }

        public void setReceiverNames(Object receiverNames) {
            this.receiverNames = receiverNames;
        }

        public Object getReceiverIds() {
            return receiverIds;
        }

        public void setReceiverIds(Object receiverIds) {
            this.receiverIds = receiverIds;
        }
    }
}
