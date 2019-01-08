package com.netposa.component.room.database;

import com.netposa.component.room.BuildConfig;
import com.netposa.component.room.converter.Converters;
import com.netposa.component.room.dao.AlarmMessageDao;
import com.netposa.component.room.dao.LoginCofigDao;
import com.netposa.component.room.dao.SearchHistoryDao;
import com.netposa.component.room.dao.SfjbSearchHistoryDao;
import com.netposa.component.room.dao.SpjkCollectionDeviceDao;
import com.netposa.component.room.dao.SpjkSearchHistoryDao;
import com.netposa.component.room.dao.YtstCarAndPeopleDao;
import com.netposa.component.room.entity.AlarmMessageEntity;
import com.netposa.component.room.entity.LoginConfigEntity;
import com.netposa.component.room.entity.SfjbSearchHistoryEntity;
import com.netposa.component.room.entity.SpjkCollectionDeviceEntity;
import com.netposa.component.room.entity.JqSearchHistoryEntity;
import com.netposa.component.room.entity.SpjkSearchHistoryEntity;
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
        YtstCarAndPeopleEntity.class}, version = BuildConfig.VERSION_DB)
public abstract class AppDatabase extends RoomDatabase {

    public abstract LoginCofigDao getLoginCofigDao();

    public abstract AlarmMessageDao getAlarmMessageDao();

    public abstract SearchHistoryDao getSearchHistoryDao();

    public abstract SpjkSearchHistoryDao getSpjkhistoryDao();

    public abstract SpjkCollectionDeviceDao getSpjkCollectionDeviceDao();

    public abstract SfjbSearchHistoryDao getSfjbHistoryDao();

    public abstract YtstCarAndPeopleDao getYtstCarAndPeopleDao();
}