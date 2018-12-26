package com.netposa.component.rltk.mvp.model.api;

import com.netposa.common.entity.HttpResponseEntity;
import com.netposa.component.rltk.mvp.model.entity.FaceDetailResponseEntity;
import com.netposa.component.rltk.mvp.model.entity.FaceLibraryRequestEntity;
import com.netposa.component.rltk.mvp.model.entity.FaceLibraryResponseEntity;
import com.netposa.common.entity.FeatureByPathRequestEntity;
import com.netposa.common.entity.FeatureByPathResponseEntity;
import com.netposa.component.rltk.mvp.model.entity.SearchDeviceRequestEntity;
import com.netposa.component.rltk.mvp.model.entity.SearchDeviceResponseEntity;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Author：yeguoqiang
 * Created time：2018/12/3 14:12
 */
public interface FaceLibraryService {

    String HEADER_ACCEPT_CONTENT = "Accept: */*";
    String HEADER_CONTENT_TYPE = "Content-type: application/json";

    /**
     * 获取车辆行驶轨迹
     */
    @Headers({HEADER_CONTENT_TYPE, HEADER_ACCEPT_CONTENT})
    @POST("ia/face/listFace")
    Observable<HttpResponseEntity<FaceLibraryResponseEntity>> getFaceList(@Body FaceLibraryRequestEntity request);

    /**
     * 根据id获取人脸详情
     */
    @Headers({HEADER_CONTENT_TYPE, HEADER_ACCEPT_CONTENT})
    @GET("ia/face/getFaceInfo/{recordId}")
    Observable<HttpResponseEntity<FaceDetailResponseEntity>> getFaceInfo(@Path("recordId") String recordId);

    /**
     *  视频监控设备列表
     */
    @Headers({HEADER_CONTENT_TYPE, HEADER_ACCEPT_CONTENT})
    @POST("ia/device/listDeviceTree")
    Observable<HttpResponseEntity<SearchDeviceResponseEntity>> getChoseDeviceList(@Body SearchDeviceRequestEntity request);

    //根据图片路径获取图片特征
    @Headers({HEADER_CONTENT_TYPE, HEADER_ACCEPT_CONTENT})
    @POST("ia/face/detectImgFeatureByPath")
    Observable<HttpResponseEntity<FeatureByPathResponseEntity>> getDetectImgFeatureByPath(@Body FeatureByPathRequestEntity request);
}
