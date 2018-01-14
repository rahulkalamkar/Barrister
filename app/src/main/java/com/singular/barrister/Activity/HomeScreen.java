package com.singular.barrister.Activity;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.messaging.FirebaseMessaging;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.shahroz.svlibrary.interfaces.onSearchListener;
import com.shahroz.svlibrary.interfaces.onSimpleSearchActionsListener;
import com.shahroz.svlibrary.utils.Util;
import com.shahroz.svlibrary.widgets.MaterialSearchView;
import com.singular.barrister.Activity.SubActivity.AddCaseActivity;
import com.singular.barrister.Activity.SubActivity.AddClientActivity;
import com.singular.barrister.Activity.SubActivity.AddCourtActivity;
import com.singular.barrister.Database.DB.DatabaseHelper;
import com.singular.barrister.Database.Tables.Case.CaseTable;
import com.singular.barrister.Database.Tables.Case.Query.CaseQuery;
import com.singular.barrister.Database.Tables.Client.BaseClientTable;
import com.singular.barrister.Database.Tables.CourtTable;
import com.singular.barrister.Database.Tables.Today.Query.TodayCaseQuery;
import com.singular.barrister.Database.Tables.Today.TodayCaseTable;
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
import java.sql.SQLException;
import java.util.List;

public class HomeScreen extends AppCompatActivity implements onSimpleSearchActionsListener, onSearchListener {

    FrameLayout frameLayout;
    CourtFragment courtFragment;
    CasesFragment casesFragment;
    ClientFragment clientFragment;
    TodaysFragment todaysFragment;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    ProgressBar progressBar;
    FloatingActionButton fab;

    private boolean mSearchViewAdded = false;
    private MaterialSearchView mSearchView;
    private WindowManager mWindowManager;
    private MenuItem searchItem;
    private boolean searchActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        registerReceiver();
        progressBar = (ProgressBar) findViewById(R.id.progressBar4);
        frameLayout = (FrameLayout) findViewById(R.id.fragmentContainer);

        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mSearchView = new MaterialSearchView(this);
        mSearchView.setOnSearchListener(this);
        mSearchView.setSearchResultsListener(this);
        mSearchView.setHintText("Search");

        if (mToolbar != null) {
            // Delay adding SearchView until Toolbar has finished loading
            mToolbar.post(new Runnable() {
                @Override
                public void run() {
                    if (!mSearchViewAdded && mWindowManager != null) {
                        mWindowManager.addView(mSearchView,
                                MaterialSearchView.getSearchViewLayoutParams(HomeScreen.this));
                        mSearchViewAdded = true;
                    }
                }
            });
        }

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

