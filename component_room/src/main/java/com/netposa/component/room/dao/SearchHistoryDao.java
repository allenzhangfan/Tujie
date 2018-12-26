package com.netposa.component.room.dao;

import com.netposa.component.room.entity.JqSearchHistoryEntity;

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
public interface SearchHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<JqSearchHistoryEntity> entities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(JqSearchHistoryEntity entity);

    @Query("SELECT * FROM JqSearchHistory")
    List<JqSearchHistoryEntity> getAll();

    @Query("DELETE FROM JqSearchHistory")
    void deleteAll();

    @Delete
    void delete(JqSearchHistoryEntity entity);

    @Query("SELECT count(*) FROM JqSearchHistory")
    int getTotalCount();
}
