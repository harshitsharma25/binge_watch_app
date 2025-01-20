
import android.content.Context
import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.movie_and_tvshows_app.data.UiState
import com.example.movie_and_tvshows_app.model.tvShows.TvShowResponseItem
import com.example.movie_and_tvshows_app.navigation.MovieAndTvScreens
import com.example.movie_and_tvshows_app.viewmodel.MovieViewModel
import com.example.movie_and_tvshows_app.utils.shimmerEffect


@Composable
fun TvShowScreen(
    navController: NavHostController,
    viewModel: MovieViewModel = hiltViewModel()
) {
    // Observe tvShows and uiState
    val tvShows by viewModel.tvShows.observeAsState(emptyList())
    val uiState by viewModel.uiState.observeAsState(UiState.Loading)
    val context : Context = LocalContext.current

    // LaunchedEffect to trigger data load if needed
    LaunchedEffect(Unit) {
        if (tvShows.isEmpty()) {
            viewModel.loadMoviesAndTvShows()
        }
    }

    // Log the tvShows list
    if (tvShows.isNotEmpty()) {
        for (i in tvShows) {
            Log.d("tvshow", "Name is ${i.name} and overview is ${i.overview}")
        }
    } else {
        Log.d("tvshow", "List is empty")
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Tv Shows",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp)) // Space between title and grid

        when (uiState) {
            is UiState.Loading -> {
                ShimmerTvShowList()
            }

            is UiState.Success -> {
                if (tvShows.isEmpty()) {
                    Text(
                        text = "No TV shows available",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(tvShows) { show ->
                            ShowCard(
                                show = show,
                                onClick = {
                                    navController.navigate("${MovieAndTvScreens.TvShowDetailScreen.name}/${show.id}")
                                }
                            )
                        }
                    }
                }
            }


            is UiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = (uiState as UiState.Error).message,
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadMoviesAndTvShows() }) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}



@Composable
fun ShowCard(show: TvShowResponseItem, onClick: () -> Unit) {
    var isHovered by remember { mutableStateOf(false) }
    var isTextVisible by remember { mutableStateOf(false) }

    val elevation by animateDpAsState(
        targetValue = if (isHovered) 12.dp else 6.dp,
        animationSpec = tween(durationMillis = 300)
    )

    LaunchedEffect(Unit) {
        isTextVisible = true
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(300.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(elevation)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Display image using Coil or another image loader
            Image(
                painter = rememberAsyncImagePainter(show.thumbnail_url),
                contentDescription = show.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Text(
                    text = show.name,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 1.2.sp
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                        .animateContentSize()
                        .graphicsLayer(alpha = if (isTextVisible) 1f else 0f)
                )
            }
        }
    }
}


@Composable
fun ShimmerItem() {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(300.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Main image area shimmer
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmerEffect()
            )

            // Text area container with dark overlay
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                // Text shimmer
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .width(120.dp)
                        .height(20.dp)
                        .shimmerEffect()
                )
            }
        }
    }
}

@Composable
fun ShimmerTvShowList() {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(10) {
            ShimmerItem()
        }
    }
}

