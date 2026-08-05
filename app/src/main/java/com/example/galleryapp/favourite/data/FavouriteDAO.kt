package com.example.galleryapp.favourite.data

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query

@Dao
interface FavouriteDAO {
    @Insert
    suspend fun addToFavourites(favouriteEntity: FavouriteEntity)

    @Query("DELETE FROM FavouriteEntity WHERE id = :imageId")
    suspend fun removeFromFavourites(imageId: Long)

    @Query("SELECT * FROM FavouriteEntity WHERE id = :imageId")
    suspend fun isInFavourites(imageId: Long): FavouriteEntity?
}