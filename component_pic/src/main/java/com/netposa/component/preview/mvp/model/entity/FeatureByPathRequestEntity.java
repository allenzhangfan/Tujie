package com.netposa.component.preview.mvp.model.entity;

import androidx.annotation.Keep;

@Keep
public class FeatureByPathRequestEntity {

    /**
     * imgPath : http://172.16.90.168:6551/DownLoadFile?filename=PFSB:/viasbimg/bimg/data/20181221/14_0/6fbf0392587c6d8dc8ccda7c1826c224
     * type : VEHICLE
     */

    private String imgPath;
    private String type;

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
