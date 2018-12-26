package com.netposa.component.jq.mvp.contract;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.netposa.component.jq.mvp.model.entity.AlarmDetailForIdResponseEntity;
import com.netposa.component.jq.mvp.model.entity.AlarmDetailResponseEntity;
import com.netposa.component.jq.mvp.model.entity.ProcessAlarmRequestEntity;
import com.netposa.component.jq.mvp.model.entity.ProcessAlarmResponseEntity;

import io.reactivex.Observable;


public interface AlarmDetailsContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {

        void getProcessAlarmInfoSuccess(ProcessAlarmResponseEntity entity);

        void getProcessAlarmInfoFail();

        void getAlarmInfoSuccess(AlarmDetailForIdResponseEntity entity);

        void getAlarmInfoFail();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<ProcessAlarmResponseEntity> getProcessAlarmInfo(ProcessAlarmRequestEntity entity);
        Observable<AlarmDetailForIdResponseEntity> getAlarmInfo(String id);
    }
}
