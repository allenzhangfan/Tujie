package com.netposa.component.clcx.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.button.MaterialButton;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.log.Log;
import com.netposa.common.utils.KeyboardUtils;
import com.netposa.common.utils.TimeUtils;
import com.netposa.commonres.widget.CaptureTimeHelper;
import com.netposa.commonres.widget.OneKeyClearEditText;
import com.netposa.component.clcx.R2;
import com.netposa.component.clcx.di.component.DaggerQueryCarComponent;
import com.netposa.component.clcx.di.module.QueryCarModule;
import com.netposa.component.clcx.mvp.contract.QueryCarContract;
import com.netposa.component.clcx.mvp.model.entity.CarTypeEntry;
import com.netposa.component.clcx.mvp.model.entity.QueryCarSearchEntity;
import com.netposa.component.clcx.mvp.presenter.QueryCarPresenter;
import com.netposa.component.clcx.R;
import com.netposa.component.clcx.mvp.ui.widget.BottomSheetDialogFragment;
import java.util.ArrayList;
import java.util.Calendar;
import javax.inject.Inject;
import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.common.core.RouterHub.CLCX_QUERY_CAR_ACTIVITY;
import static com.netposa.common.utils.TimeUtils.FORMAT_ONE;
import static com.netposa.commonres.widget.CaptureTimeHelper.START_TIME;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_CURRENT_PAGE;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_END_TIME;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_LIST_TYPE;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_PAGE_SIZE;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_PLATE_COLORS;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_PLATE_NUMBER;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_PLATE_TYPES;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_QUANBU;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_SELECT_RESULT;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_SINGLE_TYPE;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_START_TIME;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_VEHICLE_COLORS;
import static com.netposa.component.clcx.app.ClcxConstants.KEY_VEHICLE_TYPES;
import static com.netposa.component.clcx.app.ClcxConstants.REQUESTCODE_CAR_PLATE;
import static com.netposa.component.clcx.app.ClcxConstants.REQUESTCODE_CAR_TYPE;
import static com.netposa.component.clcx.app.ClcxConstants.mType_car;
import static com.netposa.component.clcx.app.ClcxConstants.mType_plate;

@Route(path = CLCX_QUERY_CAR_ACTIVITY)
public class QueryCarActivity extends BaseActivity<QueryCarPresenter> implements QueryCarContract.View, CaptureTimeHelper.Listener, BottomSheetDialogFragment.BottomSheetDialogListener {

    @BindView(R2.id.title_tv)
    TextView mTVtTitle;
    @BindView(R2.id.bt_endtime)
    MaterialButton mBtEndTime;
    @BindView(R2.id.bt_begintime)
    MaterialButton mBtBeginTime;
    @BindView(R2.id.okcl_search)
    OneKeyClearEditText mOkclSearch;
    @BindView(R2.id.tv_plate_select)
    TextView mTvPlateSelect;
    @BindView(R2.id.bt_plate_color_all)
    MaterialButton mBtPlateColorAll;
    @BindView(R2.id.iv_plate_blue)
    ImageView mIvPlateBlue;
    @BindView(R2.id.iv_plate_yellow)
    ImageView mIvPlateYellow;
    @BindView(R2.id.iv_plate_black)
    ImageView mIvPlateBlack;
    @BindView(R2.id.iv_plate_white)
    ImageView mIvPlateWhite;
    @BindView(R2.id.iv_plate_white_green)
    ImageView mIvPlateWhiteGreen;
    @BindView(R2.id.iv_plate_green_yellow)
    ImageView mIvPlateWhiteGreenYellow;
    @BindView(R2.id.bt_car_color_all)
    MaterialButton mBtCarColorAll;
    @BindView(R2.id.iv_car_white)
    ImageView mIvCarWhite;
    @BindView(R2.id.iv_car_grey)
    ImageView mIvCarGrey;
    @BindView(R2.id.iv_car_yellow)
    ImageView mIvCarYellow;
    @BindView(R2.id.iv_car_pink)
    ImageView mIvCarPink;
    @BindView(R2.id.iv_car_red)
    ImageView mIvCarRed;
    @BindView(R2.id.iv_car_purple)
    ImageView mIvCarPurple;
    @BindView(R2.id.iv_car_dark_green)
    ImageView mIvCarDarkGreen;
    @BindView(R2.id.iv_car_dark_blue)
    ImageView mIvCarDarkBlue;
    @BindView(R2.id.iv_car_dark_red)
    ImageView mIvCarDarkRed;
    @BindView(R2.id.iv_car_black)
    ImageView mIvCarBlack;
    @BindView(R2.id.tv_car_type)
    TextView mTvCarType;
    @BindView(R2.id.tv_plate_type)
    TextView mTvPlateType;

