package com.netposa.component.spjk.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import netposa.pem.sdk.PemSdkListener;
import netposa.pem.sdk.PemSdkManager;
import netposa.pem.sdk.PemSdkUtils;
import com.google.android.material.button.MaterialButton;
import com.gyf.barlibrary.ImmersionBar;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.AppManager;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.log.Log;
import com.netposa.common.net.NetWorkBroadcastReceiver;
import com.netposa.common.net.NetWorkEventInterface;
import com.netposa.common.utils.SystemUtil;
import com.netposa.component.room.entity.SpjkCollectionDeviceEntity;
import com.netposa.component.spjk.R2;
import com.netposa.component.spjk.di.component.DaggerVideoPlayComponent;
import com.netposa.component.spjk.di.module.VideoPlayModule;
import com.netposa.component.spjk.mvp.contract.VideoPlayContract;
import com.netposa.component.spjk.mvp.model.entity.PtzDirectionRequestEntity;
import com.netposa.component.spjk.mvp.presenter.VideoPlayPresenter;
import com.netposa.component.spjk.R;
import com.netposa.component.spjk.mvp.model.entity.DeviceInfoResponseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import javax.inject.Inject;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.common.constant.GlobalConstants.CAMERA_QIANG_JI;
import static com.netposa.common.constant.GlobalConstants.KEY_SINGLE_CAMERA_LOCATION_LATITUDE;
import static com.netposa.common.constant.GlobalConstants.KEY_SINGLE_CAMERA_LOCATION_LONGITUDE;
import static com.netposa.component.spjk.app.SpjkConstants.BODY_TYPE;
import static com.netposa.component.spjk.app.SpjkConstants.CAMERA_TYPE;
import static com.netposa.component.spjk.app.SpjkConstants.FACE_TYPE;
import static com.netposa.component.spjk.app.SpjkConstants.KEY_HISTORY_VIDEO_DEVICES_NAME;
import static com.netposa.component.spjk.app.SpjkConstants.KEY_HISTORY_VIDEO_ORG_NAME;
import static com.netposa.component.spjk.app.SpjkConstants.KEY_NEIGHBOURING_DEVICES;
import static com.netposa.component.spjk.app.SpjkConstants.KEY_SINGLE_CAMERA_HISTORY_VIDEO;
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
import static com.netposa.component.spjk.app.SpjkConstants.RESUME_CAMERA_ID;
import static com.netposa.component.spjk.app.SpjkConstants.TRAFFIC_TYPE;
import static netposa.pem.sdk.PemSdkManager.PEM_MSG_ERROR_CALLBACK;
import static netposa.pem.sdk.PemSdkManager.PEM_MSG_PLAY_START;

public class VideoPlayActivity extends BaseActivity<VideoPlayPresenter> implements VideoPlayContract.View, PemSdkListener, NetWorkEventInterface {

