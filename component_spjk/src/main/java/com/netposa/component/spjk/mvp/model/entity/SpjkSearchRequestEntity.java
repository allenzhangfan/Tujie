package com.netposa.component.spjk.mvp.model.entity;

public class SpjkSearchRequestEntity {
    /**
     * id : 097888615362930250
     * key : 15
     * pageInfo : {"currentPage":1,"pageSize":20}
     */
    private String id;
    private String key;
    private PageInfoBean pageInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
         */

        private int currentPage;
        private int pageSize;

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
    }
}
