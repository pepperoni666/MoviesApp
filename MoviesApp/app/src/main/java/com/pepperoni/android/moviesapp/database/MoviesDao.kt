package com.pepperoni.android.moviesapp.database

import androidx.room.*
import com.pepperoni.android.moviesapp.model.Movie

@Dao
interface MoviesDao {

    @Query("SELECT * FROM favorites")
    fun getFavorites(): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorites(vararg movie: Movie)

    @Delete
    fun deleteFavorites(vararg movie: Movie)
}