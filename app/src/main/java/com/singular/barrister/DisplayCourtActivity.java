package com.singular.barrister;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.singular.barrister.Model.Court.CourtData;

import org.w3c.dom.Text;

public class DisplayCourtActivity extends AppCompatActivity {

    TextView txtName,txtNumber,txtType,txtSubDistrict,txtDistrict,txtState;
    CourtData aCourtData;
    private MenuItem btnEdit, btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_court);

        if (getActionBar() != null) {
            getActionBar().setTitle("Court detail");
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            getSupportActionBar().setTitle("Court detail");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        aCourtData=(CourtData) getIntent().getExtras().getSerializable("Court");
        txtName=(TextView)findViewById(R.id.textViewCourtName);
        txtNumber=(TextView)findViewById(R.id.textViewCourtNumber);
        txtType=(TextView)findViewById(R.id.textViewCourtType);
        txtSubDistrict=(TextView)findViewById(R.id.textViewCourtSubDistrict);
        txtDistrict=(TextView)findViewById(R.id.textViewCourtDistrict);
        txtState=(TextView)findViewById(R.id.textViewCourtState);
        setData();
    }

    public void setData()
    {
        txtName.setText(aCourtData.getCourt_name());
        txtNumber.setText(aCourtData.getCourt_number());
        txtType.setText(aCourtData.getCourt_type());
        txtSubDistrict.setText(getSubDistrict());
        txtDistrict.setText(getDistrict());
        txtState.setText(getState());
    }

    public String getState()
    {
        if(aCourtData.getState() !=null && aCourtData.getState().getName()!=null)
        {
            return aCourtData.getState().getName();
        }
        else
            return "";
    }

    public String getDistrict()
    {
        if(aCourtData.getDistrict() !=null && aCourtData.getDistrict().getName()!=null)
        {
            return aCourtData.getDistrict().getName();
        }
        else
            return "";
    }

    public String getSubDistrict()
    {
        if(aCourtData.getSubdistrict() !=null && aCourtData.getSubdistrict().getName()!=null)
        {
            return aCourtData.getSubdistrict().getName();
        }
        else
            return "";
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.simple_done, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        btnEdit = menu.findItem(R.id.menuEdit);
        btnSubmit = menu.findItem(R.id.menuSubmit);

        btnSubmit.setVisible(false);
        btnEdit.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }
}
