package com.ahmetboluk.movilien.myFragment;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahmetboluk.movilien.api.TmdbApi;
import com.ahmetboluk.movilien.data.PageData;
import com.ahmetboluk.movilien.R;
import com.ahmetboluk.movilien.RecyclerItemClickListener;
import com.ahmetboluk.movilien.adapter.MoviesAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GenreDetailFragment extends Fragment implements BottomTabLayotListener {

    private int SELECTED = 0;
    private int SELECTED_MOVIE = 0;
    private int SELECTED_TV = 1;
    private long mLastClickTime = 0;


    public static final String API_KEY = "31b2377287f733ce461c2d352a64060e";
    Retrofit api = new Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/").addConverterFactory(GsonConverterFactory.create()).build();
    //GELEN DİZİLERİ DE MoviesAdapter ile bastım ona dikkat ett movie adapter koyduğum if ile tarihi düzelttim
    MoviesAdapter mMoviesAdapter;
    MoviesAdapter mSeriesAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    PageData pageData;
    PageData seriesPageData;
    private int page = 1;
    RecyclerView mRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SELECTED = getArguments().getInt("selected");
        if (SELECTED == SELECTED_MOVIE) {
            api.create(TmdbApi.class).listGenreMovie(API_KEY, page, getArguments().getInt("genre_id")).enqueue(new Callback<PageData>() {
                @Override
                public void onResponse(Call<PageData> call, Response<PageData> response) {
                    pageData = response.body();
                    mMoviesAdapter = new MoviesAdapter(getContext(), pageData.getResults());
                    mLayoutManager = new GridLayoutManager(getContext(), 3);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mRecyclerView.setAdapter(mMoviesAdapter);
                }

                @Override
                public void onFailure(Call<PageData> call, Throwable t) {
                    Log.i("tag", "onFailure: " + t.getMessage());
                }
            });
        } else if (SELECTED == SELECTED_TV) {
            api.create(TmdbApi.class).listGenreSeries(API_KEY, page, getArguments().getInt("genre_id")).enqueue(new Callback<PageData>() {
                @Override
                public void onResponse(Call<PageData> call, Response<PageData> response) {
                    seriesPageData = response.body();
                    mSeriesAdapter = new MoviesAdapter(getContext(), seriesPageData.getResults());
                    mLayoutManager = new GridLayoutManager(getContext(), 3);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mRecyclerView.setAdapter(mSeriesAdapter);
                }

                @Override
                public void onFailure(Call<PageData> call, Throwable t) {
                    Log.i("tag", "onFailure: " + t.getMessage());
                }
            });
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genre_detail, container, false);
        mRecyclerView = view.findViewById(R.id.rv_genre_detail);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        DetailFragment detailFragment = new DetailFragment();
                        Bundle data = new Bundle();
                        if (SELECTED == SELECTED_MOVIE) {
                            data.putInt("movie_id", mMoviesAdapter.getItem(position).getId());
                        } else if (SELECTED == SELECTED_TV) {
                            data.putInt("series_id", mSeriesAdapter.getItem(position).getId());
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
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                int totalItemCount = layoutManager.getItemCount();
                int lastVisible = layoutManager.findLastVisibleItemPosition();

                boolean endHasBeenReached = lastVisible + 1 >= totalItemCount;
                if (totalItemCount > 0 && endHasBeenReached) {
                    //you have reached to the bottom of your recycler view
                    page++;
                    Log.d("Page size", page + " ");
                    if (SELECTED == SELECTED_MOVIE) {
                        api.create(TmdbApi.class).listGenreMovie(API_KEY, page, getArguments().getInt("genre_id")).enqueue(new Callback<PageData>() {
                            @Override
                            public void onResponse(Call<PageData> call, Response<PageData> response) {
                                if (response.body().getResults().size() > 0) {
                                    pageData.getResults().addAll(response.body().getResults());
                                    mMoviesAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onFailure(Call<PageData> call, Throwable t) {

                            }
                        });
                    } else if (SELECTED == SELECTED_TV) {
                        api.create(TmdbApi.class).listGenreSeries(API_KEY, page, getArguments().getInt("genre_id")).enqueue(new Callback<PageData>() {
                            @Override
                            public void onResponse(Call<PageData> call, Response<PageData> response) {
                                if (response.body().getResults().size() > 0) {
                                    seriesPageData.getResults().addAll(response.body().getResults());
                                    mSeriesAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onFailure(Call<PageData> call, Throwable t) {

                            }
                        });

                    }
                }
            }
        });

        return view;

    }

    @Override
    public void onMovieSelected() {
        if(SELECTED==SELECTED_TV){
            getFragmentManager().popBackStack();
        }

    }

    @Override
    public void onSeriesSelected() {
        if(SELECTED==SELECTED_MOVIE){
            getFragmentManager().popBackStack();

        }
    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));

    }

}
