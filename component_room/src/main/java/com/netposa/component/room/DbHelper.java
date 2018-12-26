package com.netposa.component.room;

import com.netposa.component.room.database.AppDatabase;
import com.netposa.component.room.entity.AlarmMessageEntity;
import com.netposa.component.room.entity.DetectionInfoEntity;
import com.netposa.component.room.entity.IsCallPhoneEntity;
import com.netposa.component.room.entity.JqSearchHistoryEntity;
import com.netposa.component.room.entity.MarkerTypeEntity;
import com.netposa.component.room.entity.MoveDirectionEntity;
import com.netposa.component.room.entity.MoveSpeedEntity;
import com.netposa.component.room.entity.PeccancyTypeEntity;
import com.netposa.component.room.entity.PlateColorEntity;
import com.netposa.component.room.entity.SafetyBeltNumEntity;
import com.netposa.component.room.entity.SfjbSearchHistoryEntity;
import com.netposa.component.room.entity.SpecialCarEntity;
import com.netposa.component.room.entity.SpjkCollectionDeviceEntity;
import com.netposa.component.room.entity.SpjkSearchHistoryEntity;
import com.netposa.component.room.entity.VehicleBrandEntity;
import com.netposa.component.room.entity.VehicleClassEntity;
import com.netposa.component.room.entity.VehicleNKEntity;
import com.netposa.component.room.entity.VehicleNKVMEntity;
import com.netposa.component.room.entity.VehicleSubBrandEntity;
import com.netposa.component.room.entity.VehicleSunVisorNumEntity;
import com.netposa.component.room.entity.VehicleTypeEntity;
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
     * 插入字典码表 (违章信息)
     */
    public Observable<Boolean> insertPeccancyType(List<PeccancyTypeEntity> entity) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getPeccancyTypeDao().insertAll(entity);
                    return true;
                }));
    }

    /**
     * 获取字典码表(违章信息)
     */
    public Observable<List<PeccancyTypeEntity>> getPeccancyType() {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() ->
                        mAppDatabase.getPeccancyTypeDao().getAll()));
    }

    /**
     * 插入字典码表 (标志物)
     */
    public Observable<Boolean> insertMarkerType(List<MarkerTypeEntity> entity) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getMakertypeDao().insertAll(entity);
                    return true;
                }));
    }

    /**
     * 获取字典码表(标志物)
     */
    public Observable<List<MarkerTypeEntity>> getMarkerType() {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() ->
                        mAppDatabase.getMakertypeDao().getAll()));
    }

    /**
     * 插入字典码表 (子类品牌)
     */
    public Observable<Boolean> insertVehicleSubBrand(List<VehicleSubBrandEntity> entity) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getVehicleSubBrandDao().insertAll(entity);
                    return true;
                }));
    }

    /**
     * 获取字典码表(子类品牌)
     */
    public Observable<List<VehicleSubBrandEntity>> getVehicleSubBrand() {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() ->
                        mAppDatabase.getVehicleSubBrandDao().getAll()));
    }

    /**
     * 插入字典码表 (车类)
     */
    public Observable<Boolean> insertVehicleClass(List<VehicleClassEntity> entity) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getVehicleClassDao().insertAll(entity);
                    return true;
                }));
    }

    /**
     * 获取字典码表(车类)
     */
    public Observable<List<VehicleClassEntity>> getVehicleClass() {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() ->
                        mAppDatabase.getVehicleClassDao().getAll()));
    }

    /**
     * 插入字典码表 (特殊车辆)
     */
    public Observable<Boolean> insertSpecialCar(List<SpecialCarEntity> entity) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getSpecialCarDao().insertAll(entity);
                    return true;
                }));
    }

    /**
     * 获取字典码表(特殊车辆)
     */
    public Observable<List<SpecialCarEntity>> getSpecialCar() {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() ->
                        mAppDatabase.getSpecialCarDao().getAll()));
    }

    /**
     * 插入字典码表 (移动速度)
     */
    public Observable<Boolean> insertMoveSpeed(List<MoveSpeedEntity> entity) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getMoveSpeedDao().insertAll(entity);
                    return true;
                }));
    }

    /**
     * 获取字典码表(移动速度)
     */
    public Observable<List<MoveSpeedEntity>> getMoveSpeed() {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() ->
                        mAppDatabase.getMoveSpeedDao().getAll()));
    }

    /**
     * 插入字典码表 (移动方向)
     */
    public Observable<Boolean> insertMoveDirection(List<MoveDirectionEntity> entity) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getMoveDirectionDao().insertAll(entity);
                    return true;
                }));
    }

    /**
     * 获取字典码表(移动方向)
     */
    public Observable<List<MoveDirectionEntity>> getMoveDirection() {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() ->
                        mAppDatabase.getMoveDirectionDao().getAll()));
    }

    /**
     * 插入字典码表 (车牌颜色)
     */
    public Observable<Boolean> insertPlateColor(List<PlateColorEntity> entity) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getPlateColorDao().insertAll(entity);
                    return true;
                }));
    }

    /**
     * 获取字典码表(车牌颜色)
     */
    public Observable<List<PlateColorEntity>> getPlateColor() {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() ->
                        mAppDatabase.getPlateColorDao().getAll()));
    }

    /**
     * 插入字典码表 (车辆品牌)
     */
    public Observable<Boolean> insertVehicleBrand(List<VehicleBrandEntity> entity) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getVehicleBrandDao().insertAll(entity);
                    return true;
                }));
    }

    /**
     * 获取字典码表(车辆品牌)
     */
    public Observable<List<VehicleBrandEntity>> getVehicleBrand() {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() ->
                        mAppDatabase.getVehicleBrandDao().getAll()));
    }

    /**
     * 插入字典码表 (车辆类型)
     */
    public Observable<Boolean> insertVehicleType(List<VehicleTypeEntity> entity) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getVehicleTypeDao().insertAll(entity);
                    return true;
                }));
    }

    /**
     * 获取字典码表(车辆类型)
     */
    public Observable<List<VehicleTypeEntity>> getVehicleType() {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() ->
                        mAppDatabase.getVehicleTypeDao().getAll()));
    }

    /**
     * 获取字典码表(车辆年款类型VM)
     */
    public Observable<List<VehicleNKVMEntity>> getVehicleNkvm() {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() ->
                        mAppDatabase.getVehicleNKVMDao().getAll()));
    }

    /**
     * 插入字典码表 (车辆年款类型VM)
     */
    public Observable<Boolean> insertVehicleNkvm(List<VehicleNKVMEntity> entity) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getVehicleNKVMDao().insertAll(entity);
                    return true;
                }));
    }

    /**
     * 获取字典码表(车辆年款类型NK)
     */
    public Observable<List<VehicleNKEntity>> getVehicleNk() {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() ->
                        mAppDatabase.getVehicleNKDao().getAll()));
    }

    /**
     * 插入字典码表 (车辆年款类型NK)
     */
    public Observable<Boolean> insertVehicleNk(List<VehicleNKEntity> entity) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getVehicleNKDao().insertAll(entity);
                    return true;
                }));
    }

    /**
     * 获取字典码表(车辆年检标)
     */
    public Observable<List<DetectionInfoEntity>> getDetectionInf() {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() ->
                        mAppDatabase.getDetectionInfoDao().getAll()));
    }

    /**
     * 插入字典码表 (车辆年检标)
     */
    public Observable<Boolean> insertDetectionInf(List<DetectionInfoEntity> entity) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getDetectionInfoDao().insertAll(entity);
                    return true;
                }));
    }

    /**
     * 获取字典码表(车辆安全带)
     */
    public Observable<List<SafetyBeltNumEntity>> getSafetyBeltNumInf() {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() ->
                        mAppDatabase.getSafetyBeltNumDao().getAll()));
    }

    /**
     * 插入字典码表 (车辆安全带)
     */
    public Observable<Boolean> insertSafetyBeltNum(List<SafetyBeltNumEntity> entity) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getSafetyBeltNumDao().insertAll(entity);
                    return true;
                }));
    }

    /**
     * 获取字典码表（是否打电话)
     */
    public Observable<List<IsCallPhoneEntity>> getIsCallPhoneInf() {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() ->
                        mAppDatabase.getIsCallPhoneDao().getAll()));
    }

    /**
     * 插入字典码表 (是否打电话)
     */
    public Observable<Boolean> insertIsCallPhone(List<IsCallPhoneEntity> entity) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getIsCallPhoneDao().insertAll(entity);
                    return true;
                }));
    }

    /**
     * 获取字典码表（遮阳板)
     */
    public Observable<List<VehicleSunVisorNumEntity>> getVehicleSunVisorNumInf() {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() ->
                        mAppDatabase.getVehicleSunVisorNumDao().getAll()));
    }

    /**
     * 插入字典码表 (遮阳板)
     */
    public Observable<Boolean> insertVehicleSunVisorNum(List<VehicleSunVisorNumEntity> entity) {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getVehicleSunVisorNumDao().insertAll(entity);
                    return true;
                }));
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
     *删除所有插入的搜人和搜车的数据
     **/
    public Observable<Boolean> deleteAllYtstData() {
        return Observable
                .fromCallable(new ObservableFromCallable<>(() -> {
                    mAppDatabase.getYtstCarAndPeopleDao().deleteAll();
                    return true;
                }));
    }
}
