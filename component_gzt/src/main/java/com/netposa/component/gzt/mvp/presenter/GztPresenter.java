package com.netposa.component.gzt.mvp.presenter;

import android.app.Application;
import android.content.Context;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import androidx.lifecycle.LifecycleOwner;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import javax.inject.Inject;

import com.netposa.common.constant.GlobalConstants;
import com.netposa.common.log.Log;
import com.netposa.common.utils.NetworkUtils;
import com.netposa.common.utils.SPUtils;
import com.netposa.component.gzt.R;
import com.netposa.component.gzt.mvp.contract.GztContract;
import com.netposa.component.gzt.mvp.model.entity.AllDictionaryResponseEntity;
import com.netposa.component.room.DbHelper;
import com.netposa.component.room.dao.VehicleSunVisorNumDao;
import com.netposa.component.room.entity.DetectionInfoEntity;
import com.netposa.component.room.entity.IsCallPhoneEntity;
import com.netposa.component.room.entity.MarkerTypeEntity;
import com.netposa.component.room.entity.MoveDirectionEntity;
import com.netposa.component.room.entity.MoveSpeedEntity;
import com.netposa.component.room.entity.PeccancyTypeEntity;
import com.netposa.component.room.entity.PlateColorEntity;
import com.netposa.component.room.entity.SafetyBeltNumEntity;
import com.netposa.component.room.entity.SpecialCarEntity;
import com.netposa.component.room.entity.VehicleBrandEntity;
import com.netposa.component.room.entity.VehicleNKEntity;
import com.netposa.component.room.entity.VehicleNKVMEntity;
import com.netposa.component.room.entity.VehicleSubBrandEntity;
import com.netposa.component.room.entity.VehicleSunVisorNumEntity;
import com.netposa.component.room.entity.VehicleTypeEntity;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;

import java.util.ArrayList;
import java.util.List;


