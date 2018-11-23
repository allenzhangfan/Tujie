package com.netposa.component.spjk.mvp.model.api;

import com.netposa.common.entity.HttpResponseEntity;
import com.netposa.component.spjk.mvp.model.entity.DeviceInfoResponseEntity;
import com.netposa.component.spjk.mvp.model.entity.HistoryVideoRequestEntity;
import com.netposa.component.spjk.mvp.model.entity.HistoryVideoResponseEntity;
import com.netposa.component.spjk.mvp.model.entity.OneKilometerCamerasRequestEntity;
import com.netposa.component.spjk.mvp.model.entity.OneKilometerCamerasResponseEntity;
import com.netposa.component.spjk.mvp.model.entity.PtzDirectionRequestEntity;
import com.netposa.component.spjk.mvp.model.entity.SpjkListDeviceRequestEntity;
import com.netposa.component.spjk.mvp.model.entity.SpjkListDeviceResponseEntity;
import com.netposa.component.spjk.mvp.model.entity.SpjkSearchRequestEntity;
import com.netposa.component.spjk.mvp.model.entity.SpjkSearchResponseEntity;
import java.util.List;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Author：yeguoqiang
 * Created time：2018/11/12 21:31
 */
public interface SpjkService {
    String HEADER_ACCEPT_CONTENT = "Accept: */*";
    String HEADER_CONTENT_TYPE = "Content-type: application/json";

    //获取一公里范围圈的摄像头
    @Headers({HEADER_CONTENT_TYPE, HEADER_ACCEPT_CONTENT})
    @POST("ia/device/getNeighbouringDevice")
    Observable<HttpResponseEntity<List<OneKilometerCamerasResponseEntity>>> getNeighbouringDevice(@Body OneKilometerCamerasRequestEntity request);

    // 视频监控设备列表
    @Headers({HEADER_CONTENT_TYPE, HEADER_ACCEPT_CONTENT})
    @POST("ia/device/listDeviceTree")
    Observable<HttpResponseEntity<SpjkListDeviceResponseEntity>> getSpjkDeviceList(@Body SpjkListDeviceRequestEntity request);

    //视频监控搜索设备
    @Headers({HEADER_CONTENT_TYPE, HEADER_ACCEPT_CONTENT})
    @POST("ia/device/searchDevice")
    Observable<HttpResponseEntity<SpjkSearchResponseEntity>> getSpjkSearchResult(@Body SpjkSearchRequestEntity request);

    //获取历史视频
    @Headers({HEADER_CONTENT_TYPE, HEADER_ACCEPT_CONTENT})
    @POST("ia/device/listHistoryVideo")
    Observable<HttpResponseEntity<HistoryVideoResponseEntity>> getlistHistoryVideo(@Body HistoryVideoRequestEntity request);

    //获取播放视频的详情信息
    @Headers({HEADER_CONTENT_TYPE, HEADER_ACCEPT_CONTENT})
    @GET("ia/device/getDeviceInfo/{cameraId}")
    Observable<HttpResponseEntity<DeviceInfoResponseEntity>> getDeviceInfo(@Path("cameraId") String cameraId);

    //控制云台方向
    @Headers({HEADER_CONTENT_TYPE, HEADER_ACCEPT_CONTENT})
    @POST("ia/device/setPtzDirection")
    Observable<HttpResponseEntity<Object>> setPtzDirection(@Body PtzDirectionRequestEntity request);
}
