package com.netposa.component.login.mvp.model.api;

import com.netposa.common.entity.HttpResponseEntity;
import com.netposa.component.login.mvp.model.entity.LoginRequestEntity;
import com.netposa.component.login.mvp.model.entity.LoginResponseEntity;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Author：yeguoqiang
 * Created time：2018/11/1 12:56
 */
public interface LoginService {

    String HEADER_ACCEPT_CONTENT = "Accept: */*";
    String HEADER_CONTENT_TYPE = "Content-type: application/json";

    @Headers({HEADER_CONTENT_TYPE, HEADER_ACCEPT_CONTENT})
    @POST("ia/user/login")
    Observable<HttpResponseEntity<LoginResponseEntity>> login(@Body LoginRequestEntity request);
}
