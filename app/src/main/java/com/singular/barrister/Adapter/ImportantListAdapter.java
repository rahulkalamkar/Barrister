package com.singular.barrister.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.singular.barrister.Database.Tables.WebSite.ImportantLink;
import com.singular.barrister.R;
import com.singular.barrister.WebActivity;

import java.util.List;

/**
 * Created by rahul.kalamkar on 12/5/2017.
 */

public class ImportantListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<ImportantLink> list;

    public ImportantListAdapter(Context context, List<ImportantLink> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.court_list_item, parent, false);
        return new ImportantLinkViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImportantLinkViewHolder) {
            ImportantLinkViewHolder courtViewHolder = (ImportantLinkViewHolder) holder;
            courtViewHolder.linkName.setText(list.get(position).getWeb_name());
            courtViewHolder.linkUrl.setText(list.get(position).getWeb_site());

        }
    }

    public class ImportantLinkViewHolder extends RecyclerView.ViewHolder {
        TextView linkName, linkUrl;

        public ImportantLinkViewHolder(View itemView) {
            super(itemView);
            linkName = (TextView) itemView.findViewById(R.id.textViewCourtName);
            linkUrl = (TextView) itemView.findViewById(R.id.textViewState);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("Name", list.get(getAdapterPosition()).getWeb_name());
                    bundle.putString("Url", list.get(getAdapterPosition()).getWeb_site());
                    Intent i = new Intent(context, WebActivity.class);
                    i.putExtras(bundle);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });
        }
    }
}
