package com.ahmetboluk.movilien.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ahmetboluk.movilien.data.Result;
import com.ahmetboluk.movilien.R;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    public static final String API_KEY = "31b2377287f733ce461c2d352a64060e";
    Retrofit api = new Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/").addConverterFactory(GsonConverterFactory.create()).build();

    private Context mContext;
    private List<Result> mResults;
    private static final int MOVIE = 0;
    private static final int PERSON = 2;
    private static final int TV = 1;
    private int SEARCH_SELECTED = 0;

    public SearchAdapter(Context context, List<Result> results, int selected) {
        mContext = context;
        mResults = results;
        SEARCH_SELECTED = selected;
    }

    public Result getItem(int position) {
        return mResults.get(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView name, date, voteCount;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_search_image);
            name = itemView.findViewById(R.id.tv_search_title);
            date = itemView.findViewById(R.id.tv_search_date);
            voteCount = itemView.findViewById(R.id.tv_search_vote_average);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.sarch_item_line, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        switch (SEARCH_SELECTED) {
            case MOVIE:
                Glide.with(mContext)
                        .applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.no_photo))
                        .load("https://image.tmdb.org/t/p/w185" + mResults.get(position).getPosterPath())
                        .into(holder.imageView);
                holder.name.setText(mResults.get(position).getOriginalTitle());
                if (mResults.get(position).getReleaseDate().length()>0){
                    holder.date.setText(mResults.get(position).getReleaseDate().substring(0,4));
                }else {
                    holder.date.setText("No information");
                }
                holder.voteCount.setText(mResults.get(position).getVoteAverage().toString());
                break;
            case PERSON:
                Glide.with(mContext)
                        .applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.no_photo))
                        .load("https://image.tmdb.org/t/p/w185" + mResults.get(position).getProfilePath())
                        .into(holder.imageView);
                holder.name.setText(mResults.get(position).getName());
                holder.voteCount.setText(mResults.get(position).getPopularity().toString());
                break;
            case TV:
                Glide.with(mContext)
                        .applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.no_photo))
                        .load("https://image.tmdb.org/t/p/w185" + mResults.get(position).getPosterPath())
                        .into(holder.imageView);
                holder.name.setText(mResults.get(position).getOriginalName());
                holder.date.setText(mResults.get(position).getFirstAirDate());
                holder.voteCount.setText(mResults.get(position).getVoteAverage().toString());
                break;
            default:
                Log.i("Hata", "Burada bir sıkınrı var");
        }


    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

}
