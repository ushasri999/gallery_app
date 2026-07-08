package com.example.galleryapp.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.galleryapp.ui.theme.GalleryAppTheme

@Composable
fun GalleryScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Text(
            text = "Gallery App",
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Preview(device = "id:pixel_5", showBackground = true, showSystemUi = true)
@Composable
fun GalleryScreenPreview() {
    GalleryAppTheme {
        GalleryScreen()
    }
}
