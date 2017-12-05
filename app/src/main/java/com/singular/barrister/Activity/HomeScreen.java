package com.singular.barrister.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.singular.barrister.Activity.SubActivity.AddCaseActivity;
import com.singular.barrister.Activity.SubActivity.AddClientActivity;
import com.singular.barrister.Activity.SubActivity.AddCourtActivity;
import com.singular.barrister.Fragment.CasesFragment;
import com.singular.barrister.Fragment.ClientFragment;
import com.singular.barrister.Fragment.CourtFragment;
import com.singular.barrister.Fragment.TodaysFragment;
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.NetworkConnection;

public class HomeScreen extends AppCompatActivity {

    FrameLayout frameLayout;
    CourtFragment courtFragment;
    CasesFragment casesFragment;
    ClientFragment clientFragment;
    TodaysFragment todaysFragment;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progressBar4);
        frameLayout = (FrameLayout) findViewById(R.id.fragmentContainer);

        if (getActionBar() != null)
            getActionBar().setTitle(R.string.app_name);
        else if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.app_name);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (tabLayout.getSelectedTabPosition() == 0) {

                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    intent = new Intent(getApplicationContext(), AddCaseActivity.class);
                    startActivity(intent);
                } else if (tabLayout.getSelectedTabPosition() == 2) {
                    intent = new Intent(getApplicationContext(), AddCourtActivity.class);
                    startActivityForResult(intent, 2);
                } else {
                    intent = new Intent(getApplicationContext(), AddClientActivity.class);
                    startActivityForResult(intent, 1);
                }
            }
        });
        initializeTab();
        initializeFragments();
    }

    public void initializeFragments() {
        todaysFragment = new TodaysFragment();
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, todaysFragment, "Today's Fragment").commit();
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

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    todaysFragment = new TodaysFragment();
                    fragmentManager = getSupportFragmentManager();
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer, todaysFragment, "Today's Fragment").commit();
                } else if (tab.getPosition() == 1) {
                    casesFragment = new CasesFragment();
                    fragmentManager = getSupportFragmentManager();
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer, casesFragment, "Cases Fragment").commit();
                } else if (tab.getPosition() == 2) {
                    courtFragment = new CourtFragment();
                    fragmentManager = getSupportFragmentManager();
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer, courtFragment, "Court Fragment").commit();
                } else if (tab.getPosition() == 3) {
                    clientFragment = new ClientFragment();
                    fragmentManager = getSupportFragmentManager();
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer, clientFragment, "Client Fragment").commit();

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menuSearch).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchInFragment(newText);
                return false;
            }
        });
        return true;
    }

    public void searchInFragment(String text) {
        if (tabLayout.getSelectedTabPosition() == 0) {
            if (todaysFragment != null)
                todaysFragment.onSearch(text);
        } else if (tabLayout.getSelectedTabPosition() == 1) {
            if (casesFragment != null)
                casesFragment.onSearch(text);
        } else if (tabLayout.getSelectedTabPosition() == 2) {
            if (courtFragment != null)
                courtFragment.onSearch(text);
        } else if (tabLayout.getSelectedTabPosition() == 3) {
            if (clientFragment != null)
                clientFragment.onSearch(text);
        }
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

            case R.id.menuLogout:/*
                progressBar.setVisibility(View.VISIBLE);*/
                logOut();
                break;

            case R.id.menuLoveApp:
                intent = new Intent(HomeScreen.this, ReferralActivity.class);
                startActivity(intent);
                break;
        }

        return true;
    }

    public void logOut() {
        if (new NetworkConnection(getApplicationContext()).isNetworkAvailable()) {
            RetrofitManager retrofitManager = new RetrofitManager();
            retrofitManager.setLogOut(new UserPreferance(getApplicationContext()).getToken());
            new UserPreferance(getApplicationContext()).logOut();
            Intent intent = new Intent(HomeScreen.this, LandingScreen.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCode) {
            if (tabLayout.getSelectedTabPosition() == 0) {
                todaysFragment = new TodaysFragment();
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContainer, todaysFragment, "Today's Fragment").commit();
            } else if (tabLayout.getSelectedTabPosition() == 1) {
                casesFragment = new CasesFragment();
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContainer, casesFragment, "Cases Fragment").commit();
            } else if (tabLayout.getSelectedTabPosition() == 2) {
                courtFragment = new CourtFragment();
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContainer, courtFragment, "Court Fragment").commit();
            } else if (tabLayout.getSelectedTabPosition() == 3) {
                clientFragment = new ClientFragment();
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContainer, clientFragment, "Client Fragment").commit();
            }
        } else {

        }
    }
}
