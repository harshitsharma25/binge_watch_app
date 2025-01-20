package com.example.movie_and_tvshows_app.model.tvShows

data class TvShowResponseItem(
    val episode_number: Int,
    val id: Int,
    val imdb_id: String,
    val name: String,
    val overview: String,
    val release_date: String,
    val runtime_minutes: Int,
    val season_id: Int,
    val season_number: Int,
    val sources: List<Source>,
    val thumbnail_url: String,
    val tmdb_id: Int
)