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
package com.netposa.component.clcx.app;

/**
 * ================================================
 * Created by JessYan on 25/04/2018 16:48
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public interface ClcxConstants {
    String KEY_SINGLE_TYPE = "key_single_type";
    String KEY_SINGLE_RESULT="key_single_result";
    String KEY_DICTIONARY_TYPE_START_TIME="key_dictionary_type_start_time"; //结束时间
    String KEY_DICTIONARY_TYPE_END_TIME="key_dictionary_type_end_time"; // 开始时间
    String KEY_POSITION="key_position";
    String KEY_VEHICLE_TRACK="key_vehicle_track"; //车辆轨迹
    String mType_plate = "plate";//车牌类型；
    String mType_car = "car";//车辆类型；
    String KEY_SELECT_RESULT = "key_select_result";
    String KEY_CAR_DETAIL="key_car_detail";
    int REQUESTCODE_CAR_TYPE = 1;
    int REQUESTCODE_CAR_PLATE = 2;
    String KEY_LIST_TYPE = "key_car_type";
    String KEY_QUANBU = "全部";
    String KEY_PIC_PATH = "key_pic_path";
    String VEHICLE_YEAR_TYPE_DEEPLINT="DEEPLINT";
    String VEHICLE_YEAR_TYPE_PCC="PCC";
}
