package com.netposa.component.room.dao;

import com.netposa.component.room.entity.SpecialCarEntity;
import com.netposa.component.room.entity.VehicleClassEntity;

import java.util.List;

import androidx.annotation.Keep;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Keep
@Dao
public interface SpecialCarDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SpecialCarEntity> entity);

    @Query("SELECT * FROM SpecialCar")
    List<SpecialCarEntity> getAll();
}