    @Inject
    CaptureTimeHelper mCaptureTimeHelper;
    @Inject
    BottomSheetDialogFragment mBottomSheetDialogFragment;
    @Inject
    ArrayList<CarTypeEntry> mCarTypeList;//车
    @Inject
    ArrayList<CarTypeEntry> mPlateTypeList;// 车牌

    private Boolean[] mPlateColorSelectStates = new Boolean[7];
    private Boolean[] mCarColorSelectStates = new Boolean[11];
    private String[] mPlateColorValues = new String[]{"2", "1", "3", "0", "101", "6"}; //2；蓝色，1；黄色，3；黑色，0；白色，101；绿色，6；黄绿色
    private String[] mCarColorValues = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "j"}; //0；白色，1；灰色（银色），2；黄色，3；粉色，4；红色，5；绿色，6；蓝色，7；棕色，8；黑色，9；紫色
    private String[] mPlateTypeValues = new String[]{"1", "2", "3", "13", "15", "16", "23", "26", "28", "101", "102", "103", "104", "200", "201", "99"};// 1;大型汽车号牌,2;小型汽车号牌，3；使馆汽车号牌，13；农用运输车号牌，15；挂车号牌，16；教练汽车号牌 ，23；警用汽车号牌，26；香港汽车号牌，28；武警汽车号牌，101；黑牌小汽车，102；个性化车牌，103；单排军牌，104；双排军牌，200；新能源小车，201；新能源大车，99；其他号牌
    private String[] mCarTypeValues = new String[]{"K33", "K96", "K95", "K94", "K93", "K21", "K11", "K31", "H21", "H11", "H31", "H41"};//k93;轿车，k96;面包车，k95;皮卡，k94;商务车，k93越野车,k21;中型普通客车，k11;大型普通客车，k31;小型普通客车，H21；中型普通货车，H11；重型普通货车，H31；轻型普通货车，H41；微型普通货车
    private QueryCarSearchEntity mEntity = new QueryCarSearchEntity();
    private ArrayList<String> mPlate_color_list = new ArrayList<>(); //车牌颜色
    private ArrayList<String> mCar_color_list = new ArrayList<>(); //车辆颜色
    private ArrayList<String> mPlate_type_list = new ArrayList<>();// 车牌类型
    private ArrayList<String> mCar_type_list = new ArrayList<>();//车辆类型
    private String[] mPlateTypeSource; //车牌资源
    private String[] mCarTypeSource; //车辆资源
    private int mCurrentHour;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerQueryCarComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .queryCarModule(new QueryCarModule(this, this, getSupportFragmentManager()))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_query_car; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mTVtTitle.setText(R.string.clcx);
        mPlateTypeSource = getResources().getStringArray(R.array.plate_type_item);
        mCarTypeSource = getResources().getStringArray(R.array.car_type_item);
        //时间选择有关初始化
        Calendar beginCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        mCaptureTimeHelper.init(beginCalendar, endCalendar, this);

        //对于刚跳到一个新的界面就要弹出软键盘的情况上述代码可能由于界面为加载完全而无法弹出软键盘
        //此时应该适当的延迟弹出软键盘（保证界面的数据加载完成）
