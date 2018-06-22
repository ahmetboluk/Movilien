package com.ahmetboluk.movilien.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private Context mContext;
    private List<Result> pageDataList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count, date;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            date = (TextView) view.findViewById(R.id.date);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    public MoviesAdapter(Context Context, List<Result> pageData) {
        this.mContext = Context;
        this.pageDataList = pageData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_contact, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (pageDataList.get(position).getTitle() == null) {
            holder.title.setText(pageDataList.get(position).getName());
        } else if (pageDataList.get(position).getName() == null) {
            holder.title.setText(pageDataList.get(position).getTitle());
        }

        holder.count.setText(((pageDataList.get(position).getVoteAverage().toString())));

        if (pageDataList.get(position).getFirstAirDate() == null) {
            if (pageDataList.get(position).getReleaseDate().length() > 0) {
                holder.date.setText(pageDataList.get(position).getReleaseDate().substring(0, 4));
            } else {
                holder.date.setText("No information");
            }
        } else if (pageDataList.get(position).getReleaseDate() == null) {
            if (pageDataList.get(position).getFirstAirDate().length() > 0) {
                holder.date.setText(pageDataList.get(position).getFirstAirDate().substring(0, 4));
            } else {
                holder.date.setText("No information");
            }
        }
        Glide.with(mContext)
                .applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.no_photo))
                .load("https://image.tmdb.org/t/p/w185" + pageDataList.get(position).getPosterPath())
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return pageDataList.size();
    }

    public Result getItem(int id) {
        return pageDataList.get(id);
    }
}

