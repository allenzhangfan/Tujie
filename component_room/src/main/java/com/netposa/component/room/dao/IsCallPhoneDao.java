package com.netposa.component.room.dao;

import com.netposa.component.room.entity.DetectionInfoEntity;
import com.netposa.component.room.entity.IsCallPhoneEntity;

import java.util.List;

import androidx.annotation.Keep;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Keep
@Dao
public interface IsCallPhoneDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<IsCallPhoneEntity> entity);

    @Query("SELECT * FROM IsCallPhone")
    List<IsCallPhoneEntity> getAll();
}
