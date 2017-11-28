package com.singular.barrister.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.singular.barrister.Activity.LandingScreen;
import com.singular.barrister.Model.Cases.Case;
import com.singular.barrister.Model.Today.TodayResponse;
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.IDataChangeListener;
import com.singular.barrister.Util.IModel;
import com.singular.barrister.Util.NetworkConnection;
import com.singular.barrister.Util.WebServiceError;

import java.util.ArrayList;


/**
 * Created by rahul.kalamkar on 11/24/2017.
 */

public class TodaysFragment extends Fragment implements IDataChangeListener<IModel> {

    private ProgressBar progressBar;
    TextView errorTextView;
    private RetrofitManager retrofitManager;
    private ArrayList<Case> caseList;
    public static ViewPager mViewPager;
    public final static int LOOPS = 1;
    public CarouselPagerAdapter adapter;
    private FragmentActivity myContext;
    public static int count = 1;
    boolean isItemLoaded = false;
    //ViewPager items size
    /**
     * You shouldn't define first page = 0.
     * Let define firstpage = 'number viewpager size' to make endless carousel
     */
    public static int FIRST_PAGE = 0;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.today_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager = (ViewPager) getView().findViewById(R.id.viewPager);
        progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
        errorTextView = (TextView) getView().findViewById(R.id.textViewErrorText);
        retrofitManager = new RetrofitManager();
        caseList = new ArrayList<Case>();

        //set page margin between pages for viewpager
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int pageMargin = ((metrics.widthPixels / 4) * 2);
        mViewPager.setPageMargin(-pageMargin);
        if (!isItemLoaded) {
            getCasesList();
            caseList.clear();
        }
    }

    public void getCasesList() {
        if (new NetworkConnection(getActivity()).isNetworkAvailable()) {
            retrofitManager.getTodayCases(this, new UserPreferance(getActivity()).getToken());
        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }

    public void showError() {
        errorTextView.setText("There is no case for a day!");
        errorTextView.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void onDataReceived(IModel response) {
        if (response != null && response instanceof TodayResponse) {
            TodayResponse todayResponse = (TodayResponse) response;
            if (todayResponse.getData().getCaseList() != null) {
                caseList.addAll(todayResponse.getData().getCaseList());
                caseList.addAll(todayResponse.getData().getCaseList());
                caseList.addAll(todayResponse.getData().getCaseList());
                caseList.addAll(todayResponse.getData().getCaseList());
                caseList.addAll(todayResponse.getData().getCaseList());
                count = caseList.size();
                adapter = new CarouselPagerAdapter(getActivity(), myContext.getSupportFragmentManager(), caseList);
                mViewPager.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                isItemLoaded = true;
                mViewPager.addOnPageChangeListener(adapter);
                mViewPager.setCurrentItem(FIRST_PAGE);
                mViewPager.setOffscreenPageLimit(3);
                progressBar.setVisibility(View.GONE);
            }
            else if(todayResponse.getError()!=null && todayResponse.getError().getStatus_code() ==401)
            {
                Toast.makeText(getActivity(),"Your session is Expired",Toast.LENGTH_SHORT).show();
                new UserPreferance(getActivity()).logOut();
                Intent intent = new Intent(getActivity(), LandingScreen.class);
                startActivity(intent);
                getActivity().finish();
            }else
                showError();
        } else
        {Toast.makeText(getActivity(),"Your session is Expired",Toast.LENGTH_SHORT).show();
            new UserPreferance(getActivity()).logOut();
            Intent intent = new Intent(getActivity(), LandingScreen.class);
            startActivity(intent);
            getActivity().finish();}
    }

    @Override
    public void onDestroy() {
        caseList.clear();
        adapter = null;
        mViewPager = null;
        super.onDestroy();
    }

    @Override
    public void onDataFailed(WebServiceError error) {

    }
}
