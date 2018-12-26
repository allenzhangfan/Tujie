package com.netposa.component.room.dao;

import com.netposa.component.room.entity.SafetyBeltNumEntity;
import com.netposa.component.room.entity.VehicleNKEntity;

import java.util.List;

import androidx.annotation.Keep;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Keep
@Dao
public interface SafetyBeltNumDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SafetyBeltNumEntity> entity);

    @Query("SELECT * FROM SafetyBeltNum")
    List<SafetyBeltNumEntity> getAll();
}
