package com.netposa.component.jq.mvp.model.entity;

import androidx.annotation.Keep;

@Keep
public class ProcessAlarmRequestEntity {

    /**
     * alarmStatus : 2
     * id : 6b589337-10c3-4635-916f-a8ee894f5ea2
     * note : 当前警情有效
     * userName : 李四
     */

    private int alarmStatus;
    private String id;
    private String note;
    private String userName;

    public int getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(int alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
