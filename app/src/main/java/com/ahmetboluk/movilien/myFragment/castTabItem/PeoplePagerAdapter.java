package com.ahmetboluk.movilien.myFragment.castTabItem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ahmetboluk.movilien.data.peopleDetail.PersonDetail;

public class PeoplePagerAdapter extends FragmentStatePagerAdapter{

    int mNoOfTabs;
    PersonDetail mPersonDetail;
    Bundle bundle = new Bundle();

    public PeoplePagerAdapter(FragmentManager fm, int NumberOfTabs,PersonDetail personDetail) {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
        this.mPersonDetail = personDetail;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                PeopleTabOne tabOne = new PeopleTabOne();
                bundle.putString("person",mPersonDetail.getBiography());
                tabOne.setArguments(bundle);
                return tabOne;
            case 1:
                PeopleTabTwo tabTwo = new PeopleTabTwo();
                bundle.putSerializable("personDetail",mPersonDetail);
                tabTwo.setArguments(bundle);
                return tabTwo;
            case 2:
                PeopleTabThree tabThree = new PeopleTabThree();
                bundle.putInt("person_id",mPersonDetail.getId());
                tabThree.setArguments(bundle);
                return tabThree;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }

}
