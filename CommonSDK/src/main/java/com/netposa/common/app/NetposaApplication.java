package com.netposa.common.app;

import com.jess.arms.base.BaseApplication;
import com.netposa.component.room.DbHelper;
import com.netposa.component.room.database.AppDatabase;
import com.netposa.component.room.migration.Migrations;

import androidx.room.Room;

import static com.netposa.common.constant.GlobalConstants.MAIN_DB_NAME;

/**
 * Created by yeguoqiang on 2018/10/26.
 * 参考：https://github.com/guiying712/AndroidModulePattern.git实现加载组件中的fragment
 */
public class NetposaApplication extends BaseApplication {

    private AppDatabase mAppDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppDatabase = Room.databaseBuilder(this, AppDatabase.class, MAIN_DB_NAME)
                .allowMainThreadQueries()
                .addMigrations(Migrations.MIGRATION_1_2, Migrations.MIGRATION_2_3)
                .build();
        //初始化一次即可
        DbHelper.getInstance().setAppDataBase(mAppDatabase);
    }

    public AppDatabase getAppDatabase() {
        return mAppDatabase;
    }
}
