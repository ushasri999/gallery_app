package com.example.galleryapp.presentation.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.galleryapp.domain.GalleryImage
import com.example.galleryapp.presentation.GalleryViewEvent
import com.example.galleryapp.presentation.IGalleryViewModel
import com.example.galleryapp.presentation.viewstate.GalleryViewState

@Composable
fun GalleryScreen(viewModel: IGalleryViewModel) {
    val viewState by viewModel.viewStateFlow.collectAsStateWithLifecycle()
    val dispatchViewEvent = viewModel::dispatchGalleryViewEvent

    GalleryScreenInternal(viewState, dispatchViewEvent)
}

@Composable
fun GalleryScreenInternal(
    viewState: GalleryViewState,
    dispatchViewEvent: (GalleryViewEvent) -> Unit
) {
    if(viewState.isLoading) {
        LoadingScreen()
    }
    else if(viewState.mediaPermissionDenied) {
        PermissionAskingScreen(dispatchViewEvent)
    }
    else if(!viewState.images.isEmpty()) {
        GalleryGridScreen(viewState.images, dispatchViewEvent)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun GalleryGridScreen(
    images: List<GalleryImage>,
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
                items = images,
                key = { image -> image.id }
            ) { image ->
                Box{
                    GlideImage(
                        model = image.uri,
                        contentDescription = "Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        contentScale = ContentScale.Crop
                    )
                    Icon(
                        imageVector = if (image.isFavourite) {
                            Icons.Filled.Favorite
                        } else {
                            Icons.Default.FavoriteBorder
                        },
                        contentDescription = "Favourite icon",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .clickable {
                                dispatchViewEvent(
                                    GalleryViewEvent.ToggleFavourite(image.id)
                                )
                            }
                    )
                }

            }
        }
    }
}

@Composable
private fun PermissionAskingScreen(dispatchViewEvent: (GalleryViewEvent) -> Unit) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ){
        Text("No photo access", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Gallery app needs access to the photos on your device",
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Button(
            onClick = {dispatchViewEvent(GalleryViewEvent.AskMediaPermissions)}
        ) {
            Text("Change Permissions")
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp),
            color = Color.Gray
        )
    }
}

@Preview(device = "id:pixel_5", showBackground = true, showSystemUi = true)
@Composable
private fun PermissionAskingScreenPreview() {
    PermissionAskingScreen(
        dispatchViewEvent = {}
    )
}

@Preview(device = "id:pixel_5", showBackground = true, showSystemUi = true)
@Composable
private fun LoadingScreenPreview() {
    LoadingScreen()
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
