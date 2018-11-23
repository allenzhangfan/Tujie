package com.netposa.component.my.mvp.presenter;

import android.app.Application;
import android.content.Context;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import androidx.lifecycle.LifecycleOwner;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.netposa.common.constant.CommonConstant;
import com.netposa.common.log.Log;
import com.netposa.common.utils.ToastUtils;
import com.netposa.component.my.mvp.contract.MyContract;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;

import org.apache.commons.io.FileUtils;

import java.io.File;

import static com.netposa.common.constant.GlobalConstants.MAIN_DB_NAME;


@FragmentScope
public class MyPresenter extends BasePresenter<MyContract.Model, MyContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public MyPresenter(MyContract.Model model, MyContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    /**
     * 拷贝数据库文件
     * @param context
     */
    public void copyDbToSdCard(Context context) {
        File dbFile = context.getDatabasePath(MAIN_DB_NAME);
        if (dbFile == null) {
            Log.e(TAG, MAIN_DB_NAME + "is null !");
            return;
        }
        File srcDir = dbFile.getParentFile();
        if (!srcDir.exists()) {
            ToastUtils.showShort("待拷贝文件夹不存在");
        }

        String dbPath = CommonConstant.DB_PATH;
        String srcAbsolutePath = srcDir.getAbsolutePath();
        String srcDirName = srcAbsolutePath.substring(srcAbsolutePath.lastIndexOf('/') + 1);
        final File destDir = new File(dbPath, srcDirName);

        Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        if (destDir.exists()) {
                            FileUtils.deleteDirectory(destDir);
                        }
                        FileUtils.copyDirectory(srcDir, destDir);
                        emitter.onNext(destDir.getAbsolutePath());
                        emitter.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe(new Observer<String>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String destPath) {
                        Log.e(TAG, "开始拷贝目录:" + destPath);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "e:" + e);
                        ToastUtils.showShort("拷贝文件夹失败");
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete");
                        ToastUtils.showShort("拷贝文件夹成功\n" + destDir.getAbsolutePath());
                    }
                });
    }
}
