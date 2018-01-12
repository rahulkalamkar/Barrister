package com.singular.barrister.Activity.SubActivity;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.singular.barrister.Adapter.HearingAdapter;
import com.singular.barrister.Database.DB.DatabaseHelper;
import com.singular.barrister.Database.Tables.CaseHearingListTable;
import com.singular.barrister.Database.Tables.StateTable;
import com.singular.barrister.Model.CaseHearingResponse;
import com.singular.barrister.Model.Cases.CaseHearing;
import com.singular.barrister.Model.Client.Client;
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.IDataChangeListener;
import com.singular.barrister.Util.IModel;
import com.singular.barrister.Util.NetworkConnection;
import com.singular.barrister.Util.WebServiceError;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HearingDateActivity extends AppCompatActivity implements IDataChangeListener<IModel> {
    RecyclerView mRecycleView;
    ProgressBar mProgressBar;
    RetrofitManager retrofitManager;
    ArrayList<CaseHearing> hearingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hearing_date);
        if (getActionBar() != null) {
            getActionBar().setTitle("Last Hearing Dates");
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            getSupportActionBar().setTitle("Last Hearing Dates");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mRecycleView = (RecyclerView) findViewById(R.id.recycleView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        hearingList = new ArrayList<CaseHearing>();
        getList();
    }

    public void getList() {
        mProgressBar.setVisibility(View.VISIBLE);
        List<CaseHearingListTable> caseHearingListTables = getLocalList(getIntent().getExtras().getString("Id"));
        if (caseHearingListTables.size() > 0) {
            convertList(caseHearingListTables);
        }

        if (new NetworkConnection(getApplicationContext()).isNetworkAvailable()) {
            retrofitManager = new RetrofitManager();
            retrofitManager.getCaseHearingList(this, new UserPreferance(getApplicationContext()).getToken(), getIntent().getExtras().getString("Id"));
        } else {
            //    Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDataChanged() {

    }

    public void insertData(ArrayList<CaseHearing> list) {
        for (int i = 0; i < list.size(); i++) {
            CaseHearing caseHearing = list.get(i);
            CaseHearingListTable caseHearingListTable = new CaseHearingListTable(caseHearing.getId(), caseHearing.getCase_id(), caseHearing.getCase_hearing_date(),
                    caseHearing.getCase_disposed(), caseHearing.getCase_decision(), caseHearing.getCase_notes());

            final Dao<CaseHearingListTable, Integer> caseHearingListTableIntegerDao;
            try {
                if (getHelper(getApplicationContext()) != null) {
                    caseHearingListTableIntegerDao = getHelper(getApplicationContext()).getCaseHearingListDao();
                    caseHearingListTableIntegerDao.create(caseHearingListTable);
                    Log.e("caseHearingListTable", "Value added ");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Log.e("caseHearingListTable", "Error " + e);
            }
        }
    }

    public List<CaseHearingListTable> getLocalList(String caseId) {
        List<CaseHearingListTable> list = new ArrayList<>();
        list.clear();
        final Dao<CaseHearingListTable, Integer> caseHearingListTableIntegerDao;
        try {
            caseHearingListTableIntegerDao = getHelper(getApplicationContext()).getCaseHearingListDao();
            list = caseHearingListTableIntegerDao.queryForEq("case_id", caseId);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("caseHearingListTable", "Error " + e);
        }
        return list;
    }

    public void convertList(List<CaseHearingListTable> list) {
        for (CaseHearingListTable caseHearingListTable : list) {
            CaseHearing caseHearing = new CaseHearing(caseHearingListTable.getHearing_id(),
                    caseHearingListTable.getCase_id(),
                    caseHearingListTable.getCase_hearing_date(),
                    caseHearingListTable.getCase_decision(),
                    caseHearingListTable.getCase_notes(),
                    caseHearingListTable.getCase_disposed());
            hearingList.add(caseHearing);
        }
        setHearingAdapter(reverse(hearingList));
        mProgressBar.setVisibility(View.GONE);
    }

    public static ArrayList<CaseHearing> reverse(final ArrayList<CaseHearing> list) {
        final int size = list.size();
        final int last = size - 1;

        // create a new list, with exactly enough initial capacity to hold the (reversed) list
        final ArrayList<CaseHearing> result = new ArrayList<>(size);

        // iterate through the list in reverse order and append to the result
        for (int i = last; i >= 0; --i) {
            final CaseHearing element = list.get(i);
            result.add(element);
        }

        // result now holds a reversed copy of the original list
        return result;
    }

    public void sort(ArrayList<CaseHearing> list) {
        Collections.sort(list, new Comparator<CaseHearing>() {

            public int compare(CaseHearing lhs, CaseHearing rhs) {

                try {
                    String[] split = lhs.getCase_hearing_date().split(" ");
                    SimpleDateFormat dateFormatlhs = new SimpleDateFormat("yyyy-MM-dd");
                    Date convertedDatelhs = dateFormatlhs.parse(split[0]);
                    Calendar calendarlhs = Calendar.getInstance();
                    calendarlhs.setTime(convertedDatelhs);

                    String[] split1 = rhs.getCase_hearing_date().split(" ");
                    SimpleDateFormat dateFormatrhs = new SimpleDateFormat("yyyy-MM-dd");
                    Date convertedDaterhs = dateFormatrhs.parse(split1[0]);
                    Calendar calendarrhs = Calendar.getInstance();
                    calendarrhs.setTime(convertedDaterhs);

                    if (calendarlhs.getTimeInMillis() > calendarrhs.getTimeInMillis()) {
                        return -1;
                    } else {
                        return 1;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }

    DatabaseHelper databaseHelper = null;

    // This is how, DatabaseHelper can be initialized for future use
    private DatabaseHelper getHelper(Context context) {
        if (context == null)
            return null;
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    public void releaseHelper() {
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    HearingAdapter hearingAdapter;

    @Override
    public void onDataReceived(IModel response) {
        if (response != null && response instanceof CaseHearingResponse) {
            CaseHearingResponse caseHearingResponse = (CaseHearingResponse) response;
            if (caseHearingResponse.getData().getHearing() != null) {
                hearingList.clear();
                hearingList.addAll(caseHearingResponse.getData().getHearing());
            }


            if (hearingList != null && hearingList.size() != 0) {
                insertData(hearingList);
            }
            if (hearingAdapter != null) {
                hearingAdapter.notifyDataSetChanged();
            } else {
                setHearingAdapter(reverse(hearingList));

            }
        } else {

        }
        mProgressBar.setVisibility(View.GONE);
    }


    public void setHearingAdapter(ArrayList<CaseHearing> list) {
        sort(list);
        hearingAdapter = new HearingAdapter(getApplicationContext(), list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.setAdapter(hearingAdapter);
    }

    @Override
    public void onDataFailed(WebServiceError error) {

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
