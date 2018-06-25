package com.ahmetboluk.movilien.myFragment;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ahmetboluk.movilien.api.TmdbApi;
import com.ahmetboluk.movilien.data.Videos;
import com.ahmetboluk.movilien.data.movieDetail.Detail;
import com.ahmetboluk.movilien.data.seriesDetail.SeriesDetail;
import com.ahmetboluk.movilien.MainActivity;
import com.ahmetboluk.movilien.R;
import com.ahmetboluk.movilien.RecyclerItemClickListener;
import com.ahmetboluk.movilien.adapter.CastingAdapter;
import com.ahmetboluk.movilien.adapter.SeriesCastingAdapter;
import com.ahmetboluk.movilien.adapter.SimilarMovieAdapter;
import com.ahmetboluk.movilien.adapter.SimilarSeriesAdapter;
import com.bumptech.glide.request.RequestOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailFragment extends Fragment {

    private int SELECTED = 0;
    private int SELECTED_MOVIE = 0;
    private int SELECTED_TV = 1;

    AnimationDrawable animation;
    RecyclerView castRecyclerView;
    RecyclerView similarRecyclerView;
    CastingAdapter castingAdapter;
    SeriesCastingAdapter seriesCastingAdapter;
    SimilarMovieAdapter similarMovieAdapter;
    SimilarSeriesAdapter similarSeriesAdapter;
    LinearLayoutManager similarlayoutManager, castinglayoutManager, seriesCastinglayoutManager, similarSerieslayoutManager;
    Detail movieDetail;
    SeriesDetail seriesDetail;
    Videos videos;
    TextView titleText, dateText, minuteText, genreText, nationText, scoreText, owerviewText, watchTrailer, addToList;
    ImageView movieImage;
    ScrollView scrollView;
    RelativeLayout relativeLayout, video;
    ActionBar actionBar;
    ImageView loading;

    private long mLastClickTime = 0;

    public static final String API_KEY = "31b2377287f733ce461c2d352a64060e";
    Retrofit api = new Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/").addConverterFactory(GsonConverterFactory.create()).build();

    public DetailFragment() {

    }

    public static DetailFragment newInstance() {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SELECTED = getArguments().getInt("selected");
        if (SELECTED == SELECTED_MOVIE) {
            api.create(TmdbApi.class).listDetail(getArguments().getInt("movie_id"), API_KEY, "credits,similar").enqueue(new Callback<Detail>() {
                @Override
                public void onResponse(Call<Detail> call, Response<Detail> response) {
                    Log.d("URL", call.request().url().toString());
                    movieDetail = response.body();
                    Glide
                            .with(MainActivity.getAppContext())
                            .applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.no_photo))
                            .load("https://image.tmdb.org/t/p/w185" + movieDetail.getPosterPath()).into(movieImage);
                    titleText.setText(movieDetail.getTitle());
                    if (movieDetail.getReleaseDate().length() > 0) {
                        dateText.setText(movieDetail.getReleaseDate().substring(0, 4));
                    } else {
                        dateText.setText("No information");
                    }
                    if (movieDetail.getRuntime() != null) {
                        minuteText.setText(movieDetail.getRuntime().toString());
                    } else {
                        minuteText.setText("No information");
                    }
                    if (movieDetail.getGenres().size() != 0) {
                        genreText.setText(movieDetail.getGenres().get(0).getName());
                    } else {
                        genreText.setText("No information");
                    }
                    if (movieDetail.getProductionCountries().size() != 0) {
                        nationText.setText(movieDetail.getProductionCountries().get(0).getName());
                    } else {
                        nationText.setText("No information");
                    }
                    scoreText.setText(movieDetail.getVoteAverage().toString());
                    owerviewText.setText(movieDetail.getOverview());
                    castingAdapter = new CastingAdapter(getContext(), movieDetail.getCredits().getCast());
                    castinglayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    castRecyclerView.setLayoutManager(castinglayoutManager);
                    castRecyclerView.setAdapter(castingAdapter);

                    similarMovieAdapter = new SimilarMovieAdapter(getContext(), movieDetail.getSimilar().getResults());
                    similarlayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    similarRecyclerView.setLayoutManager(similarlayoutManager);
                    similarRecyclerView.setAdapter(similarMovieAdapter);
                    animation.stop();
                    loading.setVisibility(View.INVISIBLE);

                    scrollView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<Detail> call, Throwable t) {
                    Log.d("Error", "Olmadı amk");

                }
            });
        } else if (SELECTED == SELECTED_TV) {
            api.create(TmdbApi.class).listSeriesDetail(getArguments().getInt("series_id"), API_KEY, "credits,similar").enqueue(new Callback<SeriesDetail>() {
                @Override
                public void onResponse(Call<SeriesDetail> call, Response<SeriesDetail> response) {
                    seriesDetail = response.body();
                    Glide.with(MainActivity.getAppContext()).load("https://image.tmdb.org/t/p/w185" + seriesDetail.getPosterPath()).into(movieImage);
                    titleText.setText(seriesDetail.getName());
                    dateText.setText(seriesDetail.getFirstAirDate().substring(0, 4));
                    minuteText.setText(seriesDetail.getEpisodeRunTime().get(0).toString());
                    genreText.setText(seriesDetail.getGenres().get(0).getName());
                    nationText.setText(seriesDetail.getOriginCountry().get(0));
                    scoreText.setText(seriesDetail.getVoteAverage().toString());
                    owerviewText.setText(seriesDetail.getOverview());
                    seriesCastingAdapter = new SeriesCastingAdapter(getContext(), seriesDetail.getCredits().getCast());
                    seriesCastinglayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    castRecyclerView.setLayoutManager(seriesCastinglayoutManager);
                    castRecyclerView.setAdapter(seriesCastingAdapter);

                    similarSeriesAdapter = new SimilarSeriesAdapter(getContext(), seriesDetail.getSimilar().getResults());
                    similarSerieslayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    similarRecyclerView.setLayoutManager(similarSerieslayoutManager);
                    similarRecyclerView.setAdapter(similarSeriesAdapter);
                    animation.stop();
                    loading.setVisibility(View.INVISIBLE);

                    scrollView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<SeriesDetail> call, Throwable t) {

                }
            });
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        addToList = (TextView) view.findViewById(R.id.add_to_list);
        loading = (ImageView) view.findViewById(R.id.im_loading);
        animation = (AnimationDrawable) loading.getDrawable();
        animation.start();
        scrollView = (ScrollView) view.findViewById(R.id.sv_layout);
        video = (RelativeLayout) view.findViewById(R.id.video_view_video);
        watchTrailer = (TextView) view.findViewById(R.id.watch_trailer);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.rv_layout);
        movieImage = (ImageView) view.findViewById(R.id.iv_movie_jpeg);
        titleText = (TextView) view.findViewById(R.id.tv_movie_title);
        dateText = (TextView) view.findViewById(R.id.tv_movie_date);
        minuteText = (TextView) view.findViewById(R.id.tv_movie_minute);
        genreText = (TextView) view.findViewById(R.id.tv_movie_genre);
        nationText = (TextView) view.findViewById(R.id.tv_movie_nation);
        scoreText = (TextView) view.findViewById(R.id.tv_movie_score);
        owerviewText = (TextView) view.findViewById(R.id.tv_detail);
        castRecyclerView = (RecyclerView) view.findViewById(R.id.rv_credits);
        similarRecyclerView = (RecyclerView) view.findViewById(R.id.rv_similar_movie);
        similarRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), similarRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        DetailFragment detailFragment = new DetailFragment();
                        Bundle data = new Bundle();
                        if (SELECTED == SELECTED_MOVIE) {
                            data.putInt("movie_id", similarMovieAdapter.getItem(position).getId());
                        } else if (SELECTED == SELECTED_TV) {
                            data.putInt("series_id", similarSeriesAdapter.getItem(position).getId());
                        }
                        data.putInt("selected", SELECTED);
                        detailFragment.setArguments(data);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.add(R.id.main_activity, detailFragment, null);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );
        castRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), castRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        CastDetailFragment castDetailFragment = new CastDetailFragment();
                        Bundle data = new Bundle();
                        if (SELECTED == SELECTED_MOVIE) {
                            data.putInt("person_id", movieDetail.getCredits().getCast().get(position).getId());
                        } else if (SELECTED == SELECTED_TV) {
                            data.putInt("person_id", seriesDetail.getCredits().getCast().get(position).getId());
                        }
                        data.putInt("selected", SELECTED);
                        castDetailFragment.setArguments(data);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.add(R.id.main_activity, castDetailFragment, null);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );
        watchTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if (SELECTED == SELECTED_MOVIE) {
                    api.create(TmdbApi.class).listMovieVideos(getArguments().getInt("movie_id"), API_KEY).enqueue(new Callback<Videos>() {

                        @Override
                        public void onResponse(Call<Videos> call, Response<Videos> response) {
                            Log.d("URL", call.request().url().toString());
                            videos = response.body();
                            for (int i = 0; i < videos.getResults().size(); i++) {
                                if (videos.getResults().get(i).getType().equals("Trailer")) {
                                    YoutubeFragment fragment = new YoutubeFragment();
                                    Bundle data = new Bundle();
                                    data.putString("key", videos.getResults().get(i).getKey());
                                    fragment.setArguments(data);
                                    FragmentManager manager = getActivity().getSupportFragmentManager();
                                    manager.beginTransaction()
                                            .replace(R.id.main_activity, fragment)
                                            .addToBackStack(null)
                                            .commit();
                                    break;
                                } else if (videos.getResults().isEmpty()) {
                                    Toast.makeText(MainActivity.getAppContext(), "Trailer not found. We apologize.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Videos> call, Throwable t) {
                            Log.e("HATA", "onFailure: " + t.getMessage().toString());
                        }
                    });
                } else if (SELECTED == SELECTED_TV) {
                    api.create(TmdbApi.class).listTvVideos(getArguments().getInt("series_id"), API_KEY).enqueue(new Callback<Videos>() {

                        @Override
                        public void onResponse(Call<Videos> call, Response<Videos> response) {
                            Log.d("URL", call.request().url().toString());
                            videos = response.body();
                            for (int i = 0; i < videos.getResults().size(); i++) {
                                if (videos.getResults().get(i).getType().equals("Trailer")) {
                                    YoutubeFragment fragment = new YoutubeFragment();
                                    Bundle data = new Bundle();
                                    data.putString("key", videos.getResults().get(i).getKey());
                                    fragment.setArguments(data);
                                    FragmentManager manager = getActivity().getSupportFragmentManager();
                                    manager.beginTransaction()
                                            .replace(R.id.main_activity, fragment)
                                            .addToBackStack(null)
                                            .commit();
                                    break;
                                } else if (videos.getResults().isEmpty()) {
                                    Toast.makeText(MainActivity.getAppContext(), "Trailer not found. We apologize.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Videos> call, Throwable t) {
                            Log.e("HATA", "onFailure: " + t.getMessage().toString());
                        }
                    });
                }
            }
        });
        addToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                FragmentManager fragmentManager = getFragmentManager();
                DialogFragment dialogFragment = new DialogFragment();
                Bundle data = new Bundle();
                try {
                    if (SELECTED == SELECTED_MOVIE) {
                        data.putInt("itemid", getArguments().getInt("movie_id"));
                        data.putString("name", movieDetail.getTitle());
                        data.putString("date", movieDetail.getReleaseDate().substring(0, 4));
                        data.putString("poster", movieDetail.getPosterPath());
                        data.putString("rate", movieDetail.getVoteAverage().toString());
                    } else if (SELECTED == SELECTED_TV) {
                        data.putInt("itemid", getArguments().getInt("series_id"));
                        data.putString("name", seriesDetail.getName());
                        data.putString("date", seriesDetail.getFirstAirDate().substring(0, 4));
                        data.putString("poster", seriesDetail.getPosterPath());
                        data.putString("rate", seriesDetail.getVoteAverage().toString());
                    }
                    data.putInt("selected", SELECTED);
                    dialogFragment.setArguments(data);
                    dialogFragment.show(fragmentManager, "DialogFragmnet");
                }catch (Exception e){
                    Toast.makeText(MainActivity.getAppContext(), "This item can not be added to lists.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}