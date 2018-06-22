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
import com.ahmetboluk.movilien.data.peopleDetail.PersonDetail;
import com.ahmetboluk.movilien.R;

public class PersonDetailAdapterII extends RecyclerView.Adapter<PersonDetailAdapterII.MyViewHolder>{

    private PersonDetail mpersonDetail;
    private Context mcontext;

    public PersonDetailAdapterII(Context context ,PersonDetail personDetail){

        mpersonDetail=personDetail;
        mcontext=context;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.person_movie_tv_line,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(mcontext)
                .applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.no_photo))
                .load("https://image.tmdb.org/t/p/w185"+mpersonDetail.getTvCredits().getCast().get(position).getPosterPath())
                .into(holder.imageView);
        holder.textView.setText(mpersonDetail.getTvCredits().getCast().get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mpersonDetail.getTvCredits().getCast().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.iv_person_thumbnail);
            textView = itemView.findViewById(R.id.tv_person_title);

        }
    }
}