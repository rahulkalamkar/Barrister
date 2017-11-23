package com.singular.barrister.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.singular.barrister.Model.Cases.Case;
import com.singular.barrister.Model.Client.Client;
import com.singular.barrister.R;

import java.util.ArrayList;

/**
 * Created by rahulbabanaraokalamkar on 11/23/17.
 */

public class ClientListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Client> clientList;
    Context context;
    public ClientListAdapter(Context context, ArrayList<Client> clientList){
        this.clientList=clientList;
        this.context=context;
    }


    @Override
    public int getItemCount() {
        return clientList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.court_list_item, parent, false);
        return new ClientViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ClientViewHolder)
        {
            ClientViewHolder clientViewHolder=(ClientViewHolder)holder;
            clientViewHolder.txtCourtName.setText(clientList.get(position).getClient().getFirst_name() +" "+
                    clientList.get(position).getClient().getLast_name());
            clientViewHolder.txtStateName.setText("+"+clientList.get(position).getClient().getCountry_code() +" "+
                    clientList.get(position).getClient().getMobile());

        }
    }

    public class ClientViewHolder extends RecyclerView.ViewHolder{
        TextView txtCourtName,txtStateName;
        public ClientViewHolder(View itemView)
        {   super(itemView);
            txtCourtName=(TextView)itemView.findViewById(R.id.textViewCourtName);
            txtStateName=(TextView)itemView.findViewById(R.id.textViewState);
        }
    }
}
