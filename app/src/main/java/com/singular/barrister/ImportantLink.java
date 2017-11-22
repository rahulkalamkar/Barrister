package com.singular.barrister;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class ImportantLink extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_important_link);
        if (getActionBar() != null) {
            getActionBar().setTitle(getResources().getString(R.string.menu_important_link));
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.menu_important_link));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
