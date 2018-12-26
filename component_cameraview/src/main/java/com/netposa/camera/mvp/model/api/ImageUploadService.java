package com.netposa.camera.mvp.model.api;

import com.netposa.camera.mvp.model.entity.UploadResponseEntity;
import com.netposa.common.entity.HttpResponseEntity;
import com.netposa.common.entity.login.LoginResponseEntity;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface ImageUploadService {
    /**
     * 图片上传接口
     *
     * @param file 上传的file
     * @return 是否成功
     */
    @Multipart
    @POST("ia/face/detectImgFeature")
    Observable<HttpResponseEntity<UploadResponseEntity>> uploadImage(@Part MultipartBody.Part file);

    /**
     * 刷脸登录
     *
     * @param file 上传的file
     * @return 是否成功
     */
    @Multipart
    @POST("ia/user/faceLogin")
    Observable<HttpResponseEntity<LoginResponseEntity>> faceLogin(@Part MultipartBody.Part file,
                                                                  @Query("username") String name);
}
