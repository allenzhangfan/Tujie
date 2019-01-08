package com.netposa.component.login.mvp.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.OnClick;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.BuildConfig;
import com.netposa.common.app.ErrorDialog;
import com.netposa.common.constant.GlobalConstants;
import com.netposa.common.constant.UrlConstant;
import com.netposa.common.core.Log4jAppLifecycle;
import com.netposa.common.core.RouterHub;
import com.netposa.common.log.Log;
import com.netposa.common.utils.KeyboardUtils;
import com.netposa.common.utils.ReflectUtils;
import com.netposa.common.utils.SPUtils;
import com.netposa.common.utils.SoftKeyBoardListener;
import com.netposa.commonres.widget.DashView;
import com.netposa.commonres.widget.Dialog.LottieDialogFragment;
import com.netposa.component.login.R2;
import com.netposa.component.login.di.component.DaggerLoginComponent;
import com.netposa.component.login.di.module.LoginModule;
import com.netposa.component.login.mvp.contract.LoginContract;
import com.netposa.component.login.mvp.presenter.LoginPresenter;
import com.netposa.component.login.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.common.constant.GlobalConstants.HAS_FACE;
import static com.netposa.common.constant.GlobalConstants.IS_LOCAL_NET;
import static com.netposa.component.login.app.LoginConstants.KEY_LOGIN_IP;
import static com.netposa.component.login.app.LoginConstants.REQUEST_CODE_LOGIN_IP;

@Route(path = RouterHub.LOGIN_ACTIVITY)
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    private static final int REQUEST_PERMISSIONS = 0;

    @Inject
    LottieDialogFragment mLoadingDialogFragment;
    @Inject
    IRepositoryManager mRepositoryManager;

    @BindView(R2.id.login_scrollivew)
    ScrollView mScrollView;
    @BindView(R2.id.et_account)
    EditText mEtAccount;
    @BindView(R2.id.et_password)
    EditText mEtPassword;
    @BindView(R2.id.dv_account)
    DashView dvAccount;
    @BindView(R2.id.dv_password)
    DashView dvPassword;
    @BindView(R2.id.til_account)
    TextInputLayout mTilUsernameHit;
    @BindView(R2.id.til_psw)
    TextInputLayout mTilPasswordHit;

    // dialog 里的textview
    private TextView tell_textview, email_textview;
    private ImageView dialog_right_bt;
    private Dialog forget_pw_Dialog;
    private MaterialButton confirmBtn;
    private Boolean mHas_login;
    private String mSaveUserName;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLoginComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .loginModule(new LoginModule(this, this, getSupportFragmentManager()))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_login; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        checkPermissions();
        initEditTextBottomLine();
        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                setScrollAbility(false);
            }

            @Override
            public void keyBoardHide(int height) {
                setScrollAbility(true);
            }
        });
        if (!TextUtils.isEmpty(SPUtils.getInstance().getString(GlobalConstants.CONFIG_LAST_USER_LOGIN_NAME))) {
            mSaveUserName = SPUtils.getInstance().getString(GlobalConstants.CONFIG_LAST_USER_LOGIN_NAME);
            mEtAccount.setText(mSaveUserName);
            mTilUsernameHit.setHint(getString(R.string.user_name));
        }
