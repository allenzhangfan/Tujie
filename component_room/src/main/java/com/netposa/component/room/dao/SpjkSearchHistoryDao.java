package com.netposa.component.room.dao;

import com.netposa.component.room.entity.SpjkSearchHistoryEntity;

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
public interface SpjkSearchHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SpjkSearchHistoryEntity> entities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SpjkSearchHistoryEntity entity);

    @Query("SELECT * FROM SpjkSearchHistory")
    List<SpjkSearchHistoryEntity> getAll();

    @Query("DELETE FROM SpjkSearchHistory")
    void deleteAll();

    @Delete
    void delete(SpjkSearchHistoryEntity entity);

    @Query("SELECT count(*) FROM SpjkSearchHistory")
    int getTotalCount();
}
