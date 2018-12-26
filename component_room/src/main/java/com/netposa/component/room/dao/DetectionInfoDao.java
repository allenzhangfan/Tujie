package com.netposa.component.room.dao;

import com.netposa.component.room.entity.DetectionInfoEntity;
import com.netposa.component.room.entity.VehicleNKVMEntity;

import java.util.List;

import androidx.annotation.Keep;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Keep
@Dao
public interface DetectionInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<DetectionInfoEntity> entity);

    @Query("SELECT * FROM DetectionInfo")
    List<DetectionInfoEntity> getAll();
}
