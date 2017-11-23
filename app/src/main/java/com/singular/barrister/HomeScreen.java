package com.singular.barrister;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import com.singular.barrister.Preferance.UserPreferance;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getActionBar() != null)
            getActionBar().setTitle(R.string.app_name);
        else if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.app_name);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
        initializeTab();
    }

    private TabLayout tabLayout;

    private void initializeTab() {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabTextColors(Color.parseColor("#55FFFFFF"), Color.parseColor("#FFFFFF"));
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab1));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab2));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab3));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab4));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menuSearch).getActionView();

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String text = "";

        Intent intent;
        switch (id) {
            case R.id.menuProfile:
                intent = new Intent(HomeScreen.this, ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.menuChangePassword:
                intent = new Intent(HomeScreen.this, ChangePassword.class);
                startActivity(intent);
                break;

            case R.id.menuContactUs:
                intent = new Intent(HomeScreen.this, ContactUsActivity.class);
                startActivity(intent);
                break;

            case R.id.menuImportantWebSite:
                intent = new Intent(HomeScreen.this, ImportantLink.class);
                startActivity(intent);
                break;

            case R.id.menuLogout:
                new UserPreferance(getApplicationContext()).logOut();
                intent = new Intent(HomeScreen.this, LandingScreen.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menuLoveApp:
                intent = new Intent(HomeScreen.this, ReferralActivity.class);
                startActivity(intent);
                break;
        }

        return true;
    }
}
