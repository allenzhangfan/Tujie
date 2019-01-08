package com.netposa.component.ytst.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/**
 * Author：yeguoqiang
 * Created time：2018/12/7 16:20
 */
public class UploadPicResponseEntity {

    /**
     * sessionKey : 48ce6dc7a6f64f4fb8a7effa7b439713
     * imgPath : PFSB:/viasbimg/bimg/data/20181207/16_0/33446a48e79cc52df015ef7fa77c5b81
     * feature : v28lvZKJ6zww5wA802IgvZkqX746HI89JZKUvSQZYj7qDm89Rb4jvUxhdDoDgvo9+jZZPUDgcLtHhvo724covXeIEb6bzjo9hRLhO4ECTb2LZe49kLo3Pfnsy7uS+mK7fXjlvaSp1zze5E09Q8WmPc3TJr1RuZI9XaYivcTFTL0aRv49OnElO8dz6Ds5Ioe9zZcDvhZXTj1+JeC8b0+jvcdiIL4C4zU9W1CZu4UjtLxYyMM9Oj/rPc66tb20wLM9Y8lDPq2Syzw8mWO8sP0HvoIunL1DFRw94uqtPNcoMT5RHwm9lDliPWiqy7wR/jO9GT4uvR958r2yVha9VSCYvS3M/z0Clqi9fWoEPuF7Rz1djUA+ZVvkvVpY3j1pRlU98gNLO8bvKDxFm2k8YxtnPH3Nqb0DwGg9xtXsPPkC3rw3O5O9zt7mPYgr6DvkrZk9qgjSvYMKx7yDQZ88IacUPau4ID4OZay80sR7PUY+WL5Pziy+mHOePddQWz2e5hw9YXLpO7wCuT2pIgI+OWxGPfidTb4L/BS94IMuPhsUbj2Jtc49bsYvvg7IBj06nN09hFKjPCfTcT1C2eu9AW2BPZIMXz3z6dE9mEyLPdcJyLzzRyY+52xIvMSUW70/dvg9JpdQvP1LIT6BwdY9H2mDvehCQTzxSbK93JmDPefdiz0=
     * detectResultMap : [{"dataKey":"d76ddc05929d49d298afe03c6b1543f3","dataType":"FACE","position":"78,79,266,266"},{"dataKey":"e73c1a9b5b704a499e27daf910ebe7f1","dataType":"BODY","position":"32,2,303,322"}]
     */

    private String sessionKey;
    private String imgPath;
    private String feature;
    private ArrayList<DetectResultMapEntity> detectResultMap;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public ArrayList<DetectResultMapEntity> getDetectResultMap() {
        return detectResultMap;
    }

    public void setDetectResultMap(ArrayList<DetectResultMapEntity> detectResultMap) {
        this.detectResultMap = detectResultMap;
    }

    public static class DetectResultMapEntity implements Parcelable {
        /**
         * dataKey : d76ddc05929d49d298afe03c6b1543f3
         * dataType : FACE
         * position : 78,79,266,266
         */

        private String dataKey;
        private String dataType;
        private String position;

        protected DetectResultMapEntity(Parcel in) {
            dataKey = in.readString();
            dataType = in.readString();
            position = in.readString();
        }

        public DetectResultMapEntity() {
        }

        public static final Creator<DetectResultMapEntity> CREATOR = new Creator<DetectResultMapEntity>() {
            @Override
            public DetectResultMapEntity createFromParcel(Parcel in) {
                return new DetectResultMapEntity(in);
            }

            @Override
            public DetectResultMapEntity[] newArray(int size) {
                return new DetectResultMapEntity[size];
            }
        };

        public String getDataKey() {
            return dataKey;
        }

        public void setDataKey(String dataKey) {
            this.dataKey = dataKey;
        }

        public String getDataType() {
            return dataType;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(dataKey);
            parcel.writeString(dataType);
            parcel.writeString(position);
        }
    }
}
