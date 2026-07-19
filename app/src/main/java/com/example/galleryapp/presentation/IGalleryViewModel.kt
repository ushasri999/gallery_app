package com.example.galleryapp.presentation

import com.example.galleryapp.presentation.viewstate.GalleryViewState
import kotlinx.coroutines.flow.StateFlow


interface IGalleryViewModel {
    val viewStateFlow: StateFlow<GalleryViewState>
    fun dispatchGalleryViewEvent(event: GalleryViewEvent)
}