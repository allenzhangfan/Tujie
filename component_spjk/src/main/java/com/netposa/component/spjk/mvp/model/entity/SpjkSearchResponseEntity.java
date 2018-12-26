package com.netposa.component.spjk.mvp.model.entity;

import java.util.List;

import androidx.annotation.Keep;

@Keep
public class SpjkSearchResponseEntity {
    private int pageNo;
    private int totalRecords;
    private List<ListBean> list;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * ability : traffic,face,body,camera,nonmotor
         * cameraType : 1
         * gbId :
         * id : fxSJ000139MWIx1
         * leaf : true
         * name : 15楼大门
         * nodeType : camera
         * parentId : 090609104436423455
         * picture1 : null
         * structureType : [2]
         * angle : 0
         * latitude : 30.50547097865371
         * longitude : 114.41178942437482
         */
        private String matchName; //标记匹配
        private String ability;
        private String cameraType;
        private String gbId;
        private String id;
        private boolean leaf;
        private String name;
        private String nodeType;
        private String parentId;
        private String picture1;
        private int angle;
        private double latitude;
        private double longitude;
        private List<Integer> structureType;

        public String getMatchName() {
            return matchName;
        }

        public void setMatchName(String matchName) {
            this.matchName = matchName;
        }

        public String getAbility() {
            return ability;
        }

        public void setAbility(String ability) {
            this.ability = ability;
        }

        public String getCameraType() {
            return cameraType;
        }

        public void setCameraType(String cameraType) {
            this.cameraType = cameraType;
        }

        public String getGbId() {
            return gbId;
        }

        public void setGbId(String gbId) {
            this.gbId = gbId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isLeaf() {
            return leaf;
        }

        public void setLeaf(boolean leaf) {
            this.leaf = leaf;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNodeType() {
            return nodeType;
        }

        public void setNodeType(String nodeType) {
            this.nodeType = nodeType;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getPicture1() {
            return picture1;
        }

        public void setPicture1(String picture1) {
            this.picture1 = picture1;
        }

        public int getAngle() {
            return angle;
        }

        public void setAngle(int angle) {
            this.angle = angle;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public List<Integer> getStructureType() {
            return structureType;
        }

        public void setStructureType(List<Integer> structureType) {
            this.structureType = structureType;
        }
    }
}
