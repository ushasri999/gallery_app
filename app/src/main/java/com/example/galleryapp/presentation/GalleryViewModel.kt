package com.example.galleryapp.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.galleryapp.data.repositories.IGalleryRepository
import com.example.galleryapp.domain.GalleryImage
import com.example.galleryapp.presentation.viewstate.GalleryViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
        viewModelScope.launch {
            val updatedState = viewStateFlow.value.run {
                when(event) {
                    GalleryViewEvent.FetchImages -> handleFetchImages()
                    GalleryViewEvent.AskMediaPermissions -> handleAskMediaPermissions()
                    GalleryViewEvent.MediaPermissionsAsked -> handleMediaPermissionsAsked()
                    GalleryViewEvent.MediaPermissionDenied -> handleMediaPermissionDenied()
                    is GalleryViewEvent.ToggleFavourite -> handleToggleFavourite(event.imageId)
                }
            }

            _viewStateFlow.update { updatedState }
        }
    }

    private fun onImagesFetchCompleted(images: List<GalleryImage>) {
        _viewStateFlow.update {
            it.copy(
                images = images,
                isLoading = false
            )
        }
    }

    private fun GalleryViewState.handleFetchImages(): GalleryViewState {
        viewModelScope.launch {
            repository.fetchImages(application.applicationContext, ::onImagesFetchCompleted)
        }
        return copy(
            isLoading = true,
            galleryEvent = galleryEvent.copy(
                shouldAskMediaPermission = false
            )
        )
    }

    private fun GalleryViewState.handleAskMediaPermissions(): GalleryViewState {
        return copy(
            galleryEvent = galleryEvent.copy(
                shouldAskMediaPermission = true
            )
        )
    }

    private fun GalleryViewState.handleMediaPermissionsAsked(): GalleryViewState {
        return copy(
            galleryEvent = galleryEvent.copy(
                shouldAskMediaPermission = false
            )
        )
    }

    private fun GalleryViewState.handleMediaPermissionDenied(): GalleryViewState {
        return copy(mediaPermissionDenied = true)
    }

    private suspend fun GalleryViewState.handleToggleFavourite(imageId: Long): GalleryViewState {
        return copy(
            images = images.map { image ->
                if(image.id == imageId) {
                    if(repository.isInFavourites(imageId)) {
                        repository.removeFromFavourites(imageId)
                    } else {
                        repository.addToFavourites(imageId)
                    }
                    image.copy(isFavourite = !image.isFavourite)
                } else {
                    image
                }
            }
        )
    }
}