package com.netposa.component.room.dao;

import com.netposa.component.room.entity.YtstCarAndPeopleEntity;
import java.util.List;
import androidx.annotation.Keep;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Keep
@Dao
public interface YtstCarAndPeopleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<YtstCarAndPeopleEntity> entity);

    @Query("SELECT * FROM YtstCarAndPeople")
    List<YtstCarAndPeopleEntity> getAll();

    @Query("DELETE FROM YtstCarAndPeople")
    void deleteAll();


}
