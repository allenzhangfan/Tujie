package com.netposa.component.spjk.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.gyf.barlibrary.ImmersionBar;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.log.Log;
import com.netposa.common.net.NetWorkBroadcastReceiver;
import com.netposa.common.net.NetWorkEventInterface;
import com.netposa.common.utils.NetworkUtils;
import com.netposa.component.room.entity.SpjkCollectionDeviceEntity;
import com.netposa.component.spjk.R;
import com.netposa.component.spjk.R2;
import com.netposa.component.spjk.di.component.DaggerHistoryVideoPlayComponent;
import com.netposa.component.spjk.di.module.HistoryVideoPlayModule;
import com.netposa.component.spjk.mvp.contract.HistoryVideoPlayContract;
import com.netposa.component.spjk.mvp.presenter.HistoryVideoPlayPresenter;

import java.util.concurrent.ArrayBlockingQueue;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import netposa.pem.sdk.PemSdkListener;
import netposa.pem.sdk.PemSdkManager;
import netposa.pem.sdk.PemSdkUtils;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.common.constant.GlobalConstants.CAMERA_QIANG_JI;
import static com.netposa.component.spjk.app.SpjkConstants.KEY_HISTORY_PLAY_VIDEO;
import static com.netposa.component.spjk.app.SpjkConstants.KEY_HISTORY_VIDEO_DEVICES_NAME;
import static com.netposa.component.spjk.app.SpjkConstants.KEY_HISTORY_VIDEO_ORG_NAME;
import static com.netposa.component.spjk.app.SpjkConstants.KEY_SINGLE_CAMERA_ID;
import static com.netposa.component.spjk.app.SpjkConstants.KEY_VIDEO_TYPE;
import static com.netposa.component.spjk.app.SpjkConstants.MSG_ONERROR;
import static com.netposa.component.spjk.app.SpjkConstants.MSG_ONERROR_C;
import static com.netposa.component.spjk.app.SpjkConstants.MSG_ONERROR_NET;
import static com.netposa.component.spjk.app.SpjkConstants.MSG_OPENVIDEO_SUC;
import static com.netposa.component.spjk.app.SpjkConstants.MSG_SHOWLOADING;
import static com.netposa.component.spjk.app.SpjkConstants.NETWORK_MOBILE;
import static com.netposa.component.spjk.app.SpjkConstants.NETWORK_NONE;
import static com.netposa.component.spjk.app.SpjkConstants.NETWORK_WIFI;
import static netposa.pem.sdk.PemSdkManager.PEM_MSG_ERROR_CALLBACK;
import static netposa.pem.sdk.PemSdkManager.PEM_MSG_PLAY_START;

public class HistoryVideoPlayActivity extends BaseActivity<HistoryVideoPlayPresenter> implements HistoryVideoPlayContract.View, PemSdkListener, NetWorkEventInterface {

    @BindView(R2.id.surfaceview)
    GLSurfaceView mSurfaceView;
    @BindView(R2.id.progress)
    ProgressBar mProgress;
    @BindView(R2.id.tv_organize_name)
    TextView mTvOrganizeName;
    @BindView(R2.id.collect_iv)
    ImageView mIvAttention;
    @BindView(R2.id.iv_screenshot)
    ImageView mIvScreenShot;
    @BindView(R2.id.tv_nowifi)
    TextView mTvNoWifi;
    @BindView(R2.id.stil_play_btn)
    MaterialButton mStilPlayBtn;
    @BindView(R2.id.rl_network_layout)
    RelativeLayout mRlNetworkLayout;
    @BindView(R2.id.play_fail_tv)
    TextView mPlayFailTv;
    @BindView(R2.id.tv_fail_retry)
    TextView mTvFailRetry;
    @BindView(R2.id.rl_play_failed)
    RelativeLayout mRlPlayFailed;
    @BindView(R2.id.rl_surface_layout)
    RelativeLayout mRlSurfaceLayout;
    @BindView(R2.id.activity_main)
    LinearLayout mActivityMain;

    @Inject
    ArrayBlockingQueue<String> mCaptureblockqueue;//截图任务队列
    @Inject
    SpjkCollectionDeviceEntity mSpjkCollectionDeviceEntity;

