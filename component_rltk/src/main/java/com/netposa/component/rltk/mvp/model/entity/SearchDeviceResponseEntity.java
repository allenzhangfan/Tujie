package com.netposa.component.rltk.mvp.model.entity;

import com.netposa.common.modle.BaseBean;
import java.util.List;

public class SearchDeviceResponseEntity extends BaseBean {
    private List<DeviceTreeListBean> deviceTreeList;

    public List<DeviceTreeListBean> getDeviceTreeList() {
        return deviceTreeList;
    }

    public void setDeviceTreeList(List<DeviceTreeListBean> deviceTreeList) {
        this.deviceTreeList = deviceTreeList;
    }

    public static class DeviceTreeListBean {
        /**
         * ability : traffic,face,body,camera,nonmotor
         * allots : [{"id":"090609104436423455","name":"武昌区分局"}]
         * cameraType : 1
         * gbId :
         * id : fxSJ000139MWIx1
         * leaf : true
         * name : 15楼大门
         * nodeType : camera
         * parentId : 090609104436423455
         * picture1 : null
         * structureType : [2]
         * childrenCount : 0
         */
        private boolean choose;
        private String ability;
        private String cameraType;
        private String gbId;
        private String id;
        private boolean leaf;
        private String name;
        private String nodeType;
        private String parentId;
        private String picture1;
        private int childrenCount;
        private double latitude;
        private double longitude;
        private List<AllotsBean> allots;
        private List<Integer> structureType;

        public boolean isChoose() {
            return choose;
        }

        public void setChoose(boolean choose) {
            this.choose = choose;
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

        public int getChildrenCount() {
            return childrenCount;
        }

        public void setChildrenCount(int childrenCount) {
            this.childrenCount = childrenCount;
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

        public List<AllotsBean> getAllots() {
            return allots;
        }

        public void setAllots(List<AllotsBean> allots) {
            this.allots = allots;
        }

        public List<Integer> getStructureType() {
            return structureType;
        }

        public void setStructureType(List<Integer> structureType) {
            this.structureType = structureType;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("DeviceTreeListBean{");
            sb.append("ability='").append(ability).append('\'');
            sb.append(", cameraType='").append(cameraType).append('\'');
            sb.append(", gbId='").append(gbId).append('\'');
            sb.append(", id='").append(id).append('\'');
            sb.append(", leaf=").append(leaf);
            sb.append(", name='").append(name).append('\'');
            sb.append(", nodeType='").append(nodeType).append('\'');
            sb.append(", parentId='").append(parentId).append('\'');
            sb.append(", picture1='").append(picture1).append('\'');
            sb.append(", childrenCount=").append(childrenCount);
            sb.append(", latitude=").append(latitude);
            sb.append(", longitude=").append(longitude);
            sb.append(", allots=").append(allots);
            sb.append(", structureType=").append(structureType);
            sb.append('}');
            return sb.toString();
        }

        public static class AllotsBean {
            /**
             * id : 090609104436423455
             * name : 武昌区分局
             */

            private String id;
            private String name;

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

            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder("AllotsBean{");
                sb.append("id='").append(id).append('\'');
                sb.append(", name='").append(name).append('\'');
                sb.append('}');
                return sb.toString();
            }
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SpjkListDeviceResponseEntity{");
        sb.append("deviceTreeList=").append(deviceTreeList);
        sb.append('}');
        return sb.toString();
    }
}
