package com.netposa.component.jq.mvp.model.entity;

import androidx.annotation.Keep;

/**
 * Author：yeguoqiang
 * Created time：2018/12/6 16:49
 */
@Keep
public class AlarmListRequestEntity {

    /**
     * alarmType : 2
     * currentPage : 1
     * name : 222
     * pageSize : 20
     * score : 57
     */

    private String alarmType;
    private int currentPage;
    private String name;
    private int pageSize;
    private int score;

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
