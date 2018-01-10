package com.singular.barrister.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.singular.barrister.Activity.LandingScreen;
import com.singular.barrister.Adapter.CasesListAdapter;
import com.singular.barrister.Adapter.TodaysCaseAdapter;
import com.singular.barrister.Adapter.TodaysNewsAdapter;
import com.singular.barrister.Custom.RecyclerViewPager;
import com.singular.barrister.Database.Tables.Case.CaseTable;
import com.singular.barrister.Database.Tables.Case.Query.CaseQuery;
import com.singular.barrister.Database.Tables.Today.Query.TodayCaseQuery;
import com.singular.barrister.Database.Tables.Today.TodayCaseTable;
import com.singular.barrister.Model.Cases.Case;
import com.singular.barrister.Model.News.News;
import com.singular.barrister.Model.News.NewsResponse;
import com.singular.barrister.Model.Today.TodayResponse;
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.IDataChangeListener;
import com.singular.barrister.Util.IModel;
import com.singular.barrister.Util.NetworkConnection;
import com.singular.barrister.Util.WebServiceError;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by rahul.kalamkar on 11/24/2017.
 */

public class TodaysFragment extends Fragment implements IDataChangeListener<IModel> {

    protected RecyclerViewPager mCustomRecyclerView, mCustomRecyclerViewNews;
    private ProgressBar progressBar;
    TextView errorTextView;
    LinearLayout ltTabs;
    Button btnToday, btnNews;
    FrameLayout frameLayout, frameLayoutNews;
    private RetrofitManager retrofitManager;
    private ArrayList<Case> caseList;
    private ArrayList<News> newsList;
    //   RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.today_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        frameLayout = (FrameLayout) getView().findViewById(R.id.frameLayout1);
        frameLayoutNews = (FrameLayout) getView().findViewById(R.id.frameLayout_News);
        mCustomRecyclerView = (RecyclerViewPager) getView().findViewById(R.id.viewpager);
        mCustomRecyclerViewNews = (RecyclerViewPager) getView().findViewById(R.id.viewpagerNews);

        initRecycleView();
        initRecycleViewNews();

        ltTabs = (LinearLayout) getView().findViewById(R.id.linearLayoutTodayTab);
        btnToday = (Button) getView().findViewById(R.id.buttonToday);
        btnNews = (Button) getView().findViewById(R.id.buttonNews);

        progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
        errorTextView = (TextView) getView().findViewById(R.id.textViewErrorText);
        retrofitManager = new RetrofitManager();
        caseList = new ArrayList<Case>();
        newsList = new ArrayList<News>();

