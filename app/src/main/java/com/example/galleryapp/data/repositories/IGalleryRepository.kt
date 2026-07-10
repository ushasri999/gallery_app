package com.example.galleryapp.data.repositories

import android.content.Context
import com.example.galleryapp.data.entities.GalleryImageEntity

interface IGalleryRepository {
    fun fetchImages(context: Context): List<GalleryImageEntity>
}