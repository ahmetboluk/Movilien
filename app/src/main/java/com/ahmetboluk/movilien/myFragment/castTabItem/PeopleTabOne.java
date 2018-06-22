package com.ahmetboluk.movilien.myFragment.castTabItem;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahmetboluk.movilien.R;


public class PeopleTabOne extends Fragment {

    TextView textView;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people_tab_one, container, false);
        textView = (TextView) view.findViewById(R.id.tv_biography);

        textView.setText(getArguments().getString("person"));


        return view;
    }



}
