package com.example.movie_and_tvshows_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.movie_and_tvshows_app.navigation.MovieAndTvShowsNavigation
import com.example.movie_and_tvshows_app.ui.theme.Movie_and_Tvshows_appTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Movie_and_Tvshows_appTheme {
                var colorscheme = MaterialTheme.colorScheme
                    MovieAndHomeScreenApp(colorscheme)
            }
        }
    }
}

@Composable
fun MovieAndHomeScreenApp(colorscheme: ColorScheme) {
    Surface(color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()){

        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
            MovieAndTvShowsNavigation(colorscheme)
        }
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Movie_and_Tvshows_appTheme {
        MovieAndTvShowsNavigation(colorScheme = MaterialTheme.colorScheme)
    }
}