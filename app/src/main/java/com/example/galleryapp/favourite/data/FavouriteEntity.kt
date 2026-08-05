package com.example.galleryapp.favourite.data

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity
data class FavouriteEntity(
    @PrimaryKey
    val id: Long,

    @ColumnInfo(name = "is_favourite")
    val isFavourite: Boolean
)