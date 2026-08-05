package com.example.galleryapp.data.database

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.galleryapp.favourite.data.FavouriteDAO
import com.example.galleryapp.favourite.data.FavouriteEntity

@Database(entities = [FavouriteEntity::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun favouritesDao(): FavouriteDAO
}