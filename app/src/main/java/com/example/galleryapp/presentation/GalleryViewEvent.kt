package com.example.galleryapp.presentation

sealed class GalleryViewEvent {
    data object FetchImages: GalleryViewEvent()
    data object AskMediaPermissions: GalleryViewEvent()
    data object MediaPermissionsAsked: GalleryViewEvent()
    data object MediaPermissionDenied: GalleryViewEvent()
}