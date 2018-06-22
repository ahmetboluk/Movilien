package com.ahmetboluk.movilien.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmetboluk.movilien.data.Result;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ahmetboluk.movilien.R;
import com.ahmetboluk.movilien.dataBase.DatabaseResult;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    ArrayList<DatabaseResult> mDatabaseResultArrayList;
    Context mContext;

    public ListAdapter(Context context, ArrayList<DatabaseResult> databaseResultArrayList) {
        mContext=context;
        mDatabaseResultArrayList=databaseResultArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_line,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(mContext)
                .applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.no_photo))
                .load("https://image.tmdb.org/t/p/w185" + mDatabaseResultArrayList.get(position).getThumbnailUrl())
                .into(holder.imageView);
        holder.name.setText(mDatabaseResultArrayList.get(position).getName());
        holder.date.setText(mDatabaseResultArrayList.get(position).getYear());
        holder.voteCount.setText(mDatabaseResultArrayList.get(position).getRate().toString());

    }

    @Override
    public int getItemCount() {
        return mDatabaseResultArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView name, date, voteCount;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_list_image);
            name = itemView.findViewById(R.id.tv_list_title);
            date = itemView.findViewById(R.id.tv_list_date);
            voteCount = itemView.findViewById(R.id.tv_list_vote_average);
        }
    }
    public DatabaseResult getItem(int id) {
        return mDatabaseResultArrayList.get(id);
    }

}
