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
import com.singular.barrister.Model.Court.CourtData;
import com.singular.barrister.R;

import java.util.ArrayList;

/**
 * Created by rahul.kalamkar on 12/5/2017.
 */

public class TodaysCaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<Case> caseList;

    public TodaysCaseAdapter(Context context, ArrayList<Case> caseList) {
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

        TextView txtClientOne, txtClientTwo, txtCourtName, txtAddress;

        public TodayViewHolder(View itemView) {
            super(itemView);
            txtClientOne = (TextView) itemView.findViewById(R.id.textViewClientOne);
            txtCourtName = (TextView) itemView.findViewById(R.id.textViewCourtName);
            txtAddress = (TextView) itemView.findViewById(R.id.textViewCourtAddress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Case", caseList.get(getAdapterPosition()));
                    Intent intent = new Intent(context, DisplayCaseActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }
}
