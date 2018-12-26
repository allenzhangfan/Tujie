package com.netposa.component.my.mvp.model.api;

import com.netposa.common.entity.HttpResponseEntity;
import com.netposa.component.my.mvp.model.entity.LogOutResponseEntity;
import com.netposa.component.my.mvp.model.entity.UpdateInfoEntity;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Author：yeguoqiang
 * Created time：2018/11/1 15:37
 */
public interface MyService {

    String HEADER_ACCEPT_CONTENT = "Accept: */*";
    String HEADER_CONTENT_TYPE = "Content-type: application/json";

    @Headers({HEADER_CONTENT_TYPE, HEADER_ACCEPT_CONTENT})
    @GET("ia/user/logout")
    Observable<HttpResponseEntity<LogOutResponseEntity>> logOut();

    @Headers({HEADER_CONTENT_TYPE, HEADER_ACCEPT_CONTENT})
    @GET("ia/app/getUpdateJson")
    Observable<HttpResponseEntity<UpdateInfoEntity>> getUpdateJson();

}
