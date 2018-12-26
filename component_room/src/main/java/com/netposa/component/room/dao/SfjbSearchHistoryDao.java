package com.netposa.component.room.dao;

import com.netposa.component.room.entity.SfjbSearchHistoryEntity;

import java.util.List;

import androidx.annotation.Keep;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

/**
 * Author：yeguoqiang
 * Created time：2018/10/30 19:57
 */
@Keep
@Dao
public interface SfjbSearchHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SfjbSearchHistoryEntity> entities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SfjbSearchHistoryEntity entity);

    @Query("SELECT * FROM SfjbSearchHistory")
    List<SfjbSearchHistoryEntity> getAll();

    @Query("DELETE FROM SfjbSearchHistory")
    void deleteAll();

    @Delete
    void delete(SfjbSearchHistoryEntity entity);

    @Query("SELECT count(*) FROM SfjbSearchHistory")
    int getTotalCount();
}
