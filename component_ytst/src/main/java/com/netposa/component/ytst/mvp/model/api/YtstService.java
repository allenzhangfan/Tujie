package com.netposa.component.ytst.mvp.model.api;

import com.netposa.common.entity.FeatureByPathRequestEntity;
import com.netposa.common.entity.FeatureByPathResponseEntity;
import com.netposa.common.entity.HttpResponseEntity;
import com.netposa.component.ytst.mvp.model.entity.CarDetailRequestEntity;
import com.netposa.component.ytst.mvp.model.entity.CarDetailResponseEntity;
import com.netposa.component.ytst.mvp.model.entity.ImgSearchRequestEntity;
import com.netposa.component.ytst.mvp.model.entity.ImgSearchResponseEntity;
import com.netposa.component.ytst.mvp.model.entity.UploadPicResponseEntity;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface YtstService {
    String HEADER_ACCEPT_CONTENT = "Accept: */*";
    String HEADER_CONTENT_TYPE = "Content-type: application/json";

    //获取车辆详情
    @Headers({HEADER_CONTENT_TYPE, HEADER_ACCEPT_CONTENT})
    @POST("ia/vehicle/getDetailVehicleInfo")
    Observable<HttpResponseEntity<List<CarDetailResponseEntity>>> getDetailVehicleInfo(@Body CarDetailRequestEntity requestEntity);

    /**
     * 以图搜图搜图和搜人图片上传接口
     *
     * @param file 上传的file
     * @return 是否成功
     */
    @Multipart
    @POST("ia/face/uploadPicture")
    Observable<HttpResponseEntity<UploadPicResponseEntity>> uploadImage(@Part MultipartBody.Part file);


    /**
     * 以图搜图
     *
     * @param
     * @return
     */
    @Headers({HEADER_CONTENT_TYPE, HEADER_ACCEPT_CONTENT})
    @POST("ia/face/imgSearch")
    Observable<HttpResponseEntity<List<ImgSearchResponseEntity>>> imgSearch(@Body ImgSearchRequestEntity entity);

    //根据图片路径获取图片特征
    @Headers({HEADER_CONTENT_TYPE, HEADER_ACCEPT_CONTENT})
    @POST("ia/face/detectImgFeatureByPath")
    Observable<HttpResponseEntity<FeatureByPathResponseEntity>> getDetectImgFeatureByPath(@Body FeatureByPathRequestEntity request);

}