//        new Timer()
//                .schedule(
//                        new TimerTask() {
//                            public void run() {
//                                InputMethodManager inputManager =
//                                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                                inputManager.showSoftInput(mOkclSearch, RESULT_UNCHANGED_SHOWN);
//                            }
//
//                        },
//                        300);

        mBottomSheetDialogFragment.setBottomSheetDialogListener(this);
        //底部弹出框高度
        mBottomSheetDialogFragment.setHeightPercent(0.82f);
        //重置车辆和车牌号颜色选择
        resetPlateAndCarColorSelect();
        // 默认24小时
        setTimetoTxt();
    }

    private void setTimetoTxt() {
        //时间选择有关初始化
        Calendar startCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();
        mCurrentHour = startCalendar.get(Calendar.HOUR_OF_DAY);
        mCaptureTimeHelper.init(startCalendar, finishCalendar, this);
        // 初始默认请求此时之前的24小时
        long startDate = TimeUtils.getBeforHour(-24);// 当前时间的前24小时
        long endDate = TimeUtils.getBeforHour(0);//当前时间

        String startTimeStr = TimeUtils.millis2String(startDate, FORMAT_ONE);
        mEntity.setStartTime(TimeUtils.string2Millis(startTimeStr));
        mBtBeginTime.setText(getString(R.string.begin_time) + "\n" + startTimeStr);
        mBtBeginTime.setSelected(true);

        String endTimeStr = TimeUtils.millis2String(endDate, FORMAT_ONE);
        mEntity.setEndTime(TimeUtils.string2Millis(endTimeStr));
        mBtEndTime.setText(getString(R.string.end_time) + "\n" + endTimeStr);
        mBtEndTime.setSelected(true);
    }

    private void resetPlateAndCarColorSelect() {
        for (int i = 0; i < mPlateColorSelectStates.length; i++) {
            mPlateColorSelectStates[i] = false;
        }
        for (int i = 0; i < mCarColorSelectStates.length; i++) {
            mCarColorSelectStates[i] = false;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBottomSheetDialogFragment.removeListener();
    }

    @Override
    public void onBackPressed() {
        KeyboardUtils.hideSoftInput(this, mOkclSearch);
        super.onBackPressed();
    }

    @OnClick({
            R2.id.head_left_iv,
            R2.id.bt_begintime,
            R2.id.bt_endtime,
            R2.id.tv_plate_select,
            R2.id.btn_search,
            R2.id.bt_plate_color_all,
            R2.id.iv_plate_blue,
            R2.id.iv_plate_yellow,
            R2.id.iv_plate_black,
            R2.id.iv_plate_white,
            R2.id.iv_plate_white_green,
            R2.id.iv_plate_green_yellow,
            R2.id.bt_car_color_all,
            R2.id.iv_car_white,
            R2.id.iv_car_grey,
            R2.id.iv_car_yellow,
            R2.id.iv_car_pink,
            R2.id.iv_car_red,
            R2.id.iv_car_purple,
            R2.id.iv_car_dark_green,
            R2.id.iv_car_dark_blue,
            R2.id.iv_car_dark_red,
            R2.id.iv_car_black,
            R2.id.ry_cartype,
            R2.id.rl_platetype
    })
    public void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.head_left_iv) {
            KeyboardUtils.hideSoftInput(this, mOkclSearch);
            killMyself();
        } else if (id == R.id.btn_search) {
            setCarSearchEntity();
            Log.d(TAG,"search entity: "+mEntity.toString());
            Intent intent = new Intent(this, QueryResultActivity.class);
            intent.putExtra(KEY_CURRENT_PAGE,mEntity.getCurrentPage());
            intent.putExtra(KEY_PAGE_SIZE,mEntity.getPageSize());
            intent.putExtra(KEY_PLATE_NUMBER,mEntity.getPlateNumber());
            intent.putStringArrayListExtra(KEY_PLATE_COLORS,mEntity.getPlateColors());
            intent.putStringArrayListExtra(KEY_PLATE_TYPES,mEntity.getPlateTypes());
            intent.putStringArrayListExtra(KEY_VEHICLE_COLORS,mEntity.getVehicleColors());
            intent.putStringArrayListExtra(KEY_VEHICLE_TYPES,mEntity.getVehicleTypes());
            intent.putExtra(KEY_START_TIME,mEntity.getStartTime());
            intent.putExtra(KEY_END_TIME,mEntity.getEndTime());
            launchActivity(intent);
            mCar_color_list.clear();
            mPlate_color_list.clear();
        } else if (id == R.id.bt_begintime) {
            mCaptureTimeHelper.showStartDateDialog();
        } else if (id == R.id.bt_endtime) {
            mCaptureTimeHelper.showEndDateDialog();
        } else if (id == R.id.tv_plate_select) {
            mBottomSheetDialogFragment.show(getSupportFragmentManager(), "Dialog");
        } else if (id == R.id.bt_plate_color_all) {
            mPlateColorSelectStates[0] = true;
            setPlateColorSelect();
        } else if (id == R.id.iv_plate_blue) {
            mPlateColorSelectStates[1] = !mPlateColorSelectStates[1];
            mPlateColorSelectStates[0] = false;
            setPlateColorSelect();
        } else if (id == R.id.iv_plate_yellow) {
            mPlateColorSelectStates[2] = !mPlateColorSelectStates[2];
            mPlateColorSelectStates[0] = false;
            setPlateColorSelect();
        } else if (id == R.id.iv_plate_black) {
            mPlateColorSelectStates[3] = !mPlateColorSelectStates[3];
            mPlateColorSelectStates[0] = false;
            setPlateColorSelect();
        } else if (id == R.id.iv_plate_white) {
            mPlateColorSelectStates[4] = !mPlateColorSelectStates[4];
            mPlateColorSelectStates[0] = false;
            setPlateColorSelect();
        } else if (id == R.id.iv_plate_white_green) {
            mPlateColorSelectStates[5] = !mPlateColorSelectStates[5];
            mPlateColorSelectStates[0] = false;
            setPlateColorSelect();
        } else if (id == R.id.iv_plate_green_yellow) {
            mPlateColorSelectStates[6] = !mPlateColorSelectStates[6];
            mPlateColorSelectStates[0] = false;
            setPlateColorSelect();
        } else if (id == R.id.bt_car_color_all) {
            mCarColorSelectStates[0] = true;
            setCarColorSelect();
        } else if (id == R.id.iv_car_white) {
            mCarColorSelectStates[1] = !mCarColorSelectStates[1];
            mCarColorSelectStates[0] = false;
            setCarColorSelect();
        } else if (id == R.id.iv_car_grey) {
            mCarColorSelectStates[2] = !mCarColorSelectStates[2];
            mCarColorSelectStates[0] = false;
            setCarColorSelect();
        } else if (id == R.id.iv_car_yellow) {
            mCarColorSelectStates[3] = !mCarColorSelectStates[3];
            mCarColorSelectStates[0] = false;
            setCarColorSelect();
        } else if (id == R.id.iv_car_pink) {
            mCarColorSelectStates[4] = !mCarColorSelectStates[4];
            mCarColorSelectStates[0] = false;
            setCarColorSelect();
        } else if (id == R.id.iv_car_red) {
            mCarColorSelectStates[5] = !mCarColorSelectStates[5];
            mCarColorSelectStates[0] = false;
            setCarColorSelect();
        } else if (id == R.id.iv_car_purple) {
            mCarColorSelectStates[6] = !mCarColorSelectStates[6];
            mCarColorSelectStates[0] = false;
            setCarColorSelect();
        } else if (id == R.id.iv_car_dark_green) {
            mCarColorSelectStates[7] = !mCarColorSelectStates[7];
            mCarColorSelectStates[0] = false;
            setCarColorSelect();
        } else if (id == R.id.iv_car_dark_blue) {
            mCarColorSelectStates[8] = !mCarColorSelectStates[8];
            mCarColorSelectStates[0] = false;
            setCarColorSelect();
        } else if (id == R.id.iv_car_dark_red) {
            mCarColorSelectStates[9] = !mCarColorSelectStates[9];
            mCarColorSelectStates[0] = false;
            setCarColorSelect();
        } else if (id == R.id.iv_car_black) {
            mCarColorSelectStates[10] = !mCarColorSelectStates[10];
            mCarColorSelectStates[0] = false;
            setCarColorSelect();
        } else if (id == R.id.rl_platetype) {
            goToTypeActivity(mType_plate);
        } else if (id == R.id.ry_cartype) {
            goToTypeActivity(mType_car);
        }
    }


    @Override
    public void getFailed() {

    }


    private void setCarSearchEntity() {
        //车牌颜色选择
        for (int i = 0; i < mPlateColorValues.length; i++) {
            if (mPlateColorSelectStates[i + 1]) {
                mPlate_color_list.add(mPlateColorValues[i]);
                Log.i(TAG, "添加车牌颜色" + mPlateColorValues[i]);
            }
        }
        mEntity.setPlateColors(mPlate_color_list);
        //车辆颜色选择
        for (int i = 0; i < mCarColorValues.length; i++) {
            if (mCarColorSelectStates[i + 1]) {
                mCar_color_list.add(mCarColorValues[i]);
                Log.i(TAG, "添加车辆颜色" + mCarColorValues[i]);
            }
        }
        mEntity.setVehicleColors(mCar_color_list);
        String vehicel = mTvPlateSelect.getText().toString();
        String carNum = mOkclSearch.getText().toString();
        if (vehicel.equals(getString(R.string.all))) {
            mEntity.setPlateNumber("" + carNum);
        } else {
            mEntity.setPlateNumber(vehicel + carNum);
        }
    }

    private void goToTypeActivity(String mType) {
        Intent intent = new Intent(this, CarTypeActivity.class);
        intent.putExtra(KEY_SINGLE_TYPE, mType);
        if (mType.equals(mType_plate)) {
            intent.putParcelableArrayListExtra(KEY_LIST_TYPE, mPlateTypeList);
            startActivityForResult(intent, REQUESTCODE_CAR_PLATE); //车牌类型
        } else if (mType.equals(mType_car)) {
            intent.putExtra(KEY_LIST_TYPE, mCarTypeList);
            startActivityForResult(intent, REQUESTCODE_CAR_TYPE); //车辆类型
        }
    }

    @Override
    public void setCaptureTime(long time, int selectTag) {
        if (selectTag == START_TIME) {
            String startTimeStr = TimeUtils.millis2String(time, FORMAT_ONE);
            mEntity.setStartTime(TimeUtils.string2Millis(startTimeStr));
            mBtBeginTime.setText(getString(R.string.begin_time) + "\n" + startTimeStr);
            mBtBeginTime.setSelected(true);
        } else {
            String endTimeStr = TimeUtils.millis2String(time, FORMAT_ONE);
            mEntity.setEndTime(TimeUtils.string2Millis(endTimeStr));
            mBtEndTime.setText(getString(R.string.end_time) + "\n" + endTimeStr);
            mBtEndTime.setSelected(true);
        }
    }

    @Override
    public void setSelectResult(String name) {
        mTvPlateSelect.setText(name);
    }

    //车牌颜色选择
    private void setPlateColorSelect() {
        if (mPlateColorSelectStates[0]) {
            mBtPlateColorAll.setSelected(true);
            mIvPlateBlue.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
            mIvPlateYellow.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
            mIvPlateBlack.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
            mIvPlateWhite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
            mIvPlateWhiteGreen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
            mIvPlateWhiteGreen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
            mIvPlateWhiteGreenYellow.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
            for (int i = 0; i < mPlateColorSelectStates.length; i++) {
                if (i != 0) {
                    mPlateColorSelectStates[i] = true;
                }
            }
            return;
        }
        if (mPlateColorSelectStates[1]) {
            mIvPlateBlue.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
        } else {
            mIvPlateBlue.setImageDrawable(null);
            mBtPlateColorAll.setSelected(false);
        }
        if (mPlateColorSelectStates[2]) {
            mIvPlateYellow.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
        } else {
            mIvPlateYellow.setImageDrawable(null);
            mBtPlateColorAll.setSelected(false);
        }
        if (mPlateColorSelectStates[3]) {
            mIvPlateBlack.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
        } else {
            mIvPlateBlack.setImageDrawable(null);
            mBtPlateColorAll.setSelected(false);
        }
        if (mPlateColorSelectStates[4]) {
            mIvPlateWhite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
        } else {
            mIvPlateWhite.setImageDrawable(null);
            mBtPlateColorAll.setSelected(false);
        }
        if (mPlateColorSelectStates[5]) {
            mIvPlateWhiteGreen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
        } else {
            mIvPlateWhiteGreen.setImageDrawable(null);
            mBtPlateColorAll.setSelected(false);
        }
        if (mPlateColorSelectStates[6]) {
            mIvPlateWhiteGreenYellow.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
        } else {
            mIvPlateWhiteGreenYellow.setImageDrawable(null);
            mBtPlateColorAll.setSelected(false);
        }
        if (mPlateColorSelectStates[1]
                && mPlateColorSelectStates[2]
                && mPlateColorSelectStates[3]
                && mPlateColorSelectStates[4]
                && mPlateColorSelectStates[5]
                && mPlateColorSelectStates[6]) {
            mBtPlateColorAll.setSelected(true);
        }
    }

    //车辆颜色选择
    private void setCarColorSelect() {
        if (mCarColorSelectStates[0]) {
            mBtCarColorAll.setSelected(true);
            mIvCarWhite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
            mIvCarGrey.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
            mIvCarYellow.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
            mIvCarPink.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
            mIvCarRed.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
            mIvCarPurple.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
            mIvCarDarkGreen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
            mIvCarDarkBlue.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
            mIvCarDarkRed.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
            mIvCarBlack.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
            for (int i = 0; i < mCarColorSelectStates.length; i++) {
                if (i != 0) {
                    mCarColorSelectStates[i] = true;
                }
            }
            return;
        }
        if (mCarColorSelectStates[1]) {
            mIvCarWhite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
        } else {
            mIvCarWhite.setImageDrawable(null);
            mBtCarColorAll.setSelected(false);
        }
        if (mCarColorSelectStates[2]) {
            mIvCarGrey.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
        } else {
            mIvCarGrey.setImageDrawable(null);
            mBtCarColorAll.setSelected(false);
        }
        if (mCarColorSelectStates[3]) {
            mIvCarYellow.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
        } else {
            mIvCarYellow.setImageDrawable(null);
            mBtCarColorAll.setSelected(false);
        }
        if (mCarColorSelectStates[4]) {
            mIvCarPink.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
        } else {
            mIvCarPink.setImageDrawable(null);
            mBtCarColorAll.setSelected(false);
        }
        if (mCarColorSelectStates[5]) {
            mIvCarRed.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
        } else {
            mIvCarRed.setImageDrawable(null);
            mBtCarColorAll.setSelected(false);
        }
        if (mCarColorSelectStates[6]) {
            mIvCarPurple.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
        } else {
            mIvCarPurple.setImageDrawable(null);
            mBtCarColorAll.setSelected(false);
        }
        if (mCarColorSelectStates[7]) {
            mIvCarDarkGreen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
        } else {
            mIvCarDarkGreen.setImageDrawable(null);
            mBtCarColorAll.setSelected(false);
        }
        if (mCarColorSelectStates[8]) {
            mIvCarDarkBlue.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
        } else {
            mIvCarDarkBlue.setImageDrawable(null);
            mBtCarColorAll.setSelected(false);
        }
        if (mCarColorSelectStates[9]) {
            mIvCarDarkRed.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
        } else {
            mIvCarDarkRed.setImageDrawable(null);
            mBtCarColorAll.setSelected(false);
        }
        if (mCarColorSelectStates[10]) {
            mIvCarBlack.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_color_choose));
        } else {
            mIvCarBlack.setImageDrawable(null);
            mBtCarColorAll.setSelected(false);
        }

        if (mCarColorSelectStates[1]
                && mCarColorSelectStates[2]
                && mCarColorSelectStates[3]
                && mCarColorSelectStates[4]
                && mCarColorSelectStates[5]
                && mCarColorSelectStates[6]
                && mCarColorSelectStates[6]
                && mCarColorSelectStates[7]
                && mCarColorSelectStates[8]
                && mCarColorSelectStates[9]
                && mCarColorSelectStates[10]) {
            mBtCarColorAll.setSelected(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case REQUESTCODE_CAR_TYPE: // 选择完 车辆类型的结果
                mCarTypeList = data.getParcelableArrayListExtra(KEY_SELECT_RESULT);
                mCar_type_list.clear();
                ArrayList alist = new ArrayList();
                for (int i = 0; i < mCarTypeList.size(); i++) {
                    boolean flag = mCarTypeList.get(i).isSelect;
                    if (flag) {
                        alist.add(mCarTypeList.get(i).getTitle());
                    }
                }
                if (alist.contains(KEY_QUANBU)) {
                    alist.remove(0);
                }
                if (alist.size() > 0) {
                    mTvCarType.setText(R.string.has_choose);
                    mEntity.setVehicleTypes(addCarTypeList(alist));
//                    Log.d(TAG, alist + "--" + addCarTypeList(alist));
                } else {
                    mTvCarType.setText(R.string.select_type);
                }
                break;
            case REQUESTCODE_CAR_PLATE:// 选择完 车牌的结果
                mPlateTypeList = data.getParcelableArrayListExtra(KEY_SELECT_RESULT);
                mPlate_type_list.clear();
                ArrayList result = new ArrayList();
                for (int i = 0; i < mPlateTypeList.size(); i++) {
                    boolean flag = mPlateTypeList.get(i).isSelect;
                    if (flag) {
                        result.add(mPlateTypeList.get(i).getTitle());
                    }
                }
                if (result.contains(KEY_QUANBU)) {
                    result.remove(0);
                }
                if (result.size() > 0) {
                    mEntity.setPlateTypes(addPlateTypeList(result));
//                    Log.d(TAG, result + "--" + addCarTypeList(result));
                    mTvPlateType.setText(R.string.has_choose);
                } else {
                    mTvPlateType.setText(R.string.select_type);
                }
                break;
        }
    }

    private ArrayList<String> addCarTypeList(ArrayList list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(mCarTypeSource[1])) {
                mCar_type_list.add(mCarTypeValues[0]);
            } else if (list.get(i).equals(mCarTypeSource[2])) {
                mCar_type_list.add(mCarTypeValues[1]);
            } else if (list.get(i).equals(mCarTypeSource[3])) {
                mCar_type_list.add(mCarTypeValues[2]);
            } else if (list.get(i).equals(mCarTypeSource[4])) {
                mCar_type_list.add(mCarTypeValues[3]);
            } else if (list.get(i).equals(mCarTypeSource[5])) {
                mCar_type_list.add(mCarTypeValues[4]);
            } else if (list.get(i).equals(mCarTypeSource[6])) {
                mCar_type_list.add(mCarTypeValues[5]);
            } else if (list.get(i).equals(mCarTypeSource[7])) {
                mCar_type_list.add(mCarTypeValues[6]);
            } else if (list.get(i).equals(mCarTypeSource[8])) {
                mCar_type_list.add(mCarTypeValues[7]);
            } else if (list.get(i).equals(mCarTypeSource[9])) {
                mCar_type_list.add(mCarTypeValues[8]);
            } else if (list.get(i).equals(mCarTypeSource[10])) {
                mCar_type_list.add(mCarTypeValues[9]);
            } else if (list.get(i).equals(mCarTypeSource[11])) {
                mCar_type_list.add(mCarTypeValues[10]);
            } else if (list.get(i).equals(mCarTypeSource[12])) {
                mCar_type_list.add(mCarTypeValues[11]);
            } else if (list.get(i).equals(mCarTypeSource[13])) {
                mCar_type_list.add(mCarTypeValues[12]);
            }
        }
        return mCar_type_list;
    }

    private ArrayList<String> addPlateTypeList(ArrayList list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(mPlateTypeSource[1])) {
                mPlate_type_list.add(mPlateTypeValues[0]);
            } else if (list.get(i).equals(mPlateTypeSource[2])) {
                mPlate_type_list.add(mPlateTypeValues[1]);
            } else if (list.get(i).equals(mPlateTypeSource[3])) {
                mPlate_type_list.add(mPlateTypeValues[2]);
            } else if (list.get(i).equals(mPlateTypeSource[4])) {
                mPlate_type_list.add(mPlateTypeValues[3]);
            } else if (list.get(i).equals(mPlateTypeSource[5])) {
                mPlate_type_list.add(mPlateTypeValues[4]);
            } else if (list.get(i).equals(mPlateTypeSource[6])) {
                mPlate_type_list.add(mPlateTypeValues[5]);
            } else if (list.get(i).equals(mPlateTypeSource[7])) {
                mPlate_type_list.add(mPlateTypeValues[6]);
            } else if (list.get(i).equals(mPlateTypeSource[8])) {
                mPlate_type_list.add(mPlateTypeValues[7]);
            } else if (list.get(i).equals(mPlateTypeSource[9])) {
                mPlate_type_list.add(mPlateTypeValues[8]);
            } else if (list.get(i).equals(mPlateTypeSource[10])) {
                mPlate_type_list.add(mPlateTypeValues[9]);
            } else if (list.get(i).equals(mPlateTypeSource[11])) {
                mPlate_type_list.add(mPlateTypeValues[10]);
            } else if (list.get(i).equals(mPlateTypeSource[12])) {
                mPlate_type_list.add(mPlateTypeValues[11]);
            } else if (list.get(i).equals(mPlateTypeSource[13])) {
                mPlate_type_list.add(mPlateTypeValues[12]);
            } else if (list.get(i).equals(mPlateTypeSource[14])) {
                mPlate_type_list.add(mPlateTypeValues[13]);
            } else if (list.get(i).equals(mPlateTypeSource[15])) {
                mPlate_type_list.add(mPlateTypeValues[14]);
            } else if (list.get(i).equals(mPlateTypeSource[16])) {
                mPlate_type_list.add(mPlateTypeValues[15]);
            }
        }
        return mPlate_type_list;
    }
}
