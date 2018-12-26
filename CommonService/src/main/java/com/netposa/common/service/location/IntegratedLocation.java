package com.netposa.common.service.location;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.amap.api.location.AMapLocation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import androidx.annotation.StringDef;

/**
 * Created by yexiaokang on 2018/11/23.
 */
@SuppressWarnings("WeakerAccess")
public class IntegratedLocation extends Location implements Parcelable {

    private static final String TAG = "IntegratedLocation";

    /**
     * 原生GPS定位
     */
    public static final String METHOD_GPS = "gps";
    /**
     * 高德SDK定位
     */
    public static final String METHOD_AMAP = "amap";

    @StringDef({
            METHOD_GPS,
            METHOD_AMAP
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface LocateMethod {
    }

    private String mAddress;
    /**
     * 定位方案
     */
    @LocateMethod
    private String mLocateMethod;

    public IntegratedLocation(Location l) {
        super(l);
        setLocateMethod(METHOD_GPS);
    }

    public IntegratedLocation(AMapLocation l) {
        super(l);
        setLocateMethod(METHOD_AMAP);
        setLongitude(l.getLongitude());
        setLatitude(l.getLatitude());
        setAddress(l.getAddress());
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    @LocateMethod
    public String getLocateMethod() {
        return mLocateMethod;
    }

    public void setLocateMethod(@LocateMethod String locateMethod) {
        mLocateMethod = locateMethod;
    }

    public static final Creator<IntegratedLocation> CREATOR = new Creator<IntegratedLocation>() {

        @Override
        public IntegratedLocation createFromParcel(Parcel source) {
            Location location = Location.CREATOR.createFromParcel(source);
            IntegratedLocation integratedLocation = new IntegratedLocation(location);
            integratedLocation.mAddress = source.readString();
            integratedLocation.mLocateMethod = source.readString();
            return integratedLocation;
        }

        @Override
        public IntegratedLocation[] newArray(int size) {
            return new IntegratedLocation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        try {
            super.writeToParcel(parcel, flags);
            parcel.writeString(mAddress);
            parcel.writeString(mLocateMethod);
        } catch (Throwable t) {
            Log.e(TAG, "writeToParcel", t);
        }
    }

    @Override
    public String toString() {
        return "IntegratedLocation[" +
                super.toString() +
                " address=" + mAddress +
                " locateMethod=" + mLocateMethod +
                ']';
    }
}
