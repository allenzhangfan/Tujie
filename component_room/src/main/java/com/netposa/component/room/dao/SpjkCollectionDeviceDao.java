package com.netposa.component.room.dao;

import com.netposa.component.room.entity.SpjkCollectionDeviceEntiry;

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
    void insterDevice(SpjkCollectionDeviceEntiry entity);

    @Query("SELECT * FROM SpjkCollectionDevice")
    List<SpjkCollectionDeviceEntiry> getAllCollectionDevice();

    @Query("DELETE FROM SpjkCollectionDevice WHERE camerid=:id")
    void delete(String id);

    @Query("SELECT COUNT(*) FROM SpjkCollectionDevice WHERE camerid = :id")
    int loadById(String id);
}
