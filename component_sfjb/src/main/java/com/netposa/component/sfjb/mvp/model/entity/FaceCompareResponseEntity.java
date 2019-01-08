package com.netposa.component.sfjb.mvp.model.entity;

import java.util.List;
import androidx.annotation.Keep;

@Keep
public class FaceCompareResponseEntity {

        private int currentPage;
        private int pageNum;
        private int pageSize;
        private int startIndex;
        private int total;
        private int pageTotal;
        private List<ListBean> list;

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getStartIndex() {
            return startIndex;
        }

        public void setStartIndex(int startIndex) {
            this.startIndex = startIndex;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPageTotal() {
            return pageTotal;
        }

        public void setPageTotal(int pageTotal) {
            this.pageTotal = pageTotal;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * personId : 516938654355030016
             * libId : 516937167956606976
             * birthday : 1542556800000
             * gender : 0
             * cardType : 2
             * idCard : 456675474734
             * censusRegister : 关山大道0003
             * name : wxf
             * nation : 01
             * remark : 似的发动感时尚的
             * createTime : 1543289379454
             * threshold : null
             * task_type : null
             * availableCount : 0
             * unavailableCount : 0
             * lastestTime : 0
             * taskId : null
             * photoInfos : []
             * deviceObj : null
             * photoInfoExts : [{"photoId":"516938566203342848","personId":"516938654355030016","url":"PFSB:/viasbimg/bimg/data/20181127/11_0/8635f83b55489a5edaf825adbae0cbfc","feature":"DfX7BfsA+QME/hDn9/D7+Q4RARX++RH5/g4R//AOAvwO+gkOCPH/AP/vBPoA+Qj5CQ7xGQDx+vz/+QTzBAQA6/n//f3/COILBf3/C/EA+xr8+O4M9wERCfcS8+HxBgDvBO8DAPD5+ALqCAjsBAkYAPoE9+4EA/4K+fsMAv71BAMPAPr2APT8DvDv9Aj3/AL07hUFAQADI+38Fw7w7Q4F9g397PQVA+3/BP8N9gAXCe0SCwb+9AUmAuwAAgL3+vYM8e/15gsW+eTrBN8AAAf9CQwA//TzBQTyBAH2D/QD9vECA/8RDvj4Fun5EQD/8vAF/gQRDOkM3/ICA/8D+AIb+gcDAREOAQTuAwgD/AcG+gf1/PYAA/UA9AsJ/wTy9gQKBgj0CQ358f/2+PbjL/wPAewXAPQaBPn1BwED+vMPAvT+AQQJ7wP1HRAK/AALAv/+Bfvs7gT/CP3+4fYK/QAO9+sBEhMJERH0/gggBgX79Av8ARfpDgjzAyntGiD4A+/2Z054Q1D3gzs=","score":99.99846649169922}]
             * libName :
             */

            private String personId;
            private String libId;
            private long birthday;
            private String gender;
            private String cardType;
            private String idCard;
            private String censusRegister;
            private String name;
            private String nation;
            private String remark;
            private long createTime;
            private Object threshold;
            private Object task_type;
            private int availableCount;
            private int unavailableCount;
            private int lastestTime;
            private Object taskId;
            private Object deviceObj;
            private String libName;
            private List<?> photoInfos;
            private List<PhotoInfoExtsBean> photoInfoExts;
            private boolean select;

            public boolean isSelect() {
                return select;
            }

            public void setSelect(boolean select) {
                this.select = select;
            }

            public String getPersonId() {
                return personId;
            }

            public void setPersonId(String personId) {
                this.personId = personId;
            }

            public String getLibId() {
                return libId;
            }

            public void setLibId(String libId) {
                this.libId = libId;
            }

            public long getBirthday() {
                return birthday;
            }

            public void setBirthday(long birthday) {
                this.birthday = birthday;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getCardType() {
                return cardType;
            }

            public void setCardType(String cardType) {
                this.cardType = cardType;
            }

            public String getIdCard() {
                return idCard;
            }

            public void setIdCard(String idCard) {
                this.idCard = idCard;
            }

            public String getCensusRegister() {
                return censusRegister;
            }

            public void setCensusRegister(String censusRegister) {
                this.censusRegister = censusRegister;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNation() {
                return nation;
            }

            public void setNation(String nation) {
                this.nation = nation;
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

            public Object getThreshold() {
                return threshold;
            }

            public void setThreshold(Object threshold) {
                this.threshold = threshold;
            }

            public Object getTask_type() {
                return task_type;
            }

            public void setTask_type(Object task_type) {
                this.task_type = task_type;
            }

            public int getAvailableCount() {
                return availableCount;
            }

            public void setAvailableCount(int availableCount) {
                this.availableCount = availableCount;
            }

            public int getUnavailableCount() {
                return unavailableCount;
            }

            public void setUnavailableCount(int unavailableCount) {
                this.unavailableCount = unavailableCount;
            }

            public int getLastestTime() {
                return lastestTime;
            }

            public void setLastestTime(int lastestTime) {
                this.lastestTime = lastestTime;
            }

            public Object getTaskId() {
                return taskId;
            }

            public void setTaskId(Object taskId) {
                this.taskId = taskId;
            }

            public Object getDeviceObj() {
                return deviceObj;
            }

            public void setDeviceObj(Object deviceObj) {
                this.deviceObj = deviceObj;
            }

            public String getLibName() {
                return libName;
            }

            public void setLibName(String libName) {
                this.libName = libName;
            }

            public List<?> getPhotoInfos() {
                return photoInfos;
            }

            public void setPhotoInfos(List<?> photoInfos) {
                this.photoInfos = photoInfos;
            }

            public List<PhotoInfoExtsBean> getPhotoInfoExts() {
                return photoInfoExts;
            }

            public void setPhotoInfoExts(List<PhotoInfoExtsBean> photoInfoExts) {
                this.photoInfoExts = photoInfoExts;
            }

            public static class PhotoInfoExtsBean {
                /**
                 * photoId : 516938566203342848
                 * personId : 516938654355030016
                 * url : PFSB:/viasbimg/bimg/data/20181127/11_0/8635f83b55489a5edaf825adbae0cbfc
                 * feature : DfX7BfsA+QME/hDn9/D7+Q4RARX++RH5/g4R//AOAvwO+gkOCPH/AP/vBPoA+Qj5CQ7xGQDx+vz/+QTzBAQA6/n//f3/COILBf3/C/EA+xr8+O4M9wERCfcS8+HxBgDvBO8DAPD5+ALqCAjsBAkYAPoE9+4EA/4K+fsMAv71BAMPAPr2APT8DvDv9Aj3/AL07hUFAQADI+38Fw7w7Q4F9g397PQVA+3/BP8N9gAXCe0SCwb+9AUmAuwAAgL3+vYM8e/15gsW+eTrBN8AAAf9CQwA//TzBQTyBAH2D/QD9vECA/8RDvj4Fun5EQD/8vAF/gQRDOkM3/ICA/8D+AIb+gcDAREOAQTuAwgD/AcG+gf1/PYAA/UA9AsJ/wTy9gQKBgj0CQ358f/2+PbjL/wPAewXAPQaBPn1BwED+vMPAvT+AQQJ7wP1HRAK/AALAv/+Bfvs7gT/CP3+4fYK/QAO9+sBEhMJERH0/gggBgX79Av8ARfpDgjzAyntGiD4A+/2Z054Q1D3gzs=
                 * score : 99.99846649169922
                 */

                private String photoId;
                private String personId;
                private String url;
                private String feature;
                private double score;

                public String getPhotoId() {
                    return photoId;
                }

                public void setPhotoId(String photoId) {
                    this.photoId = photoId;
                }

                public String getPersonId() {
                    return personId;
                }

                public void setPersonId(String personId) {
                    this.personId = personId;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getFeature() {
                    return feature;
                }

                public void setFeature(String feature) {
                    this.feature = feature;
                }

                public double getScore() {
                    return score;
                }

                public void setScore(double score) {
                    this.score = score;
                }
            }

            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder("ListBean{");
                sb.append("personId='").append(personId).append('\'');
                sb.append(", libId='").append(libId).append('\'');
                sb.append(", birthday=").append(birthday);
                sb.append(", gender='").append(gender).append('\'');
                sb.append(", cardType='").append(cardType).append('\'');
                sb.append(", idCard='").append(idCard).append('\'');
                sb.append(", censusRegister='").append(censusRegister).append('\'');
                sb.append(", name='").append(name).append('\'');
                sb.append(", nation='").append(nation).append('\'');
                sb.append(", remark='").append(remark).append('\'');
                sb.append(", createTime=").append(createTime);
                sb.append(", threshold=").append(threshold);
                sb.append(", task_type=").append(task_type);
                sb.append(", availableCount=").append(availableCount);
                sb.append(", unavailableCount=").append(unavailableCount);
                sb.append(", lastestTime=").append(lastestTime);
                sb.append(", taskId=").append(taskId);
                sb.append(", deviceObj=").append(deviceObj);
                sb.append(", libName='").append(libName).append('\'');
                sb.append(", photoInfos=").append(photoInfos);
                sb.append(", photoInfoExts=").append(photoInfoExts);
                sb.append('}');
                return sb.toString();
            }
        }
}
