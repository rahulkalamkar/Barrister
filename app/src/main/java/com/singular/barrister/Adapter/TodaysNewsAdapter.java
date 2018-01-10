package com.singular.barrister.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.singular.barrister.Activity.ShowNewsActivity;
import com.singular.barrister.Activity.SubActivity.HearingDateActivity;
import com.singular.barrister.DisplayCaseActivity;
import com.singular.barrister.Model.Cases.Case;
import com.singular.barrister.Model.Court.CourtData;
import com.singular.barrister.Model.News.News;
import com.singular.barrister.R;
import com.singular.barrister.Util.NetworkConnection;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by rahul.kalamkar on 12/5/2017.
 */

public class TodaysNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<News> newsArrayList;

    public TodaysNewsAdapter(Context context, ArrayList<News> newsArrayList) {
        this.context = context;
        this.newsArrayList = newsArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.today_news_layout, parent, false);
        return new TodayViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TodayViewHolder) {
            TodayViewHolder todayViewHolder = (TodayViewHolder) holder;

            todayViewHolder.headLine.setText(newsArrayList.get(position).getTitle());

            if (new NetworkConnection(context).isNetworkAvailable()) {
                Picasso.with(context)
                        .load(newsArrayList.get(position).getUrlToImage())
                        .error(R.drawable.gradient_blue)
                        .placeholder(R.drawable.gradient_blue)
                        .into(todayViewHolder.imageViewNews);
            }
        }
    }


    @Override
    public int getItemCount() {
        return newsArrayList.size();
    }

    public class TodayViewHolder extends RecyclerView.ViewHolder {


        ImageView imageViewNews;
        TextView headLine, viewAll;

        public TodayViewHolder(View itemView) {
            super(itemView);
            headLine = (TextView) itemView.findViewById(R.id.textViewHeadline);
            imageViewNews = (ImageView) itemView.findViewById(R.id.imageView);
            viewAll = (TextView) itemView.findViewById(R.id.txtViewAll);

            viewAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("Name", "News");
                    bundle.putString("Url", "" + newsArrayList.get(getAdapterPosition()).getUrl());
                    Intent i = new Intent(context, ShowNewsActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtras(bundle);
                    context.startActivity(i);
                }
            });
        }
    }

}
