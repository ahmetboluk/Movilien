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
import com.ahmetboluk.movilien.data.Genres;
import com.ahmetboluk.movilien.R;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;
import java.util.Map;

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.MyviewHolder> {
    public Context mContext;
    public Genres mGenres;
    public int mType;
    Map<Integer, String> genreAndImage;
    String[] posterPathMovie, posterPathSeries;

    int[] genreIdMovie, genreIdSeries;

    public GenresAdapter(Context context, Genres genres, int type) {
        mContext = context;
        mGenres = genres;
        mType = type;
        posterPathMovie = new String[]{
                "/bOGkgRGdhrBYJSLpXaxhXVstddV.jpg", "/7LZ0K4FsALrt7OeNIGOVLNuKQRU.jpg",
                "/yo1ef57MEPkEE4BDZKTZGH9uDcX.jpg", "/n1y094tVDFATSzkTnFxoGZ1qNsG.jpg",
                "/6xKCYgH16UuwEGAyroLU6p8HLIn.jpg", "/7OvtDKB770aSj2Dbo5QbShFmF9z.jpg",
                "/8vA6dxBzRqSajvqdmTLtoguAa3T.jpg", "/4rsrxYDfIzvtAsIE9wevxPRXAk4.jpg",
                "/6aUWe0GSl69wMTSWWexsorMIvwU.jpg", "/ozdXqMwijV192aSrojjFQMGeWSh.jpg",
                "/7Yup0zaw4AhNyw1JvyAE1EytWsq.jpg", "/qnzdloPp03sOhEpCyTQpjbdcGW5.jpg",
                "/sGzuQYSTwJvLBc2PnuSVLHhuFeh.jpg", "/6PQyCsluWxE0LPlp2YB6IkMqVQj.jpg",
                "/s2bT29y0ngXxxu2IA8AOzzXTRhd.jpg", "/bxPw6s61bG5Of19lp1E00n0dDWW.jpg",
                "/fmLWuAfDPaUa3Vi5nO1YUUyZaX6.jpg", "/bk0GylJLneaSbpQZXpgTwleYigq.jpg",
                "/jHwMpGEMQwRkAKBC3zVqKtYTj7S.jpg"};
        genreIdMovie = new int[]{28, 12, 16, 35, 80, 99, 18, 10751, 14, 36, 27, 10402, 9648, 10749, 878, 10770, 53, 10752, 37};
        posterPathSeries = new String[]{
                "/xVzvD5BPAU4HpleFSo8QOdHkndo.jpg", "/f5uNbUC76oowt5mt5J9QlqrIYQ6.jpg",
                "/efiX8iir6GEBWCD0uCFIi5NAyYA.jpg", "/dKxkwAJfGuznW8Hu0mhaDJtna0n.jpg",
                "/ag2oZaayXrPL7XFEre5x4rBrxue.jpg", "/y6JABtgWMVYPx84Rvy7tROU5aNH.jpg",
                "/vLdBD1XSrhe8LzA09ouBBjhAa2L.jpg", "/gV2FiYtScFdixj4FtmMjtm02i1y.jpg",
                "/koMUCyGWNtH5LXYbGqjsUwvgtsT.jpg", "/uyilhJ7MBLjiaQXboaEwe44Z0jA.jpg",
                "/nRcPtIn39NEKCavn7S4wkO0WrwB.jpg", "/mmxxEpTqVdwBlu5Pii7tbedBkPC.jpg",
                "/vIDHmF9U0gvQ1Oml9lV1LafHwqb.jpg", "/2EJJewSPIjfAXG4oUm533Noqp02.jpg",
                "/8UVzqlBMG3azowsi7f8lqZwhcBZ.jpg", "/yp94aOXzuqcQHva90B3jxLfnOO9.jpg"};
        genreIdSeries = new int[]{10759, 16, 35, 80, 99, 18, 10751, 10762, 9648, 10763, 10764, 10765, 10766, 10767, 10768, 37};
        genreAndImage = new HashMap<>();
        if (mType==0) {
            for (int i = 0; i < 19; i++) {
                genreAndImage.put(genreIdMovie[i], posterPathMovie[i]);
            }
        } else if (mType==1) {
            for (int k = 0; k < 16; k++) {
                genreAndImage.put(genreIdSeries[k], posterPathSeries[k]);
            }
        }

    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.genres_line, parent, false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        Glide
                .with(mContext)
                .applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.select_rounded_background))
                .load("https://image.tmdb.org/t/p/w780" + genreAndImage.get(mGenres.getGenres().get(position).getId()))
                .into(holder.imageView);
        holder.textView.setText(mGenres.getGenres().get(position).getName());

    }

    @Override
    public int getItemCount() {
        return mGenres.getGenres().size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ImageView imageView;

        public MyviewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.tv_movie_genre_name);
            imageView = itemView.findViewById(R.id.genre_image);
        }
    }
}
