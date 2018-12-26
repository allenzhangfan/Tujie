package com.netposa.component.ytst.mvp.model.entity;

import java.util.List;

import androidx.annotation.Keep;

@Keep
public class CarDetailRequestEntity {
    private List<String> recordId;

    public List<String> getRecordId() {
        return recordId;
    }

    public void setRecordId(List<String> recordId) {
        this.recordId = recordId;
    }
}
