package com.singular.barrister.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.singular.barrister.Model.Cases.Case;
import com.singular.barrister.Model.Client.Client;
import com.singular.barrister.Model.Court.CourtData;
import com.singular.barrister.R;

import java.util.ArrayList;

/**
 * Created by rahulbabanaraokalamkar on 11/23/17.
 */

public class CasesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Case> casesList;
    Context context;
    public CasesListAdapter(Context context, ArrayList<Case> casesList){
        this.casesList=casesList;
        this.context=context;
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
        if(holder instanceof CourtListAdapter.CourtViewHolder)
        {
            CasesViewHolder courtViewHolder=(CasesViewHolder)holder;
            courtViewHolder.txtCourtName.setText(casesList.get(position).getPersons().get(0).getOpp_name()+" VS "+
                    casesList.get(position).getPersons().get(1).getOpp_name());
            courtViewHolder.txtStateName.setText(casesList.get(position).getCourt().getSubdistrict() +", "+
                    casesList.get(position).getCourt().getDistrict()+", "+casesList.get(position).getCourt().getState());

        }
    }

    public class CasesViewHolder extends RecyclerView.ViewHolder{
        TextView txtCourtName,txtStateName;
        public CasesViewHolder(View itemView)
        {   super(itemView);
            txtCourtName=(TextView)itemView.findViewById(R.id.textViewCourtName);
            txtStateName=(TextView)itemView.findViewById(R.id.textViewState);
        }
    }
}
