package com.example.galleryapp.data.repositories.api

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.example.galleryapp.data.entities.GalleryImageEntity
import com.example.galleryapp.data.repositories.IGalleryRepository
import com.example.galleryapp.domain.GalleryImage
import com.example.galleryapp.favourite.data.FavouriteDAO
import com.example.galleryapp.favourite.data.FavouriteEntity

class GalleryRepositoryApi(
    private val favouriteDAO: FavouriteDAO
): IGalleryRepository {
    override suspend fun fetchImages(
        context: Context,
        onImagesFetchCompleted: (List<GalleryImage>) -> Unit
    ) {
        val images = mutableListOf<GalleryImageEntity>()

        val projection = arrayOf(MediaStore.Images.Media._ID)
        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )?.use { cursor ->
            val columId = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            while(cursor.moveToNext()) {
                val id = cursor.getLong(columId)
                val uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                images.add(
                    GalleryImageEntity(
                        id = id,
                        uri = uri
                    )
                )
            }
        }

        onImagesFetchCompleted(images.map { it.toDomain() })
    }

    override suspend fun isInFavourites(imageId: Long): Boolean {
        return favouriteDAO.isInFavourites(imageId) != null
    }

    override suspend fun addToFavourites(imageId: Long) {
        favouriteDAO.addToFavourites(
            FavouriteEntity(
                id = imageId,
                true
            )
        )
    }

    override suspend fun removeFromFavourites(imageId: Long) {
        favouriteDAO.removeFromFavourites(imageId)
    }


    private suspend fun GalleryImageEntity.toDomain(): GalleryImage {
        return GalleryImage(
            id = id,
            uri = uri,
            isFavourite = isInFavourites(id)
        )
    }

}