        initializeNotification();
        initializeTab();
        initializeFragments();
    }

    public void openPlayStore() {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public void initializeFragments() {
        if (searchItem != null && searchItem.isVisible())
            searchItem.setVisible(false);
        hideSearchView();
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
                    if (searchItem != null && searchItem.isVisible())
                        searchItem.setVisible(false);
                    hideSearchView();
                    fab.setVisibility(View.GONE);
                    todaysFragment = new TodaysFragment();
                    fragmentManager = getSupportFragmentManager();
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer, todaysFragment, "Today's Fragment").commit();
                } else if (tab.getPosition() == 1) {
                    showSearch();
                    fab.setVisibility(View.VISIBLE);
                    casesFragment = new CasesFragment();
                    fragmentManager = getSupportFragmentManager();
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer, casesFragment, "Cases Fragment").commit();
                } else if (tab.getPosition() == 2) {
                    showSearch();
                    fab.setVisibility(View.VISIBLE);
                    courtFragment = new CourtFragment();
                    fragmentManager = getSupportFragmentManager();
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer, courtFragment, "Court Fragment").commit();
                } else if (tab.getPosition() == 3) {
                    showSearch();
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

        return true;
    }

    public void showSearch() {
        if (searchItem != null && !searchItem.isVisible()) {
            searchItem.setVisible(true);
        }
        hideSearchView();
    }

    public void hideSearchView() {
        if (mSearchView != null) {
            searchActive = false;
            mSearchView.hide();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        searchItem = (MenuItem) menu.findItem(R.id.menuSearch).getActionView();

        searchItem = menu.findItem(R.id.menuSearch);
        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mSearchView.display();
                openKeyboard();
                return true;
            }
        });
        if (searchActive)
            mSearchView.display();

        return super.onPrepareOptionsMenu(menu);
    }

    private void openKeyboard() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                mSearchView.getSearchView().dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                mSearchView.getSearchView().dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
            }
        }, 200);
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
            case R.id.menuRateApp:
                openPlayStore();
                break;

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

    public void deleteAllData() {
        // delete all  today's cases
        TodayCaseQuery todayCaseQuery = new TodayCaseQuery(getApplicationContext());
        todayCaseQuery.deleteAllTable();

        // delete all clients
        deleteClient();

        // delete all court
        deleteCourt();

        // delete all cases

        List<CaseTable> list = new CaseQuery(getApplicationContext()).getList();
        for (CaseTable caseTable : list) {
            deleteCase(caseTable);
        }
    }

    DatabaseHelper databaseHelper;

    private DatabaseHelper getHelper(Context context) {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    public void deleteCourt() {
        List<CourtTable> list = getAllCourt();
        for (CourtTable courtTable : list) {
            Dao<CourtTable, Integer> courtTableDao = null;
            try {
                if (getHelper(getApplicationContext()) != null)
                    courtTableDao = getHelper(getApplicationContext()).getCourtTableDao();
                courtTableDao.delete(courtTable);
            } catch (SQLException e) {
            }
        }
    }

    public void deleteCase(CaseTable caseTable) {
        Dao<CaseTable, Integer> caseTableIntegerDao;
        try {
            if (getHelper(getApplicationContext()) != null) {
                caseTableIntegerDao = getHelper(getApplicationContext()).getACaseTableDao();
                caseTableIntegerDao.delete(caseTable);
                Log.e("Case table", "deleted");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("Case table", "" + e.getMessage());
        }
    }

    public List<CourtTable> getAllCourt() {
        Dao<CourtTable, Integer> courtTableDao = null;
        try {
            if (getHelper(getApplicationContext()) != null)
                courtTableDao = getHelper(getApplicationContext()).getCourtTableDao();
            return courtTableDao.queryForAll();
        } catch (SQLException e) {
            return null;
        }
    }


    public void deleteClient() {

        List<BaseClientTable> list;
        list = getClientList();

        for (BaseClientTable baseClientTable : list) {
            deleteClient(baseClientTable);
        }

    }

    public List<BaseClientTable> getClientList() {
        Dao<BaseClientTable, Integer> baseClientTables = null;
        try {
            if (getHelper(getApplicationContext()) != null) {
                baseClientTables = getHelper(getApplicationContext()).getBaseClientTableDao();
            }
            return baseClientTables.queryForAll();
        } catch (SQLException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public void deleteClient(BaseClientTable baseClientTable) {
        Dao<BaseClientTable, Integer> baseClientTables;
        try {
            if (getHelper(getApplicationContext()) != null) {
                baseClientTables = getHelper(getApplicationContext()).getBaseClientTableDao();
                baseClientTables.delete(baseClientTable);
            }
            Log.e("BAseClient table", "deleted");
        } catch (Exception e) {
            Log.e("BAseClient table", "" + e);
        }
    }

    public void logOut() {
        if (new NetworkConnection(getApplicationContext()).isNetworkAvailable()) {

            new LogoutAsync().execute();

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
                if (searchItem != null && searchItem.isVisible())
                    searchItem.setVisible(false);
                hideSearchView();
                fab.setVisibility(View.GONE);
                todaysFragment = new TodaysFragment();
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContainer, todaysFragment, "Today's Fragment").commit();
            } else if (tabLayout.getSelectedTabPosition() == 1) {
                showSearch();
                fab.setVisibility(View.VISIBLE);
                casesFragment = new CasesFragment();
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContainer, casesFragment, "Cases Fragment").commit();
            } else if (tabLayout.getSelectedTabPosition() == 2) {
                showSearch();
                fab.setVisibility(View.VISIBLE);
                courtFragment = new CourtFragment();
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContainer, courtFragment, "Court Fragment").commit();
            } else if (tabLayout.getSelectedTabPosition() == 3) {
                showSearch();
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

    @Override
    public void onSearch(String query) {
        searchInFragment(query);
    }

    @Override
    public void searchViewOpened() {

    }

    @Override
    public void searchViewClosed() {

    }

    @Override
    public void onItemClicked(String item) {
    }

    @Override
    public void onCancelSearch() {
        searchActive = false;
        mSearchView.hide();
    }

    @Override
    public void onScroll() {

    }

    @Override
    public void error(String localizedMessage) {

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

    public class LogoutAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            deleteAllData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

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
            super.onPostExecute(aVoid);
        }
    }
}
