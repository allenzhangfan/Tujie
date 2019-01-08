package com.netposa.camera.mvp.contract;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.netposa.camera.mvp.model.entity.UploadResponseEntity;
import com.netposa.common.entity.HttpResponseEntity;
import com.netposa.common.entity.login.LoginResponseEntity;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Part;


public interface CropPictureContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void goToHomeActivity();
        void uploadImageSuccess(HttpResponseEntity<UploadResponseEntity> entity);
        void uploadImageFail();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<HttpResponseEntity<UploadResponseEntity>> uploadImage(@Part MultipartBody.Part file);
        Observable<LoginResponseEntity> faceLogin(MultipartBody.Part file, String name);
    }
}
