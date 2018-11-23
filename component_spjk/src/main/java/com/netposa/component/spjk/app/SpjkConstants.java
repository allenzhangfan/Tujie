/*
 * Copyright 2018 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netposa.component.spjk.app;

/**
 * ================================================
 * Created by JessYan on 25/04/2018 16:48
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public interface SpjkConstants {
    int NUMBER_OF_PAGE = 10;
    int PAGE_SIZE = 1;
    String MONITOR = "monitor";
    String CAMERA = "camera";
    String ORG = "org";
    int CHILD_COUNT = 0;
    double DEFAULT_LATITUDE = 30.4920980180;
    double DEFAULT_LONGITUDE = 114.4104945660;
    float DEFAULT_MAPBOX_CAMERAZOOM = 13;
    String KEY_SINGLE_CAMERA_ID = "key_single_camera_id";
    String KEY_SINGLE_CAMERA_LOCATION = "key_single_camera_location";
    String KEY_SINGLE_CAMERA_HISTORY_VIDEO = "key_single_camera_history_video";
    String KEY_HISTORY_PLAY_VIDEO = "key_history_play_video";
    String KEY_HISTORY_VIDEO_ORG_NAME = "key_history_video_org_name";
    String KEY_NEIGHBOURING_DEVICES = "key_neighbouring_devices";
    int MSG_OPENVIDEO_SUC = 30001;
    int MSG_SHOWLOADING = 30002;
    int MSG_ONERROR = 30003;
    int MSG_ONERROR_C = 30004;
    int MSG_ONERROR_NET = 30005;
    int MIN_CLICK_DELAY_TIME = 1000;
    String JPG = "_screenshot.jpg";
    String DATA_STRING = "yyyyMMddhhmmss";

    int NETWORK_NONE = -1;
    int NETWORK_MOBILE = 0;
    int NETWORK_WIFI = 1;
    String CAMERA_TYPE = "camera";
    String TRAFFIC_TYPE = "traffic";
    String FACE_TYPE = "face";
    String BODY_TYPE = "body";
    String KEY_CAMERA_ATTENTION_FLAG = "is_collection";
    String RESUME_CAMERA_ID="resumeCameraId";
    String KEY_VIDEO_TYPE = "cameratype";
    String KEY_HIDE_VIEW = "hide";
}
