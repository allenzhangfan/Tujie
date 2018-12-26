package com.netposa.component.ytst.mvp.contract;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.netposa.common.entity.FeatureByPathRequestEntity;
import com.netposa.common.entity.FeatureByPathResponseEntity;
import com.netposa.component.ytst.mvp.model.entity.CarDetailRequestEntity;
import com.netposa.component.ytst.mvp.model.entity.CarDetailResponseEntity;

import java.util.List;

import io.reactivex.Observable;


public interface CompareIvContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
       void getDetailVehicleInfoSuc(List<CarDetailResponseEntity> entity);
       void getDetailVehicleInfoFail();

        void getDetectImgFeatureByPathSucess(FeatureByPathResponseEntity entity);
        void getDetectImgFeatureByPathFail();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<List<CarDetailResponseEntity>> getDetailVehicleInfo(CarDetailRequestEntity entity);
        Observable<FeatureByPathResponseEntity> getDetectImgFeatureByPath (FeatureByPathRequestEntity request);
    }
}
