package com.singular.barrister.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.singular.barrister.Activity.LandingScreen;
import com.singular.barrister.Adapter.CasesListAdapter;
import com.singular.barrister.Adapter.CourtListAdapter;
import com.singular.barrister.Database.Tables.Case.CaseTable;
import com.singular.barrister.Database.Tables.Case.Query.CaseQuery;
import com.singular.barrister.DisplayCaseActivity;
import com.singular.barrister.Interface.RecycleItemCase;
import com.singular.barrister.Model.Cases.Case;
import com.singular.barrister.Model.Cases.CasesData;
import com.singular.barrister.Model.Cases.CasesResponse;
import com.singular.barrister.Model.Client.Client;
import com.singular.barrister.Model.Court.CourtData;
import com.singular.barrister.Model.Court.CourtResponse;
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.IDataChangeListener;
import com.singular.barrister.Util.IModel;
import com.singular.barrister.Util.NetworkConnection;
import com.singular.barrister.Util.WebServiceError;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahulbabanaraokalamkar on 11/23/17.
 */

public class CasesFragment extends Fragment implements IDataChangeListener<IModel>, RecycleItemCase {

    private RecyclerView mRecycleView;
    private ProgressBar progressBar;
    TextView errorTextView;
    private RetrofitManager retrofitManager;
    private ArrayList<Case> caseList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_court, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecycleView = (RecyclerView) getView().findViewById(R.id.courtRecycleView);
        progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
        errorTextView = (TextView) getView().findViewById(R.id.textViewErrorText);
        retrofitManager = new RetrofitManager();
        caseList = new ArrayList<Case>();
        getCasesList();

