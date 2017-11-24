package com.singular.barrister.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.singular.barrister.HomeScreen;
import com.singular.barrister.Model.Cases.Case;
import com.singular.barrister.R;
import com.singular.barrister.Util.CarouselLinearLayout;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rahul.kalamkar on 11/24/2017.
 */

public class ItemFragment extends Fragment {
    private static final String POSITON = "position";
    private static final String SCALE = "scale";
    private static final String LIST = "list";
    private int screenWidth;
    private int screenHeight;
    ArrayList<Case> caseList;
    TextView txtClientOne, txtClientTwo, txtCourtName, txtCourtAddress;

    public static Fragment newInstance(Activity context, int pos, float scale, ArrayList<Case> caseList) {
        Bundle b = new Bundle();
        b.putInt(POSITON, pos);
        b.putFloat(SCALE, scale);
        b.putSerializable(LIST, (Serializable) caseList);

        return Fragment.instantiate(context, ItemFragment.class.getName(), b);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWidthAndHeight();
    }

    /**
     * Get device screen width and height
     */
    private void getWidthAndHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        final int postion = this.getArguments().getInt(POSITON);
        float scale = this.getArguments().getFloat(SCALE);

        caseList = (ArrayList<Case>) this.getArguments().getSerializable(LIST);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth / 2, screenHeight / 2);
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.today_case_item, container, false);

        CarouselLinearLayout root = (CarouselLinearLayout) linearLayout.findViewById(R.id.root_container);

        txtClientOne = (TextView) linearLayout.findViewById(R.id.textViewClientOne);
        txtClientTwo = (TextView) linearLayout.findViewById(R.id.textViewClientTwo);
        txtCourtName = (TextView) linearLayout.findViewById(R.id.textViewCourtName);
        txtCourtAddress = (TextView) linearLayout.findViewById(R.id.textViewCourtAddress);


        txtClientOne.setText(caseList.get(postion).getPersons().get(0).getOpp_name());
        txtClientTwo.setText(caseList.get(postion).getPersons().get(1).getOpp_name());
        txtCourtName.setText(caseList.get(postion).getCourt().getCourt_name());
        txtCourtAddress.setText(caseList.get(postion).getCourt().getSubdistrict() != null ? caseList.get(postion).getCourt().getSubdistrict().getName() + ", " : "" +
                caseList.get(postion).getCourt().getDistrict() != null ? caseList.get(postion).getCourt().getDistrict().getName() + ", " : "" +
                caseList.get(postion).getCourt().getState() != null ? caseList.get(postion).getCourt().getState().getName() : ""
        );

        root.setScaleBoth(scale);

        return linearLayout;
    }

}
