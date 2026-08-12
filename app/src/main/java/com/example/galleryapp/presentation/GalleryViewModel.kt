package com.example.galleryapp.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.galleryapp.data.repositories.IGalleryRepository
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
                images = emptyList(),
                filteredImages = emptyList()
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
                    is GalleryViewEvent.FilterAll -> handleFilterAll()
                    is GalleryViewEvent.FilterFavourites -> handleFilterFavourites()
                }
            }

            _viewStateFlow.update { updatedState }
        }
    }

    private fun GalleryViewState.handleFetchImages(): GalleryViewState {
        viewModelScope.launch {
            val images = repository.fetchImages(application.applicationContext)
            _viewStateFlow.update {
                it.copy(
                    filteredImages = images,
                    images = images,
                    isLoading = false
                )
            }
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
        val updatedImages = images.map { image ->
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

        val isInFilteredImages = filteredImages.any {
            it.id == imageId
        }

        val updatedFilteredImages = if (isInFilteredImages) {
            filteredImages.filter {
                it.id != imageId
            }
        } else {
            val imageToAdd = updatedImages.first {
                it.id == imageId
            }

            filteredImages + imageToAdd
        }

        return copy(
            images = updatedImages,
            filteredImages = updatedFilteredImages
        )
    }

    private fun GalleryViewState.handleFilterAll(): GalleryViewState {
        return copy(
            filteredImages = images
        )
    }

    private fun GalleryViewState.handleFilterFavourites(): GalleryViewState {
        return copy(
            filteredImages = images.filter { it.isFavourite }
        )
    }
}