package com.netposa.component.room.dao;

import com.netposa.component.room.database.AppDatabase;
import com.netposa.component.room.entity.JqSearchHistoryEntity;
import com.netposa.component.room.entity.SfjbSearchHistoryEntity;
import com.netposa.component.room.entity.SpjkCollectionDeviceEntiry;
import com.netposa.component.room.entity.SpjkSearchHistoryEntity;

import java.util.List;
import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableFromCallable;

/**
 * Author：yeguoqiang
 * Created time：2018/10/31 10:17
 * 参考：https://github.com/MindorksOpenSource/android-mvvm-architecture.git
 */
public class DbHelper {

    private static final String TAG = DbHelper.class.getSimpleName();

    AppDatabase mAppDatabase;

    private static final DbHelper sInstance = new DbHelper();

    private DbHelper() {
    }

    public static DbHelper getInstance() {
        return sInstance;
    }

    public void setAppDataBase(AppDatabase appDataBase) {
        this.mAppDatabase = appDataBase;
    }

    /**
     * 获取所有警情搜索历史
     **/
    public Observable<List<JqSearchHistoryEntity>> getAllSearchHistories() {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> mAppDatabase.getSearchHistoryDao().getAll()));
    }

    /**
     * 插入所有警情搜索历史
     *
     * @param entities
     * @return
     */
    public Observable<Boolean> insertAllSearchHistories(List<JqSearchHistoryEntity> entities) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getSearchHistoryDao().insertAll(entities);
                    return true;
                }));
    }

    /**
     * 删除所有警情搜索历史
     *
     * @return
     */
    public Observable<Boolean> deleteAllSearchHistories() {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getSearchHistoryDao().deleteAll();
                    return true;
                }));
    }

    /**
     * 删除单条警情搜索历史
     * @return
     */
    public Observable<Boolean> deleteSearchHistory(JqSearchHistoryEntity entity) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getSearchHistoryDao().delete(entity);
                    return true;
                }));
    }

    /**
     * 获取所有视频监控搜索历史
     **/
    public Observable<List<SpjkSearchHistoryEntity>> getAllSpjkSearchHistories() {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> mAppDatabase.getSpjkhistoryDao().getAll()));
    }

    /**
     * 插入所有视频监控搜索历史
     *
     * @param entities
     * @return
     */
    public Observable<Boolean> insertAllSpjkSearchHistories(List<SpjkSearchHistoryEntity> entities) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getSpjkhistoryDao().insertAll(entities);
                    return true;
                }));
    }

    /**
     * 删除所有视频监控搜索历史
     *
     * @return
     */
    public Observable<Boolean> deleteSpjkAllSearchHistories() {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getSpjkhistoryDao().deleteAll();
                    return true;
                }));
    }

    /**
     * 删除单条视频监控搜索历史
     * @return
     */
    public Observable<Boolean> deleteSpjkSearchHistory(SpjkSearchHistoryEntity entity) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getSpjkhistoryDao().delete(entity);
                    return true;
                }));
    }

    /**
     * 获取所库比对搜索历史
     **/
    public Observable<List<SfjbSearchHistoryEntity>> getAllSfjbSearchHistories() {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> mAppDatabase.getSfjbHistoryDao().getAll()));
    }

    /**
     * 插入所有库搜索历史
     *
     * @param entities
     * @return
     */
    public Observable<Boolean> insertAllSfjbSearchHistories(List<SfjbSearchHistoryEntity> entities) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getSfjbHistoryDao().insertAll(entities);
                    return true;
                }));
    }

    /**
     * 删除所有库搜索历史
     *
     * @return
     */
    public Observable<Boolean> deleteSfjbAllSearchHistories() {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getSfjbHistoryDao().deleteAll();
                    return true;
                }));
    }

    /**
     * 删除单条对比库搜索历史
     * @return
     */
    public Observable<Boolean> deleteSfjbSearchHistory(SfjbSearchHistoryEntity entity) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getSfjbHistoryDao().delete(entity);
                    return true;
                }));
    }

    /**
     * 视频监控 插入关注的摄像头
     **/
    public Observable<Boolean> insterOneDevice(SpjkCollectionDeviceEntiry entity) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getSpjkCollectionDeviceDao().insterDevice(entity);
                    return true;
                }));
    }

    /**
     * 获取关注的摄像头
     **/
    public Observable<List<SpjkCollectionDeviceEntiry>> getAllCollectionDevice() {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() ->
                        mAppDatabase.getSpjkCollectionDeviceDao().getAllCollectionDevice()));
    }

    /**
     * 删除一条关注的摄像头
     */
    public Observable<Boolean> deleteDevice(String entity) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getSpjkCollectionDeviceDao().delete(entity);
                    return true;
                }));
    }

    /**
     * 根据camerid 查看数据库里是否存在
     * @param camerid
     * @return
     */
    public Observable<Integer> ckeckDevice(String camerid) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() ->
                        mAppDatabase.getSpjkCollectionDeviceDao().loadById(camerid)));
    }



}
