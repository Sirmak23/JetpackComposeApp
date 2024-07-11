package com.anddevcorp.jetpackcomposeapp.model


sealed class MovieUiState(
    open val movies: MovieResponse? = null
) {
    data class Success(override val movies: MovieResponse) : MovieUiState(movies)

    data class Error(val error: String) : MovieUiState()

    data object Loading : MovieUiState()

    data object Empty : MovieUiState()
}
