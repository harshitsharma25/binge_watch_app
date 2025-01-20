package com.example.movie_and_tvshows_app.repository

import com.example.movie_and_tvshows_app.model.movies.Release
import com.example.movie_and_tvshows_app.model.tvShows.TvShowResponseItem
import android.util.Log

import com.example.movie_and_tvshows_app.network.WatchmodeApiService
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val TAG = "MovieRepository"

data class CombinedResult(
    val movies: List<Release>,
    val tvShows: List<TvShowResponseItem>
)

class MovieRepository @Inject constructor(private val apiService: WatchmodeApiService) {

    private val gson = Gson()

    fun fetchMoviesAndTvShows(): Single<CombinedResult> {
        return Single.zip(
            // First API call for movies with logging
            apiService.getMovies()
                .doOnSuccess { response ->
                    Log.d(TAG, "Movies Response: ${gson.toJson(response)}")
                    Log.d(TAG, "Number of movies received: ${response.releases.size}")

                    // Log first movie details as a sample
                    response.releases.firstOrNull()?.let { movie ->
                        Log.d(TAG, """
                            Sample Movie:
                            Title: ${movie.title}
                            ID: ${movie.id}
                            IMDB ID: ${movie.imdb_id}
                            Release Date: ${movie.source_release_date}
                            Type: ${movie.type}
                        """.trimIndent())
                    }
                }
                .doOnError { error ->
                    Log.e(TAG, "Movies API Error: ${error.message}", error)
                }
                .onErrorResumeNext { throwable ->
                    Single.error(handleError(throwable, "Movies"))
                },

            // Second API call for TV shows with logging
            apiService.getTvShows()
                .doOnSuccess { response ->
                    Log.d(TAG, "TV Shows Response: ${gson.toJson(response)}")
                    Log.d(TAG, "Number of TV show episodes received: ${response.size}")

                    // Log first TV show details as a sample
                    response.firstOrNull()?.let { tvShow ->
                        Log.d(TAG, """
                            Sample TV Show Episode:
                            Name: ${tvShow.name}
                            ID: ${tvShow.id}
                            IMDB ID: ${tvShow.imdb_id}
                            Episode: ${tvShow.episode_number}
                            Season: ${tvShow.season_number}
                            Release Date: ${tvShow.release_date}
                        """.trimIndent())
                    }
                }
                .doOnError { error ->
                    Log.e(TAG, "TV Shows API Error: ${error.message}", error)
                }
                .onErrorResumeNext { throwable ->
                    Single.error(handleError(throwable, "TV Shows"))
                }
        ) { movieResponse, tvShowResponse ->
            // Log the combined results
            Log.d(TAG, """
                Combined Results Summary:
                Total Movies: ${movieResponse.releases.size}
                Total TV Show Episodes: ${tvShowResponse.size}
            """.trimIndent())

            CombinedResult(
                movies = movieResponse.releases,
                tvShows = tvShowResponse
            )
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { error ->
                Log.e(TAG, "Combined Request Error: ${error.message}", error)
            }
            .onErrorResumeNext { throwable ->
                Single.error(handleError(throwable, "Combined"))
            }
    }

    private fun handleError(throwable: Throwable, source: String): Throwable {
        val error = when (throwable) {
            is HttpException -> {
                when (throwable.code()) {
                    401 -> Exception("$source: Unauthorized - Please check your API key")
                    403 -> Exception("$source: Forbidden - Access denied")
                    404 -> Exception("$source: Not found - The requested resource doesn't exist")
                    429 -> Exception("$source: Rate limit exceeded - Too many requests")
                    else -> Exception("$source: Network error (${throwable.code()}) - ${throwable.message}")
                }
            }
            is IOException -> Exception("$source: Network error - Please check your internet connection")
            else -> Exception("$source: ${throwable.message ?: "Unknown error occurred"}")
        }

        // Log the error details
        Log.e(TAG, "Error in $source request", error)
        return error
    }
}

//class MovieRepository @Inject constructor(private val apiService: WatchmodeApiService) {
//    fun fetchMoviesAndTvShows() {
//
//    }
//}
