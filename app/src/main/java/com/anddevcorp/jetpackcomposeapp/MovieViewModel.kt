package com.anddevcorp.jetpackcomposeapp

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anddevcorp.jetpackcomposeapp.data.repository.MovieRepositoryImpl
import com.anddevcorp.jetpackcomposeapp.model.MovieUiState
import com.anddevcorp.jetpackcomposeapp.data.request.MovieRequest
import com.anddevcorp.jetpackcomposeapp.data.request.getRequestModel
import com.anddevcorp.jetpackcomposeapp.data.usecase.MovieUseCase
import com.anddevcorp.jetpackcomposeapp.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieUseCase: MovieUseCase,
    private val movieRepositoryImpl: MovieRepositoryImpl
) : ViewModel() {

    private val _movies = MutableStateFlow<MovieUiState>(MovieUiState.Loading)
    val movies = _movies.asStateFlow()


    fun getMovies(request: MovieRequest) {
        viewModelScope.launch {
            movieUseCase.invoke(request).collect { result ->
                result.onSuccess { response ->
                    if (response.results.isNotEmpty()) {
                        _movies.emit(MovieUiState.Success(response.results.toMutableStateList()))
                    } else {
                        _movies.emit(MovieUiState.Empty)
                    }
                }.onFailure { error ->
                    _movies.emit(MovieUiState.Error(error.localizedMessage.orEmpty()))
                }
            }
        }
    }
    fun changeData(movieIndex: Int) {
        viewModelScope.launch {
            val currentState = _movies.value
            if (currentState is MovieUiState.Success) {
                var movies = currentState.movies[movieIndex]
                currentState.movies[movieIndex] = movies.copy(posterPath = currentState.movies[movieIndex].backdropPath)
//                val movies = currentState.movies
//                val movie = movies[movieIndex]
//                movies[movieIndex] = movie.copy(posterPath = movie.backdropPath)
//                _movies.value = MovieUiState.Success(movies)
            }
        }
    }






















//    private val initialList = listOf(
//        Person(
//            adult = false,
//            backdropPath = "/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg",
//            id = 0,
//            originalLanguage = "en",
//            originalTitle = "Original Title 1",
//            overview = "This is a dummy overview for movie 1.",
//            popularity = 10.0,
//            posterPath = "/3aYym07c0oJjg6h00ras1SCPKt9.jpg",
//            releaseDate = "2023-01-01",
//            title = "Dummy Movie 1",
//            video = false,
//            voteAverage = 7.5,
//            voteCount = 100
//        ),
//        Person(
//            adult = false,
//            backdropPath = "/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg",
//            id = 1,
//            originalLanguage = "en",
//            originalTitle = "Original Title 2",
//            overview = "This is a dummy overview for movie 2.",
//            popularity = 20.0,
//            posterPath = "/3aYym07c0oJjg6h00ras1SCPKt9.jpg",
//            releaseDate = "2023-02-01",
//            title = "Dummy Movie 2",
//            video = false,
//            voteAverage = 7.0,
//            voteCount = 150
//        ),
//        Person(
//            adult = false,
//            backdropPath = "/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg",
//            id = 2,
//            originalLanguage = "en",
//            originalTitle = "Original Title 3",
//            overview = "This is a dummy overview for movie 3.",
//            popularity = 30.0,
//            posterPath = "/3aYym07c0oJjg6h00ras1SCPKt9.jpg",
//            releaseDate = "2023-03-01",
//            title = "Dummy Movie 3",
//            video = false,
//            voteAverage = 6.5,
//            voteCount = 200
//        ),
//        Person(
//            adult = false,
//            backdropPath = "/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg",
//            id = 3,
//            originalLanguage = "en",
//            originalTitle = "Original Title 4",
//            overview = "This is a dummy overview for movie 4.",
//            popularity = 40.0,
//            posterPath = "/3aYym07c0oJjg6h00ras1SCPKt9.jpg",
//            releaseDate = "2023-04-01",
//            title = "Dummy Movie 4",
//            video = false,
//            voteAverage = 8.0,
//            voteCount = 250
//        ),
//        Person(
//            adult = false,
//            backdropPath = "/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg",
//            id = 4,
//            originalLanguage = "en",
//            originalTitle = "Original Title 5",
//            overview = "This is a dummy overview for movie 5.",
//            popularity = 50.0,
//            posterPath = "/3aYym07c0oJjg6h00ras1SCPKt9.jpg",
//            releaseDate = "2023-05-01",
//            title = "Dummy Movie 5",
//            video = false,
//            voteAverage = 7.2,
//            voteCount = 300
//        ),
//        Person(
//            adult = false,
//            backdropPath = "/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg",
//            id = 5,
//            originalLanguage = "en",
//            originalTitle = "Original Title 6",
//            overview = "This is a dummy overview for movie 6.",
//            popularity = 60.0,
//            posterPath = "/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg",
//            releaseDate = "2023-06-01",
//            title = "Dummy Movie 6",
//            video = false,
//            voteAverage = 8.5,
//            voteCount = 350
//        ),
//        Person(
//            adult = false,
//            backdropPath = "/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg",
//            id = 6,
//            originalLanguage = "en",
//            originalTitle = "Original Title 7",
//            overview = "This is a dummy overview for movie 7.",
//            popularity = 70.0,
//            posterPath = "/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg",
//            releaseDate = "2023-07-01",
//            title = "Dummy Movie 7",
//            video = false,
//            voteAverage = 6.8,
//            voteCount = 400
//        ),
//        Person(
//            adult = false,
//            backdropPath = "/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg",
//            id = 7,
//            originalLanguage = "en",
//            originalTitle = "Original Title 8",
//            overview = "This is a dummy overview for movie 8.",
//            popularity = 80.0,
//            posterPath = "/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg",
//            releaseDate = "2023-08-01",
//            title = "Dummy Movie 8",
//            video = false,
//            voteAverage = 9.0,
//            voteCount = 450
//        ),
//        Person(
//            adult = false,
//            backdropPath = "/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg",
//            id = 8,
//            originalLanguage = "en",
//            originalTitle = "Original Title 9",
//            overview = "This is a dummy overview for movie 9.",
//            popularity = 90.0,
//            posterPath = "/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg",
//            releaseDate = "2023-09-01",
//            title = "Dummy Movie 9",
//            video = false,
//            voteAverage = 7.9,
//            voteCount = 500
//        ),
//        Person(
//            adult = false,
//            backdropPath = "/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg",
//            id = 9,
//            originalLanguage = "en",
//            originalTitle = "Original Title 10",
//            overview = "This is a dummy overview for movie 10.",
//            popularity = 100.0,
//            posterPath = "/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg",
//            releaseDate = "2023-10-01",
//            title = "Dummy Movie 10",
//            video = false,
//            voteAverage = 8.1,
//            voteCount = 550
//        ),
//        Person(
//            adult = false,
//            backdropPath = "/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg",
//            id = 10,
//            originalLanguage = "en",
//            originalTitle = "Original Title 11",
//            overview = "This is a dummy overview for movie 11.",
//            popularity = 110.0,
//            posterPath = "/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg",
//            releaseDate = "2023-11-01",
//            title = "Dummy Movie 11",
//            video = false,
//            voteAverage = 6.9,
//            voteCount = 600
//        ),
//        Person(
//            adult = false,
//            backdropPath = "/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg",
//            id = 11,
//            originalLanguage = "en",
//            originalTitle = "Original Title 12",
//            overview = "This is a dummy overview for movie 12.",
//            popularity = 120.0,
//            posterPath = "/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg",
//            releaseDate = "2023-12-01",
//            title = "Dummy Movie 12",
//            video = false,
//            voteAverage = 8.3,
//            voteCount = 650
//        ),
//        Person(
//            adult = false,
//            backdropPath = "/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg",
//            id = 12,
//            originalLanguage = "en",
//            originalTitle = "Original Title 13",
//            overview = "This is a dummy overview for movie 13.",
//            popularity = 130.0,
//            posterPath = "/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg",
//            releaseDate = "2024-01-01",
//            title = "Dummy Movie 13",
//            video = false,
//            voteAverage = 7.4,
//            voteCount = 700
//        ),
//        Person(
//            adult = false,
//            backdropPath = "/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg",
//            id = 13,
//            originalLanguage = "en",
//            originalTitle = "Original Title 14",
//            overview = "This is a dummy overview for movie 14.",
//            popularity = 140.0,
//            posterPath = "/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg",
//            releaseDate = "2024-02-01",
//            title = "Dummy Movie 14",
//            video = false,
//            voteAverage = 8.7,
//            voteCount = 750
//        ),
//        Person(
//            adult = false,
//            backdropPath = "/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg",
//            id = 14,
//            originalLanguage = "en",
//            originalTitle = "Original Title 15",
//            overview = "This is a dummy overview for movie 15.",
//            popularity = 150.0,
//            posterPath = "/3aYym07c0oJjg6h00ras1SCPKt9.jpg",
//            releaseDate = "2024-03-01",
//            title = "Dummy Movie 15",
//            video = false,
//            voteAverage = 7.8,
//            voteCount = 800
//        )
//    )

//    var peopleList = mutableStateListOf<Movie>().apply {
////        addAll(initialList)
//        addAll(initial2)
//    }

//    fun toggleSelection(index: Int) {
////        val item = peopleList[index]
////        peopleList[index] = item.copy(posterPath = item.backdropPath)
//        val list = people
//        people.forEachIndexed {i, item ->
//            if (i == index) {
//                people[i] = item.copy(posterPath = item.backdropPath)
//            }
//        }
//    }
//
////    fun toggleSelectionPeople(index: Int) {
////        val item = people[index]
////        people[index] = item.copy(posterPath = item.backdropPath)
////    }


}

//data class Person(
//    val adult: Boolean = false,
//    val backdropPath: String,
//    val id: Int,
//    val originalLanguage: String,
//    val originalTitle: String,
//    val overview: String,
//    val popularity: Double,
//    val posterPath: String,
//    val releaseDate: String,
//    val title: String,
//    val video: Boolean,
//    val voteAverage: Double,
//    val voteCount: Int,
//)
