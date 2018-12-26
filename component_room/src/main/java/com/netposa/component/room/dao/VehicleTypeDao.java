package com.netposa.component.room.dao;

import com.netposa.component.room.entity.VehicleTypeEntity;

import java.util.List;

import androidx.annotation.Keep;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Keep
@Dao
public interface VehicleTypeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<VehicleTypeEntity> entity);

    @Query("SELECT * FROM VehicleType")
    List<VehicleTypeEntity> getAll();
}
