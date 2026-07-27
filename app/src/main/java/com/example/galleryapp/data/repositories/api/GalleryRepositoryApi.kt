package com.example.galleryapp.data.repositories.api

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.example.galleryapp.data.entities.GalleryImageEntity
import com.example.galleryapp.data.repositories.IGalleryRepository
import com.example.galleryapp.domain.GalleryImage

class GalleryRepositoryApi: IGalleryRepository {
    override fun fetchImages(context: Context): List<GalleryImage> {
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

        return images.map { it.toDomain() }
    }

    private fun GalleryImageEntity.toDomain(): GalleryImage {
        return GalleryImage(id, uri);
    }

}