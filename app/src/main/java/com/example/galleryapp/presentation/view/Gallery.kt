package com.example.galleryapp.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.galleryapp.presentation.GalleryViewEvent
import com.example.galleryapp.presentation.IGalleryViewModel
import com.example.galleryapp.presentation.viewstate.GalleryViewState

@Composable
fun GalleryScreen(viewModel: IGalleryViewModel) {
    val viewState by viewModel.viewStateFlow.collectAsStateWithLifecycle()
    val dispatchViewEvent = viewModel::dispatchGalleryViewEvent

    GalleryScreenInternal(viewState, dispatchViewEvent)
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GalleryScreenInternal(
    viewState: GalleryViewState,
    dispatchViewEvent: (GalleryViewEvent) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar() },

        ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(
                items = viewState.images,
                key = { image -> image.id }
            ) { image ->
                GlideImage(
                    model = image.uri,
                    contentDescription = "Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(title = { Text("Gallery App") })
}

//@Preview(device = "id:pixel_5", showBackground = true, showSystemUi = true)
//@Composable
//fun GalleryScreenPreview() {
//    GalleryAppTheme {
//        GalleryScreen()
//    }
//}