    @BindView(R2.id.tv_nowifi)
    TextView mTvNoWiFiRemark;
    @BindView(R2.id.stil_play_btn)
    MaterialButton mStilPlay;
    @BindView(R2.id.surfaceview)
    GLSurfaceView mSurfaceView;
    @BindView(R2.id.iv_full_screen)
    ImageView mIvFullScreen;
    @BindView(R2.id.lineview)
    View mView;
    @BindView(R2.id.progress)
    ProgressBar mProgress;
    @BindView(R2.id.tv_devicetype)
    TextView mTvDeviceType;
    @BindView(R2.id.tv_orgname)
    TextView mTvOrgName;
    @BindView(R2.id.rl_surface_layout)
    RelativeLayout mRlSurface;//视频显示栏的layout
    @BindView(R2.id.rl_middle_Layout)
    RelativeLayout mRlMiddle;//视频中间栏的layout
    @BindView(R2.id.scroll_layout)
    ScrollView mScroll;//视频下部分栏的layout
    @BindView(R2.id.rl_network_layout)
    RelativeLayout mRlNetWork;//网络提示的lauout
    @BindView(R2.id.rl_direction_layout)
    RelativeLayout mRlDirection;//控制云台的方向layout
    @BindView(R2.id.iv_attention)
    ImageView mTvAttention;
    @BindView(R2.id.tv_video_name)
    TextView mTvName;
    @BindView(R2.id.iv_screenshot)
    ImageView mTvScreenShot;
    @BindView(R2.id.iv_up)
    ImageView mIvUp;
    @BindView(R2.id.iv_down)
    ImageView mIvDown;
    @BindView(R2.id.iv_left)
    ImageView mIvLeft;
    @BindView(R2.id.iv_right)
    ImageView mIvRight;
    @BindView(R2.id.tv_camera_name)
    TextView mIvTitleName;
    @BindView(R2.id.iv_camera_type)
    ImageView mIvCameraType;
    @BindView(R2.id.iv_traffic_type)
    ImageView mIvTrafficType;
    @BindView(R2.id.iv_face_type)
    ImageView mIvFaceType;
    @BindView(R2.id.iv_body_type)
    ImageView mIvBodyType;
    @BindView(R2.id.iv_follow)
    ImageView mIvFollow;
    @BindView(R2.id.tv_follow)
    TextView mTvFollow;
    @BindView(R2.id.ll_container)
    LinearLayout mLlContainer;
    @BindView(R2.id.iv_finish_activity)
    ImageView mIvFinish;


    @Inject
    ArrayBlockingQueue<String> mCaptureblockqueue;//截图任务队列
    @Inject
    PtzDirectionRequestEntity mPtzDirectionRequestEntity;
    @Inject
    SpjkCollectionDeviceEntity mSpjkCollectionDeviceEntity;
    @Inject
    SystemUtil mSystemUtil;

    private boolean mCollected = false;
    private PemSdkManager mPemSdkManager;
    private int mOrientation = Configuration.ORIENTATION_PORTRAIT;
    //activity走onstop后再走onresume要重新播放，但第一次走onresume不直接播放
    int mCount = 0;

    private boolean mIsPlaying = false;
    private String mCameraId;
    private String mUrl = null;
    // 触屏的标志
    private int mMotionEventFlag;
    private DeviceInfoResponseEntity mCurrentDevice;
    private int[] mImageId = new int[]{R.drawable.ic_type1, R.drawable.ic_type2, R.drawable.ic_type3, R.drawable.ic_type4};

