package com.netposa.component.room.dao;

import com.netposa.component.room.entity.LoginConfigEntity;

import java.util.List;

import androidx.annotation.Keep;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

/**
 * Author：yeguoqiang
 * Created time：2018/10/22 16:27
 * 管理所有登录过的用户
 */
@Keep
@Dao
public interface LoginCofigDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LoginConfigEntity entity);

    @Query("SELECT * FROM LoginConfig WHERE username =:username ")
    LoginConfigEntity findByUsername(String username);

    @Update()
    int update(LoginConfigEntity entity);

    @Query("SELECT * FROM LoginConfig")
    List<LoginConfigEntity> getAllLoginConfigs();
}
