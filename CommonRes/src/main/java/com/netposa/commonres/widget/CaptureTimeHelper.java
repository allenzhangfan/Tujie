package com.netposa.commonres.widget;

import com.netposa.common.log.Log;
import com.netposa.commonres.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

/**
 * Author：yeguoqiang
 * Created time：2018/11/21 10:29
 */
public class CaptureTimeHelper {

    private static final String TAG = CaptureTimeHelper.class.getSimpleName();
    public static final String FORMAT_PATTERN = "yyyy.MM.dd HH:mm:ss";
    public static final String FORMAT_PATTERN2 = "yyyy-MM-dd HH:mm:ss";
    public static final int START_TIME = 0x0;
    public static final int END_TIME = 0x1;
    private Calendar mStartCalendar;
    private Calendar mEndCalender;
    private FragmentManager mFm;
    private Listener mListener;

    public CaptureTimeHelper(FragmentManager fm) {
        mFm = fm;
    }

    public interface Listener {
        void setCaptureTime(long time, int selectTag);
    }

    /**
     * 设置必须的控件和参数
     *
     * @param startCalendar 开始时间
     * @param endCalender   结束时间
     */
    public void init(@NonNull Calendar startCalendar, @NonNull Calendar endCalender,Listener listener) {
        mStartCalendar = startCalendar;
        mEndCalender = endCalender;
        mListener = listener;
    }

    /**
     * 显示开始时间的选择框
     */
    public void showStartDateDialog() {
        DatePickerDialog dpd = DatePickerDialog.newInstance((view, year, monthOfYear, dayOfMonth) -> {
            mStartCalendar.set(Calendar.YEAR, year);
            mStartCalendar.set(Calendar.MONTH, monthOfYear);
            mStartCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Log.i(TAG, "showStartDateDialog: " + year + "/" + monthOfYear + "/" + dayOfMonth);
            showStartTimeDialog();
        }, mStartCalendar);
        dpd.setStyle(DialogFragment.STYLE_NORMAL, R.style.DateTimeDialog);
        dpd.show(mFm, "showStartDateDialog");
    }

    private void showStartTimeDialog() {
        TimePickerDialog tpd = TimePickerDialog.newInstance((view, hourOfDay, minute, second) -> {
                    mStartCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    mStartCalendar.set(Calendar.MINUTE, minute);
                    mStartCalendar.set(Calendar.SECOND, second);
                    Log.i(TAG, "showStartTimeDialog: " + hourOfDay + "/" + minute + "/" + second);
                    mListener.setCaptureTime(mStartCalendar.getTimeInMillis(),START_TIME);
                },
                mStartCalendar.get(Calendar.HOUR_OF_DAY),
                mStartCalendar.get(Calendar.MINUTE),
                mStartCalendar.get(Calendar.SECOND), true);
        tpd.enableSeconds(true);
        tpd.setStyle(DialogFragment.STYLE_NORMAL, R.style.DateTimeDialog);
        tpd.show(mFm, "showStartTimeDialog");
    }

    /**
     * 显示结束时间的选择框
     */
    public void showEndDateDialog() {
        DatePickerDialog dpd = DatePickerDialog.newInstance((view, year, monthOfYear, dayOfMonth) -> {
            mEndCalender.set(Calendar.YEAR, year);
            mEndCalender.set(Calendar.MONTH, monthOfYear);
            mEndCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Log.i(TAG, "showEndDateDialog: " + year + "/" + monthOfYear + "/" + dayOfMonth);
            showEndTimeDialog();
        }, mEndCalender);
        dpd.setStyle(DialogFragment.STYLE_NORMAL, R.style.DateTimeDialog);
        dpd.show(mFm, "showEndDateDialog");
    }

    private void showEndTimeDialog() {
        TimePickerDialog tpd = TimePickerDialog.newInstance((view, hourOfDay, minute, second) -> {
                    mEndCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    mEndCalender.set(Calendar.MINUTE, minute);
                    mEndCalender.set(Calendar.SECOND, second);
                    Log.i(TAG, "showEndTimeDialog: " + hourOfDay + "/" + minute + "/" + second);
                    mListener.setCaptureTime(mEndCalender.getTimeInMillis(),END_TIME);
                },
                mEndCalender.get(Calendar.HOUR_OF_DAY),
                mEndCalender.get(Calendar.MINUTE),
                mEndCalender.get(Calendar.SECOND), true);
        tpd.enableSeconds(true);
        tpd.setStyle(DialogFragment.STYLE_NORMAL, R.style.DateTimeDialog);
        tpd.show(mFm, "showEndTimeDialog");
    }
}
