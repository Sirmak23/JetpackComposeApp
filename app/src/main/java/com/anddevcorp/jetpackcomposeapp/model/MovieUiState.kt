package com.anddevcorp.jetpackcomposeapp.model

import androidx.compose.runtime.snapshots.SnapshotStateList


sealed class MovieUiState(
    open val movies: SnapshotStateList<Movie>? = null
) {
    data class Success(override val movies: SnapshotStateList<Movie>) : MovieUiState(movies)

    data class Error(val error: String) : MovieUiState()

    data object Loading : MovieUiState()

    data object Empty : MovieUiState()
}
