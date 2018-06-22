package com.ahmetboluk.movilien.api;

import com.ahmetboluk.movilien.data.Genres;
import com.ahmetboluk.movilien.data.Videos;
import com.ahmetboluk.movilien.data.PageData;
import com.ahmetboluk.movilien.data.SeriesPageData;
import com.ahmetboluk.movilien.data.movieDetail.Detail;
import com.ahmetboluk.movilien.data.peopleDetail.PersonDetail;
import com.ahmetboluk.movilien.data.seriesDetail.SeriesDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TmdbApi {
    @GET("movie/popular")
    Call<PageData> listPopular(@Query("api_key") String apikey, @Query("page") int page);

    @GET("movie/top_rated")
    Call<PageData> listTopRated(@Query("api_key") String apikey, @Query("page") int page);

    @GET("movie/now_playing")
    Call<PageData> listMovieNowPlaying(@Query("api_key") String apikey, @Query("page") int page);

    @GET("movie/upcoming")
    Call<PageData> listUpComing(@Query("api_key") String apikey, @Query("page") int page);

    @GET("movie/{movie_id}")
    Call<Detail> listDetail(@Path("movie_id") int movie_id, @Query("api_key") String apikey, @Query("append_to_response") String appendToResponse);

    @GET("person/{person_id}")
    Call<PersonDetail> listPersonDetail(@Path("person_id") int person_id, @Query("api_key") String apikey, @Query("append_to_response") String appendToResponse);

    @GET("genre/movie/list")
    Call<Genres> listGenres(@Query("api_key") String apikey);

    @GET("discover/movie")
    Call<PageData> listGenreMovie( @Query("api_key") String apikey, @Query("page") int page, @Query("with_genres") int genre_id);

    @GET("tv/popular")
    Call<SeriesPageData> listSeriesPopular(@Query("api_key") String apikey, @Query("page") int page);

    @GET("tv/top_rated")
    Call<SeriesPageData> listSeriesTopRated(@Query("api_key") String apikey, @Query("page") int page);

    @GET("tv/airing_today")
    Call<SeriesPageData> listSeriesAiringToday(@Query("api_key") String apikey, @Query("page") int page);

    @GET("tv/on_the_air")
    Call<SeriesPageData> listSeriesOnTheAir(@Query("api_key") String apikey, @Query("page") int page);

    @GET("tv/{tv_id}")
    Call<SeriesDetail> listSeriesDetail(@Path("tv_id") int tv_id, @Query("api_key") String apikey, @Query("append_to_response") String appendToResponse);

    @GET("genre/tv/list")
    Call<Genres> listSeriesGenres(@Query("api_key") String apikey);

    @GET("discover/tv")
    Call<PageData> listGenreSeries( @Query("api_key") String apikey, @Query("page") int page, @Query("with_genres") int genre_id);

    @GET("movie/{movie_id}/videos")
    Call<Videos> listMovieVideos (@Path("movie_id") int movie_id, @Query("api_key") String apikey);

    @GET("tv/{tv_id}/videos")
    Call<Videos> listTvVideos (@Path("tv_id") int movie_id, @Query("api_key") String apikey);

    @GET("search/movie")
    Call<PageData> listSearchMovieResult(@Query("api_key") String apikey, @Query("query") String query, @Query("page") int page);

    @GET("search/person")
    Call<PageData> listSearchPersonResult(@Query("api_key") String apikey, @Query("query") String query, @Query("page") int page);

    @GET("search/tv")
    Call<PageData> listSearchSeriesResult(@Query("api_key") String apikey, @Query("query") String query, @Query("page") int page);

}