package com.singular.barrister.Activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.singular.barrister.Fragment.ImportantLinkFragment;
import com.singular.barrister.R;

public class ImportantLink extends AppCompatActivity {

    FloatingActionButton actionButton;
    private MenuItem btnEdit, btnSubmit;
    ImportantLinkFragment importantLinkFragment;
    ProgressBar mProgressBar;
    FrameLayout frameLayout;
    RecyclerView mRecycleView;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;

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
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar3);
        mRecycleView = (RecyclerView) findViewById(R.id.recycleViewImportantLink);
        actionButton = (FloatingActionButton) findViewById(R.id.fab);
        frameLayout = (FrameLayout) findViewById(R.id.fragmentContainer);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditScreen(true);
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
            mRecycleView.setVisibility(View.VISIBLE);
            fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack();
            frameLayout.setVisibility(View.GONE);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menuSubmit:
                btnSubmit.setVisible(false);
                btnEdit.setVisible(false);
                actionButton.setVisibility(View.VISIBLE);
                showEditScreen(false);
                break;
        }
        return true;
    }
}
