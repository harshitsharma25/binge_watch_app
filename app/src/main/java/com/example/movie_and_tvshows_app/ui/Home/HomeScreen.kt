package com.example.movie_and_tvshows_app.ui.Home

import TvShowScreen
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.movie_and_tvshows_app.R
import com.example.movie_and_tvshows_app.ui.movie.MovieScreen



@Composable
fun HomeScreen(navController: NavHostController, colorScheme: ColorScheme) {

    var selectedTab by rememberSaveable { mutableStateOf(0) }

    // Main column to hold everything
    Scaffold(
        bottomBar = {
            // Bottom Navigation Bar using NavigationBar
            NavigationBar(containerColor = colorScheme.secondary) {
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_tv),
                            contentDescription = "Movies",
                            tint = if (selectedTab == 0) colorScheme.primary else colorScheme.tertiary
                        )
                    },
                    label = {
                        Text(
                            text = "Movies",
                            color = if (selectedTab == 0) Color.White else Color.Gray
                        )
                    },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_show),
                            contentDescription = "TV Shows",
                            tint = if (selectedTab == 1) colorScheme.primary else colorScheme.tertiary
                        )
                    },
                    label = {
                        Text(
                            text = "TV Shows",
                            color = if (selectedTab == 1) Color.White else Color.Gray
                        )
                    },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
            }
        }
    )  { innerPadding ->
        // Display content based on the selected tab
        Column(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                0 -> {
                    MovieScreen(navController)


                } // Replace with your actual Movies screen composable
                1 -> TvShowScreen(navController) // Replace with your actual TV Shows screen composable
            }
        }
    }
}