    private PemSdkManager mPemSdkManager;
    private boolean mIsPlaying = false;
    private String mUrl = null;
    private boolean mCollected = false;
    int mCount = 0;//activity走onstop后再走onresume要重新播放，但第一次走onresume不直接播放
    private String mOrgName;
    private String mActiveCameraId;
    private int mCameraTypeInt;
    private String mDeviceName;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerHistoryVideoPlayComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .historyVideoPlayModule(new HistoryVideoPlayModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return 0; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_video_play);
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarColor(R.color.transparent)
                .statusBarDarkFont(false, 0.2f)
                .init();
        //绑定到butterknife
        ButterKnife.bind(this);
        //注册广播
        NetWorkBroadcastReceiver.register(this, this);
        Intent data = getIntent();
        if (data == null) {
            return;
        }
        mUrl = data.getStringExtra(KEY_HISTORY_PLAY_VIDEO);
        mOrgName = data.getStringExtra(KEY_HISTORY_VIDEO_ORG_NAME);
        mDeviceName = data.getStringExtra(KEY_HISTORY_VIDEO_DEVICES_NAME);
        mActiveCameraId = data.getStringExtra(KEY_SINGLE_CAMERA_ID);
        mCameraTypeInt = data.getIntExtra(KEY_VIDEO_TYPE, CAMERA_QIANG_JI);
        mTvOrganizeName.setText(mDeviceName);
        Log.i(TAG, "play url:" + mUrl);
        mPresenter.checkDevice(mActiveCameraId);
        //初始化video sdk
        mPemSdkManager = PemSdkManager.newInstance();
        mPemSdkManager.init(this);
        mPemSdkManager.setmGLsurfaceview(mSurfaceView);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        //do nothing here
    }

    /**
     * 每次播放视频成功都会回调此方法
     *
     * @param pfslen
     */
    @Override
    public void prePlay(int pfslen) {
        Log.d(TAG, "prePlay: " + pfslen);
        if (mCount > 0) {
            runOnUiThread(() -> {
                handler.sendEmptyMessage(MSG_OPENVIDEO_SUC);
            });
        }
    }

    @Override
    public void getOpenState(boolean openState) {
        Log.d(TAG, "getOpenState: " + openState);
        if (!openState) {
            handler.sendEmptyMessage(MSG_ONERROR);
        } else {
            if (mCount == 0) {
                handler.sendEmptyMessage(MSG_OPENVIDEO_SUC);
            }
        }
    }

    @Override
    public void getYUVI420(byte[] i420Data, int width, int height) {
        mPemSdkManager.renderVideo(i420Data, width, height);
        if (mCaptureblockqueue.size() > 0) {
            String path = mCaptureblockqueue.poll();
            PemSdkUtils.yuv4202jpeg(path, i420Data, width, height);
        }
    }

    @Override
    public void onSpeedUpdate(int speed) {
    }


    @Override
    public void onSeekUpdate(int progress) {
    }

    /**
     * 视频sdk  c++ 调用java
     *
     * @param what
     * @param arg1
     * @param arg2
     */
    @Override
    public void onError(int what, int arg1, int arg2) {
        switch (what) {
            case PEM_MSG_ERROR_CALLBACK:
                switch (arg1) {
                    case PemSdkManager.SOCKETERROR_CONNECTCLOSE:
                        onErrorMsg(getString(R.string.video_socket_close));
                        break;
                    case PemSdkManager.SOCKETERROR_TIMEOUT:// 没有视频流
                        onErrorMsg(getString(R.string.video_no_stream));
                        break;
                    case PemSdkManager.SOCKETERROR:
                        onErrorMsg(getString(R.string.connect_server_error));
                        break;
                    default:
                        handler.sendEmptyMessage(MSG_ONERROR);
                        break;
                }
                break;
            case PEM_MSG_PLAY_START:
                switch (arg1) {
                    default:
                        onErrorMsg(getString(R.string.connect_server_error));
                        break;
                }
                break;
        }
    }

    public void onErrorMsg(String error) {
        Message msg = new Message();
        msg.obj = error;
        msg.what = MSG_ONERROR_C;
        handler.sendMessage(msg);
    }

    /**
     * 网络监测回调接口
     *
     * @param netWorkStatus 取值：-1：没有网络；0；移动网络；1：无线网络
     */
    @Override
    public void onNetWorkChange(int netWorkStatus) {
        Log.i(TAG, "receive network changed:" + netWorkStatus + ",isPlaying:" + mIsPlaying);
        if (netWorkStatus == NETWORK_NONE) { //没有网络
            if (mIsPlaying) {
                stopPlay();
            }
            handler.sendEmptyMessage(MSG_ONERROR_NET);
        } else if (netWorkStatus == NETWORK_WIFI) {// wifi
            if (mIsPlaying) {
                replay();
            } else {
                //第一次进入视频播放界面会触发此次回调，此时url为空
                if (mUrl != null) {
                    play();
                }
            }
            runOnUiThread(() -> {
                if (mRlNetworkLayout.getVisibility() == View.VISIBLE) {
                    mRlNetworkLayout.setVisibility(View.GONE);
                }
            });
        } else if (netWorkStatus == NETWORK_MOBILE) {//数据流量
            runOnUiThread(() -> {
                mRlNetworkLayout.setVisibility(View.VISIBLE);
                hidePlayNetLayout();
            });
        }
    }

    @Override
    public void showLoading(String message) {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    private void play() {
        if (null == mUrl) {
            Log.e(TAG, "url is null ,just return !");
            return;
        }
        Log.i(TAG, "start to play !");
        if (null != mUrl) {
            handler.sendEmptyMessage(MSG_SHOWLOADING);
            mPemSdkManager.play(mUrl, 0, "", 0);
            mIsPlaying = true;
        }
    }

    /**
     * 播放视频失败layout 隐藏
     */
    private void hidePlayNetLayout() {
        if (mRlPlayFailed.getVisibility() == View.VISIBLE) {
            mRlPlayFailed.setVisibility(View.GONE);
        }
    }

    /**
     * 播放的Stop 方法
     */
    private void stopPlay() {
        mIsPlaying = false;
        mPemSdkManager.stop();
    }

    /**
     * 恢复播放
     **/
    public void replay() {
        mIsPlaying = true;
        mPemSdkManager.replay();
    }

    /**
     * 播放的pause 方法
     */
    private void pause() {
        mIsPlaying = false;
        mPemSdkManager.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume count:" + mCount + ",isPlaying:" + mIsPlaying);
        if (!mIsPlaying && mCount > 0) {
            play();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop count:" + mCount + ",isPlaying:" + mIsPlaying);
        if (mIsPlaying) {
            stopPlay();
            if (mCount == Integer.MAX_VALUE - 2) {
                mCount = 0;
            }
            mCount++;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetWorkBroadcastReceiver.unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsPlaying) {
                stopPlay();
            }
            killMyself();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_OPENVIDEO_SUC:
                    Log.d(TAG, "MSG_OPENVIDEO_SUC");
                    if (mProgress.getVisibility() == View.VISIBLE) {
                        mProgress.setVisibility(View.GONE);
                    }
                    hidePlayNetLayout();
                    break;
                case MSG_SHOWLOADING:
                    Log.d(TAG, "MSG_SHOWLOADING");
                    if (mProgress.getVisibility() == View.GONE) {
                        mProgress.setVisibility(View.VISIBLE);
                    }
                    break;
                case MSG_ONERROR:
                    Log.d(TAG, "MSG_ONERROR");
                    mIsPlaying = false;
                    mProgress.setVisibility(View.GONE);
                    mTvNoWifi.setVisibility(View.GONE);
                    mRlPlayFailed.setVisibility(View.VISIBLE);
                    break;
                case MSG_ONERROR_C:// c++ call 过来的error
                    Log.d(TAG, "MSG_ONERROR_C");
                    mProgress.setVisibility(View.GONE);
                    String content = (String) msg.obj;
                    mRlPlayFailed.setVisibility(View.VISIBLE);
                    if (content != null) {
                        mPlayFailTv.setText(content);
                        mTvFailRetry.setVisibility(View.GONE);
                    }
                    mIsPlaying = false;
                    break;
                case MSG_ONERROR_NET:
                    Log.d(TAG, "MSG_ONERROR_NET");
                    mIsPlaying = false;
                    mProgress.setVisibility(View.GONE);
                    mRlPlayFailed.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @OnClick({R2.id.tv_organize_name,
            R2.id.collect_iv,
            R2.id.iv_screenshot,
            R2.id.stil_play_btn,
            R2.id.tv_fail_retry
    })
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.tv_organize_name) {
            if (mIsPlaying) {
                stopPlay();
            }
            killMyself();
        } else if (id == R.id.stil_play_btn) {
            mRlNetworkLayout.setVisibility(View.GONE);
            if (mIsPlaying) {
                replay();
            } else {
                play();
            }
        } else if (id == R.id.collect_iv) {
            mSpjkCollectionDeviceEntity.setCamerid(mActiveCameraId);
            mSpjkCollectionDeviceEntity.setCamername(mOrgName);
            mSpjkCollectionDeviceEntity.setCamertype(mCameraTypeInt);
            if (mCollected) {// 已经关注了 做删除操作
                mPresenter.deleteDevice(mActiveCameraId);
                mCollected = false;
                setLandscapeFollowImage();
                showToast(false);
            } else {// 关注操作
                mPresenter.insterDevice(mSpjkCollectionDeviceEntity);
                mCollected = true;
                setLandscapeFollowImage();
                showToast(true);
            }
        } else if (id == R.id.iv_screenshot) {
            if (mIsPlaying) {
                mPresenter.onScreenCapture();
            } else {
                showMessage(getString(R.string.video_not_played));
            }
        } else if (id == R.id.tv_fail_retry) {
            // 正常播放失败后刷新播放
            if (NetworkUtils.getNetWrokState() != NETWORK_NONE) {
                stopPlay();
                play();
            } else {
                showMessage(getString(R.string.network_connect_fail));
            }
        }
    }

    /**
     * 横屏关注的图片
     */
    public void setLandscapeFollowImage() {
        if (mCollected) {
            mIvAttention.setImageResource(R.drawable.ic_attention_on);
        } else {
            mIvAttention.setImageResource(R.drawable.ic_attention_up);
        }
    }

    private void showToast(boolean flag) {
        if (flag) {
            showMessage(getString(R.string.subscribe));
        } else {
            showMessage(getString(R.string.unsubscribe));
        }
    }

    @Override
    public void checkDeviceSuccess(int count) {
        // 查询展示
        if (count > 0) {
            mCollected = true;//已经关注了
        }
        setLandscapeFollowImage();
    }

    @Override
    public void checkDeviceFail() {

    }
}
