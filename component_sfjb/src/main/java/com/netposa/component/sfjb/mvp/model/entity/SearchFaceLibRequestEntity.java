package com.netposa.component.sfjb.mvp.model.entity;

import androidx.annotation.Keep;

@Keep
public class SearchFaceLibRequestEntity {

    /**
     * name :
     * orgId :
     */

    private String name;
    private String orgId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
