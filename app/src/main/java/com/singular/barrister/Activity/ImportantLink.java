package com.singular.barrister.Activity;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.singular.barrister.Adapter.ImportantListAdapter;
import com.singular.barrister.Database.DB.DatabaseHelper;
import com.singular.barrister.Database.Tables.CourtTable;
import com.singular.barrister.Fragment.ImportantLinkFragment;
import com.singular.barrister.R;

import java.sql.SQLException;
import java.util.List;

public class ImportantLink extends AppCompatActivity {

    FloatingActionButton actionButton;
    private MenuItem btnEdit, btnSubmit;
    ImportantLinkFragment importantLinkFragment;
    ProgressBar mProgressBar;
    FrameLayout frameLayout;
    RecyclerView mRecycleView;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    List<com.singular.barrister.Database.Tables.WebSite.ImportantLink> linkList;
    List<com.singular.barrister.Database.Tables.WebSite.ImportantLink> tempLinkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_important_link);
        if (getActionBar() != null) {
            getActionBar().setTitle("Important Websites");
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Important Websites");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar3);
        mRecycleView = (RecyclerView) findViewById(R.id.recycleViewImportantLink);
        actionButton = (FloatingActionButton) findViewById(R.id.fab);
        frameLayout = (FrameLayout) findViewById(R.id.fragmentContainer);

        linkList = getList();
        if (linkList != null) {
            importantListAdapter = new ImportantListAdapter(getApplicationContext(), linkList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRecycleView.setLayoutManager(linearLayoutManager);
            mRecycleView.setAdapter(importantListAdapter);
        }

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEditing = true;
                showEditScreen(true);
                btnEdit.setVisible(false);
                actionButton.setVisibility(View.GONE);
                btnSubmit.setVisible(true);
            }
        });
    }

    public void showEditScreen(boolean show) {
        if (show) {
            mRecycleView.setVisibility(View.GONE);
            importantLinkFragment = new ImportantLinkFragment();
            fragmentManager = getSupportFragmentManager();
            transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragmentContainer, importantLinkFragment, "Add link").commit();
            frameLayout.setVisibility(View.VISIBLE);
        } else {
            saveData();
        }
    }

    public void saveData() {
        if (importantLinkFragment.checkValues()) {
            String webName = importantLinkFragment.edtWebName.getText().toString();
            String webUrl = importantLinkFragment.edtWebSite.getText().toString();

            importantLinkFragment.edtWebName.setText("");
            importantLinkFragment.edtWebSite.setText("");

            com.singular.barrister.Database.Tables.WebSite.ImportantLink importantLink = new com.singular.barrister.Database.Tables.WebSite.ImportantLink(webName, webUrl);

            Dao<com.singular.barrister.Database.Tables.WebSite.ImportantLink, Integer> importantLinkIntegerDao;
            try {
                importantLinkIntegerDao = getHelper(getApplicationContext()).getImportantWebDao();
                importantLinkIntegerDao.create(importantLink);
                Log.e("Important link table", "inserted");
            } catch (SQLException e) {
                Log.e("Important link table", "" + e);
            }

            mRecycleView.setVisibility(View.VISIBLE);
            fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack();
            frameLayout.setVisibility(View.GONE);
            fetchList();
        }
    }

    public List<com.singular.barrister.Database.Tables.WebSite.ImportantLink> getList() {
        Dao<com.singular.barrister.Database.Tables.WebSite.ImportantLink, Integer> importantLinkIntegerDao;
        List<com.singular.barrister.Database.Tables.WebSite.ImportantLink> list = null;

        try {
            importantLinkIntegerDao = getHelper(getApplicationContext()).getImportantWebDao();
            list = importantLinkIntegerDao.queryForAll();
            Log.e("Important link table", "" + list.size());
        } catch (Exception e) {
            Log.e("Important link table", "" + e);
        }
        return list;
    }

    DatabaseHelper databaseHelper;

    private DatabaseHelper getHelper(Context context) {
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
        btnEdit.setVisible(false);
        return true;
    }

    boolean isEditing = false;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (isEditing) {
                    btnSubmit.setVisible(false);
                    btnEdit.setVisible(true);
                    actionButton.setVisibility(View.VISIBLE);
                    mRecycleView.setVisibility(View.VISIBLE);
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.popBackStack();
                    frameLayout.setVisibility(View.GONE);
                    fetchList();
                    isEditing = false;
                } else {
                    finish();
                }
                break;
            case R.id.menuSubmit:
                isEditing = false;
                btnSubmit.setVisible(false);
                btnEdit.setVisible(false);
                actionButton.setVisibility(View.VISIBLE);
                showEditScreen(false);
                break;
        }
        return true;
    }

    ImportantListAdapter importantListAdapter;

    public void fetchList() {
        if (linkList != null)
            linkList.clear();
        tempLinkList = getList();
        linkList.addAll(tempLinkList);
        if (linkList != null) {
         /*   if (importantListAdapter != null)
                importantListAdapter.notifyDataSetChanged();
            else {*/
            importantListAdapter = new ImportantListAdapter(getApplicationContext(), linkList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRecycleView.setLayoutManager(linearLayoutManager);
            mRecycleView.setAdapter(importantListAdapter);
            //   }
            mRecycleView.setVisibility(View.VISIBLE);
        }

    }

}
