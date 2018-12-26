package com.netposa.component.room.dao;

import com.netposa.component.room.entity.VehicleSubBrandEntity;

import java.util.List;

import androidx.annotation.Keep;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Keep
@Dao
public interface VehicleSubBrandDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<VehicleSubBrandEntity> entity);

    @Query("SELECT * FROM VehicleSubBrand")
    List<VehicleSubBrandEntity> getAll();
}
