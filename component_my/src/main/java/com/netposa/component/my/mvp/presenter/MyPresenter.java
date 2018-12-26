package com.netposa.component.my.mvp.presenter;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.http.imageloader.ImageLoader;

import androidx.lifecycle.LifecycleOwner;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.jess.arms.mvp.BasePresenter;
import com.netposa.common.constant.GlobalConstants;
import com.netposa.common.log.Log;
import com.netposa.common.utils.FileUtils;
import com.netposa.common.utils.ToastUtils;
import com.netposa.component.my.R;
import com.netposa.component.my.mvp.contract.MyContract;
import com.netposa.component.my.mvp.contract.PersonInfoContract;
import com.netposa.component.my.mvp.model.entity.MenuEntity;
import com.netposa.component.my.mvp.ui.adapter.MyMenuAdapter;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;

import java.io.File;
import java.util.List;

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
    Context mContext;
    @Inject
    List<MenuEntity> mBeanList;
    @Inject
    MyMenuAdapter mAdapter;

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
     *
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

        String dbPath = GlobalConstants.DB_PATH;
        String srcAbsolutePath = srcDir.getAbsolutePath();
        String srcDirName = srcAbsolutePath.substring(srcAbsolutePath.lastIndexOf('/') + 1);
        final File destDir = new File(dbPath, srcDirName);

        Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        if (destDir.exists()) {
                            FileUtils.deleteDir(destDir);
                        }
                        FileUtils.copyDir(srcDir, destDir);
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
                        Log.i(TAG, "开始拷贝目录:" + destPath);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "e:" + e);
                        ToastUtils.showShort("拷贝文件夹失败");
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete");
                        ToastUtils.showShort("拷贝文件夹成功\n" + destDir.getAbsolutePath());
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void clearCache() {
        Completable
                .fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        FileUtils.deleteFilesInDirs(mContext.getCacheDir(), mContext.getExternalCacheDir());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        String dirsSize = FileUtils.getDirsSize(mContext.getCacheDir(), mContext.getExternalCacheDir());
                        refreshCacheSize("0.00B");
                        Log.i(TAG, "清除之后的dirsSize:" + dirsSize);
                        ToastUtils.showShort(R.string.clear_cache_success);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError");
                    }
                });
    }

    public void getCacheSize() {
        Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) {
                        String dirsSize = FileUtils.getDirsSize(mContext.getCacheDir(), mContext.getExternalCacheDir());
                        emitter.onNext(dirsSize);
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
                    public void onNext(String dirsSize) {
                        Log.i(TAG, "cache dirsSize:" + dirsSize);
                        refreshCacheSize(dirsSize);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "e:" + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete");
                    }
                });
    }

    private void refreshCacheSize(String cacheSize){
        mBeanList.get(1).setValue(cacheSize);
        mAdapter.notifyDataSetChanged();
    }
}
