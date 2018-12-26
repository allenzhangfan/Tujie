package com.netposa.component.spjk.mvp.contract;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.netposa.component.spjk.mvp.model.entity.DeviceInfoResponseEntity;
import com.netposa.component.spjk.mvp.model.entity.PtzDirectionRequestEntity;

import io.reactivex.Observable;


public interface VideoPlayContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void showDeviceInfoSuccess(DeviceInfoResponseEntity reponse);

        void showDeviceInfoFailed();

        void setDirectionSuccess(Object reponse);

        //查看当前设备是否关注的成功的回调
        void checkDeviceSuccess(int count);

        void checkDeviceFailed();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {

        Observable<DeviceInfoResponseEntity> getDeviceInfo(String carmerId);

        Observable<Object> setPtzDirection(PtzDirectionRequestEntity requestEntity);
    }
}
