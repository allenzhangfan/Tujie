package com.netposa.component.room.dao;

import com.netposa.component.room.entity.VehicleClassEntity;
import com.netposa.component.room.entity.VehicleSubBrandEntity;

import java.util.List;

import androidx.annotation.Keep;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Keep
@Dao
public interface VehicleClassDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<VehicleClassEntity> entity);

    @Query("SELECT * FROM VehicleClass")
    List<VehicleClassEntity> getAll();
}