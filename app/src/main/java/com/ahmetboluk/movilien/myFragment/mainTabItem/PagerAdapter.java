package com.ahmetboluk.movilien.myFragment.mainTabItem;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter{

    int mNoOfTabs;


    public PagerAdapter(FragmentManager fm, int NumberOfTabs) {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                TabGenres tabGenres = new TabGenres();
                return tabGenres;
            case 1:
                TabOne tabOne = new TabOne();
                return tabOne;
            case 2:
                TabTwo tabTwo = new TabTwo();
                return tabTwo;
            case 3:
                TabThree tabThree = new TabThree();
                return tabThree;
            case 4:
                TabFour tabFour  = new TabFour();
                return tabFour;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
