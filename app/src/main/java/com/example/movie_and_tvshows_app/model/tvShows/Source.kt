package com.example.movie_and_tvshows_app.model.tvShows

data class Source(
    val android_url: String,
    val format: String,
    val ios_url: String,
    val name: String,
    val price: Double,
    val region: String,
    val source_id: Int,
    val type: String,
    val web_url: String
)