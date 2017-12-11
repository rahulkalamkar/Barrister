package com.singular.barrister.Activity.ClientLogin;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.singular.barrister.Activity.ChangePassword;
import com.singular.barrister.Activity.ClientLogin.Fragment.ClientCaseFragment;
import com.singular.barrister.Activity.ClientLogin.Fragment.ClientTodayFragment;
import com.singular.barrister.Activity.ContactUsActivity;
import com.singular.barrister.Activity.HomeScreen;
import com.singular.barrister.Activity.ImportantLink;
import com.singular.barrister.Activity.LandingScreen;
import com.singular.barrister.Activity.ProfileActivity;
import com.singular.barrister.Activity.ReferralActivity;
import com.singular.barrister.Fragment.CasesFragment;
import com.singular.barrister.Fragment.ClientFragment;
import com.singular.barrister.Fragment.CourtFragment;
import com.singular.barrister.Fragment.TodaysFragment;
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.NetworkConnection;

import java.lang.reflect.Field;

public class ClientHomeScreen extends AppCompatActivity {

    FrameLayout frameLayout;
    ClientTodayFragment clientTodayFragment;
    ClientCaseFragment clientCaseFragment;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar4);
        frameLayout = (FrameLayout) findViewById(R.id.fragmentContainer);

        if (getActionBar() != null)
            getActionBar().setTitle(R.string.app_name);
        else if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.app_name);

        initializeTab();
    }

    private TabLayout tabLayout;

    private void initializeTab() {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabTextColors(Color.parseColor("#55FFFFFF"), Color.parseColor("#FFFFFF"));
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab1));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab2));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    clientTodayFragment = new ClientTodayFragment();
                    fragmentManager = getSupportFragmentManager();
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer, clientTodayFragment, "Today's Fragment").commit();
                } else if (tab.getPosition() == 1) {
                    clientCaseFragment = new ClientCaseFragment();
                    fragmentManager = getSupportFragmentManager();
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer, clientCaseFragment, "Cases Fragment").commit();
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
        inflater.inflate(R.menu.client_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menuSearch).getActionView();

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, R.drawable.cursor); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception e) {
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // searchInFragment(newText);
                return false;
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
                intent = new Intent(ClientHomeScreen.this, ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.menuContactUs:
                intent = new Intent(ClientHomeScreen.this, ContactUsActivity.class);
                startActivity(intent);
                break;

            case R.id.menuLogout:
                showConfirmationDialog();
                break;

            case R.id.menuLoveApp:
                intent = new Intent(ClientHomeScreen.this, ReferralActivity.class);
                startActivity(intent);
                break;
        }

        return true;
    }

    public void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ClientHomeScreen.this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logOut();
                        dialog.dismiss();
                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void logOut() {
        if (new NetworkConnection(getApplicationContext()).isNetworkAvailable()) {
            RetrofitManager retrofitManager = new RetrofitManager();
            retrofitManager.setLogOut(new UserPreferance(getApplicationContext()).getToken());
            new UserPreferance(getApplicationContext()).logOut();
            Intent intent = new Intent(ClientHomeScreen.this, LandingScreen.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }

}
