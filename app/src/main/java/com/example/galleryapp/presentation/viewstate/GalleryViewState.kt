package com.example.galleryapp.presentation.viewstate

import com.example.galleryapp.domain.GalleryImage

data class GalleryViewState(
    val isLoading: Boolean = false,
    val mediaPermissionDenied: Boolean = false,
    val images: List<GalleryImage>,
    val galleryEvent: GalleryEvent = GalleryEvent()
)

data class GalleryEvent(
    val shouldAskMediaPermission: Boolean = false
)
