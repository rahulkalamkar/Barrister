package com.singular.barrister.Util;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.singular.barrister.Model.State;
import com.singular.barrister.R;

import java.util.ArrayList;

/**
 * Created by rahul.kalamkar on 11/29/2017.
 */

public class StatePopUpWindow {

    public StatePopUpWindow() {
    }

    public void showPopUp(Context context, View view, ArrayList<State> stateList) {
        LayoutInflater inflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.pop_up_with_search, null);

        final PopupWindow menuWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        menuWindow.setOutsideTouchable(false);
        menuWindow.setFocusable(true);
        if (Build.VERSION.SDK_INT >= 21) {
            menuWindow.setElevation(5.0f);
        }

        EditText editText = (EditText) customView.findViewById(R.id.editTextSearch);
        RecyclerView mRecycleView = (RecyclerView) customView.findViewById(R.id.recycleView);

        RecycleAdapter recycleAdapter = new RecycleAdapter(context, stateList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.setAdapter(recycleAdapter);

        menuWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public class RecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context context;
        ArrayList<State> stateList;

        public RecycleAdapter(Context context, ArrayList<State> stateList) {
            this.context = context;
            this.stateList = stateList;
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

        public class StateViewHolder extends RecyclerView.ViewHolder {
            TextView txtName;

            public StateViewHolder(View itemView) {
                super(itemView);
                txtName = (TextView) itemView.findViewById(R.id.textViewName);
            }
        }
    }
}
