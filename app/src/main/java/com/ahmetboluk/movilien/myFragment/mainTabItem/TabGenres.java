package com.ahmetboluk.movilien.myFragment.mainTabItem;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ahmetboluk.movilien.api.TmdbApi;
import com.ahmetboluk.movilien.data.Genres;
import com.ahmetboluk.movilien.myFragment.BottomTabLayotListener;
import com.ahmetboluk.movilien.myFragment.GenreDetailFragment;
import com.ahmetboluk.movilien.R;
import com.ahmetboluk.movilien.RecyclerItemClickListener;
import com.ahmetboluk.movilien.adapter.GenresAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TabGenres extends Fragment implements BottomTabLayotListener {

    AnimationDrawable animation;

    public RecyclerView genreRecyclerView;
    public Genres mMovieGenres;
    public Genres mSeriesGenres;
    private GenresAdapter mMovieGenresAdapter;
    private GenresAdapter mSeriesGenresAdapter;
    private int SELECTED = 0;
    private int SELECTED_MOVIE = 0;
    private int SELECTED_TV = 1;
    private long mLastClickTime = 0;


    public RecyclerView.LayoutManager mLayoutManager;

    public static final String API_KEY = "31b2377287f733ce461c2d352a64060e";
    Retrofit api = new Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/").addConverterFactory(GsonConverterFactory.create()).build();

    public TabGenres() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab_genres, container, false);

        ImageView loading = (ImageView) view.findViewById(R.id.im_loading);
        animation = (AnimationDrawable) loading.getDrawable();
        animation.start();
        api.create(TmdbApi.class).listGenres(API_KEY).enqueue(new Callback<Genres>() {
            @Override
            public void onResponse(Call<Genres> call, Response<Genres> response) {
                mMovieGenres = response.body();
                mLayoutManager = new LinearLayoutManager(getContext());
                mMovieGenresAdapter = new GenresAdapter(getContext(), mMovieGenres, 0);
                genreRecyclerView.setLayoutManager(mLayoutManager);
                genreRecyclerView.setAdapter(mMovieGenresAdapter);
                animation.stop();

            }

            @Override
            public void onFailure(Call<Genres> call, Throwable t) {

            }
        });

        genreRecyclerView = view.findViewById(R.id.rv_genre_name);
        genreRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), genreRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                GenreDetailFragment genreDetailFragment = new GenreDetailFragment();
                Bundle data = new Bundle();
                if (SELECTED == SELECTED_MOVIE) {
                    data.putInt("genre_id", mMovieGenres.getGenres().get(position).getId());
                } else if (SELECTED == SELECTED_TV) {
                    data.putInt("genre_id", mSeriesGenres.getGenres().get(position).getId());

                }
                data.putInt("selected", SELECTED);
                genreDetailFragment.setArguments(data);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.rl_solution, genreDetailFragment, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

        }));

        return view;
    }

    @Override
    public void onMovieSelected() {
        animation.start();

        SELECTED = SELECTED_MOVIE;
        genreRecyclerView.setAdapter(mMovieGenresAdapter);
        animation.stop();


    }

    @Override
    public void onSeriesSelected() {
        SELECTED = SELECTED_TV;

        animation.start();
        api.create(TmdbApi.class).listSeriesGenres(API_KEY).enqueue(new Callback<Genres>() {
            @Override
            public void onResponse(Call<Genres> call, Response<Genres> response) {
                mSeriesGenres = response.body();
                mLayoutManager = new LinearLayoutManager(getContext());
                mSeriesGenresAdapter = new GenresAdapter(getContext(), mSeriesGenres, 1);
                genreRecyclerView.setLayoutManager(mLayoutManager);
                genreRecyclerView.setAdapter(mSeriesGenresAdapter);
                animation.stop();

            }

            @Override
            public void onFailure(Call<Genres> call, Throwable t) {

            }
        });

    }
}
