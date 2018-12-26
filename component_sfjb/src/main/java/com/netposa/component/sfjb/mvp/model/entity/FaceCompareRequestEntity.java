package com.netposa.component.sfjb.mvp.model.entity;

import java.util.List;

import androidx.annotation.Keep;

@Keep
public class FaceCompareRequestEntity {


    /**
     * currentPage : 1
     * feature : string
     * libList : ["517290799503769600"]
     * orgList : ["59c6a180-1602-41fd-8e9a-ed96646c6a87"]
     * pageSize : 20
     */

    private int currentPage;
    private String feature;
    private int pageSize;
    private List<String> libList;
    private List<String> orgList;
    private int similarity;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<String> getLibList() {
        return libList;
    }

    public void setLibList(List<String> libList) {
        this.libList = libList;
    }

    public List<String> getOrgList() {
        return orgList;
    }

    public void setOrgList(List<String> orgList) {
        this.orgList = orgList;
    }

    public int getSimilarity() {
        return similarity;
    }

    public void setSimilarity(int similarity) {
        this.similarity = similarity;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FaceCompareRequestEntity{");
        sb.append("currentPage=").append(currentPage);
        sb.append(", feature='").append(feature).append('\'');
        sb.append(", pageSize=").append(pageSize);
        sb.append(", libList=").append(libList);
        sb.append(", orgList=").append(orgList);
        sb.append(", similarity=").append(similarity);
        sb.append('}');
        return sb.toString();
    }
}
