package com.netposa.component.room.dao;

import com.netposa.component.room.entity.MoveSpeedEntity;
import com.netposa.component.room.entity.PeccancyTypeEntity;

import java.util.List;

import androidx.annotation.Keep;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Keep
@Dao
public interface MoveSpeedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MoveSpeedEntity> entity);

    @Query("SELECT * FROM MoveSpeed")
    List<MoveSpeedEntity> getAll();
}
