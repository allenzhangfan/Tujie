package com.netposa.component.room.dao;

import com.netposa.component.room.entity.VehicleBrandEntity;
import com.netposa.component.room.entity.VehicleClassEntity;

import java.util.List;

import androidx.annotation.Keep;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Keep
@Dao
public interface VehicleBrandDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<VehicleBrandEntity> entity);

    @Query("SELECT * FROM VehicleBrand")
    List<VehicleBrandEntity> getAll();
}
