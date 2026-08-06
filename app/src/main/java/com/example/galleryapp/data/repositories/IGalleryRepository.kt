package com.example.galleryapp.data.repositories

import android.content.Context
import com.example.galleryapp.domain.GalleryImage

interface IGalleryRepository {
    suspend fun fetchImages(context: Context): List<GalleryImage>
    suspend fun isInFavourites(imageId: Long): Boolean
    suspend fun addToFavourites(imageId: Long)
    suspend fun removeFromFavourites(imageId: Long)
}