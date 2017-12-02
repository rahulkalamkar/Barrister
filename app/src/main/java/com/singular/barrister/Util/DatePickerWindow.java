package com.singular.barrister.Util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.singular.barrister.Interface.CaseListeners;
import com.singular.barrister.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rahulbabanaraokalamkar on 11/29/17.
 */

public class DatePickerWindow {

    String selectedDate, selectedTime;
    boolean isDateSelected = false;

    public DatePickerWindow(final Context context, View view, final CaseListeners listeners) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.date_picker_layout, null);

        final PopupWindow DateWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        if (Build.VERSION.SDK_INT >= 21) {
            DateWindow.setElevation(5.0f);
        }

        final DatePicker datePicker = (DatePicker) customView.findViewById(R.id.datePicker);

        final TimePicker timePicker = (TimePicker) customView.findViewById(R.id.timePicker);
        TextView btnSave = (TextView) customView.findViewById(R.id.textViewSave);
        TextView btnCancel = (TextView) customView.findViewById(R.id.textViewCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateWindow.dismiss();
            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                selectedTime = hour + ":" + minute;
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isDateSelected) {
                    isDateSelected = true;
                    int day = datePicker.getDayOfMonth();
                    int month = datePicker.getMonth() + 1;
                    int year = datePicker.getYear();

                    selectedDate = year + "-" + month + "-" + day;

                    Log.e("Date", selectedDate);
                    datePicker.setVisibility(View.GONE);
                    timePicker.setVisibility(View.VISIBLE);
                } else if (selectedTime == null) {
                    datePicker.setVisibility(View.VISIBLE);
                    timePicker.setVisibility(View.GONE);
                    Date currentTime = Calendar.getInstance().getTime();
                    DateFormat df = new SimpleDateFormat("HH:mm");
                    String date = df.format(Calendar.getInstance().getTime());

                    listeners.dateTime(selectedDate + " " + date);
                    DateWindow.dismiss();
                } else {

                    datePicker.setVisibility(View.VISIBLE);
                    timePicker.setVisibility(View.GONE);

                    listeners.dateTime(selectedDate + " " + selectedTime);
                    DateWindow.dismiss();
                }
            }
        });

        DateWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
}
