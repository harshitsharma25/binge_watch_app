package com.example.movie_and_tvshows_app.data

import com.example.movie_and_tvshows_app.repository.CombinedResult

sealed class UiState {
    object Loading : UiState() // Represents a loading state
    data class Success(val data: CombinedResult) : UiState() // Represents success with data
    data class Error(val message: String) : UiState() // Represents an error with a message
}
