package com.example.galleryapp.presentation.viewstate

import com.example.galleryapp.data.entities.GalleryImageEntity

data class GalleryViewState(
    val isLoading: Boolean = false,
    val mediaPermissionDenied: Boolean = false,
    val images: List<GalleryImageEntity>,
    val galleryEvent: GalleryEvent = GalleryEvent()
)

data class GalleryEvent(
    val shouldAskMediaPermission: Boolean = false
)
