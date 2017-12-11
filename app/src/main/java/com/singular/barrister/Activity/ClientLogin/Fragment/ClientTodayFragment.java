package com.singular.barrister.Activity.ClientLogin.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.singular.barrister.Activity.LandingScreen;
import com.singular.barrister.Adapter.TodaysCaseAdapter;
import com.singular.barrister.Database.Tables.Client.BaseClientTable;
import com.singular.barrister.Database.Tables.Today.Query.TodayCaseQuery;
import com.singular.barrister.Model.Cases.Case;
import com.singular.barrister.Model.Client.Client;
import com.singular.barrister.Model.Court.CourtData;
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
 * Created by rahul.kalamkar on 12/11/2017.
 */

public class ClientTodayFragment extends Fragment implements IDataChangeListener<IModel> {

    private RecyclerView mRecycleView;
    private ProgressBar progressBar;
    TextView errorTextView;
    private RetrofitManager retrofitManager;
    ArrayList<Case> caseList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_court, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecycleView = getView().findViewById(R.id.courtRecycleView);
        progressBar = getView().findViewById(R.id.progressBar);
        errorTextView = getView().findViewById(R.id.textViewErrorText);
        retrofitManager = new RetrofitManager();
        caseList = new ArrayList<Case>();
        getCaseList();
    }

    public void getCaseList() {
        if (new NetworkConnection(getActivity()).isNetworkAvailable()) {
            progressBar.setVisibility(View.VISIBLE);
            retrofitManager.getTodayCases(this, new UserPreferance(getActivity()).getToken());
        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
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
                ListAdapter todaysCaseAdapter = new ListAdapter(getActivity(), caseList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                mRecycleView.setLayoutManager(linearLayoutManager);
                mRecycleView.setAdapter(todaysCaseAdapter);
                progressBar.setVisibility(View.GONE);
                if (caseList != null && caseList.size() > 0) {
                    TodayCaseQuery caseQuery = new TodayCaseQuery(getActivity());
                    caseQuery.addList(caseList);
                } else {
                    showError();
                }
            } else if (todayResponse.getError() != null && todayResponse.getError().getStatus_code() == 401) {
                Toast.makeText(getActivity(), "Your session is Expired", Toast.LENGTH_SHORT).show();
                new UserPreferance(getActivity()).logOut();
                Intent intent = new Intent(getActivity(), LandingScreen.class);
                startActivity(intent);
                getActivity().finish();
            } else
                showError();
        } else {
            showError();
        }
    }

    public void showError() {
        errorTextView.setText("There is no case for a day!");
        errorTextView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDataFailed(WebServiceError error) {

    }

    public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context context;
        ArrayList<Case> caseList;

        public ListAdapter(Context context, ArrayList<Case> caseList) {
            this.context = context;
            this.caseList = caseList;
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.today_case_item, parent, false);
            return new TodayViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof TodayViewHolder) {
                TodayViewHolder todayViewHolder = (TodayViewHolder) holder;

                if (caseList.get(position).getClient() != null) {
                    if (caseList.get(position).getPersons().get(0).getType().equalsIgnoreCase("Client")) {
                        todayViewHolder.txtClientOne.setText(caseList.get(position).getClient().getFirst_name() + " " + caseList.get(position).getClient()
                                .getLast_name() + " VS " + caseList.get(position).getPersons().get(0).getOpp_name());

                    } else {
                        todayViewHolder.txtClientOne.setText(caseList.get(position).getClient().getFirst_name() + " " + caseList.get(position).getClient()
                                .getLast_name() + " VS " + caseList.get(position).getPersons().get(1).getOpp_name());
                    }
                }

                todayViewHolder.txtCourtName.setText(caseList.get(position).getCourt().getCourt_name());
                todayViewHolder.txtAddress.setText(getAddress(caseList.get(position).getCourt()));
            }
        }

        public String getAddress(CourtData aCaseDetail) {
            String address = "";
            if (aCaseDetail.getSubdistrict() != null && aCaseDetail.getSubdistrict().getName() != null) {
                address = aCaseDetail.getSubdistrict().getName() + ", ";
            }


            if (aCaseDetail.getDistrict() != null && aCaseDetail.getDistrict().getName() != null) {
                address = address + aCaseDetail.getDistrict().getName() + ", ";
            }


            if (aCaseDetail.getState() != null && aCaseDetail.getState().getName() != null) {
                address = address + aCaseDetail.getState().getName();
            }
            return address;
        }


        @Override
        public int getItemCount() {
            return caseList.size();
        }

        public class TodayViewHolder extends RecyclerView.ViewHolder {

            TextView txtClientOne, txtCourtName, txtAddress;

            public TodayViewHolder(View itemView) {
                super(itemView);
                txtClientOne = itemView.findViewById(R.id.textViewClientOne);
                txtCourtName = itemView.findViewById(R.id.textViewCourtName);
                txtAddress = itemView.findViewById(R.id.textViewCourtAddress);
            }
        }
    }
}
