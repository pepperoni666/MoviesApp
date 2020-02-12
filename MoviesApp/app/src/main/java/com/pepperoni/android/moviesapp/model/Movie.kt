package com.pepperoni.android.moviesapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class Movie(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int = 11111,
    @Ignore
    val title:String = "",
    @Ignore
    val poster_path: String = "",
    @Ignore
    val release_date: String = "",
    @Ignore
    val vote_average: Double = -1.0,
    @Ignore
    var isFavorite: Boolean = false
)