package com.netposa.component.spjk.mvp.model.entity;

/**
 * Author：yeguoqiang
 * Created time：2018/11/12 21:09
 */
public class HistoryVideoRequestEntity {

    /**
     * beginTime : 1541668318000
     * cameraId : fxSJ000139MWIx1
     * endTime : 1541671918000
     */

    private long beginTime;
    private String cameraId;
    private long endTime;
    private PageInfoBean pageInfo;

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HistoryVideoRequestEntity{");
        sb.append("beginTime=").append(beginTime);
        sb.append(", cameraId='").append(cameraId).append('\'');
        sb.append(", endTime=").append(endTime);
        sb.append('}');
        return sb.toString();
    }

    public PageInfoBean getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfoBean pageInfo) {
        this.pageInfo = pageInfo;
    }

    public static class PageInfoBean {
        /**
         * currentPage : 1
         * pageSize : 20
         * total : 20
         */

        private int currentPage;
        private int pageSize;
        private int total;

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }
}
