package com.netposa.component.rltk.mvp.contract;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.netposa.component.rltk.mvp.model.entity.FaceDetailResponseEntity;
import com.netposa.common.entity.FeatureByPathRequestEntity;
import com.netposa.common.entity.FeatureByPathResponseEntity;

import io.reactivex.Observable;


public interface PhotoInformationContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void getSuceese(FaceDetailResponseEntity entity);
        void getFailed();

        void getDetectImgFeatureByPathSucess(FeatureByPathResponseEntity entity);
        void getDetectImgFeatureByPathFail();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
       Observable<FaceDetailResponseEntity> getFace(String recordId);
        Observable<FeatureByPathResponseEntity> getDetectImgFeatureByPath (FeatureByPathRequestEntity request);
    }
}
