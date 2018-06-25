package com.ahmetboluk.movilien.myFragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.ahmetboluk.movilien.R;

public class ListFragment extends Fragment {
    TextView textViewWactch, textViewWactched, textViewFavorities;
    Bundle bundle;
    View v1,v2,v3;
    View view;
    private long mLastClickTime = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);

        v1 = view.findViewById(R.id.ll_list_1);
        v2 = view.findViewById(R.id.ll_list_2);
        v3 = view.findViewById(R.id.ll_list_3);


        textViewWactch = view.findViewById(R.id.tv_watch);
        textViewWactched = view.findViewById(R.id.tv_watched);
        textViewFavorities = view.findViewById(R.id.tv_favorities);

        textViewWactch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                v1.animate().scaleX((view.getWidth())/(v1.getWidth())*2).setDuration(300).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        DetailListFragment detailListFragment = new DetailListFragment();
                        bundle = new Bundle();
                        bundle.putInt("list", 1);
                        detailListFragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.rl_solution, detailListFragment,"DetailList");
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();


                    }
                }).start();


            }
        });
        textViewWactched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                v2.animate().scaleX((view.getWidth())/(v2.getWidth())*2).setDuration(300).withEndAction(new Runnable() {
                    @Override
                    public void run() {DetailListFragment detailListFragment = new DetailListFragment();
                        bundle = new Bundle();
                        bundle.putInt("list", 2);
                        detailListFragment.setArguments(bundle);

                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.rl_solution, detailListFragment,"DetailList");
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    }
                }).start();
            }
        });
        textViewFavorities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                v3.animate().scaleX((view.getWidth())/(v3.getWidth())*2).setDuration(300).withEndAction(new Runnable() {
                    @Override
                    public void run() {

                        DetailListFragment detailListFragment = new DetailListFragment();
                        bundle = new Bundle();
                        bundle.putInt("list", 3);
                        detailListFragment.setArguments(bundle);

                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.rl_solution, detailListFragment,"DetailList");
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();


                    }
                }).start();


            }
        });
        return view;
    }
    public void scaleView(View v, float startScale, float endScale) {
        Animation anim = new ScaleAnimation(
                1f, 1f, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(1000);
        v.startAnimation(anim);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}