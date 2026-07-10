package com.example.galleryapp.presentation

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.galleryapp.data.entities.GalleryImageEntity

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GalleryScreen(images: List<GalleryImageEntity>) {
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
