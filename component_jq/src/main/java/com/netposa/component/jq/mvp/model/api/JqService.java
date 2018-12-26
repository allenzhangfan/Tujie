package com.netposa.component.jq.mvp.model.api;

import com.netposa.common.entity.HttpResponseEntity;
import com.netposa.component.jq.mvp.model.entity.AlarmDetailForIdResponseEntity;
import com.netposa.component.jq.mvp.model.entity.AlarmDetailResponseEntity;
import com.netposa.component.jq.mvp.model.entity.AlarmListRequestEntity;
import com.netposa.component.jq.mvp.model.entity.AlarmListResponseEntity;
import com.netposa.component.jq.mvp.model.entity.ProcessAlarmRequestEntity;
import com.netposa.component.jq.mvp.model.entity.ProcessAlarmResponseEntity;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Author：yeguoqiang
 * Created time：2018/12/6 16:29
 */
public interface JqService {

    String HEADER_ACCEPT_CONTENT = "Accept: */*";
    String HEADER_CONTENT_TYPE = "Content-type: application/json";

    //获取实时报警列表
    @Headers({HEADER_CONTENT_TYPE, HEADER_ACCEPT_CONTENT})
    @POST("ia/alarm/listAlarmInfo")
    Observable<HttpResponseEntity<AlarmListResponseEntity>> getlistAlarmInfo(@Body AlarmListRequestEntity request);

    // 警情处理（有效/无效）
    @Headers({HEADER_CONTENT_TYPE, HEADER_ACCEPT_CONTENT})
    @POST("ia/alarm/processAlarmInfo")
    Observable<HttpResponseEntity<ProcessAlarmResponseEntity>> getProcessAlarmInfo(@Body ProcessAlarmRequestEntity request);

    // 根据ID获取报警详情
    @Headers({HEADER_CONTENT_TYPE, HEADER_ACCEPT_CONTENT})
    @GET("ia/alarm/getAlarmInfo/{id}")
    Observable<HttpResponseEntity<AlarmDetailForIdResponseEntity>> getAlarmInfo(@Path("id") String Id);
}
