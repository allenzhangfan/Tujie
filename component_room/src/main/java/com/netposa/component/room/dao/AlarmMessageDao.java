package com.netposa.component.room.dao;

import com.netposa.component.room.entity.AlarmMessageEntity;

import java.util.List;

import androidx.annotation.Keep;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Maybe;

/**
 * Created by yexiaokang on 2018/10/8.
 */
@Keep
@Dao
public interface AlarmMessageDao {

    @Query("SELECT * FROM AlarmMessage")
    Maybe<List<AlarmMessageEntity>> getAllAlarmMessages();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AlarmMessageEntity entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<AlarmMessageEntity> entities);

    @Delete
    void deleteAll(List<AlarmMessageEntity> entities);

    @Query("DELETE FROM AlarmMessage")
    void deleteAll();

    @Query("SELECT count(*) FROM AlarmMessage")
    int getTotalCount();
}
