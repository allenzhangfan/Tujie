package com.netposa.component.room.dao;

import com.netposa.component.room.entity.VehicleBrandEntity;
import com.netposa.component.room.entity.VehicleNKVMEntity;

import java.util.List;

import androidx.annotation.Keep;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Keep
@Dao
public interface VehicleNKVMDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<VehicleNKVMEntity> entity);

    @Query("SELECT * FROM VehicleNKVM")
    List<VehicleNKVMEntity> getAll();
}
