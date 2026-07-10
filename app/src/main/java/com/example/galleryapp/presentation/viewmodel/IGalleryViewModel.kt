package com.example.galleryapp.presentation.viewmodel

import com.example.galleryapp.data.entities.GalleryImageEntity

interface IGalleryViewModel {
    fun fetchImages(): List<GalleryImageEntity>

}