    private int mCurrentNetWorkStatus;
    private String mCarmeraName;
    private int mCameraTypeInt;


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerVideoPlayComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .videoPlayModule(new VideoPlayModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return 0; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != savedInstanceState) {
            mCameraId = savedInstanceState.getString(RESUME_CAMERA_ID);
        }
        setContentView(R.layout.activity_video_play);
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
            Log.e(TAG, "intent data is null,please check !");
            return;
        }
        mCameraId = data.getStringExtra(KEY_SINGLE_CAMERA_ID);
        Log.i(TAG, "cameraId:" + mCameraId);
        //初始化video sdk
        mPemSdkManager = PemSdkManager.newInstance();
        mPemSdkManager.init(this);
        mPemSdkManager.setmGLsurfaceview(mSurfaceView);
        mPresenter.getDeviceInfo(mCameraId);
        mIvUp.setOnTouchListener(new CustomVideoOnTouchMotion());
        mIvDown.setOnTouchListener(new CustomVideoOnTouchMotion());
        mIvLeft.setOnTouchListener(new CustomVideoOnTouchMotion());
        mIvRight.setOnTouchListener(new CustomVideoOnTouchMotion());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!TextUtils.isEmpty(mCameraId)) {
            outState.putString(RESUME_CAMERA_ID, mCameraId);
        }
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        //do nothing here
    }

    @OnClick({
            R2.id.iv_leftback,
            R2.id.stil_play_btn,
            R2.id.iv_full_screen,
            R2.id.iv_left,
            R2.id.iv_up,
            R2.id.iv_right,
            R2.id.iv_down,
            R2.id.iv_screenshot,
            R2.id.history_cardView,
            R2.id.nearby_carview,
            R2.id.ll_device_adrress,
            R2.id.rl_collect_tv,
            R2.id.iv_attention,
            R2.id.iv_finish_activity})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.iv_leftback) {
            if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                if (mIsPlaying) {
                    stopPlay();
                }
                killMyself();
            }
        } else if (i == R.id.stil_play_btn) {
            mRlNetWork.setVisibility(View.GONE);
            if (mIsPlaying) {
                replay();
            } else {
                play();
            }
        } else if (i == R.id.iv_full_screen) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (i == R.id.iv_screenshot) {
            if (mIsPlaying) {
                mPresenter.onScreenCapture();
            } else {
                showMessage(getString(R.string.video_not_played));
            }
        } else if (i == R.id.history_cardView) {
            if (mCurrentDevice == null) {
                showMessage(getString(R.string.device_info_do_not_receive_yet));
                return;
            }
            Intent dataIntent = new Intent(this, HistoryVideoActivity.class);
            String orgname = mCurrentDevice.getOrgname();
            if (!TextUtils.isEmpty(orgname)) {
                dataIntent.putExtra(KEY_SINGLE_CAMERA_HISTORY_VIDEO, mCameraId);
                dataIntent.putExtra(KEY_HISTORY_VIDEO_ORG_NAME, mCurrentDevice.getOrgname());
                dataIntent.putExtra(KEY_HISTORY_VIDEO_DEVICES_NAME, mCarmeraName);
                dataIntent.putExtra(KEY_VIDEO_TYPE, mCameraTypeInt);
                launchActivity(dataIntent);
            }
        } else if (i == R.id.nearby_carview) {
            if (!mSystemUtil.isFastDoubleClick()) {
                String latitude = mCurrentDevice.getLatitude();
                String longitude = mCurrentDevice.getLongitude();
                //未拉取到摄像头也可以从列表进入视频播放界面，此时设备没有经纬度信息
                if (mCurrentDevice == null || TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longitude)) {
                    showMessage(getString(R.string.device_info_do_not_receive_yet));
                    return;
                }
                Intent dataIntent = new Intent(this, NeighbouringDevicesActivity.class);
                dataIntent.putExtra(KEY_NEIGHBOURING_DEVICES, mCurrentDevice);
                launchActivity(dataIntent);
            }
        } else if (i == R.id.ll_device_adrress) {
            if (mCurrentDevice == null) {
                showMessage(getString(R.string.device_info_do_not_receive_yet));
                return;
            }
            Intent dataIntent = new Intent(this, SingleCameraLocationActivity.class);
            dataIntent.putExtra(KEY_SINGLE_CAMERA_LOCATION_LATITUDE, mCurrentDevice.getLatitude());
            dataIntent.putExtra(KEY_SINGLE_CAMERA_LOCATION_LONGITUDE, mCurrentDevice.getLongitude());
            launchActivity(dataIntent);
        } else if (i == R.id.rl_collect_tv) {
            // true 表示关注状态，进行删除 即取消关注
            if (mCollected) {
                mPresenter.deleteDevice(mCameraId);
                setPortraitFollowImage(false);
                mCollected = false;
                showToast(false);
            } else {// 关注操作
                mPresenter.insterDevice(provideCollectionEntiry());
                setPortraitFollowImage(true);
                showToast(true);
                mCollected = true;
            }
        } else if (i == R.id.iv_attention) {
            if (mCollected) {
                mCollected = false;
                mPresenter.deleteDevice(mCameraId);
                setLandscapeFollowImage();
                showToast(false);
            } else {// 关注操作
                mPresenter.insterDevice(provideCollectionEntiry());
                mCollected = true;
                setLandscapeFollowImage();
                showToast(true);
            }
        } else if (i == R.id.iv_finish_activity) {
            //删除所有的activity 只保留最底层的activity 返回到主界面
            List<Activity> activitys = AppManager.getAppManager().getActivityList();
            Activity activity = activitys.get(0);
            AppManager.getAppManager().killAll(activity.getClass());
        }
    }

    /**
     * 提供关注和取消的对象
     *
     * @return
     */
    private SpjkCollectionDeviceEntity provideCollectionEntiry() {
        mSpjkCollectionDeviceEntity.setCamertype(mCameraTypeInt);
        mSpjkCollectionDeviceEntity.setCamerid(mCameraId);
        mSpjkCollectionDeviceEntity.setCamername(mCarmeraName);
        return mSpjkCollectionDeviceEntity;
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

    /**
     * 仅第一次播放视频成功才回调此方法
     *
     * @param openState
     */
    @Override
    public void getOpenState(final boolean openState) {
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
    public void onSeekUpdate(int progress) {

    }

    @Override
    public void onSpeedUpdate(int speed) {

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
     * 调用pem sdk播放视频
     */
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mOrientation = newConfig.orientation;
        try {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
                onLandscape();
            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {//竖屏
                onPortrait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                if (mIsPlaying) {
                    stopPlay();
                }
                killMyself();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void onLandscape() {
        //切换成横屏时，设置播放器高度
        FrameLayout.LayoutParams param = (FrameLayout.LayoutParams) mRlSurface.getLayoutParams();
        param.height = MATCH_PARENT;
        param.width = MATCH_PARENT;
        mRlSurface.setLayoutParams(param);
        mTvScreenShot.setTranslationX(-150);
        mRlMiddle.setVisibility(View.GONE);
        mScroll.setVisibility(View.GONE);
        mView.setVisibility(View.GONE);
        mIvFullScreen.setVisibility(View.GONE);
        mTvAttention.setVisibility(View.VISIBLE);
        mIvTitleName.setVisibility(View.VISIBLE);
        if (mCollected) {
            mTvAttention.setImageResource(R.drawable.ic_add_follow);
        } else {
            mTvAttention.setImageResource(R.drawable.ic_cancel_follow);
        }
        mIvFinish.setVisibility(View.GONE);
    }

    private void onPortrait() {
        FrameLayout.LayoutParams param = (FrameLayout.LayoutParams) mRlSurface.getLayoutParams();
        param.height = MATCH_PARENT;
        param.width = MATCH_PARENT;
        mRlSurface.setLayoutParams(param);
        mTvScreenShot.setTranslationX(0);
        mRlMiddle.setVisibility(View.VISIBLE);
        mScroll.setVisibility(View.VISIBLE);
        mView.setVisibility(View.VISIBLE);
        mIvFullScreen.setVisibility(View.VISIBLE);
        mTvAttention.setVisibility(View.GONE);
        mIvTitleName.setVisibility(View.GONE);
        setPortraitFollowImage(mCollected);
        mIvFinish.setVisibility(View.VISIBLE);
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
                    mTvNoWiFiRemark.setVisibility(View.VISIBLE);
                    break;
                case MSG_ONERROR_C:// c++ call 过来的error
                    Log.d(TAG, "MSG_ONERROR_C");
                    mRlNetWork.setVisibility(View.VISIBLE);
                    mProgress.setVisibility(View.GONE);
                    String content = (String) msg.obj;
                    if (content != null) {
                        mTvNoWiFiRemark.setText(content);
                    }
                    mIsPlaying = false;
                    break;
                case MSG_ONERROR_NET:
                    Log.d(TAG, "MSG_ONERROR_NET");
                    mIsPlaying = false;
                    mProgress.setVisibility(View.GONE);

                    break;
            }
        }
    };

    /**
     * 网络监测回调接口
     *
     * @param netWorkStatus 取值：-1：没有网络；0；移动网络；1：无线网络
     */
    @Override
    public void onNetWorkChange(int netWorkStatus) {
        mCurrentNetWorkStatus = netWorkStatus;
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
                if (mRlNetWork.getVisibility() == View.VISIBLE) {
                    mRlNetWork.setVisibility(View.GONE);
                }
            });
        } else if (netWorkStatus == NETWORK_MOBILE) {//数据流量
            runOnUiThread(() -> {
                mRlNetWork.setVisibility(View.VISIBLE);
            });
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
        if (!TextUtils.isEmpty(mCameraId)) {
            mCollected = false;
            mPresenter.checkDevice(mCameraId);
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
        if (mIsPlaying) {
            stopPlay();
        }
        NetWorkBroadcastReceiver.unregister(this);
    }

    // 调接口成功
    @Override
    public void showDeviceInfoSuccess(DeviceInfoResponseEntity reponse) {
        Log.e(TAG, "showDeviceInfoSuccess:" + reponse);
        mCurrentDevice = reponse;
        mUrl = reponse.getPlayUrl();
        if (TextUtils.isEmpty(mUrl)) {
            //服务器转码失败
            mRlNetWork.setVisibility(View.VISIBLE);
            mStilPlay.setVisibility(View.GONE);
            mTvNoWiFiRemark.setText(R.string.connect_server_encoder_error);
            mIsPlaying = false;
        } else {
            if (mCurrentNetWorkStatus == NETWORK_WIFI) {
                play();
            }
        }
        // 摄像头名称
        mCarmeraName = reponse.getName();
        if (!TextUtils.isEmpty(mCarmeraName)) {
            mTvName.setText(mCarmeraName);
            mIvTitleName.setText(mCarmeraName);
        }
        // 1 球机
        String cameraType = reponse.getCameraType();
        if (!TextUtils.isEmpty(cameraType) && cameraType.equals("1")) {
            mRlDirection.setVisibility(View.VISIBLE);
            mTvDeviceType.setText(getString(R.string.qiu_ji));
        } else {
            mTvDeviceType.setText(getString(R.string.qiangji));
        }
        // 组织结构
        String orgname = reponse.getOrgname();
        if (!TextUtils.isEmpty(orgname)) {
            mTvOrgName.setText(orgname);
        }
        //能力类型
        String ability = reponse.getAbility();
        if (!TextUtils.isEmpty(ability)) {
            List<String> abilityData = null;
            String[] strArray = reponse.getAbility().split(",");
            if (null != strArray && strArray.length > 0) {
                abilityData = new ArrayList<>(strArray.length);
            }

            // 过滤数据 只要"face,body,camera,traffic"这四种类型
            for (int i = 0; i < strArray.length; i++) {
                if (strArray[i].equals(CAMERA_TYPE) || strArray[i].equals(TRAFFIC_TYPE) || strArray[i].equals(FACE_TYPE) || strArray[i].equals(BODY_TYPE)) {
                    abilityData.add(strArray[i]);
                }
            }
            if (abilityData.size() == 1) {
                mIvCameraType.setImageResource(getPicType(abilityData.get(0)));
            }
            if (abilityData.size() == 2) {
                mIvCameraType.setImageResource(getPicType(abilityData.get(0)));
                mIvTrafficType.setImageResource(getPicType(abilityData.get(1)));
            }
            if (abilityData.size() == 3) {
                mIvCameraType.setImageResource(getPicType(abilityData.get(0)));
                mIvTrafficType.setImageResource(getPicType(abilityData.get(1)));
                mIvFaceType.setImageResource(getPicType(abilityData.get(2)));
            }
            if (abilityData.size() == 4) {
                mIvCameraType.setImageResource(getPicType(abilityData.get(0)));
                mIvTrafficType.setImageResource(getPicType(abilityData.get(1)));
                mIvFaceType.setImageResource(getPicType(abilityData.get(2)));
                mIvBodyType.setImageResource(getPicType(abilityData.get(3)));
            }
        }
        String type = reponse.getCameraType();
        if (TextUtils.isEmpty(type)) {
            mCameraTypeInt = CAMERA_QIANG_JI;
        } else {
            mCameraTypeInt = Integer.parseInt(type);
        }
    }

    // 调接口失败
    @Override
    public void showDeviceInfoFailed() {
        Log.e(TAG, "showDeviceInfoFailed");
    }

    @Override
    public void setDirectionSuccess(Object reponse) {
        if (mMotionEventFlag == MotionEvent.ACTION_UP) {
            directionCanel();
        }
    }

    @Override
    public void checkDeviceSuccess(int count) {

        // 查询展示
        if (count > 0) {
            mCollected = true;//已经关注了
            setPortraitFollowImage(true);
        } else {
            setPortraitFollowImage(false);
        }
    }

    @Override
    public void checkDeviceFailed() {
        Log.e(TAG, "checkDeviceFailed");
    }

    /**
     * param在0-10之间，0为停止，10为速度最大
     * 0-右转；1 右上角；2-上转；3-左上角；4-左转；
     * 5-左下角；6-向下；7-右下角
     * cmd 方向
     */
    private class CustomVideoOnTouchMotion implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int i = view.getId();
            if (i == R.id.iv_up) { //上转
                mPtzDirectionRequestEntity.setCmd(2);
            } else if (i == R.id.iv_down) {// 下转
                mPtzDirectionRequestEntity.setCmd(6);
            } else if (i == R.id.iv_left) {//左转
                mPtzDirectionRequestEntity.setCmd(4);
            } else if (i == R.id.iv_right) {//右转
                mPtzDirectionRequestEntity.setCmd(0);
            }
            mPtzDirectionRequestEntity.setCameraId(mCameraId);
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d("OnTouchListener Down");
                mPtzDirectionRequestEntity.setParam("5");
                mPresenter.setPtzDirection(mPtzDirectionRequestEntity);
                return false;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                Log.d("OnTouchListener Up");
                directionCanel();
                return false;
            }
            return false;
        }
    }

    public void directionCanel() {
        mPtzDirectionRequestEntity.setCmd(4);
        mPtzDirectionRequestEntity.setCameraId(mCameraId);
        mPtzDirectionRequestEntity.setParam("0");
        mPresenter.setPtzDirection(mPtzDirectionRequestEntity);
    }

    /**
     * 横屏关注的图片
     */
    public void setLandscapeFollowImage() {
        if (mCollected) {
            mTvAttention.setImageResource(R.drawable.ic_attention_on);
        } else {
            mTvAttention.setImageResource(R.drawable.ic_attention_up);
        }
    }

    //接口会返回除了下面的四种类型外  "face,body,camera,traffic", 还会有其他的类型，
    public int getPicType(String str) {
        if (str.equals(CAMERA_TYPE)) {
            return mImageId[0];
        } else if (str.equals(TRAFFIC_TYPE)) {
            return mImageId[1];
        } else if (str.equals(FACE_TYPE)) {
            return mImageId[2];
        } else if (str.equals(BODY_TYPE)) {
            return mImageId[3];
        } else {
            return 4;//返回一个callback参数 不做处理
        }
    }

    /**
     * 设置竖屏关注的图片
     *
     * @param isFollowImgClicked
     */
    private void setPortraitFollowImage(boolean isFollowImgClicked) {
        if (isFollowImgClicked) {// 已关注  显示红心tr
            mIvFollow.setImageResource(R.drawable.ic_add_follow);
            mTvFollow.setText(getString(R.string.unsubscribe));
        } else {// 取消关注
            mIvFollow.setImageResource(R.drawable.ic_cancel_follow);
            mTvFollow.setText(getString(R.string.subscribe));
        }
    }

    private void showToast(boolean flag) {
        if (flag) {
            showMessage(getString(R.string.subscribe));
        } else {
            showMessage(getString(R.string.unsubscribe));
        }
    }
}
