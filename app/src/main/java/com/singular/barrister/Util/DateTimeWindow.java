package com.singular.barrister.Util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.singular.barrister.Activity.SubActivity.CasesNewHearingActivity;
import com.singular.barrister.Interface.CaseListeners;
import com.singular.barrister.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by rahul.kalamkar on 12/12/2017.
 */

public class DateTimeWindow {
    String selectedDate, selectedTime;
    boolean isDateSelected = false;
    boolean showTimer = false;
    Activity activity;
    CaseListeners listeners;
    TimePickerDialog.OnTimeSetListener timeSetListener;
    com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener dateSetListener;

    public void showTimer(boolean showTimer) {
        this.showTimer = showTimer;
    }

    public DateTimeWindow(Activity activity, CaseListeners listeners) {
        this.activity = activity;
        this.listeners = listeners;
        initializeListeners();
        datePicker(activity);
    }

    public void initializeListeners() {
        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
                String minuteString = minute < 10 ? "0" + minute : "" + minute;
                String secondString = second < 10 ? "0" + second : "" + second;
                String time = "You picked the following time: " + hourString + "h" + minuteString + "m" + secondString + "s";
                selectedTime = hourString + ":" + minuteString + ":" + secondString;

                listeners.dateTime(selectedDate + " " + selectedTime);
            }
        };

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                selectedDate = year + "-" + (++monthOfYear) + "-" + dayOfMonth;
                if (!showTimer) {
                    timePicker(activity);
                } else {
                    listeners.dateTime(selectedDate);
                }
            }
        };
    }

    public void datePicker(Activity activity) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog datepickerdialog = DatePickerDialog.newInstance(
                dateSetListener,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        datepickerdialog.setThemeDark(false);
        datepickerdialog.vibrate(true);
        datepickerdialog.dismissOnPause(true);
        datepickerdialog.showYearPickerFirst(false);
        datepickerdialog.setAccentColor(activity.getResources().getColor(R.color.colorPrimary));
        datepickerdialog.setTitle("Select a date");
        datepickerdialog.show(activity.getFragmentManager(), "Datepickerdialog");
    }

    public void timePicker(Activity activity) {
        Calendar now = Calendar.getInstance();
        TimePickerDialog timepickerdialog = TimePickerDialog.newInstance(timeSetListener,
                now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true);
        timepickerdialog.setThemeDark(false);
        timepickerdialog.vibrate(false);
        timepickerdialog.dismissOnPause(false);
        timepickerdialog.enableSeconds(true);

        timepickerdialog.show(activity.getFragmentManager(), "Timepickerdialog");
    }
}
