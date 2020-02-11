package com.pepperoni.android.moviesapp.model

data class Movie(
    val id: Int,
    val title:String,
    val poster_path: String,
    val release_date: String,
    val vote_average: Double
)