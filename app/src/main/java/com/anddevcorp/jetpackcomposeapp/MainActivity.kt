package com.anddevcorp.jetpackcomposeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.anddevcorp.jetpackcomposeapp.data.request.getRequestModel
import com.anddevcorp.jetpackcomposeapp.model.Movie
import com.anddevcorp.jetpackcomposeapp.model.MovieUiState
import com.anddevcorp.jetpackcomposeapp.ui.ext.GetImage
import com.anddevcorp.jetpackcomposeapp.ui.ext.customBorderShape
import com.anddevcorp.jetpackcomposeapp.ui.ext.customShape
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this, defaultViewModelProviderFactory)[MovieViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel.getMovies(getRequestModel())
            MovieScreen(viewModel)

        }
    }
}


@Composable
fun MovieScreen(viewModel: MovieViewModel) {
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
                MovieList(movies.results)
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
fun MovieItem(movie: Movie) {

    var isSelected by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(5.dp)
            .clickable {
                isSelected = !isSelected
            }
            .border(2.dp, Color.Black, customBorderShape)
            .background(Color.White, customShape)
            .height(200.dp)
            .clip(customShape)
    ) {
        AnimatedVisibility(
            visible = isSelected,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { if (isSelected) -it else it })
        ) {
            Column(
                modifier = Modifier
                    .animateContentSize()
                    .padding(5.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = movie.originalTitle,
                    style = TextStyle(
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(text = movie.overview)
            }
        }
        AnimatedVisibility(
            visible = !isSelected,
            enter = slideInVertically(initialOffsetY = { if (isSelected) it else -it }),
            exit = slideOutVertically(targetOffsetY = { -it })
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
}


@Composable
fun MovieList(movies: List<Movie>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(10.dp),
    ) {
        item(span = { GridItemSpan(3) }) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Divider(modifier = Modifier.fillMaxWidth(), thickness = 2.dp)
                Text(
                    "Recomposition", style = TextStyle(
                        color = Color.White, fontWeight = FontWeight.Bold
                    )
                )
            }
        }
        items(movies) { movie ->
            MovieItem(movie = movie)
        }
        item(span = { GridItemSpan(3) }) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Divider(modifier = Modifier.fillMaxWidth(), thickness = 2.dp)
                Text(
                    "Composition", style = TextStyle(
                        color = Color.White, fontWeight = FontWeight.Bold
                    )
                )
            }
        }
        items(movies) { movie ->
            MovieItemComposition(movie = movie)
        }
    }
}

@Composable
fun MovieItemComposition(movie: Movie) {
    Box(
        modifier = Modifier
            .padding(5.dp)
            .border(2.dp, Color.Black, customBorderShape)
            .background(Color.White, customShape)
            .height(200.dp)
            .clip(customShape)
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

//// recomposition error
//@Composable
//fun MovieItem(movie: Movie, change: Boolean, onClick: () -> Unit ) {
//
//    var isSelected by remember { mutableStateOf(false) }
//
//    Box(
//        modifier = Modifier
//            .padding(5.dp)
//            .clickable {
//                isSelected = !change
//                onClick()
//            }
//            .border(2.dp, Color.Black, customBorderShape)
//            .background(Color.White, customShape)
//            .height(200.dp)
//            .clip(customShape)
//    ) {
//        AnimatedVisibility(
//            visible = isSelected,
//            enter = slideInVertically(initialOffsetY = { it }),
//            exit =  slideOutVertically(targetOffsetY = { if (isSelected) -it else it })
//        ) {
//            Column(
//                modifier = Modifier
//                    .animateContentSize()
//                    .padding(5.dp)
//            ) {
//                Text(text = movie.originalTitle)
//                Text(text = movie.overview)
//            }
//        }
//        AnimatedVisibility(
//            visible = !isSelected,
//            enter = slideInVertically(initialOffsetY = { if (isSelected) it else -it }),
//            exit = slideOutVertically(targetOffsetY = { -it })
//        ) {
//            GetImage(
//                movie.posterPath,
//                modifier = Modifier
//                    .align(Alignment.Center)
//                    .fillMaxSize()
//                    .scale(1.8f)
//                    .aspectRatio(1f)
//            )
//        }
//    }
//}
//
//
//@Composable
//fun MovieList(movies: List<Movie>) {
//    var change by remember {
//        mutableStateOf(false)
//    }
//    LazyVerticalGrid(
//        columns = GridCells.Fixed(3),
//        contentPadding = PaddingValues(10.dp),
//    ) {
//        items(movies) { movie ->
//            MovieItem(movie = movie, change, onClick = {
//                change = true
//            })
//        }
//    }
//}