//        mHas_login = SPUtils.getInstance().getBoolean(GlobalConstants.HAS_LOGIN, false);
//        if (!mHas_login) {
////            SPUtils.getInstance().put(HAS_FACE, false);
//        }
        //调试账号密码
        //admin admin123
        if (BuildConfig.DEBUG) {
            mEtAccount.setText("admin");
            mEtPassword.setText("a123456");
            SPUtils.getInstance().put(IS_LOCAL_NET, true);
        } else {
            SPUtils.getInstance().put(IS_LOCAL_NET, false);
        }
        mTilUsernameHit.setHint(getString(R.string.user_name));
        mTilPasswordHit.setHint(getString(R.string.password));
        mEtAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                mTilUsernameHit.setHint(getString(R.string.user_name));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    mTilUsernameHit.setHint(getString(R.string.login_plz_input_username));
                }
            }
        });
        mEtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                mTilPasswordHit.setHint(getString(R.string.password));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    mTilPasswordHit.setHint(getString(R.string.login_plz_input_pwd));
                }
            }
        });
    }

    private void checkPermissions() {
        List<String> permissions = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA);
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE) != PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissions.toArray(new String[0]), REQUEST_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions
            , @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                boolean result = true;
                for (int grantResult : grantResults) {
                    if (grantResult != PERMISSION_GRANTED) {
                        result = false;
                        break;
                    }
                }
                if (result) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Log4jAppLifecycle.log4jConfigure(Environment.getExternalStorageState()
                                .equals(Environment.MEDIA_MOUNTED));
                    }
                    //初始化应用目录
                    mPresenter.initDirs();
                } else {
                    runOnUiThread(() -> ErrorDialog.newInstance(getString(R.string.require_several_permission)).show(getSupportFragmentManager(), "dialog"));
                }
                break;
        }
    }

    private void initEditTextBottomLine() {
        mEtAccount.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                dvAccount.start();
            } else {
                dvAccount.reset();
            }
        });
        mEtPassword.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                dvPassword.start();
            } else {
                dvPassword.reset();
            }
        });
    }

    @Override
    public void showLoading(String message) {
        mLoadingDialogFragment.show(getSupportFragmentManager(), "LoadingDialog");
    }

    @Override
    public void hideLoading() {
        mLoadingDialogFragment.dismissAllowingStateLoss();
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


    @OnClick({R2.id.forget_pwd_txt, R2.id.set_ip_img, R2.id.btn_login, R2.id.face_type_login})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.forget_pwd_txt) {
            showForgetpwdDialog();
        } else if (id == R.id.set_ip_img) {
            Intent setIpIntent = new Intent(this, SetUpIpActivity.class);
            startActivityForResult(setIpIntent, REQUEST_CODE_LOGIN_IP);
        } else if (id == R.id.btn_login) {
//            goToHomeActivity();
            login();
        } else if (id == R.id.face_type_login) {
            String userName = mEtAccount.getText().toString();
            if (TextUtils.isEmpty(userName)) {
                showMessage(getString(R.string.login_plz_input_username));
                return;
            }
            //保存的用户和输入的用户名不一样
            if (null != mSaveUserName && !(userName.equals(mSaveUserName))) {
                showMessage(getString(R.string.face_login_notice));
                return;
            }
            if (SPUtils.getInstance().getBoolean(HAS_FACE)) {
                ARouter.getInstance().build(RouterHub.CAMERA_FACE_LOGIN_ACTIVITY)
                        .withString(HAS_FACE, userName)
                        .navigation(this);
            } else {
                showMessage(getString(R.string.face_login_notice));
            }
        }
    }

    public void showForgetpwdDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View forget_pwd_view = inflater.inflate(R.layout.forget_pwd_dialog, null);

        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        forget_pw_Dialog = new Dialog(this, R.style.ForgetPwdDialog);
        forget_pw_Dialog.setCancelable(true);
        forget_pw_Dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);
        forget_pw_Dialog.setContentView(forget_pwd_view, new LinearLayout.LayoutParams(
                dm.widthPixels * 7 / 8,
                dm.heightPixels * 3 / 7
        ));

        forget_pw_Dialog.show();
        tell_textview = forget_pwd_view.findViewById(R.id.tell_copy_tv);
        email_textview = forget_pwd_view.findViewById(R.id.email_copy_tv);

        dialog_right_bt = forget_pwd_view.findViewById(R.id.dialog_right);
        confirmBtn = forget_pwd_view.findViewById(R.id.btn_confirm);

        email_textview.setOnClickListener(this::onClick);
        tell_textview.setOnClickListener(this::onClick);

        dialog_right_bt.setOnClickListener(this::onClick);
        confirmBtn.setOnClickListener(this::onClick);

    }

    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tell_copy_tv) {
            onClickCopy(getString(R.string.tell_text));
        } else if (i == R.id.email_copy_tv) {
            onClickCopy(getString(R.string.email_text));
        } else if (i == R.id.dialog_right || i == R.id.btn_confirm) {
            forget_pw_Dialog.dismiss();
        }
    }

    public void onClickCopy(String messageContent) {
        // 从API11开始android推荐使用android.content.ClipboardManager
        // 低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        //  cm.setText(messageContent);
        ClipData myClip = ClipData.newPlainText("text", messageContent);
        cm.setPrimaryClip(myClip);
        showMessage(getString(R.string.copy_success));
    }

    /**
     * 点击返回键返回桌面而不是退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOGIN_IP) {
            if (data == null) {
                Log.e(TAG, "ip is null !");
                return;
            }
            String newBaseUrl = UrlConstant.sBaseUrl = data.getStringExtra(KEY_LOGIN_IP);
            /**参考{@link UrlConstant#sBaseUrl},使用setGlobalDomain会替换全局baseurl**/
            if (BuildConfig.DEBUG) {
                SPUtils.getInstance().put(IS_LOCAL_NET, false);
            } else {
                SPUtils.getInstance().put(IS_LOCAL_NET, true);
            }
            ReflectUtils.setRetrofitBaseUrlTemporarily(mRepositoryManager, newBaseUrl, true);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setScrollAbility(boolean flag) {
        mScrollView.setOnTouchListener((view, motionEvent) -> {
            //不能滑动 是true   可以滑动 false
            return flag;
        });
    }

    private void login() {
        String username = mEtAccount.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            showMessage(getString(R.string.login_plz_input_username));
            KeyboardUtils.hideSoftInput(this, mEtAccount);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            showMessage(getString(R.string.login_plz_input_pwd));
            KeyboardUtils.hideSoftInput(this, mEtPassword);
            return;
        }
        if (mEtPassword.getText().length() < 6) {
            showMessage(getString(R.string.password_width));
        } else {
            Objects.requireNonNull(mPresenter).login(username, password);
        }
    }

    @Override
    public void goToHomeActivity() {
        ARouter.getInstance().build(RouterHub.APP_HOME_ACTIVITY).navigation(this);
        killMyself();
    }
}
