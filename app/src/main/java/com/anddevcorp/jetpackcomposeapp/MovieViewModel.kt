package com.anddevcorp.jetpackcomposeapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anddevcorp.jetpackcomposeapp.model.MovieUiState
import com.anddevcorp.jetpackcomposeapp.data.request.MovieRequest
import com.anddevcorp.jetpackcomposeapp.data.usecase.MovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieUseCase: MovieUseCase
) : ViewModel() {

    private val _movies = MutableStateFlow<MovieUiState>(MovieUiState.Loading)
    val movies = _movies.asStateFlow()

    fun getMovies(request: MovieRequest) {
        viewModelScope.launch {
            movieUseCase.invoke(request).collect { result ->
                result.onSuccess { response ->
                    if (response.results.isNotEmpty()) {
                        _movies.emit(MovieUiState.Success(response))
                    } else {
                        _movies.emit(MovieUiState.Empty)
                    }
                }.onFailure { error ->
                    _movies.emit(MovieUiState.Error(error.localizedMessage.orEmpty()))
                }
            }
        }
    }
}
