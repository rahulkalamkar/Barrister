package com.singular.barrister.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.singular.barrister.DisplayCourtActivity;
import com.singular.barrister.Model.Cases.Case;
import com.singular.barrister.Model.Court.CourtData;
import com.singular.barrister.R;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rahulbabanaraokalamkar on 11/23/17.
 */

public class CourtListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    ArrayList<CourtData> courtList;
    ArrayList<CourtData> arrayList;
    Context context;

    public CourtListAdapter(Context context, ArrayList<CourtData> courtList) {
        this.courtList = courtList;
        this.arrayList = courtList;

        this.context = context;
    }

    @Override
    public int getItemCount() {
        return courtList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.court_list_item, parent, false);
        return new CourtViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CourtViewHolder) {
            CourtViewHolder courtViewHolder = (CourtViewHolder) holder;
            courtViewHolder.txtCourtName.setText(courtList.get(position).getCourt_name());
            courtViewHolder.txtStateName.setText(getAddress(courtList.get(position)));

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
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    ValueFilter valueFilter;

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            String charString = charSequence.toString();

            if (courtList == null)
                return null;

            if (charString.isEmpty()) {

                courtList = arrayList;
            } else {

                ArrayList<CourtData> filteredList = new ArrayList<>();

                for (CourtData courtData : courtList) {

                    if (courtData.getCourt_name().toLowerCase().contains(charString.toLowerCase())) {

                        filteredList.add(courtData);
                    }
                }

                courtList = filteredList;
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = courtList;
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            if (results != null) {
                courtList = (ArrayList<CourtData>) results.values;
                notifyDataSetChanged();
            }
        }

    }

    public class CourtViewHolder extends RecyclerView.ViewHolder {
        TextView txtCourtName, txtStateName;

        public CourtViewHolder(View itemView) {
            super(itemView);
            txtCourtName = (TextView) itemView.findViewById(R.id.textViewCourtName);
            txtStateName = (TextView) itemView.findViewById(R.id.textViewState);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Court", (Serializable) courtList.get(getAdapterPosition()));
                    Intent intent = new Intent(context, DisplayCourtActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }
}
