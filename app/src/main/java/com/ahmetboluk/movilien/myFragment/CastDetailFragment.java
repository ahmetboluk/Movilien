package com.ahmetboluk.movilien.myFragment;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ahmetboluk.movilien.api.TmdbApi;
import com.ahmetboluk.movilien.data.peopleDetail.PersonDetail;
import com.ahmetboluk.movilien.myFragment.castTabItem.PeoplePagerAdapter;
import com.ahmetboluk.movilien.R;
import com.bumptech.glide.request.RequestOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CastDetailFragment extends Fragment {

    public static final String API_KEY = "31b2377287f733ce461c2d352a64060e";
    Retrofit api = new Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/").addConverterFactory(GsonConverterFactory.create()).build();
    AnimationDrawable animation;
    RelativeLayout relativeLayout;

    PersonDetail personDetail;
    ImageView personImage, backgraundImage;
    TextView personName, personBirdDay, personNation;
    ViewPager viewPager;
    PeoplePagerAdapter pagerAdapter;
    TabLayout tabLayout;

    public CastDetailFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_cast_detail, container, false);

        final ImageView loading = (ImageView) view.findViewById(R.id.im_loading);
        animation = (AnimationDrawable) loading.getDrawable();
        animation.start();
        relativeLayout = (RelativeLayout) view.findViewById(R.id.rl_people_layout);

        personImage = (ImageView) view.findViewById(R.id.iv_people_image);
        backgraundImage = (ImageView) view.findViewById(R.id.iv_person_background);
        personName = (TextView) view.findViewById(R.id.tv_people_name);
        personBirdDay = (TextView) view.findViewById(R.id.tv_people_born_date);
        personNation = (TextView) view.findViewById(R.id.tv_people_nation);

        tabLayout = (TabLayout) view.findViewById(R.id.people_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Description"));
        tabLayout.addTab(tabLayout.newTab().setText("Movie"));
        tabLayout.addTab(tabLayout.newTab().setText("TV"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) view.findViewById(R.id.people_pager);


            api.create(TmdbApi.class).listPersonDetail(getArguments().getInt("person_id"), API_KEY, "movie_credits,tv_credits").enqueue(new Callback<PersonDetail>() {
                @Override
                public void onResponse(Call<PersonDetail> call, Response<PersonDetail> response) {
                    personDetail = response.body();
                    try {
                        Glide.with(getContext()).load("https://image.tmdb.org/t/p/w185" + personDetail.getProfilePath()).into(personImage);
                        Glide
                                .with(getContext())
                                .applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.select_rounded_background))
                                .load("https://image.tmdb.org/t/p/w400" + personDetail.getMovieCredits().getCast().get(0).getBackdropPath())
                                .into(backgraundImage);
                        personName.setText(personDetail.getName());
                        personBirdDay.setText(personDetail.getBirthday().substring(0, 4));
                        personNation.setText(personDetail.getPlaceOfBirth());

                        pagerAdapter = new PeoplePagerAdapter(getChildFragmentManager(), tabLayout.getTabCount(), personDetail);
                        viewPager.setAdapter(pagerAdapter);
                        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                        viewPager.setOffscreenPageLimit(3);
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
                    }catch (Exception e){

                    }

                    animation.stop();
                    loading.setVisibility(View.INVISIBLE);
                    relativeLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<PersonDetail> call, Throwable t) {
                }
            });


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
