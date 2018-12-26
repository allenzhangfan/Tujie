package com.netposa.component.room.dao;

import com.netposa.component.room.entity.MoveDirectionEntity;
import com.netposa.component.room.entity.MoveSpeedEntity;

import java.util.List;

import androidx.annotation.Keep;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Keep
@Dao
public interface MoveDirectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MoveDirectionEntity> entity);

    @Query("SELECT * FROM MoveDirection")
    List<MoveDirectionEntity> getAll();
}