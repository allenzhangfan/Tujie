package com.netposa.component.sfjb.mvp.contract;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.netposa.component.sfjb.mvp.model.entity.SearchFaceLibRequestEntity;
import com.netposa.component.sfjb.mvp.model.entity.SearchFaceLibResponseEntity;

import io.reactivex.Observable;


public interface SearchLibContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void refreshData();

        void loadDataForFirstTimeSuccess();

        void loadDataForFirstTimeFail();

        void getListSuccess(SearchFaceLibResponseEntity entity);

        void getListFailed();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<SearchFaceLibResponseEntity> getSearch(SearchFaceLibRequestEntity entity);
    }
}