        btnNews.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showNewsLayout(true);
                    }
                }
        );

        btnToday.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showNewsLayout(false);
                    }
                }
        );

        showNewsLayout(false);
    }

    public void getCasesList() {
        if (new NetworkConnection(getActivity()).isNetworkAvailable()) {
            if (getActivity() != null) {
                List<TodayCaseTable> list = new TodayCaseQuery(getActivity()).getList();
                if (list != null) {
                    caseList = (ArrayList<Case>) new TodayCaseQuery(getActivity()).convertListToOnLineList(list);
                    TodaysCaseAdapter todaysCaseAdapter = new TodaysCaseAdapter(getActivity(), caseList);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,
                            false);
                    mCustomRecyclerView.setLayoutManager(linearLayoutManager);
                    mCustomRecyclerView.setAdapter(todaysCaseAdapter);
                    frameLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
            if (caseList != null && caseList.size() == 0)
                progressBar.setVisibility(View.VISIBLE);
            retrofitManager.getTodayCases(this, new UserPreferance(getActivity()).getToken());
        } else {
            if (getActivity() != null) {
                List<TodayCaseTable> list = new TodayCaseQuery(getActivity()).getList();
                if (list != null && list.size() != 0) {
                    caseList = (ArrayList<Case>) new TodayCaseQuery(getActivity()).convertListToOnLineList(list);
                    TodaysCaseAdapter todaysCaseAdapter = new TodaysCaseAdapter(getActivity(), caseList);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,
                            false);
                    mCustomRecyclerView.setLayoutManager(linearLayoutManager);
                    mCustomRecyclerView.setAdapter(todaysCaseAdapter);
                    frameLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                } else {
                    showError();
                }
            }
        }
    }

    public void showNewsLayout(boolean show) {
        if (show) {
            HideError();
            btnToday.setBackgroundResource(R.drawable.today_buttpn);
            btnToday.setTextColor(getResources().getColor(R.color.colorAccent));
            btnNews.setBackgroundResource(R.drawable.today_right_corner);
            btnNews.setTextColor(getResources().getColor(R.color.switch_selected_text_color));

            getNewsList();
            frameLayoutNews.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
        } else {
            HideNewsError();
            btnToday.setBackgroundResource(R.drawable.today_buttpn_clicked);
            btnToday.setTextColor(getResources().getColor(R.color.switch_selected_text_color));
            btnNews.setBackgroundResource(R.drawable.today_right_corner_clicked);
            btnNews.setTextColor(getResources().getColor(R.color.colorAccent));
            getCasesList();
            frameLayoutNews.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
        }
    }

    public void getNewsList() {
        if (new NetworkConnection(getActivity()).isNetworkAvailable()) {
            progressBar.setVisibility(View.VISIBLE);
            retrofitManager.getAllNews(this);
        } else {
            Toast.makeText(getActivity(), "No internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void showError() {
        errorTextView.setText("There is no case for a day!");
        errorTextView.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    public void showNewsError() {
        errorTextView.setText("There is no news for a day!");
        errorTextView.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    public void HideError() {
        errorTextView.setText("There is no case for a day!");
        errorTextView.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void HideNewsError() {
        errorTextView.setText("There is no news for a day!");
        errorTextView.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDataChanged() {

    }

    public void initRecycleView() {
        mCustomRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                int childCount = mCustomRecyclerView.getChildCount();
                int width = mCustomRecyclerView.getChildAt(0).getWidth();
                int padding = (mCustomRecyclerView.getWidth() - width) / 2;

                for (int j = 0; j < childCount; j++) {
                    View v = recyclerView.getChildAt(j);
                    float rate = 0;
                    ;
                    if (v.getLeft() <= padding) {
                        if (v.getLeft() >= padding - v.getWidth()) {
                            rate = (padding - v.getLeft()) * 1f / v.getWidth();
                        } else {
                            rate = 1;
                        }
                        v.setScaleY(1 - rate * 0.1f);
                        v.setScaleX(1 - rate * 0.1f);

                    } else {
                        if (v.getLeft() <= recyclerView.getWidth() - padding) {
                            rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                        }
                        v.setScaleY(0.9f + rate * 0.1f);
                        v.setScaleX(0.9f + rate * 0.1f);
                    }
                }
            }
        });
        mCustomRecyclerView.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {
            @Override
            public void OnPageChanged(int oldPosition, int newPosition) {
                Log.d("test", "oldPosition:" + oldPosition + " newPosition:" + newPosition);
            }
        });

        mCustomRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (mCustomRecyclerView.getChildCount() < 3) {
                    if (mCustomRecyclerView.getChildAt(1) != null) {
                        if (mCustomRecyclerView.getCurrentPosition() == 0) {
                            View v1 = mCustomRecyclerView.getChildAt(1);
                            v1.setScaleY(0.9f);
                            v1.setScaleX(0.9f);
                        } else {
                            View v1 = mCustomRecyclerView.getChildAt(0);
                            v1.setScaleY(0.9f);
                            v1.setScaleX(0.9f);
                        }
                    }
                } else {
                    if (mCustomRecyclerView.getChildAt(0) != null) {
                        View v0 = mCustomRecyclerView.getChildAt(0);
                        v0.setScaleY(0.9f);
                        v0.setScaleX(0.9f);
                    }
                    if (mCustomRecyclerView.getChildAt(2) != null) {
                        View v2 = mCustomRecyclerView.getChildAt(2);
                        v2.setScaleY(0.9f);
                        v2.setScaleX(0.9f);
                    }
                }

            }
        });
    }

    public void initRecycleViewNews() {
        mCustomRecyclerViewNews.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                int childCount = mCustomRecyclerViewNews.getChildCount();
                int width = mCustomRecyclerViewNews.getChildAt(0).getWidth();
                int padding = (mCustomRecyclerViewNews.getWidth() - width) / 2;

                for (int j = 0; j < childCount; j++) {
                    View v = recyclerView.getChildAt(j);
                    float rate = 0;
                    ;
                    if (v.getLeft() <= padding) {
                        if (v.getLeft() >= padding - v.getWidth()) {
                            rate = (padding - v.getLeft()) * 1f / v.getWidth();
                        } else {
                            rate = 1;
                        }
                        v.setScaleY(1 - rate * 0.1f);
                        v.setScaleX(1 - rate * 0.1f);

                    } else {
                        if (v.getLeft() <= recyclerView.getWidth() - padding) {
                            rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                        }
                        v.setScaleY(0.9f + rate * 0.1f);
                        v.setScaleX(0.9f + rate * 0.1f);
                    }
                }
            }
        });
        mCustomRecyclerViewNews.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {
            @Override
            public void OnPageChanged(int oldPosition, int newPosition) {
                Log.d("test", "oldPosition:" + oldPosition + " newPosition:" + newPosition);
            }
        });

        mCustomRecyclerViewNews.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (mCustomRecyclerViewNews.getChildCount() < 3) {
                    if (mCustomRecyclerViewNews.getChildAt(1) != null) {
                        if (mCustomRecyclerViewNews.getCurrentPosition() == 0) {
                            View v1 = mCustomRecyclerViewNews.getChildAt(1);
                            v1.setScaleY(0.9f);
                            v1.setScaleX(0.9f);
                        } else {
                            View v1 = mCustomRecyclerViewNews.getChildAt(0);
                            v1.setScaleY(0.9f);
                            v1.setScaleX(0.9f);
                        }
                    }
                } else {
                    if (mCustomRecyclerViewNews.getChildAt(0) != null) {
                        View v0 = mCustomRecyclerViewNews.getChildAt(0);
                        v0.setScaleY(0.9f);
                        v0.setScaleX(0.9f);
                    }
                    if (mCustomRecyclerViewNews.getChildAt(2) != null) {
                        View v2 = mCustomRecyclerViewNews.getChildAt(2);
                        v2.setScaleY(0.9f);
                        v2.setScaleX(0.9f);
                    }
                }

            }
        });
    }

    @Override
    public void onDataReceived(IModel response) {
        if (response != null && response instanceof TodayResponse) {
            TodayResponse todayResponse = (TodayResponse) response;
            if (todayResponse.getData().getCaseList() != null && todayResponse.getData().getCaseList().size() != 0) {
                caseList.clear();
                caseList.addAll(todayResponse.getData().getCaseList());
                if (getActivity() != null) {
                    TodaysCaseAdapter todaysCaseAdapter = new TodaysCaseAdapter(getActivity(), caseList);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,
                            false);
                    mCustomRecyclerView.setLayoutManager(linearLayoutManager);
                    mCustomRecyclerView.setAdapter(todaysCaseAdapter);
                    frameLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    if (caseList != null && caseList.size() > 0) {
                        TodayCaseQuery caseQuery = new TodayCaseQuery(getActivity());
                        caseQuery.addList(caseList);
                    } else {
                        showError();
                    }
                } else {
                    showError();
                }
            } else if (todayResponse.getError() != null && todayResponse.getError().getStatus_code() == 401) {
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "Your session is Expired", Toast.LENGTH_SHORT).show();
                    new UserPreferance(getActivity()).logOut();
                    Intent intent = new Intent(getActivity(), LandingScreen.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            } else {
                TodayCaseQuery todayCaseQuery = new TodayCaseQuery(getActivity());
                todayCaseQuery.deleteAllTable();
                showError();
            }
        } else if (response != null && response instanceof NewsResponse) {
            NewsResponse newsResponse = (NewsResponse) response;
            newsList = new ArrayList<News>();
            newsList.clear();
            if (newsResponse.getArticles() != null && newsResponse.getArticles().size() > 0) {
                newsList.addAll(newsResponse.getArticles());
                TodaysNewsAdapter todaysNewsAdapter = new TodaysNewsAdapter(getActivity(), newsList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,
                        false);
                mCustomRecyclerViewNews.setLayoutManager(linearLayoutManager);
                mCustomRecyclerViewNews.setAdapter(todaysNewsAdapter);
                frameLayoutNews.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            } else {
                showNewsError();
            }
        } else {
            if (getActivity() != null) {
                Toast.makeText(getActivity(), "Your session is Expired", Toast.LENGTH_SHORT).show();
                new UserPreferance(getActivity()).logOut();
                Intent intent = new Intent(getActivity(), LandingScreen.class);
                startActivity(intent);
                getActivity().finish();
            }
        }
    }

    @Override
    public void onDataFailed(WebServiceError error) {

    }

    public void onSearch(String text) {
        Log.e("TodaysFragment", text);
    }
}
