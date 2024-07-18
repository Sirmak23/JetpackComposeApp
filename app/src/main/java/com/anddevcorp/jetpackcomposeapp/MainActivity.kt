package com.anddevcorp.jetpackcomposeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.anddevcorp.jetpackcomposeapp.data.request.getRequestModel
import com.anddevcorp.jetpackcomposeapp.model.Movie
import com.anddevcorp.jetpackcomposeapp.model.MovieUiState
import com.anddevcorp.jetpackcomposeapp.ui.ext.GetImage
import com.anddevcorp.jetpackcomposeapp.ui.ext.customBorderShape
import com.anddevcorp.jetpackcomposeapp.ui.ext.customShape
import com.anddevcorp.jetpackcomposeapp.ui.theme.MyTryAppTheme
import com.theapache64.rebugger.Rebugger
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MovieViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTryAppTheme {
                MovieScreen()
            }
        }
    }
}

@Composable
fun MovieScreen(
    viewModel: MovieViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.getMovies(getRequestModel())
    }
    val movieUiState by viewModel.movies.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        when (movieUiState) {
            is MovieUiState.Success -> {
                val movies = (movieUiState as MovieUiState.Success).movies
                MovieList(movies = movies, onClick = { index ->
                    viewModel.changeData(index)
                })
            }

            MovieUiState.Empty -> {
                Text(
                    text = "No Movie available",
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }

            is MovieUiState.Error -> {
                Text(
                    text = "Error: ${(movieUiState as MovieUiState.Error).error}",
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }

            is MovieUiState.Loading -> {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, position: Int, onClick: (Int) -> Unit) {
    Rebugger(trackMap = mapOf("movie" to movie, "onClick" to onClick))

    Box(
        modifier = Modifier
            .padding(5.dp)
            .border(2.dp, Color.Black, customBorderShape)
            .background(Color.White, customShape)
            .height(200.dp)
            .clip(customShape)
            .clickable { onClick(position) }
    ) {
        GetImage(
            movie.posterPath,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .scale(1.8f)
                .aspectRatio(1f)
        )
    }
}

@Composable
fun MovieList(
    movies: List<Movie>,
    onClick: (Int) -> Unit
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(10.dp),
    ) {
        itemsIndexed(
            movies,
            key = { _, movie -> movie.id },
            span = { _, _ -> GridItemSpan(1) }
        ) { index, movie ->
            MovieItem(movie = movie, position = index, onClick = onClick)
        }
    }
}

