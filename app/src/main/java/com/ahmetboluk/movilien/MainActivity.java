package com.ahmetboluk.movilien;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


import com.ahmetboluk.movilien.myFragment.AboutFragment;
import com.ahmetboluk.movilien.myFragment.BottomTabLayotListener;
import com.ahmetboluk.movilien.myFragment.ListFragment;
import com.ahmetboluk.movilien.myFragment.SearchFragment;
import com.ahmetboluk.movilien.myFragment.mainTabItem.PagerAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MOVIES_SELECTED = 0;
    private static final int SERIES_SELECTED = 1;
    private int lastchooseOne = 0;
    private int lastchooseTwo = 0;


    private static Context mContext;
    private ImageButton imageButton;

    public static Context getAppContext() {
        return mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.INTERNET}, 0);
        }
        mContext = getApplicationContext();
        imageButton = findViewById(R.id.main_search_button);


        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("GENRES"));
        tabLayout.addTab(tabLayout.newTab().setText("POPULAR"));
        tabLayout.addTab(tabLayout.newTab().setText("TOP RATED"));
        tabLayout.addTab(tabLayout.newTab().setText("NOW PLAYING"));
        tabLayout.addTab(tabLayout.newTab().setText("UP COMING"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);



        final TabLayout tabLayoutbottom = (TabLayout) findViewById(R.id.tab_layout_bottom);
        tabLayoutbottom.addTab(tabLayoutbottom.newTab().setText("Movie"));
        tabLayoutbottom.addTab(tabLayoutbottom.newTab().setText("Tv"));
        tabLayoutbottom.addTab(tabLayoutbottom.newTab().setText("Lists"));
        tabLayoutbottom.addTab(tabLayoutbottom.newTab().setText("Credits"));
        tabLayoutbottom.setTabGravity(tabLayoutbottom.GRAVITY_FILL);


        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(4);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabLayoutbottom.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onBottomLayoutSelected(tab.getPosition());
                if (tab.getPosition() == 1) {
                    tabLayout.getTabAt(3).setText("AIRING TODAY");
                    tabLayout.getTabAt(4).setText("ON THE AIR");
                } else {
                    tabLayout.getTabAt(3).setText("NOW PLAYING");
                    tabLayout.getTabAt(4).setText("UP COMING");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                SearchFragment searchFragment = new SearchFragment();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_bottom_right, R.anim.exit_from_top_right, R.anim.enter_from_bottom_right, R.anim.exit_from_top_right);
                fragmentTransaction.add(R.id.main_activity, searchFragment, "SEARCH");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


    }

    private void onBottomLayoutSelected(int position) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        //FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        FragmentManager manager = this.getSupportFragmentManager();
        //BU SATIRLAR GERİ GELİNMESİ DURUMLARINI AYARLANMAK İÇİN EKLENMİŞTİR
        //
        if (position == MOVIES_SELECTED) {
            for (int i = 0; i < fragments.size(); i++) {
                if (fragments.get(i) instanceof BottomTabLayotListener) {
                    ((BottomTabLayotListener) fragments.get(i)).onMovieSelected();
                }
                if (lastchooseOne == 1) {
                    getSupportFragmentManager().beginTransaction().detach(manager.findFragmentByTag("List")).commit();
                    if(getSupportFragmentManager().findFragmentByTag("DetailList")!=null ) {
                        getSupportFragmentManager().beginTransaction().detach(manager.findFragmentByTag("DetailList")).commit();
                    }
                    lastchooseOne = 0;
                }else if (lastchooseTwo == 2) {
                    getSupportFragmentManager().beginTransaction().detach(manager.findFragmentByTag("About")).commit();
                    lastchooseTwo = 0;
                }
            }
        } else if (position == SERIES_SELECTED) {
            for (int i = 0; i < fragments.size(); i++) {
                if (fragments.get(i) instanceof BottomTabLayotListener) {
                    ((BottomTabLayotListener) fragments.get(i)).onSeriesSelected();
                }
                if (lastchooseOne == 1) {
                    getSupportFragmentManager().beginTransaction().detach(manager.findFragmentByTag("List")).commit();
                    if(getSupportFragmentManager().findFragmentByTag("DetailList")!=null ) {
                        getSupportFragmentManager().beginTransaction().detach(manager.findFragmentByTag("DetailList")).commit();
                    }
                    lastchooseOne = 0;
                }else if (lastchooseTwo == 2) {
                    getSupportFragmentManager().beginTransaction().detach(manager.findFragmentByTag("About")).commit();

                    lastchooseTwo = 0;
                }
            }
        } else if (position == 2) {
            ListFragment listFragment = new ListFragment();
            //fragmentTransaction.replace(R.id.rl_solution, listFragment, "LIST");
            //fragmentTransaction.commit();
            manager.beginTransaction()
                    .replace(R.id.rl_solution, listFragment, "List")
                    .commit();
            lastchooseOne = 1;
            lastchooseTwo = 0;
            //BU SATIRLAR GERİ GELİNMESİ DURUMLARINI AYARLANMAK İÇİN EKLENMİŞTİR
        } else if (position == 3) {
            AboutFragment aboutFragment = new AboutFragment();
            //fragmentTransaction.replace(R.id.rl_solution, aboutFragment, "ABOUT");
            //fragmentTransaction.commit();
            manager.beginTransaction()
                    .replace(R.id.rl_solution, aboutFragment, "About")
                    .commit();
            lastchooseTwo = 2;
            lastchooseOne = 0;
            //BU SATIRLAR GERİ GELİNMESİ DURUMLARINI AYARLANMAK İÇİN EKLENMİŞTİR
        }
    }/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(MainActivity.this);
            alertbox.setIcon(R.drawable.minus);
            alertbox.setTitle("Are you sure you want to exit in App");
            alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    finish();
                }
            });
            alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    // Nothing will be happened when clicked on no button
                    // of Dialog
                }
            });
            alertbox.show();
        }
        return super.onKeyDown(keyCode, event);
    }
*/

}