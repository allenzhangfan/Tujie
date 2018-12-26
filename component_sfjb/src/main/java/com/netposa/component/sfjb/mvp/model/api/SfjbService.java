package com.netposa.component.sfjb.mvp.model.api;

import com.netposa.common.entity.HttpResponseEntity;
import com.netposa.component.sfjb.mvp.model.entity.FaceCompareRequestEntity;
import com.netposa.component.sfjb.mvp.model.entity.FaceCompareResponseEntity;
import com.netposa.component.sfjb.mvp.model.entity.LibTreeRequestEntity;
import com.netposa.component.sfjb.mvp.model.entity.LibTreeResponseEntity;
import com.netposa.component.sfjb.mvp.model.entity.SearchFaceLibRequestEntity;
import com.netposa.component.sfjb.mvp.model.entity.SearchFaceLibResponseEntity;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Author：yeguoqiang
 * Created time：2018/12/3 15:26
 */
public interface SfjbService {

    String HEADER_ACCEPT_CONTENT = "Accept: */*";
    String HEADER_CONTENT_TYPE = "Content-type: application/json";

    /**
     * 图片上传接口
     * @param file  上传的file
     * @return 是否成功
     */
    @Multipart
    @POST("ia/face/detectImgFeature")
    Observable<HttpResponseEntity<String>> uploadImage(@Part MultipartBody.Part file);

    /**
     *
     * 获取组织人脸库
     */
    @Headers({HEADER_CONTENT_TYPE, HEADER_ACCEPT_CONTENT})
    @POST("ia/lib/getFaceLibraryTree")
    Observable<HttpResponseEntity<LibTreeResponseEntity>> getFaceLibraryTree(@Body LibTreeRequestEntity entity);

    /**
     *
     *模糊搜索人脸库
     */
    @Headers({HEADER_CONTENT_TYPE, HEADER_ACCEPT_CONTENT})
    @POST("ia/lib/searchFaceLibrary")
    Observable<HttpResponseEntity<SearchFaceLibResponseEntity>> getSearchFaceLib(@Body SearchFaceLibRequestEntity entity);

    /**
     *
     *人脸比对鉴别人脸结果
     */
    @Headers({HEADER_CONTENT_TYPE, HEADER_ACCEPT_CONTENT})
    @POST("ia/face/faceAuthentication")
    Observable<HttpResponseEntity<FaceCompareResponseEntity>> getFaceCompareResult(@Body FaceCompareRequestEntity entity);
}
