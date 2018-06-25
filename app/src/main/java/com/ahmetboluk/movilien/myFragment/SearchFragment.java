package com.ahmetboluk.movilien.myFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmetboluk.movilien.api.TmdbApi;
import com.ahmetboluk.movilien.data.PageData;
import com.ahmetboluk.movilien.data.Result;
import com.ahmetboluk.movilien.R;
import com.ahmetboluk.movilien.RecyclerItemClickListener;
import com.ahmetboluk.movilien.adapter.SearchAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFragment extends Fragment {
    public static final String API_KEY = "31b2377287f733ce461c2d352a64060e";
    Retrofit api = new Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/").addConverterFactory(GsonConverterFactory.create()).build();
    private Context mContext;
    private List<ImageButton> buttonList = null;
    private EditText editText;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private SearchAdapter movieAdapter, personAdapter, seriesAdapter;
    private List<Result> movieSearchResult, seriesSearchResult, personSearchResult;
    private ImageButton imageButton;
    private static final int MOVIE = 0;
    private static final int PERSON = 2;
    private static final int TV = 1;
    private int SEARCH_SELECTED = 0;
    private String search_text;
    int page = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);


        imageButton = (ImageButton) view.findViewById(R.id.search_button);
        editText = (EditText) view.findViewById(R.id.et_search_text);
        movieSearchResult = new ArrayList<>();
        seriesSearchResult = new ArrayList<>();
        personSearchResult = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.search_recyclerview);
        linearLayoutManager = new LinearLayoutManager(mContext);

        DividerItemDecoration itemDecorator = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));


        recyclerView.addItemDecoration(itemDecorator);
        recyclerView.setLayoutManager(linearLayoutManager);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                movieSearchResult.clear();
                seriesSearchResult.clear();
                personSearchResult.clear();
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    search_text = editText.getText().toString();
                    if (search_text.length() != 0) {

                        api.create(TmdbApi.class).listSearchMovieResult(API_KEY, search_text, page).enqueue(new Callback<PageData>() {
                            @Override
                            public void onResponse(Call<PageData> call, Response<PageData> response) {
                                movieSearchResult.addAll(response.body().getResults());
                                movieAdapter.notifyDataSetChanged();
                                if (movieSearchResult.size()==0&&SEARCH_SELECTED==MOVIE){
                                    Toast.makeText(mContext, "No Results Found", Toast.LENGTH_SHORT).show();}
                            }

                            @Override
                            public void onFailure(Call<PageData> call, Throwable t) {

                            }
                        });
                        api.create(TmdbApi.class).listSearchPersonResult(API_KEY, search_text, page).enqueue(new Callback<PageData>() {
                            @Override
                            public void onResponse(Call<PageData> call, Response<PageData> response) {
                                personSearchResult.addAll(response.body().getResults());
                                personAdapter.notifyDataSetChanged();
                                if (personSearchResult.size()==0&&SEARCH_SELECTED==PERSON){
                                    Toast.makeText(mContext, "No Results Found", Toast.LENGTH_SHORT).show();}

                            }

                            @Override
                            public void onFailure(Call<PageData> call, Throwable t) {

                            }
                        });
                        api.create(TmdbApi.class).listSearchSeriesResult(API_KEY, search_text, page).enqueue(new Callback<PageData>() {
                            @Override
                            public void onResponse(Call<PageData> call, Response<PageData> response) {
                                seriesSearchResult.addAll(response.body().getResults());
                                seriesAdapter.notifyDataSetChanged();
                                if (seriesSearchResult.size()==0&&SEARCH_SELECTED==TV){
                                    Toast.makeText(mContext, "No Results Found", Toast.LENGTH_SHORT).show();}

                            }

                            @Override
                            public void onFailure(Call<PageData> call, Throwable t) {

                            }
                        });



                        movieAdapter = new SearchAdapter(mContext, movieSearchResult, MOVIE);
                        personAdapter = new SearchAdapter(mContext, personSearchResult, PERSON);
                        seriesAdapter = new SearchAdapter(mContext, seriesSearchResult, TV);

                        switch (SEARCH_SELECTED) {
                            case MOVIE:
                                recyclerView.setAdapter(movieAdapter);
                                break;
                            case PERSON:
                                recyclerView.setAdapter(personAdapter);
                                break;
                            case TV:
                                recyclerView.setAdapter(seriesAdapter);
                                break;
                            default:
                                Log.i("HATA", "Bir sıkıntı var");
                        }
                    }
                }
                return false;
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movieSearchResult.clear();
                seriesSearchResult.clear();
                personSearchResult.clear();
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                search_text = editText.getText().toString();
                if (search_text.length() != 0) {

                    api.create(TmdbApi.class).listSearchMovieResult(API_KEY, search_text, page).enqueue(new Callback<PageData>() {
                        @Override
                        public void onResponse(Call<PageData> call, Response<PageData> response) {
                            if (response.body() != null) {
                                movieSearchResult.addAll(response.body().getResults());
                            }else{
                                movieSearchResult=new ArrayList<Result>();
                            }
                            movieAdapter.notifyDataSetChanged();
                            if (movieSearchResult.size()==0&&SEARCH_SELECTED==MOVIE){
                                Toast.makeText(mContext, "No Results Found", Toast.LENGTH_SHORT).show();}
                        }

                        @Override
                        public void onFailure(Call<PageData> call, Throwable t) {

                        }
                    });
                    api.create(TmdbApi.class).listSearchPersonResult(API_KEY, search_text, page).enqueue(new Callback<PageData>() {
                        @Override
                        public void onResponse(Call<PageData> call, Response<PageData> response) {
                            if (response.body() != null) {
                                personSearchResult.addAll(response.body().getResults());
                            }else{
                                personSearchResult=new ArrayList<Result>();
                            }
                            personAdapter.notifyDataSetChanged();
                            if (personSearchResult.size()==0&&SEARCH_SELECTED==PERSON){
                                Toast.makeText(mContext, "No Results Found", Toast.LENGTH_SHORT).show();}

                        }

                        @Override
                        public void onFailure(Call<PageData> call, Throwable t) {

                        }
                    });
                    api.create(TmdbApi.class).listSearchSeriesResult(API_KEY, search_text, page).enqueue(new Callback<PageData>() {
                        @Override
                        public void onResponse(Call<PageData> call, Response<PageData> response) {
                            if (response.body() != null) {
                                seriesSearchResult.addAll(response.body().getResults());
                            }else{
                                seriesSearchResult=new ArrayList<Result>();
                            }
                            seriesAdapter.notifyDataSetChanged();
                            if (seriesSearchResult.size()==0&&SEARCH_SELECTED==TV){
                                Toast.makeText(mContext, "No Results Found", Toast.LENGTH_SHORT).show();}

                        }

                        @Override
                        public void onFailure(Call<PageData> call, Throwable t) {

                        }
                    });

                    movieAdapter = new SearchAdapter(mContext, movieSearchResult, MOVIE);
                    personAdapter = new SearchAdapter(mContext, personSearchResult, PERSON);
                    seriesAdapter = new SearchAdapter(mContext, seriesSearchResult, TV);

                    switch (SEARCH_SELECTED) {
                        case MOVIE:
                            recyclerView.setAdapter(movieAdapter);
                            break;
                        case PERSON:
                            recyclerView.setAdapter(personAdapter);
                            break;
                        case TV:
                            recyclerView.setAdapter(seriesAdapter);
                            break;
                        default:
                            Log.i("HATA", "Bir sıkıntı var");
                    }
                }
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    switch (SEARCH_SELECTED) {
                        case MOVIE:
                            api.create(TmdbApi.class).listSearchMovieResult(API_KEY, search_text, page).enqueue(new Callback<PageData>() {
                                @Override
                                public void onResponse(Call<PageData> call, Response<PageData> response) {
                                    movieSearchResult.addAll(response.body().getResults());
                                    movieAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onFailure(Call<PageData> call, Throwable t) {

                                }
                            });
                            break;
                        case PERSON:
                            api.create(TmdbApi.class).listSearchPersonResult(API_KEY, search_text, page).enqueue(new Callback<PageData>() {
                                @Override
                                public void onResponse(Call<PageData> call, Response<PageData> response) {
                                    personSearchResult.addAll(response.body().getResults());
                                    personAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onFailure(Call<PageData> call, Throwable t) {

                                }
                            });
                            break;
                        case TV:
                            api.create(TmdbApi.class).listSearchSeriesResult(API_KEY, search_text, page).enqueue(new Callback<PageData>() {
                                @Override
                                public void onResponse(Call<PageData> call, Response<PageData> response) {
                                    seriesSearchResult.addAll(response.body().getResults());
                                    seriesAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onFailure(Call<PageData> call, Throwable t) {

                                }
                            });
                            break;
                    }
                }
            }
        });
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        DetailFragment detailFragment = new DetailFragment();
                        CastDetailFragment castDetailFragment = new CastDetailFragment();
                        Bundle data = new Bundle();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        switch (SEARCH_SELECTED) {
                            case MOVIE:
                                data.putInt("movie_id", movieAdapter.getItem(position).getId());
                                data.putInt("selected", MOVIE);
                                detailFragment.setArguments(data);
                                fragmentTransaction.add(R.id.main_activity, detailFragment, null);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                                break;
                            case PERSON:
                                data.putInt("person_id", personAdapter.getItem(position).getId());
                                castDetailFragment.setArguments(data);
                                fragmentTransaction.add(R.id.main_activity, castDetailFragment, null);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                                break;
                            case TV:
                                data.putInt("series_id", seriesAdapter.getItem(position).getId());
                                data.putInt("selected", TV);
                                detailFragment.setArguments(data);
                                fragmentTransaction.add(R.id.main_activity, detailFragment, null);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                                break;
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );
        buttonList = new ArrayList<>();
        ImageButton dayBtn = (ImageButton) view.findViewById(R.id.btn_day);
        ImageButton weekBtn = (ImageButton) view.findViewById(R.id.btn_week);
        ImageButton monthBtn = (ImageButton) view.findViewById(R.id.btn_month);
        dayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SEARCH_SELECTED = MOVIE;
                buttonList.get(0).setBackgroundResource(R.drawable.select_accent_color);
                buttonList.get(1).setBackgroundResource(R.drawable.select_rounded_background);
                buttonList.get(2).setBackgroundResource(R.drawable.select_rounded_background);
                if (!movieSearchResult.isEmpty()) {
                    recyclerView.setAdapter(movieAdapter);
                }
            }
        });
        weekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SEARCH_SELECTED = PERSON;
                buttonList.get(0).setBackgroundResource(R.drawable.select_rounded_background);
                buttonList.get(1).setBackgroundResource(R.drawable.select_accent_color);
                buttonList.get(2).setBackgroundResource(R.drawable.select_rounded_background);
                if (!personSearchResult.isEmpty()) {
                    recyclerView.setAdapter(personAdapter);
                }
            }
        });
        monthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SEARCH_SELECTED = TV;
                buttonList.get(0).setBackgroundResource(R.drawable.select_rounded_background);
                buttonList.get(1).setBackgroundResource(R.drawable.select_rounded_background);
                buttonList.get(2).setBackgroundResource(R.drawable.select_accent_color);
                if (!seriesSearchResult.isEmpty()) {
                    recyclerView.setAdapter(seriesAdapter);
                }
            }
        });
        buttonList.add(dayBtn);
        buttonList.add(weekBtn);
        buttonList.add(monthBtn);

        return view;
    }

}
