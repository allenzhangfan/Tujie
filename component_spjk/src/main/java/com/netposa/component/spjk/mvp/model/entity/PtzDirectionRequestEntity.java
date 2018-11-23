package com.netposa.component.spjk.mvp.model.entity;

import androidx.annotation.Keep;

@Keep
public class PtzDirectionRequestEntity {


    /**
     * cameraId : fxSJ000139MWIx1
     * cmd : 10
     */

    private String cameraId;
    private int cmd;
    private String param;

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
