package com.example.movie_and_tvshows_app.ui.movie

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.movie_and_tvshows_app.data.UiState
import com.example.movie_and_tvshows_app.model.movies.Release
import com.example.movie_and_tvshows_app.navigation.MovieAndTvScreens
import com.example.movie_and_tvshows_app.utils.ShimmerMovieCard
import com.example.movie_and_tvshows_app.viewmodel.MovieViewModel


@Composable
fun MovieScreen(
    navController: NavHostController,
    viewModel: MovieViewModel = hiltViewModel()
) {
    var isLoading by remember { mutableStateOf(true) } // State to track loading
    val movies by viewModel.movies.observeAsState(emptyList())
    val uiState by viewModel.uiState.observeAsState(UiState.Loading)

    // Simulate loading time with a 3-second delay
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1500) // Wait for 3 seconds
        isLoading = false // Stop shimmer effect
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            text = "Watch Movies",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 12.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(
                movies.size
            ) { index ->
                if (isLoading) {
                    ShimmerMovieCard()
                } else {
                    MovieCard(
                        movie = movies[index],
                        onClick = {
                            navController.navigate("${MovieAndTvScreens.MovieDetailScreen.name}/${movies[index].id}")
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun MovieCard(movie: Release, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = movie.poster_url,
                contentDescription = movie.title,
                modifier = Modifier.fillMaxSize().height(180.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

