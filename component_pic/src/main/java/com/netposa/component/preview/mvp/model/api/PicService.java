package com.netposa.component.preview.mvp.model.api;

import com.netposa.common.entity.HttpResponseEntity;
import com.netposa.component.preview.mvp.model.entity.FeatureByPathRequestEntity;
import com.netposa.component.preview.mvp.model.entity.FeatureByPathResponseEntity;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Author：yeguoqiang
 * Created time：2018/12/6 16:29
 */
public interface PicService {

    String HEADER_ACCEPT_CONTENT = "Accept: */*";
    String HEADER_CONTENT_TYPE = "Content-type: application/json";

    //根据图片路径获取图片特征
    @Headers({HEADER_CONTENT_TYPE, HEADER_ACCEPT_CONTENT})
    @POST("ia/face/detectImgFeatureByPath")
    Observable<HttpResponseEntity<FeatureByPathResponseEntity>> getDetectImgFeatureByPath(@Body FeatureByPathRequestEntity request);
}
