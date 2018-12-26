package com.netposa.component.room.dao;

import com.netposa.component.room.entity.SpjkCollectionDeviceEntity;

import java.util.List;

import androidx.annotation.Keep;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Keep
@Dao
public interface SpjkCollectionDeviceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insterDevice(SpjkCollectionDeviceEntity entity);

    @Query("SELECT * FROM SpjkCollectionDevice")
    List<SpjkCollectionDeviceEntity> getAllCollectionDevice();

    @Query("DELETE FROM SpjkCollectionDevice WHERE camerid=:id")
    void delete(String id);

    @Query("SELECT COUNT(*) FROM SpjkCollectionDevice WHERE camerid = :id")
    int loadById(String id);
}
