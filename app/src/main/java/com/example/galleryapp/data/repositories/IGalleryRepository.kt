package com.example.galleryapp.data.repositories

import android.content.Context
import com.example.galleryapp.domain.GalleryImage

interface IGalleryRepository {
    fun fetchImages(context: Context): List<GalleryImage>
}