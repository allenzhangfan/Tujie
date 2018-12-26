package com.netposa.component.room.database;

import com.netposa.component.room.BuildConfig;
import com.netposa.component.room.converter.Converters;
import com.netposa.component.room.dao.AlarmMessageDao;
import com.netposa.component.room.dao.DetectionInfoDao;
import com.netposa.component.room.dao.IsCallPhoneDao;
import com.netposa.component.room.dao.LoginCofigDao;
import com.netposa.component.room.dao.MakertypeDao;
import com.netposa.component.room.dao.MoveDirectionDao;
import com.netposa.component.room.dao.MoveSpeedDao;
import com.netposa.component.room.dao.PeccancyTypeDao;
import com.netposa.component.room.dao.PlateColorDao;
import com.netposa.component.room.dao.SafetyBeltNumDao;
import com.netposa.component.room.dao.SearchHistoryDao;
import com.netposa.component.room.dao.SfjbSearchHistoryDao;
import com.netposa.component.room.dao.SpecialCarDao;
import com.netposa.component.room.dao.SpjkCollectionDeviceDao;
import com.netposa.component.room.dao.SpjkSearchHistoryDao;
import com.netposa.component.room.dao.VehicleBrandDao;
import com.netposa.component.room.dao.VehicleClassDao;
import com.netposa.component.room.dao.VehicleNKDao;
import com.netposa.component.room.dao.VehicleNKVMDao;
import com.netposa.component.room.dao.VehicleSubBrandDao;
import com.netposa.component.room.dao.VehicleSunVisorNumDao;
import com.netposa.component.room.dao.VehicleTypeDao;
import com.netposa.component.room.dao.YtstCarAndPeopleDao;
import com.netposa.component.room.entity.AlarmMessageEntity;
import com.netposa.component.room.entity.DetectionInfoEntity;
import com.netposa.component.room.entity.IsCallPhoneEntity;
import com.netposa.component.room.entity.MarkerTypeEntity;
import com.netposa.component.room.entity.MoveDirectionEntity;
import com.netposa.component.room.entity.MoveSpeedEntity;
import com.netposa.component.room.entity.PeccancyTypeEntity;
import com.netposa.component.room.entity.PlateColorEntity;
import com.netposa.component.room.entity.SafetyBeltNumEntity;
import com.netposa.component.room.entity.SfjbSearchHistoryEntity;
import com.netposa.component.room.entity.SpecialCarEntity;
import com.netposa.component.room.entity.SpjkCollectionDeviceEntity;
import com.netposa.component.room.entity.JqSearchHistoryEntity;
import com.netposa.component.room.entity.LoginConfigEntity;
import com.netposa.component.room.entity.SpjkSearchHistoryEntity;
import com.netposa.component.room.entity.VehicleBrandEntity;
import com.netposa.component.room.entity.VehicleClassEntity;
import com.netposa.component.room.entity.VehicleNKEntity;
import com.netposa.component.room.entity.VehicleNKVMEntity;
import com.netposa.component.room.entity.VehicleSubBrandEntity;
import com.netposa.component.room.entity.VehicleSunVisorNumEntity;
import com.netposa.component.room.entity.VehicleTypeEntity;
import com.netposa.component.room.entity.YtstCarAndPeopleEntity;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * Created by yexiaokang on 2018/10/8.
 */
@TypeConverters({Converters.class})
@Database(entities = {
        LoginConfigEntity.class,
        AlarmMessageEntity.class,
        JqSearchHistoryEntity.class,
        SpjkSearchHistoryEntity.class,
        SpjkCollectionDeviceEntity.class,
        SfjbSearchHistoryEntity.class,
        PeccancyTypeEntity.class,
        MarkerTypeEntity.class,
        VehicleSubBrandEntity.class,
        VehicleClassEntity.class,
        SpecialCarEntity.class,
        MoveSpeedEntity.class,
        MoveDirectionEntity.class,
        PlateColorEntity.class,
        VehicleTypeEntity.class,
        VehicleNKVMEntity.class,
        VehicleNKEntity.class,
        DetectionInfoEntity.class,
        SafetyBeltNumEntity.class,
        IsCallPhoneEntity.class,
        VehicleSunVisorNumEntity.class,
        VehicleBrandEntity.class,
        YtstCarAndPeopleEntity.class}, version = BuildConfig.VERSION_DB)
public abstract class AppDatabase extends RoomDatabase {

    public abstract LoginCofigDao getLoginCofigDao();

    public abstract AlarmMessageDao getAlarmMessageDao();

    public abstract SearchHistoryDao getSearchHistoryDao();

    public abstract SpjkSearchHistoryDao getSpjkhistoryDao();

    public abstract SpjkCollectionDeviceDao getSpjkCollectionDeviceDao();

    public abstract SfjbSearchHistoryDao getSfjbHistoryDao();

    public abstract PeccancyTypeDao getPeccancyTypeDao();

    public abstract MakertypeDao getMakertypeDao();

    public abstract VehicleSubBrandDao getVehicleSubBrandDao();

    public abstract VehicleClassDao getVehicleClassDao();

    public abstract SpecialCarDao getSpecialCarDao();

    public abstract MoveSpeedDao getMoveSpeedDao();

    public abstract MoveDirectionDao getMoveDirectionDao();

    public abstract PlateColorDao getPlateColorDao();

    public abstract VehicleBrandDao getVehicleBrandDao();

    public abstract VehicleTypeDao getVehicleTypeDao();

    public abstract YtstCarAndPeopleDao getYtstCarAndPeopleDao();

    public abstract VehicleNKVMDao getVehicleNKVMDao();

    public abstract VehicleNKDao getVehicleNKDao();

    public abstract DetectionInfoDao getDetectionInfoDao();

    public abstract SafetyBeltNumDao getSafetyBeltNumDao();

    public abstract IsCallPhoneDao getIsCallPhoneDao();

    public abstract VehicleSunVisorNumDao getVehicleSunVisorNumDao();
}