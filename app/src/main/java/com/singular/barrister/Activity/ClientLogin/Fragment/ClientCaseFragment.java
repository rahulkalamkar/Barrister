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
import com.singular.barrister.Adapter.CasesListAdapter;
import com.singular.barrister.Database.Tables.Case.CaseTable;
import com.singular.barrister.Database.Tables.Case.Query.CaseQuery;
import com.singular.barrister.Model.Cases.Case;
import com.singular.barrister.Model.Cases.CasePersons;
import com.singular.barrister.Model.Cases.CasesResponse;
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

public class ClientCaseFragment extends Fragment implements IDataChangeListener<IModel> {
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
        mRecycleView = getView().findViewById(R.id.courtRecycleView);
        progressBar = getView().findViewById(R.id.progressBar);
        errorTextView = getView().findViewById(R.id.textViewErrorText);
        retrofitManager = new RetrofitManager();
        caseList = new ArrayList<Case>();
        getCasesList();
    }

    ListAdapter listAdapter;

    public void getCasesList() {
        if (new NetworkConnection(getActivity()).isNetworkAvailable()) {
            progressBar.setVisibility(View.VISIBLE);
            retrofitManager.getCasesList(this, new UserPreferance(getActivity()).getToken());
        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
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
    public void onDataReceived(IModel response) {
        if (response != null && response instanceof CasesResponse) {
            CasesResponse casesResponse = (CasesResponse) response;
            if (casesResponse.getData().getCaseList() != null) {
                caseList.clear();
                caseList.addAll(casesResponse.getData().getCaseList());
                listAdapter = new ListAdapter(getActivity(), caseList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                mRecycleView.setLayoutManager(linearLayoutManager);
                mRecycleView.setAdapter(listAdapter);
                progressBar.setVisibility(View.GONE);
            } else if (casesResponse.getError() != null && casesResponse.getError().getStatus_code() == 401) {
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

    @Override
    public void onDataFailed(WebServiceError error) {

    }

    public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        ArrayList<Case> casesList;
        Context context;

        public ListAdapter(Context context, ArrayList<Case> caseList) {
            this.context = context;
            this.casesList = caseList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.court_list_item, parent, false);
            return new CasesViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof CasesViewHolder) {
                CasesViewHolder courtViewHolder = (CasesViewHolder) holder;
                if (casesList.get(position).getClient() != null) {
                    if (casesList.get(position).getPersons().get(0).getType().equalsIgnoreCase("Client")) {
                        courtViewHolder.txtCourtName.setText(casesList.get(position).getClient().getFirst_name() + " " + casesList.get(position).getClient()
                                .getLast_name() + " VS " + casesList.get(position).getPersons().get(0).getOpp_name());

                    } else {
                        courtViewHolder.txtCourtName.setText(casesList.get(position).getClient().getFirst_name() + " " + casesList.get(position).getClient()
                                .getLast_name() + " VS " + casesList.get(position).getPersons().get(1).getOpp_name());
                    }
                }
                courtViewHolder.txtStateName.setText(getAddress(casesList.get(position)));

            }
        }

        public String getAddress(Case aCaseDetail) {
            String address = "";
            if (aCaseDetail.getCourt().getSubdistrict() != null && aCaseDetail.getCourt().getSubdistrict().getName() != null) {
                address = aCaseDetail.getCourt().getSubdistrict().getName() + ", ";
            }


            if (aCaseDetail.getCourt().getDistrict() != null && aCaseDetail.getCourt().getDistrict().getName() != null) {
                address = address + aCaseDetail.getCourt().getDistrict().getName() + ", ";
            }


            if (aCaseDetail.getCourt().getState() != null && aCaseDetail.getCourt().getState().getName() != null) {
                address = address + aCaseDetail.getCourt().getState().getName();
            }
            return address;
        }

        public String getOppositionName(Case cases) {
            String str = "";
            if (cases.getPersons().get(0).getType().equalsIgnoreCase("Client")) {
                str = cases.getPersons().get(0).getOpp_name();
            } else {
                str = cases.getPersons().get(1).getOpp_name();
            }
            return str;
        }

        public class CasesViewHolder extends RecyclerView.ViewHolder {
            TextView txtCourtName, txtStateName;

            public CasesViewHolder(View itemView) {
                super(itemView);
                txtCourtName = (TextView) itemView.findViewById(R.id.textViewCourtName);
                txtStateName = (TextView) itemView.findViewById(R.id.textViewState);
            }
        }


        public CasePersons getPerson(String type, Case cases) {
            if (cases.getPersons().get(0).getType().equalsIgnoreCase(type))
                return cases.getPersons().get(0);
            else
                return cases.getPersons().get(1);
        }


        @Override
        public int getItemCount() {
            return caseList.size();
        }
    }
}
