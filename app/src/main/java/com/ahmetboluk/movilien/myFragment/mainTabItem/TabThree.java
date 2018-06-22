package com.ahmetboluk.movilien.myFragment.mainTabItem;

import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
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
import android.widget.ImageView;

import com.ahmetboluk.movilien.api.TmdbApi;
import com.ahmetboluk.movilien.data.PageData;
import com.ahmetboluk.movilien.data.Result;
import com.ahmetboluk.movilien.data.SeriesPageData;
import com.ahmetboluk.movilien.data.SeriesResult;
import com.ahmetboluk.movilien.myFragment.BottomTabLayotListener;
import com.ahmetboluk.movilien.myFragment.DetailFragment;
import com.ahmetboluk.movilien.R;
import com.ahmetboluk.movilien.RecyclerItemClickListener;
import com.ahmetboluk.movilien.adapter.MoviesAdapter;
import com.ahmetboluk.movilien.adapter.TvAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TabThree extends Fragment implements BottomTabLayotListener {

    AnimationDrawable animation;

    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private TvAdapter tvAdapter;
    private List<Result> popularMoviesList;
    private List<SeriesResult> seriesResultList;
    private int page=1;
    private int SELECTED=0;
    private int SELECTED_MOVIE=0;
    private int SELECTED_TV=1;
    public static final String API_KEY="31b2377287f733ce461c2d352a64060e";
    Retrofit api =new Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/").addConverterFactory(GsonConverterFactory.create()).build();

    public TabThree() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api.create(TmdbApi.class).listMovieNowPlaying(API_KEY,page).enqueue(new Callback<PageData>() {
            @Override
            public void onResponse(Call<PageData> call, Response<PageData> response) {
                if (response.body().getResults().size()>0){
                    popularMoviesList.addAll(response.body().getResults());
                    adapter.notifyDataSetChanged();
                    animation.stop();
                    Log.d("Response",response.body().getResults().toString());
                }
            }

            @Override
            public void onFailure(Call<PageData> call, Throwable t) {
                Log.d("Error","Olmadı amk");
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_three, container, false);

        ImageView loading = (ImageView) view.findViewById(R.id.im_loading);
        animation= (AnimationDrawable)loading.getDrawable();
        animation.start();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        popularMoviesList = new ArrayList<>();
        seriesResultList = new ArrayList<>();
        adapter = new MoviesAdapter(getContext(), popularMoviesList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(9, dpToPx(1), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager=LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                int totalItemCount = layoutManager.getItemCount();
                int lastVisible = layoutManager.findLastVisibleItemPosition();

                boolean endHasBeenReached = lastVisible+1 >= totalItemCount;
                if (totalItemCount > 0 && endHasBeenReached) {
                    //you have reached to the bottom of your recycler view
                    page++;
                    Log.d("Page size",page+" ");
                    if(SELECTED==SELECTED_MOVIE){
                        api.create(TmdbApi.class).listMovieNowPlaying(API_KEY,page).enqueue(new Callback<PageData>() {
                            @Override
                            public void onResponse(Call<PageData> call, Response<PageData> response) {
                                if (response.body().getResults().size()>0){
                                    popularMoviesList.addAll(response.body().getResults());
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onFailure(Call<PageData> call, Throwable t) {

                            }
                        });
                    }else if(SELECTED==SELECTED_TV){
                        api.create(TmdbApi.class).listSeriesAiringToday(API_KEY,page).enqueue(new Callback<SeriesPageData>() {
                            @Override
                            public void onResponse(Call<SeriesPageData> call, Response<SeriesPageData> response) {
                                seriesResultList.addAll(response.body().getResults());
                                tvAdapter.notifyDataSetChanged();
                            }
                            @Override
                            public void onFailure(Call<SeriesPageData> call, Throwable t) {
                                Log.e("HATA", "onFailure: "+t.getMessage().toString() );
                            }
                        });
                    }
                }
            }
        });
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(),recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        DetailFragment detailFragment = new DetailFragment();
                        Bundle data=new Bundle();
                        if(SELECTED==SELECTED_MOVIE) {
                            data.putInt("movie_id", adapter.getItem(position).getId());
                        }else if(SELECTED==SELECTED_TV){
                            data.putInt("series_id", tvAdapter.getItem(position).getId());
                        }
                        data.putInt("selected",SELECTED);                        detailFragment.setArguments(data);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.add(R.id.main_activity,detailFragment,null);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );
        return view;

    }

    @Override
    public void onMovieSelected() {
        SELECTED=SELECTED_MOVIE;
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSeriesSelected() {
        SELECTED=SELECTED_TV;
        page=1;
        if(seriesResultList.isEmpty()){
            api.create(TmdbApi.class).listSeriesAiringToday(API_KEY,page).enqueue(new Callback<SeriesPageData>() {
                @Override
                public void onResponse(Call<SeriesPageData> call, Response<SeriesPageData> response) {
                    seriesResultList=response.body().getResults();
                    tvAdapter= new TvAdapter(getContext(),seriesResultList);
                    recyclerView.setAdapter(tvAdapter);
                }
                @Override
                public void onFailure(Call<SeriesPageData> call, Throwable t) {
                    Log.e("HATA", "onFailure: "+t.getMessage().toString() );
                }
            });}else {recyclerView.setAdapter(tvAdapter);}


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