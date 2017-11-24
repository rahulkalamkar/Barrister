package com.singular.barrister.Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.singular.barrister.HomeScreen;
import com.singular.barrister.Model.Cases.Case;
import com.singular.barrister.R;
import com.singular.barrister.Util.CarouselLinearLayout;

import java.util.ArrayList;

public class CarouselPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 0.7f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;
    private TodaysFragment context;
    private FragmentManager fragmentManager;
    private float scale;
    ArrayList<Case> caseList;

    public CarouselPagerAdapter(TodaysFragment context, FragmentManager fm, ArrayList<Case> caseList) {
        super(fm);
        this.fragmentManager = fm;
        this.context = context;
        this.caseList = caseList;
    }

    @Override
    public Fragment getItem(int position) {
        // make the first pager bigger than others
        try {
            if (position == TodaysFragment.FIRST_PAGE)
                scale = BIG_SCALE;
            else
                scale = SMALL_SCALE;

            position = position % TodaysFragment.count;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ItemFragment.newInstance(context.getActivity(), position, scale, caseList);
    }

    @Override
    public int getCount() {/*
        int count = 0;
        try {
            count = TodaysFragment.count * TodaysFragment.LOOPS;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }*/
        return caseList.size();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        try {
            if (positionOffset >= 0f && positionOffset <= 1f) {
                CarouselLinearLayout cur = getRootView(position);
                CarouselLinearLayout next = getRootView(position + 1);

                cur.setScaleBoth(BIG_SCALE - DIFF_SCALE * positionOffset);
                next.setScaleBoth(SMALL_SCALE + DIFF_SCALE * positionOffset);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @SuppressWarnings("ConstantConditions")
    private CarouselLinearLayout getRootView(int position) {
        return (CarouselLinearLayout) fragmentManager.findFragmentByTag(this.getFragmentTag(position))
                .getView().findViewById(R.id.root_container);
    }

    private String getFragmentTag(int position) {
        return "android:switcher:" + context.mViewPager.getId() + ":" + position;
    }
}