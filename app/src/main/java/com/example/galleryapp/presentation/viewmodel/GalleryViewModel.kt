package com.example.galleryapp.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.galleryapp.data.entities.GalleryImageEntity
import com.example.galleryapp.data.repositories.IGalleryRepository

class GalleryViewModel(
    private val application: Application,
    private val repository: IGalleryRepository
) : ViewModel(), IGalleryViewModel {

    class Factory(
        private val application: Application,
        private val repository: IGalleryRepository
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val viewModel = GalleryViewModel(application, repository)
            return viewModel as T
        }

    }

    override fun fetchImages(): List<GalleryImageEntity> {
        return repository.fetchImages(application.applicationContext);
    }
}