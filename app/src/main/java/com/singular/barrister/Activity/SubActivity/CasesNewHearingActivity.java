package com.singular.barrister.Activity.SubActivity;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.singular.barrister.R;

import java.util.Calendar;

public class CasesNewHearingActivity extends AppCompatActivity {

    EditText edtDate,edtCaseNotes;
    ProgressBar mProgressBar;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cases_sub);
        if (getActionBar() != null) {
            getActionBar().setTitle("New hearing date");
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("New hearing date");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mProgressBar=(ProgressBar)findViewById(R.id.progressBar);
        edtDate=(EditText)findViewById(R.id.editTextDate);
        edtCaseNotes=(EditText)findViewById(R.id.editTextCaseNotes);

        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void showDatePicker()
    {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

    }

    public void showTimePicker(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.simple_done, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId())
       {
           case android.R.id.home:
               finish();
               break;
           case R.id.menuSubmit:
               break;
       }
        return true;
    }
}
