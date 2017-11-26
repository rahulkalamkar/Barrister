package com.singular.barrister.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.singular.barrister.DisplayCaseActivity;
import com.singular.barrister.Model.Cases.Case;
import com.singular.barrister.Model.Client.Client;
import com.singular.barrister.Model.Court.CourtData;
import com.singular.barrister.R;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rahulbabanaraokalamkar on 11/23/17.
 */

public class CasesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Case> casesList;
    Context context;

    public CasesListAdapter(Context context, ArrayList<Case> casesList) {
        this.casesList = casesList;
        this.context = context;
    }


    @Override
    public int getItemCount() {
        return casesList.size();
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
            if(casesList.get(position).getPersons().get(0).getType().equalsIgnoreCase("Client")) {
                courtViewHolder.txtCourtName.setText(casesList.get(position).getClient().getFirst_name()+ " "+ casesList.get(position).getClient()
                        .getLast_name()+ " VS " +casesList.get(position).getPersons().get(0).getOpp_name());

            }
            else {
                courtViewHolder.txtCourtName.setText(casesList.get(position).getClient().getFirst_name()+ " "+ casesList.get(position).getClient()
                        .getLast_name()+ " VS " +casesList.get(position).getPersons().get(1).getOpp_name());
            }

            courtViewHolder.txtStateName.setText(getAddress(casesList.get(position)));

        }
    }
    public String getAddress(Case aCaseDetail)
    {
        String address ="";
        if(aCaseDetail.getCourt().getSubdistrict() !=null && aCaseDetail.getCourt().getSubdistrict().getName()!=null)
        {
            address=aCaseDetail.getCourt().getSubdistrict().getName()+", ";
        }


        if(aCaseDetail.getCourt().getDistrict() !=null && aCaseDetail.getCourt().getDistrict().getName()!=null)
        {
            address=address+aCaseDetail.getCourt().getDistrict().getName()+", ";
        }


        if(aCaseDetail.getCourt().getState() !=null && aCaseDetail.getCourt().getState().getName()!=null)
        {
            address=address+aCaseDetail.getCourt().getState().getName();
        }
        return address;
    }
    public class CasesViewHolder extends RecyclerView.ViewHolder {
        TextView txtCourtName, txtStateName;

        public CasesViewHolder(View itemView) {
            super(itemView);
            txtCourtName = (TextView) itemView.findViewById(R.id.textViewCourtName);
            txtStateName = (TextView) itemView.findViewById(R.id.textViewState);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("Case",(Serializable)casesList.get(getAdapterPosition()));
                    Intent intent =new Intent(context, DisplayCaseActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }
}
