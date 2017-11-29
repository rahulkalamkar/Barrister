package com.singular.barrister.Activity.SubActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.DatePicker;

import com.singular.barrister.R;

public class DateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);


        DatePicker datePicker=(DatePicker) findViewById(R.id.datePicker);
    }
}
