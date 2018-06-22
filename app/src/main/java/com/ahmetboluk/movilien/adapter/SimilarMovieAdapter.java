package com.ahmetboluk.movilien.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ahmetboluk.movilien.data.movieDetail.Result;
import com.ahmetboluk.movilien.R;

import java.util.List;

public class SimilarMovieAdapter extends RecyclerView.Adapter<SimilarMovieAdapter.MyViewHolder>{

    private Context mContect;
    private List<Result> resultList;

    public SimilarMovieAdapter(Context context, List<Result> result){
        mContect=context;
        resultList=result;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContect).inflate(R.layout.similar_movie,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(mContect)
                .applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.no_photo))
                .load("https://image.tmdb.org/t/p/w185"+resultList.get(position).getPosterPath())
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public Result getItem(int position) { return resultList.get(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView ;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_similar_movie);
        }
    }
    public void setItems(List<Result> items){
        resultList=items;
        notifyDataSetChanged();
    }
}