@FragmentScope
public class GztPresenter extends BasePresenter<GztContract.Model, GztContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    Context mContext;

    private final DbHelper mDbHelper;

    @Inject
    public GztPresenter(GztContract.Model model, GztContract.View rootView) {
        super(model, rootView);
        mDbHelper = DbHelper.getInstance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void getDictionaryInfo() {
        if (!NetworkUtils.isConnected()) {
            mRootView.showMessage(mContext.getString(R.string.network_disconnect));
            mRootView.hideLoading();
            return;
        }
        if (SPUtils.getInstance().getBoolean(GlobalConstants.HAS_FIRST_DICTIONARY)) {
            Log.i(TAG, mContext.getString(R.string.clcx_database_copy));
            return;
        }
        mModel.getDictionaryAll()
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> mRootView.showLoading(""))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (mRootView != null) mRootView.hideLoading();
                })
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe(new ErrorHandleSubscriber<AllDictionaryResponseEntity>(mErrorHandler) {
                    @Override
                    public void onNext(AllDictionaryResponseEntity responseEntity) {
                        Log.i("responseEntity", responseEntity.toString());
                        if( null!=responseEntity.getData() ){
                  //          insertDictionary(responseEntity);
                            SPUtils.getInstance().put(GlobalConstants.HAS_FIRST_DICTIONARY, true);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e("Throwable", t.toString());
                        mRootView.getFailed();
                    }
                });
    }

    // 插入字典数据
    private void insertDictionary(AllDictionaryResponseEntity responseEntity) {
        // 标志物
        List<AllDictionaryResponseEntity.DataBean.MarkerTypeBean> markerTypeList = responseEntity.getData().getMarkerType();
        List<MarkerTypeEntity> insertDataList = new ArrayList<>(markerTypeList.size());
        for (int i = 0; i < markerTypeList.size(); i++) {
            AllDictionaryResponseEntity.DataBean.MarkerTypeBean markerTypeBean = markerTypeList.get(i);
            MarkerTypeEntity entity = new MarkerTypeEntity();
            entity.setKey(markerTypeBean.getKey());
            entity.setValue(markerTypeBean.getValue());
            insertDataList.add(entity);
        }
        mDbHelper
                .insertMarkerType(insertDataList)
                .subscribeOn(Schedulers.io())
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe();

        // 子类品牌
        List<AllDictionaryResponseEntity.DataBean.VehicleSubBrandBean> vehicleSubBrandList = responseEntity.getData().getVehicleSubBrand();
        List<VehicleSubBrandEntity> insertSubBrandList = new ArrayList<>(vehicleSubBrandList.size());
        for (int i = 0; i < vehicleSubBrandList.size(); i++) {
            AllDictionaryResponseEntity.DataBean.VehicleSubBrandBean vehicleSubBrandBean = vehicleSubBrandList.get(i);
            VehicleSubBrandEntity entity = new VehicleSubBrandEntity();
            entity.setKey(vehicleSubBrandBean.getKey());
            entity.setValue(vehicleSubBrandBean.getValue());
            insertSubBrandList.add(entity);
        }
        mDbHelper
                .insertVehicleSubBrand(insertSubBrandList)
                .subscribeOn(Schedulers.io())
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe();

        // 特殊车辆
        List<AllDictionaryResponseEntity.DataBean.SpecialCarBean> specialCarList = responseEntity.getData().getSpecialCar();
        List<SpecialCarEntity> insertSpecialCarList = new ArrayList<>(specialCarList.size());
        for (int i = 0; i < specialCarList.size(); i++) {
            AllDictionaryResponseEntity.DataBean.SpecialCarBean specialCarsBean = specialCarList.get(i);
            SpecialCarEntity entity = new SpecialCarEntity();
            entity.setKey(specialCarsBean.getKey());
            entity.setValue(specialCarsBean.getValue());
            insertSpecialCarList.add(entity);
        }
        mDbHelper
                .insertSpecialCar(insertSpecialCarList)
                .subscribeOn(Schedulers.io())
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe();

        //移动速度
        List<AllDictionaryResponseEntity.DataBean.MoveSpeedBean> moveSpeedList = responseEntity.getData().getMoveSpeed();
        List<MoveSpeedEntity> insertMoveSpeedList = new ArrayList<>(moveSpeedList.size());
        for (int i = 0; i < moveSpeedList.size(); i++) {
            AllDictionaryResponseEntity.DataBean.MoveSpeedBean moveSpeedBean = moveSpeedList.get(i);
            MoveSpeedEntity entity = new MoveSpeedEntity();
            entity.setKey(moveSpeedBean.getKey());
            entity.setValue(moveSpeedBean.getValue());
            insertMoveSpeedList.add(entity);
        }
        mDbHelper
                .insertMoveSpeed(insertMoveSpeedList)
                .subscribeOn(Schedulers.io())
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe();

        //移动方向
        List<AllDictionaryResponseEntity.DataBean.MoveDirectionBean> moveDirectionList = responseEntity.getData().getMoveDirection();
        List<MoveDirectionEntity> insertMoveDirectionList = new ArrayList<>(moveDirectionList.size());
        for (int i = 0; i < moveDirectionList.size(); i++) {
            AllDictionaryResponseEntity.DataBean.MoveDirectionBean moveDirectionBean = moveDirectionList.get(i);
            MoveDirectionEntity entity = new MoveDirectionEntity();
            entity.setKey(moveDirectionBean.getKey());
            entity.setValue(moveDirectionBean.getValue());
            insertMoveDirectionList.add(entity);
        }
        mDbHelper
                .insertMoveDirection(insertMoveDirectionList)
                .subscribeOn(Schedulers.io())
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe();

        //车牌颜色
        List<AllDictionaryResponseEntity.DataBean.PlateColorBean> plateColorList = responseEntity.getData().getPlateColor();
        List<PlateColorEntity> insertPlateColorList = new ArrayList<>(plateColorList.size());
        for (int i = 0; i < plateColorList.size(); i++) {
            AllDictionaryResponseEntity.DataBean.PlateColorBean plateColorBean = plateColorList.get(i);
            PlateColorEntity entity = new PlateColorEntity();
            entity.setKey(plateColorBean.getKey());
            entity.setValue(plateColorBean.getValue());
            insertPlateColorList.add(entity);
        }
        mDbHelper
                .insertPlateColor(insertPlateColorList)
                .subscribeOn(Schedulers.io())
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe();

        //车品牌
        List<AllDictionaryResponseEntity.DataBean.VehicleBrandBean> vehiclebrandList = responseEntity.getData().getVehicleBrand();
        List<VehicleBrandEntity> insertvehiclebrandList = new ArrayList<>(vehiclebrandList.size());
        for (int i = 0; i < vehiclebrandList.size(); i++) {
            AllDictionaryResponseEntity.DataBean.VehicleBrandBean vehicleBrandBean = vehiclebrandList.get(i);
            VehicleBrandEntity entity = new VehicleBrandEntity();
            entity.setKey(vehicleBrandBean.getKey());
            entity.setValue(vehicleBrandBean.getValue());
            insertvehiclebrandList.add(entity);
        }
        mDbHelper
                .insertVehicleBrand(insertvehiclebrandList)
                .subscribeOn(Schedulers.io())
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe();

        //违章信息
        List<AllDictionaryResponseEntity.DataBean.PeccancyTypeBean> peccancyTypeList = responseEntity.getData().getPeccancyType();
        List<PeccancyTypeEntity> insertPeccancyTypeList = new ArrayList<>(peccancyTypeList.size());
        for (int i = 0; i < peccancyTypeList.size(); i++) {
            AllDictionaryResponseEntity.DataBean.PeccancyTypeBean peccancyTypeBean = peccancyTypeList.get(i);
            PeccancyTypeEntity entity = new PeccancyTypeEntity();
            entity.setKey(peccancyTypeBean.getKey());
            entity.setValue(peccancyTypeBean.getValue());
            insertPeccancyTypeList.add(entity);
        }
        mDbHelper
                .insertPeccancyType(insertPeccancyTypeList)
                .subscribeOn(Schedulers.io())
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe();

        //车辆类型
        List<AllDictionaryResponseEntity.DataBean.VehicleTypeBean> vehicleTypeList = responseEntity.getData().getVehicleType();
        List<VehicleTypeEntity> insertVehicleTypeList = new ArrayList<>(vehicleTypeList.size());
        for (int i = 0; i < vehicleTypeList.size(); i++) {
            AllDictionaryResponseEntity.DataBean.VehicleTypeBean vehicleTypeBean = vehicleTypeList.get(i);
            VehicleTypeEntity entity = new VehicleTypeEntity();
            entity.setKey(vehicleTypeBean.getKey());
            entity.setValue(vehicleTypeBean.getValue());
            insertVehicleTypeList.add(entity);
        }
        mDbHelper
                .insertVehicleType(insertVehicleTypeList)
                .subscribeOn(Schedulers.io())
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe();

        //车辆年款VM
        List<AllDictionaryResponseEntity.DataBean.VehicleNKVMBean> VehicleNKVMList = responseEntity.getData().getVehicleNK_VM();
        List<VehicleNKVMEntity> insertVehicleNKVMList = new ArrayList<>(VehicleNKVMList.size());
        for (int i = 0; i < VehicleNKVMList.size(); i++) {
            AllDictionaryResponseEntity.DataBean.VehicleNKVMBean vehicleNKVMBean = VehicleNKVMList.get(i);
            VehicleNKVMEntity entity = new VehicleNKVMEntity();
            entity.setKey(vehicleNKVMBean.getKey());
            entity.setValue(vehicleNKVMBean.getValue());
            insertVehicleNKVMList.add(entity);
        }
        mDbHelper
                .insertVehicleNkvm(insertVehicleNKVMList)
                .subscribeOn(Schedulers.io())
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe();

        //车辆年款Nk
        List<AllDictionaryResponseEntity.DataBean.VehicleNKBean> VehicleNKList = responseEntity.getData().getVehicleNK();
        List<VehicleNKEntity> insertVehicleNKList = new ArrayList<>(VehicleNKList.size());
        for (int i = 0; i < VehicleNKList.size(); i++) {
            AllDictionaryResponseEntity.DataBean.VehicleNKBean vehicleNKBean = VehicleNKList.get(i);
            VehicleNKEntity entity = new VehicleNKEntity();
            entity.setKey(vehicleNKBean.getKey());
            entity.setValue(vehicleNKBean.getValue());
            insertVehicleNKList.add(entity);
        }
        mDbHelper
                .insertVehicleNk(insertVehicleNKList)
                .subscribeOn(Schedulers.io())
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe();

        //车辆年检标
        List<AllDictionaryResponseEntity.DataBean.DETECTIONINFOBean> DetectionInfoList = responseEntity.getData().getDETECTIONINFO();
        List<DetectionInfoEntity> insertDetectionInfoList = new ArrayList<>(DetectionInfoList.size());
        for (int i = 0; i < DetectionInfoList.size(); i++) {
            AllDictionaryResponseEntity.DataBean.DETECTIONINFOBean detectionInfoBean = DetectionInfoList.get(i);
            DetectionInfoEntity entity = new DetectionInfoEntity();
            entity.setKey(detectionInfoBean.getKey());
            entity.setValue(detectionInfoBean.getValue());
            insertDetectionInfoList.add(entity);
        }
        mDbHelper
                .insertDetectionInf(insertDetectionInfoList)
                .subscribeOn(Schedulers.io())
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe();

        //车辆安全带
        List<AllDictionaryResponseEntity.DataBean.SAFETYBELTNUMBean> SafetyBeltNumList = responseEntity.getData().getSAFETYBELTNUM();
        List<SafetyBeltNumEntity> insertSafetyBeltNumList = new ArrayList<>(SafetyBeltNumList.size());
        for (int i = 0; i < SafetyBeltNumList.size(); i++) {
            AllDictionaryResponseEntity.DataBean.SAFETYBELTNUMBean safetyBeltNumBean = SafetyBeltNumList.get(i);
            SafetyBeltNumEntity entity = new SafetyBeltNumEntity();
            entity.setKey(safetyBeltNumBean.getKey());
            entity.setValue(safetyBeltNumBean.getValue());
            insertSafetyBeltNumList.add(entity);
        }
        mDbHelper
                .insertSafetyBeltNum(insertSafetyBeltNumList)
                .subscribeOn(Schedulers.io())
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe();

        //是否打电话
        List<AllDictionaryResponseEntity.DataBean.ISCALLPHONEBean> IsCallPhoneList = responseEntity.getData().getISCALLPHONE();
        List<IsCallPhoneEntity> insertIsCallPhoneList = new ArrayList<>(IsCallPhoneList.size());
        for (int i = 0; i < IsCallPhoneList.size(); i++) {
            AllDictionaryResponseEntity.DataBean.ISCALLPHONEBean iscallphoneBean = IsCallPhoneList.get(i);
            IsCallPhoneEntity entity = new IsCallPhoneEntity();
            entity.setKey(iscallphoneBean.getKey());
            entity.setValue(iscallphoneBean.getValue());
            insertIsCallPhoneList.add(entity);
        }
        mDbHelper
                .insertIsCallPhone(insertIsCallPhoneList)
                .subscribeOn(Schedulers.io())
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe();

        //是否遮阳板
        List<AllDictionaryResponseEntity.DataBean.VEHICLESUNVISORNUMBean> VehicleSunVisorNumList = responseEntity.getData().getVEHICLESUNVISORNUM();
        List<VehicleSunVisorNumEntity> insertVehicleSunVisorNumList = new ArrayList<>(VehicleSunVisorNumList.size());
        for (int i = 0; i < VehicleSunVisorNumList.size(); i++) {
            AllDictionaryResponseEntity.DataBean.VEHICLESUNVISORNUMBean vehicleSunVisorNumBean = VehicleSunVisorNumList.get(i);
            VehicleSunVisorNumEntity entity = new VehicleSunVisorNumEntity();
            entity.setKey(vehicleSunVisorNumBean.getKey());
            entity.setValue(vehicleSunVisorNumBean.getValue());
            insertVehicleSunVisorNumList.add(entity);
        }
        mDbHelper
                .insertVehicleSunVisorNum(insertVehicleSunVisorNumList)
                .subscribeOn(Schedulers.io())
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe();
    }
}
