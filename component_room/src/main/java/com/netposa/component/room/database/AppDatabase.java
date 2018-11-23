package com.netposa.component.room.database;

import com.netposa.component.room.converter.Converters;
import com.netposa.component.room.dao.LoginCofigDao;
import com.netposa.component.room.dao.SearchHistoryDao;
import com.netposa.component.room.dao.SfjbSearchHistoryDao;
import com.netposa.component.room.dao.SpjkCollectionDeviceDao;
import com.netposa.component.room.dao.SpjkSearchHistoryDao;
import com.netposa.component.room.entity.SfjbSearchHistoryEntity;
import com.netposa.component.room.entity.SpjkCollectionDeviceEntiry;
import com.netposa.component.room.entity.JqSearchHistoryEntity;
import com.netposa.component.room.entity.LoginConfigEntity;
import com.netposa.component.room.entity.SpjkSearchHistoryEntity;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * Created by yexiaokang on 2018/10/8.
 */
@TypeConverters({Converters.class})
@Database(entities = {
        LoginConfigEntity.class,
        JqSearchHistoryEntity.class,
        SpjkSearchHistoryEntity.class,
        SpjkCollectionDeviceEntiry.class,
        SfjbSearchHistoryEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract LoginCofigDao getLoginCofigDao();
    public abstract SearchHistoryDao getSearchHistoryDao();
    public abstract SpjkSearchHistoryDao getSpjkhistoryDao();
    public abstract SpjkCollectionDeviceDao getSpjkCollectionDeviceDao();
    public abstract SfjbSearchHistoryDao getSfjbHistoryDao();
}