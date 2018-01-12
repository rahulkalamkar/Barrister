package com.singular.barrister.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.singular.barrister.Activity.Update_Hearing;
import com.singular.barrister.Model.Cases.CaseHearing;
import com.singular.barrister.R;
import com.singular.barrister.Util.Utils;

import java.io.Serializable;
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

            if (TextUtils.isEmpty(hearings.get(position).getCase_disposed())) {
                hearingViewHolder.textViewHearingDate.setText(Utils.getDateFormat(hearings.get(position).getCase_hearing_date()));
            } else {
                hearingViewHolder.textViewHearingDate.setText(Utils.getDateFormat(hearings.get(position).getCase_hearing_date()) + " (" + hearings.get(position).getCase_disposed() + ") ");
            }

            if (TextUtils.isEmpty(hearings.get(position).getCase_notes())) {
                hearingViewHolder.textViewHearingText.setVisibility(View.GONE);
            } else {
                hearingViewHolder.textViewHearingText.setText("Notes:\n" + hearings.get(position).getCase_notes());
                hearingViewHolder.textViewHearingText.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(hearings.get(position).getCase_decision())) {
                hearingViewHolder.txtDecision.setVisibility(View.GONE);
            } else {
                hearingViewHolder.txtDecision.setText("Decision:\n" + hearings.get(position).getCase_decision());
                hearingViewHolder.txtDecision.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public int getItemCount() {
        return hearings.size();
    }

    public class HearingViewHolder extends RecyclerView.ViewHolder {
        TextView textViewHearingDate, textViewHearingText, txtDecision;

        public HearingViewHolder(View itemView) {
            super(itemView);
            textViewHearingDate = (TextView) itemView.findViewById(R.id.textViewCourtName);
            textViewHearingText = (TextView) itemView.findViewById(R.id.textViewState);
            txtDecision = (TextView) itemView.findViewById(R.id.textViewNotes);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("CaseHearing", (Serializable) hearings.get(getAdapterPosition()));
                    Intent i = new Intent(context, Update_Hearing.class);
                    i.putExtras(bundle);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });
        }
    }
}
