package com.singular.barrister.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.singular.barrister.DisplayCourtActivity;
import com.singular.barrister.Model.Court.CourtData;
import com.singular.barrister.R;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rahulbabanaraokalamkar on 11/23/17.
 */

public class CourtListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<CourtData> courtList;
    Context context;
    public CourtListAdapter(Context context, ArrayList<CourtData> courtList){
        this.courtList=courtList;
        this.context=context;
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
        if(holder instanceof CourtViewHolder)
        {
            CourtViewHolder courtViewHolder=(CourtViewHolder)holder;
        courtViewHolder.txtCourtName.setText(courtList.get(position).getCourt_name());
            courtViewHolder.txtStateName.setText(courtList.get(position).getDistrict().getName() +", "+
            courtList.get(position).getState().getName());

        }
    }

    public class CourtViewHolder extends RecyclerView.ViewHolder{
        TextView txtCourtName,txtStateName;
        public CourtViewHolder(View itemView)
        {   super(itemView);
            txtCourtName=(TextView)itemView.findViewById(R.id.textViewCourtName);
            txtStateName=(TextView)itemView.findViewById(R.id.textViewState);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("Court",(Serializable)courtList.get(getAdapterPosition()));
                    Intent intent=new Intent(context, DisplayCourtActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }
}
