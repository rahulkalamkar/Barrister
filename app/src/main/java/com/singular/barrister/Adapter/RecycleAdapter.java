package com.singular.barrister.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.singular.barrister.Interface.StateSelection;
import com.singular.barrister.Model.State;
import com.singular.barrister.R;

import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    Context context;
    ArrayList<State> mArrayList;
    ArrayList<State> stateList;
    StateSelection stateSelection;

    public RecycleAdapter(Context context, ArrayList<State> stateList, StateSelection stateSelection) {
        this.context = context;
        mArrayList = stateList;
        this.stateList = stateList;
        this.stateSelection = stateSelection;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.state_item, parent, false);
        return new StateViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder != null && holder instanceof StateViewHolder) {
            StateViewHolder stateViewHolder = (StateViewHolder) holder;
            stateViewHolder.txtName.setText(stateList.get(position).getName());
        }
    }

    @Override
    public int getItemCount() {
        return stateList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    stateList = mArrayList;
                } else {

                    ArrayList<State> filteredList = new ArrayList<>();

                    for (State state : stateList) {

                        if (state.getName().toLowerCase().contains(charString) /*|| state.getName().toLowerCase().contains(charString) || state.getVer().toLowerCase().contains(charString)*/) {

                            filteredList.add(state);
                        }
                    }

                    stateList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = stateList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                stateList = (ArrayList<State>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class StateViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;

        public StateViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.textViewName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stateSelection.getSelectedState(stateList.get(getAdapterPosition()));
                }
            });
        }
    }
}