        registerForContextMenu(mRecycleView);
    }

    public void refreshData() {
        try {
            if (caseList == null)
                caseList = new ArrayList<Case>();
            caseList.clear();
            if (new NetworkConnection(getActivity()).isNetworkAvailable()) {
                progressBar.setVisibility(View.VISIBLE);
                retrofitManager.getCasesList(this, new UserPreferance(getActivity()).getToken());
            } else {
                if (getActivity() != null) {
                    List<CaseTable> list = new CaseQuery(getActivity()).getList();
                    if (list != null) {
                        caseList = (ArrayList<Case>) new CaseQuery(getActivity()).convertListToOnLineList(list);
                        courtListAdapter = new CasesListAdapter(getActivity(), caseList, this);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        mRecycleView.setLayoutManager(linearLayoutManager);
                        mRecycleView.setAdapter(courtListAdapter);
                        progressBar.setVisibility(View.GONE);
                    }
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {

        }
    }

    public void getCasesList() {
        if (new NetworkConnection(getActivity()).isNetworkAvailable()) {
            List<CaseTable> list = new CaseQuery(getActivity()).getList();
            if (list != null) {
                caseList = (ArrayList<Case>) new CaseQuery(getActivity()).convertListToOnLineList(list);
                courtListAdapter = new CasesListAdapter(getActivity(), caseList, this);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                mRecycleView.setLayoutManager(linearLayoutManager);
                mRecycleView.setAdapter(courtListAdapter);
                progressBar.setVisibility(View.GONE);
            }
            if (caseList != null && caseList.size() == 0)
                progressBar.setVisibility(View.VISIBLE);
            retrofitManager.getCasesList(this, new UserPreferance(getActivity()).getToken());
        } else {
            if (getActivity() != null) {
                List<CaseTable> list = new CaseQuery(getActivity()).getList();
                if (list != null) {
                    caseList = (ArrayList<Case>) new CaseQuery(getActivity()).convertListToOnLineList(list);
                    courtListAdapter = new CasesListAdapter(getActivity(), caseList, this);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    mRecycleView.setLayoutManager(linearLayoutManager);
                    mRecycleView.setAdapter(courtListAdapter);
                    progressBar.setVisibility(View.GONE);
                }
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showError() {
        errorTextView.setText("There is no case added yet!");
        errorTextView.setVisibility(View.VISIBLE);
        mRecycleView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void onDataFailed(WebServiceError error) {

    }

    CasesListAdapter courtListAdapter;

    @Override
    public void onDataReceived(IModel response) {
        if (response != null && response instanceof CasesResponse) {
            CasesResponse casesResponse = (CasesResponse) response;
            if (casesResponse.getData().getCaseList() != null) {
                caseList.clear();
                caseList.addAll(casesResponse.getData().getCaseList());
                if (courtListAdapter == null) {
                    if (getActivity() != null) {
                        courtListAdapter = new CasesListAdapter(getActivity(), caseList, this);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        mRecycleView.setLayoutManager(linearLayoutManager);
                        mRecycleView.setAdapter(courtListAdapter);
                        progressBar.setVisibility(View.GONE);
                    }
                } else
                    courtListAdapter.notifyDataSetChanged();
                if (caseList != null && caseList.size() > 0 && getActivity() != null) {
                    CaseQuery caseQuery = new CaseQuery(getActivity());
                    caseQuery.addList(caseList);
                } else {
                    showError();
                }
            } else if (casesResponse.getError() != null && casesResponse.getError().getStatus_code() == 401) {
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "Your session is Expired", Toast.LENGTH_SHORT).show();
                    new UserPreferance(getActivity()).logOut();
                    Intent intent = new Intent(getActivity(), LandingScreen.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            } else
                showError();
        } else {
            if (getActivity() != null) {
                Toast.makeText(getActivity(), "Your session is Expired", Toast.LENGTH_SHORT).show();
                new UserPreferance(getActivity()).logOut();
                Intent intent = new Intent(getActivity(), LandingScreen.class);
                startActivity(intent);
                getActivity().finish();
            }
        }
        progressBar.setVisibility(View.GONE);
    }

    public void onSearch(String text) {
        if (courtListAdapter != null) {
            courtListAdapter.getFilter().filter(text);
        }
    }

    @Override
    public void onItemClick(Case aCase) {
        if (getActivity() != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("Case", aCase);
            Intent intent = new Intent(getActivity(), DisplayCaseActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public Case selectedItem;

    @Override
    public void onItemLongClick(Case aCase) {
        selectedItem = aCase;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Delete");
        menu.add(0, v.getId(), 0, "Call");
        menu.add(0, v.getId(), 0, "SMS");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Call") {
            String phone = "";
            phone = selectedItem.getClient().getCountry_code() + "" + selectedItem.getClient().getMobile();
            if (!TextUtils.isEmpty(phone)) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        } else if (item.getTitle() == "SMS") {
            String phone = "";
            phone = selectedItem.getClient().getCountry_code() + "" + selectedItem.getClient().getMobile();
            sendSMS(phone);
        } else if (item.getTitle() == "Delete") {
            if (getActivity() != null) {
                retrofitManager.deleteCase(new UserPreferance(getActivity()).getToken(), selectedItem.getId());
                caseList.remove(selectedItem);
                courtListAdapter.notifyDataSetChanged();
            }
        } else {
            return false;
        }
        return true;
    }

    public void sendSMS(String number) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least KitKat
        {
            try {

                String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(getActivity()); // Need to change the build to API 19

                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + Uri.encode(number)));
                sendIntent.setType("text/plain");

                if (defaultSmsPackageName != null)// Can be null in case that there is no default, then the user would be able to choose
                // any app that support this intent.
                {
                    sendIntent.setPackage(defaultSmsPackageName);
                }
                startActivity(sendIntent);

            } catch (Exception e) {
                String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(getActivity()); // Need to change the build to API 19

                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");

                if (defaultSmsPackageName != null)// Can be null in case that there is no default, then the user would be able to choose
                // any app that support this intent.
                {
                    sendIntent.setPackage(defaultSmsPackageName);
                }
                startActivity(sendIntent);
            }
        } else // For early versions, do what worked for you before.
        {
            Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address", number);
            startActivity(smsIntent);
        }
    }
}
