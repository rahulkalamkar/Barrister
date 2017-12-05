package com.singular.barrister.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.singular.barrister.Activity.SubActivity.DisplayClientActivity;
import com.singular.barrister.Fragment.ClientFragment;
import com.singular.barrister.Interface.RecycleItem;
import com.singular.barrister.Interface.RecycleItemClient;
import com.singular.barrister.Model.Cases.Case;
import com.singular.barrister.Model.Client.Client;
import com.singular.barrister.Model.SubDistrict;
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;
import com.singular.barrister.RetrofitManager.RetrofitManager;

import java.util.ArrayList;

/**
 * Created by rahulbabanaraokalamkar on 11/23/17.
 */

public class ClientListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    public ArrayList<Client> clientArrayList;
    ArrayList<Client> arrayList;
    Context context;
    RecycleItemClient recycleItem;
    ValueFilter valueFilter;

    public ClientListAdapter(Context context, ArrayList<Client> clientList, RecycleItemClient recycleItem) {
        this.clientArrayList = clientList;
        this.arrayList = clientList;
        this.context = context;
        this.recycleItem = recycleItem;
    }


    @Override
    public int getItemCount() {
        return clientArrayList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.court_list_item, parent, false);
        return new ClientViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ClientViewHolder) {
            ClientViewHolder clientViewHolder = (ClientViewHolder) holder;
            clientViewHolder.txtCourtName.setText(clientArrayList.get(position).getClient().getFirst_name() + " " +
                    clientArrayList.get(position).getClient().getLast_name());
            clientViewHolder.txtStateName.setText("+" + clientArrayList.get(position).getClient().getCountry_code() + " " +
                    clientArrayList.get(position).getClient().getMobile());
        }
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            String charString = charSequence.toString();

            if (clientArrayList == null)
                return null;

            if (charString.isEmpty()) {

                clientArrayList = arrayList;
            } else {

                ArrayList<Client> filteredList = new ArrayList<>();

                for (Client client : clientArrayList) {

                    if (client.getClient().getFirst_name().toLowerCase().contains(charString.toLowerCase()) ||
                            client.getClient().getLast_name().toLowerCase().contains(charString.toLowerCase())) {

                        filteredList.add(client);
                    }
                }

                clientArrayList = filteredList;
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = clientArrayList;
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            if (results != null) {
                clientArrayList = (ArrayList<Client>) results.values;
                notifyDataSetChanged();
            }
        }

    }

    public class ClientViewHolder extends RecyclerView.ViewHolder {
        public TextView txtCourtName, txtStateName, txtDelete;
        public RelativeLayout bgLayout;
        public LinearLayout fgLayout;

        public ClientViewHolder(View itemView) {
            super(itemView);
            txtCourtName = (TextView) itemView.findViewById(R.id.textViewCourtName);
            txtStateName = (TextView) itemView.findViewById(R.id.textViewState);
            bgLayout = (RelativeLayout) itemView.findViewById(R.id.view_background);
            fgLayout = (LinearLayout) itemView.findViewById(R.id.view_foreground);
            txtDelete = (TextView) itemView.findViewById(R.id.textViewDelete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recycleItem.onItemClick(clientArrayList.get(getAdapterPosition()));
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    recycleItem.onItemLongClick(clientArrayList.get(getAdapterPosition()));
                    return false;
                }
            });
        }
    }

    public void removeItem(int position) {
        deleteItem(position);
        clientArrayList.remove(position);
        notifyItemRemoved(position);
    }

    public void deleteItem(int position) {
        RetrofitManager retrofitManager = new RetrofitManager();
        retrofitManager.deleteClient(new UserPreferance(context).getToken(), clientArrayList.get(position).getId());
    }
}
