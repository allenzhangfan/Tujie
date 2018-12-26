package com.netposa.component.spjk.mvp.presenter;

import android.app.Application;
import android.content.Context;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.mapbox.geojson.Point;
import com.netposa.common.log.Log;
import com.netposa.common.utils.NetworkUtils;
import com.netposa.component.room.DbHelper;
import com.netposa.component.room.entity.SpjkCollectionDeviceEntity;
import com.netposa.component.spjk.R;
import com.netposa.component.spjk.mvp.contract.SpjkContract;
import com.netposa.component.spjk.mvp.model.entity.OneKilometerCamerasRequestEntity;
import com.netposa.component.spjk.mvp.model.entity.OneKilometerCamerasResponseEntity;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import static com.netposa.component.spjk.mvp.model.entity.OneKilometerCamerasRequestEntity.TYPE_POLYGON;


@ActivityScope
public class SpjkPresenter extends BasePresenter<SpjkContract.Model, SpjkContract.View> {
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
    @Inject
    OneKilometerCamerasRequestEntity mOKCRequestEntity;
    @Inject
    FragmentManager mFm;
    private final DbHelper mDbHelper;

    @Inject
    public SpjkPresenter(SpjkContract.Model model, SpjkContract.View rootView) {
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

    public void getNeighbouringDevice(List<Point> circleGeometryPoints) {
        Log.i(TAG, "start to getNeighbouringDevice");
        if (!NetworkUtils.isConnected()) {
            mRootView.showMessage(mContext.getString(R.string.network_disconnect));
            mRootView.hideLoading();
            return;
        }
        mOKCRequestEntity.setType(TYPE_POLYGON);
        List<String> abilities = new ArrayList<>();
        abilities.add("camera");
        abilities.add("face");
        abilities.add("traffic");
        abilities.add("body");
        abilities.add("nonmotor");
        abilities.add("wifi");
        abilities.add("rfid");
        mOKCRequestEntity.setAbilities(abilities);

        List<List<Double>> allPoints = new ArrayList<>();
        for (Point circleGeometryPoint : circleGeometryPoints) {
            List<Double> point = new ArrayList<>();
            point.add(circleGeometryPoint.longitude());
            point.add(circleGeometryPoint.latitude());
            allPoints.add(point);
        }
        mOKCRequestEntity.setCoordinates(allPoints);

        mModel.getNeighbouringDevice(mOKCRequestEntity)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading("");//显示下拉刷新的进度条
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();//隐藏下拉刷新的进度条
                })
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe(new ErrorHandleSubscriber<List<OneKilometerCamerasResponseEntity>>(mErrorHandler) {
                    @Override
                    public void onNext(List<OneKilometerCamerasResponseEntity> reponse) {
                        Log.i(TAG, "reponse:" + reponse);
                        mRootView.onGetNeighbouringDeviceSuccess(reponse);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.onGetNeighbouringDeviceFail();
                    }
                });
    }

    /**
     * 插入一条关注数据
     *
     * @param deviceEntity
     */
    public void insterDevice(SpjkCollectionDeviceEntity deviceEntity) {
        Log.d(TAG,"insterDevice:"+deviceEntity);
        mDbHelper.insterOneDevice(deviceEntity)
                .subscribeOn(Schedulers.io())
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe();
    }

    /**
     * 查看当前的设备是否被关注
     *
     * @param cameraId
     */
    public void checkDevice(String cameraId) {
        Log.d(TAG,"checkDevice:"+cameraId);
        mDbHelper.ckeckDevice(cameraId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading("");//显示下拉刷新的进度条
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();//隐藏下拉刷新的进度条
                })
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe(new ErrorHandleSubscriber<Integer>(mErrorHandler) {
                    @Override
                    public void onNext(Integer count) {
                        Log.i(TAG, "ckeckDevice :" + count);
                        mRootView.checkDeviceSuccess(count);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        Log.i(TAG, "ckeckDeviceFail");
                        mRootView.checkDeviceFail();
                    }
                });
    }

    /**
     * 根据id 删除存在数据库的一条数据 即取消关注
     *
     * @param cameraId
     */
    public void deleteDevice(String cameraId) {
        mDbHelper.deleteDevice(cameraId)
                .subscribeOn(Schedulers.io())
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe();
    }

}
