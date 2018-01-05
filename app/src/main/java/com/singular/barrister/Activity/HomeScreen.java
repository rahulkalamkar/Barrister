package com.singular.barrister.Activity;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.messaging.FirebaseMessaging;
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
import com.singular.barrister.Util.FCM.Utils.Config;
import com.singular.barrister.Util.FCM.Utils.NotificationUtils;
import com.singular.barrister.Util.NetworkConnection;

import java.lang.reflect.Field;

public class HomeScreen extends AppCompatActivity {

    FrameLayout frameLayout;
    CourtFragment courtFragment;
    CasesFragment casesFragment;
    ClientFragment clientFragment;
    TodaysFragment todaysFragment;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    ProgressBar progressBar;
    private AdView mAdView;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        registerReceiver();
        progressBar = (ProgressBar) findViewById(R.id.progressBar4);
        frameLayout = (FrameLayout) findViewById(R.id.fragmentContainer);

        if (getActionBar() != null)
            getActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + getString(R.string.app_name) + "</font>"));
        else if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + getString(R.string.app_name) + "</font>"));

        fab = (FloatingActionButton) findViewById(R.id.fab);
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


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        initializeNotification();
        initializeTab();
        initializeFragments();
    }

    public void initializeFragments() {
        fab.setVisibility(View.GONE);
        todaysFragment = new TodaysFragment();
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, todaysFragment, "Today's Fragment").commit();
    }

    private TabLayout tabLayout;

    private void initializeTab() {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabTextColors(Color.parseColor("#55000000"), Color.parseColor("#000000"));
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#000000"));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab1));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab2));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab3));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab4));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    fab.setVisibility(View.GONE);
                    todaysFragment = new TodaysFragment();
                    fragmentManager = getSupportFragmentManager();
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer, todaysFragment, "Today's Fragment").commit();
                } else if (tab.getPosition() == 1) {
                    fab.setVisibility(View.VISIBLE);
                    casesFragment = new CasesFragment();
                    fragmentManager = getSupportFragmentManager();
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer, casesFragment, "Cases Fragment").commit();
                } else if (tab.getPosition() == 2) {
                    fab.setVisibility(View.VISIBLE);
                    courtFragment = new CourtFragment();
                    fragmentManager = getSupportFragmentManager();
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer, courtFragment, "Court Fragment").commit();
                } else if (tab.getPosition() == 3) {
                    fab.setVisibility(View.VISIBLE);
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
                showConfirmationDialog();
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
            try {
                getApplicationContext().deleteDatabase("barrister.db");
            } catch (Exception e) {

            }
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

    public void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCode) {
            if (tabLayout.getSelectedTabPosition() == 0) {
                fab.setVisibility(View.GONE);
                todaysFragment = new TodaysFragment();
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContainer, todaysFragment, "Today's Fragment").commit();
            } else if (tabLayout.getSelectedTabPosition() == 1) {
                fab.setVisibility(View.VISIBLE);
                casesFragment = new CasesFragment();
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContainer, casesFragment, "Cases Fragment").commit();
            } else if (tabLayout.getSelectedTabPosition() == 2) {
                fab.setVisibility(View.VISIBLE);
                courtFragment = new CourtFragment();
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContainer, courtFragment, "Court Fragment").commit();
            } else if (tabLayout.getSelectedTabPosition() == 3) {
                fab.setVisibility(View.VISIBLE);
                clientFragment = new ClientFragment();
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContainer, clientFragment, "Client Fragment").commit();
            }
        } else {

        }
    }

    MyReceiver receiver;

    public void registerReceiver() {
        IntentFilter filter = new IntentFilter("com.singular.barrister");
        receiver = new MyReceiver();
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
    }

    public class MyReceiver extends BroadcastReceiver {

        public MyReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            refresh();
        }
    }

    public void refresh() {
      /*  if (tabLayout.getSelectedTabPosition() == 0) {
        } else */
        if (tabLayout.getSelectedTabPosition() == 1) {
            casesFragment.refreshData();
        } else if (tabLayout.getSelectedTabPosition() == 2) {
            courtFragment.refreshData();
        } else if (tabLayout.getSelectedTabPosition() == 3) {
            clientFragment.refreshData();
        }
    }


    private BroadcastReceiver mRegistrationBroadcastReceiver;

    public void initializeNotification() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    /*txtMessage.setText(message);*/
                }
            }
        };

        displayFirebaseRegId();
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        if (!TextUtils.isEmpty(regId)) {
            RetrofitManager retrofitManager = new RetrofitManager();
            retrofitManager.updateToken(regId, new UserPreferance(this).getToken());
            Log.e("HomeScreen", "Firebase Reg Id: " + regId);
        } else
            Log.e("HomeScreen", "Firebase Reg Id is not received yet!");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
}
