package com.netposa.component.room.dao;

import com.netposa.component.room.entity.MarkerTypeEntity;
import com.netposa.component.room.entity.PeccancyTypeEntity;

import java.util.List;

import androidx.annotation.Keep;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Keep
@Dao
public interface MakertypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MarkerTypeEntity> entity);

    @Query("SELECT * FROM MarkerType")
    List<MarkerTypeEntity> getAll();
}
