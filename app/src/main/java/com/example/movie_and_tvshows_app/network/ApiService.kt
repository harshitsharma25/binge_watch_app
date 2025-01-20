package com.example.movie_and_tvshows_app.network

import com.example.movie_and_tvshows_app.model.movies.MovieResponse
import com.example.movie_and_tvshows_app.model.tvShows.TvShowResponse
import com.example.movie_and_tvshows_app.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WatchmodeApiService {
    @GET("releases/")
    fun getMovies(
        @Query("apiKey") apiKey : String = Constants.API_KEY
    ): Single<MovieResponse>


    @GET("title/345534/episodes/")
    fun getTvShows(
        @Query("apiKey") apiKey : String = Constants.API_KEY
    ): Single<TvShowResponse>
}
