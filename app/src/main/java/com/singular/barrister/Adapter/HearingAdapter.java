package com.singular.barrister.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.singular.barrister.Model.Cases.CaseHearing;
import com.singular.barrister.R;

import java.util.ArrayList;

/**
 * Created by rahul.kalamkar on 11/28/2017.
 */

public class HearingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<CaseHearing> hearings;

    public HearingAdapter(Context context, ArrayList<CaseHearing> hearings) {
        this.context = context;
        this.hearings = hearings;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.court_list_item, parent, false);
        return new HearingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HearingViewHolder) {
            HearingViewHolder hearingViewHolder = (HearingViewHolder) holder;
            hearingViewHolder.textViewHearingDate.setText(hearings.get(position).getCase_hearing_date());
            hearingViewHolder.textViewHearingText.setText(hearings.get(position).getCase_decision());
        }
    }

    @Override
    public int getItemCount() {
        return hearings.size();
    }

    public class HearingViewHolder extends RecyclerView.ViewHolder {
        TextView textViewHearingDate, textViewHearingText;

        public HearingViewHolder(View itemView) {
            super(itemView);
            textViewHearingDate = (TextView) itemView.findViewById(R.id.textViewCourtName);
            textViewHearingText = (TextView) itemView.findViewById(R.id.textViewState);
        }
    }
}
