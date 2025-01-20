package com.example.movie_and_tvshows_app.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movie_and_tvshows_app.data.UiState
import com.example.movie_and_tvshows_app.model.movies.Release
import com.example.movie_and_tvshows_app.model.tvShows.TvShowResponseItem
import com.example.movie_and_tvshows_app.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _tvShows = MutableLiveData<List<TvShowResponseItem>>()
    val tvShows: LiveData<List<TvShowResponseItem>> get() = _tvShows

    private val _movies = MutableLiveData<List<Release>>()
    val movies: LiveData<List<Release>> get() = _movies


    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> get() = _uiState

    fun getTvShowById(id: Int): TvShowResponseItem? {
        return _tvShows.value?.find { it.id == id }
    }

    fun getMovieById(id: Int): Release? {
        return _movies.value?.find { it.id == id }
    }

    init {
        // Load data when ViewModel is created
        loadMoviesAndTvShows()
    }

    fun loadMoviesAndTvShows() {
        val disposable = repository.fetchMoviesAndTvShows()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _uiState.postValue(UiState.Loading) }
            .subscribe(
                { result ->
                    Log.d("API Response", result.toString())
                    _uiState.postValue(UiState.Success(result))
                    _tvShows.postValue(result.tvShows) // Add this line to store Tv shows
                    _movies.postValue(result.movies) // Add this line to store movies
                },
                { error ->
                    Log.e("API Error", error.message ?: "Unknown error")
                    _uiState.postValue(UiState.Error(error.message ?: "An error occurred"))
                }
            )

        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
