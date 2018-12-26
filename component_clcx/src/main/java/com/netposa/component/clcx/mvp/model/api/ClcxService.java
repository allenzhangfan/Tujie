package com.netposa.component.clcx.mvp.model.api;

import com.netposa.common.entity.HttpResponseEntity;
import com.netposa.component.clcx.mvp.model.entity.CarDetailRequestEntity;
import com.netposa.component.clcx.mvp.model.entity.CarDetailResponseEntity;
import com.netposa.component.clcx.mvp.model.entity.QueryCarSearchEntity;
import com.netposa.component.clcx.mvp.model.entity.QueryCarSearchResponseEntity;
import com.netposa.component.clcx.mvp.model.entity.VehicleTrackRequestEntity;
import com.netposa.component.clcx.mvp.model.entity.VehicleTrackResponseEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ClcxService {
    String HEADER_ACCEPT_CONTENT = "Accept: */*";
    String HEADER_CONTENT_TYPE = "Content-type: application/json";

    //获取车辆搜索结果
    @Headers({HEADER_CONTENT_TYPE, HEADER_ACCEPT_CONTENT})
    @POST("ia/vehicle/searchVehicle")
    Observable<HttpResponseEntity<List<QueryCarSearchResponseEntity>>> getSearchVehicle(@Body QueryCarSearchEntity request);

    //获取车辆详情
    @Headers({HEADER_CONTENT_TYPE, HEADER_ACCEPT_CONTENT})
    @POST("ia/vehicle/getDetailVehicleInfo")
    Observable<HttpResponseEntity<List<CarDetailResponseEntity>>> getDetailVehicleInfo(@Body CarDetailRequestEntity requestEntity);

    //获取车辆行驶轨迹
    @Headers({HEADER_CONTENT_TYPE, HEADER_ACCEPT_CONTENT})
    @POST("ia/vehicle/getVehicleTrack")
    Observable<HttpResponseEntity<VehicleTrackResponseEntity>> getVehicleTrack(@Body VehicleTrackRequestEntity request);
}
