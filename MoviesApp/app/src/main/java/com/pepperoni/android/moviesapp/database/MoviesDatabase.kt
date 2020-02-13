package com.pepperoni.android.moviesapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pepperoni.android.moviesapp.model.Movie

@Database(entities = [Movie::class], version = 1)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
}