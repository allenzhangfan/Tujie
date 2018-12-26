package com.netposa.component.room.dao;

import com.netposa.component.room.entity.IsCallPhoneEntity;
import com.netposa.component.room.entity.VehicleSunVisorNumEntity;

import java.util.List;

import androidx.annotation.Keep;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Keep
@Dao
public interface VehicleSunVisorNumDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<VehicleSunVisorNumEntity> entity);

    @Query("SELECT * FROM VehicleSunVisorNum")
    List<VehicleSunVisorNumEntity> getAll();
}
