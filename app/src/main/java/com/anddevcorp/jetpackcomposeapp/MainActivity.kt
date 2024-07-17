package com.anddevcorp.jetpackcomposeapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anddevcorp.jetpackcomposeapp.data.request.getRequestModel
import com.anddevcorp.jetpackcomposeapp.model.Movie
import com.anddevcorp.jetpackcomposeapp.model.MovieUiState
import com.anddevcorp.jetpackcomposeapp.ui.ext.GetImage
import com.anddevcorp.jetpackcomposeapp.ui.ext.customBorderShape
import com.anddevcorp.jetpackcomposeapp.ui.ext.customShape
import com.theapache64.rebugger.Rebugger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MovieViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

//            MovieListScreen()
            MovieScreen(
                onClick = { index ->
                    viewModel.changeData(index)
                }
            )
//            viewModel.getMovies(getRequestModel())
//            val people = viewModel.peopleList
//            ListScreen(
//                people = people,
//                onItemClick = {
//                    viewModel.toggleSelection(it)
//                }
//            )
        }
    }
}

@Composable
fun MovieScreen(
    viewModel: MovieViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onClick: (Int) -> Unit
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
                MovieList(movies = movies, onClick = onClick)
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
fun MovieItem(movie: Movie, position:Int, onClick: (Int) -> Unit) {
    Rebugger(trackMap = mapOf("movie" to movie, "onClick" to onClick))
    SideEffect {
        Log.d("MyComposable", "Recomposed!")
    }
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

//
//@Composable
//fun MovieItem(movie: Movie, onClick: () -> Unit) {
//    Rebugger(mapOf("movie" to movie, "onClick" to onClick))
//    var isSelected by remember { mutableStateOf(false) }
//
//    Box(
//        modifier = Modifier
//            .padding(5.dp)
//            .clickable {
//                isSelected = !isSelected
//                onClick()
//            }
//            .border(2.dp, Color.Black, customBorderShape)
//            .background(Color.White, customShape)
//            .height(200.dp)
//            .clip(customShape)
//    ) {
//        AnimatedVisibility(
//            visible = false, // isSelected,
//            enter = slideInVertically(initialOffsetY = { it }),
//            exit = slideOutVertically(targetOffsetY = { if (isSelected) -it else it })
//        ) {
//            Column(
//                modifier = Modifier
//                    .animateContentSize()
//                    .padding(5.dp)
//                    .verticalScroll(rememberScrollState())
//            ) {
//                Text(
//                    modifier = Modifier.align(Alignment.CenterHorizontally),
//                    text = movie.originalTitle,
//                    style = TextStyle(
//                        color = Color.Red,
//                        fontWeight = FontWeight.Bold
//                    )
//                )
//                Text(text = movie.overview)
//            }
//        }
//        AnimatedVisibility(
//            visible = true , //!isSelected,
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
            span = { _, _ -> GridItemSpan(1) }) { index, movie ->
            MovieItem(movie = movie, position = index, onClick = onClick)
        }
    }
}

@Composable
fun ListScreen(people: List<Movie>, onItemClick: (Int) -> Unit) {
    Rebugger(trackMap = mapOf("movie" to people, "onClick" to onItemClick))
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(items = people, key = { index, item ->  item.id }) {index, it ->
            ListItem(item = it, onItemClick = {onItemClick(index)})
        }
    }
}

@Composable
fun ListItem(item: Movie, onItemClick: () -> Unit) {
    Column(
        modifier = Modifier.border(3.dp, Color.Black)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onItemClick()
                }
                .padding(8.dp)
        ) {
            Text("Index: Name ${item.title}", fontSize = 20.sp)
            GetImage(
                imageUrl = item.posterPath,
                modifier = Modifier
                    .align(Alignment.Center)
                    .height(50.dp)
                    .width(50.dp)
                    .scale(1.8f)
                    .aspectRatio(1f)
            )
        }
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
