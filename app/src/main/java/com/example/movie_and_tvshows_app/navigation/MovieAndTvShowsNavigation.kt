package com.example.movie_and_tvshows_app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movie_and_tvshows_app.ui.Home.HomeScreen
import com.example.movie_and_tvshows_app.ui.movie.MovieDetailsScreen
import com.example.movie_and_tvshows_app.viewmodel.MovieViewModel
import com.example.movie_and_tvshows_app.ui.splash.SplashScreen
import com.example.movie_and_tvshows_app.ui.tvShows.TvShowDetailsScreen
import androidx.compose.ui.Modifier


@Composable
fun MovieAndTvShowsNavigation(colorScheme: ColorScheme,viewModel: MovieViewModel = hiltViewModel()){

    val navController : NavHostController = rememberNavController()
    val tvShows by viewModel.tvShows.observeAsState(emptyList())

    NavHost(
        navController = navController,
        startDestination = MovieAndTvScreens.SplashScreen.name
    ){
        composable(route = MovieAndTvScreens.SplashScreen.name){
            SplashScreen(navController)
        }

        composable(route = MovieAndTvScreens.HomeScreen.name){
            HomeScreen(navController,colorScheme)
        }

        composable(
            route = "${MovieAndTvScreens.MovieDetailScreen.name}/{movieId}",
            arguments = listOf(
                navArgument("movieId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId")
            val movie = movieId?.let { viewModel.getMovieById(it) }
            //val movie = movieList?.let { viewModel.getMovieById(it) } // Find movie by ID

            if (movie != null) {
                MovieDetailsScreen(navController = navController, movie = movie)
            }
        }

        composable(
            route = "${MovieAndTvScreens.TvShowDetailScreen.name}/{tvShowId}",
            arguments = listOf(
                navArgument("tvShowId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val tvShowId = backStackEntry.arguments?.getInt("tvShowId")
            // Get the TV show from ViewModel using the ID
            val tvShow = tvShowId?.let { viewModel.getTvShowById(it) }

            if (tvShow != null) {
                TvShowDetailsScreen(navController = navController, tvShow = tvShow)
            } else {
                // Show error or loading state
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "TV Show not found",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Red
                    )
                }
            }
        }

    }
}