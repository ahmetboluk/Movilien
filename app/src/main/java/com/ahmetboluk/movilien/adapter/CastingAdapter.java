package com.ahmetboluk.movilien.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ahmetboluk.movilien.data.movieDetail.Cast;

import com.ahmetboluk.movilien.R;

import java.util.List;

public class CastingAdapter extends RecyclerView.Adapter<CastingAdapter.MyViewHolder>{

    private Context mContext;
    private List<Cast> mCasts;

    public CastingAdapter (Context context, List<Cast> casts){
        mContext = context;
        mCasts = casts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cast_movie,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText(mCasts.get(position).getName());
        Glide.with(mContext)
                .applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.no_photo))
                .load("https://image.tmdb.org/t/p/w185"+mCasts.get(position).getProfilePath())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mCasts.size();
    }

    public void setItems(List<Cast> items) {
        this.mCasts = items;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView ;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_casting_name);
            imageView = (ImageView) itemView.findViewById(R.id.im_casting_image);
        }
    }
}
