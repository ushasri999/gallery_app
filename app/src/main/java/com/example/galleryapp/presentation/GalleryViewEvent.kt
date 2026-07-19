package com.example.galleryapp.presentation

sealed class GalleryViewEvent {
    data object FetchImages: GalleryViewEvent()
}