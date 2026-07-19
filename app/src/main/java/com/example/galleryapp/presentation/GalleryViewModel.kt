package com.example.galleryapp.presentation

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.galleryapp.data.entities.GalleryImageEntity
import com.example.galleryapp.data.repositories.IGalleryRepository
import com.example.galleryapp.presentation.viewstate.GalleryViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update

class GalleryViewModel(
    private val application: Application,
    private val repository: IGalleryRepository,
) : ViewModel(), IGalleryViewModel {
    private val _viewStateFlow: MutableStateFlow<GalleryViewState> =
        MutableStateFlow(
            GalleryViewState(
                images = emptyList()
            )
        )

    override val viewStateFlow: StateFlow<GalleryViewState>
        get() = _viewStateFlow

    class Factory(
        private val application: Application,
        private val repository: IGalleryRepository
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val viewModel = GalleryViewModel(application, repository)
            return viewModel as T
        }

    }

    override fun dispatchGalleryViewEvent(event: GalleryViewEvent) {
        val updatedState = viewStateFlow.value.run {
            when(event) {
                GalleryViewEvent.FetchImages -> handleFetchImages()
            }
        }

        _viewStateFlow.update { updatedState }
    }

    private fun GalleryViewState.handleFetchImages(): GalleryViewState{
        return copy(images = repository.fetchImages(application.applicationContext))
    }
}