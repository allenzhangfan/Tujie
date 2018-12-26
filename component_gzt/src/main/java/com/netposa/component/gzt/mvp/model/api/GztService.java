package com.netposa.component.gzt.mvp.model.api;

import com.netposa.common.entity.HttpResponseEntity;
import com.netposa.component.gzt.mvp.model.entity.AllDictionaryResponseEntity;

import io.reactivex.Observable;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GztService {
    String HEADER_ACCEPT_CONTENT = "Accept: */*";
    String HEADER_CONTENT_TYPE = "Content-type: application/json";

    //获取所有字典码表
    @Headers({HEADER_CONTENT_TYPE, HEADER_ACCEPT_CONTENT})
    @POST("ia/dictionary/getAll")
    Observable<HttpResponseEntity<AllDictionaryResponseEntity>> getAll();

}
