package com.netposa.component.room;

import com.netposa.component.room.database.AppDatabase;
import com.netposa.component.room.entity.AlarmMessageEntity;
import com.netposa.component.room.entity.JqSearchHistoryEntity;
import com.netposa.component.room.entity.LoginConfigEntity;
import com.netposa.component.room.entity.SfjbSearchHistoryEntity;
import com.netposa.component.room.entity.SpjkCollectionDeviceEntity;
import com.netposa.component.room.entity.SpjkSearchHistoryEntity;
import com.netposa.component.room.entity.YtstCarAndPeopleEntity;

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
     * 登录后插入一条用户信息
     */
    public Observable<Boolean> insertLoginConfig(LoginConfigEntity entity) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getLoginCofigDao().insert(entity);
                    return true;
                }));
    }

    /**
     * 获取所有用户信息
     *
     * @return
     */
    public Observable<List<LoginConfigEntity>> getAllLoginConfigs() {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> mAppDatabase.getLoginCofigDao().getAllLoginConfigs()));
    }

    /**
     * 根据username查找用户信息
     *
     * @param username
     * @return
     */
    public LoginConfigEntity findByUsername(String username) {
        return mAppDatabase.getLoginCofigDao().findByUsername(username);
    }

    /**
     * 更新登录用户信息
     *
     * @param entity
     * @return
     */
    public Observable<Integer> updateLoginEntity(LoginConfigEntity entity) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> mAppDatabase.getLoginCofigDao().update(entity)));
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
     *
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
     *
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
     *
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
    public Observable<Boolean> insterOneDevice(SpjkCollectionDeviceEntity entity) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getSpjkCollectionDeviceDao().insterDevice(entity);
                    return true;
                }));
    }

    /**
     * 获取关注的摄像头
     **/
    public Observable<List<SpjkCollectionDeviceEntity>> getAllCollectionDevice() {
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
     *
     * @param camerid
     * @return
     */
    public Observable<Integer> ckeckDevice(String camerid) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() ->
                        mAppDatabase.getSpjkCollectionDeviceDao().loadById(camerid)));
    }

    /**
     * 插入单条警情
     */
    public Observable<Boolean> insertAlarmMessage(AlarmMessageEntity entity) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getAlarmMessageDao().insert(entity);
                    return true;
                }));
    }

    /**
     * 插入获取到的所有警情
     */
    public Observable<Boolean> insertAllAlarmMessages(List<AlarmMessageEntity> entities) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getAlarmMessageDao().insertAll(entities);
                    return true;
                }));
    }

    /**
     * 删除所有警情缓存消息
     *
     * @return
     */
    public Observable<Boolean> deleteAllAlarmMessages() {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getAlarmMessageDao().deleteAll();
                    return true;
                }));
    }


    /**
     * 插入获取到的搜人 搜车的数据
     */
    public Observable<Boolean> insertAllYtstData(List<YtstCarAndPeopleEntity> entities) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getYtstCarAndPeopleDao().insertAll(entities);
                    return true;
                }));
    }

    /**
     * 获取所有插入的搜人和搜车的数据
     **/
    public Observable<List<YtstCarAndPeopleEntity>> getAllYtstData() {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> mAppDatabase.getYtstCarAndPeopleDao().getAll()));
    }

    /**
     * 删除所有插入的搜人和搜车的数据
     **/
    public Observable<Boolean> deleteAllYtstData() {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getYtstCarAndPeopleDao().deleteAll();
                    return true;
                }));
    }
}
