package com.netposa.component.ytst.mvp.contract;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.netposa.component.ytst.mvp.model.entity.ImgSearchRequestEntity;
import com.netposa.component.ytst.mvp.model.entity.ImgSearchResponseEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;


public interface SearchResultContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void imageSearchSuccess(List<ImgSearchResponseEntity> entity);
        void imageSearchuploadImageFail();

    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<List<ImgSearchResponseEntity>> imgSearch(@Body ImgSearchRequestEntity entity);
    }
}
