package com.pepperoni.android.moviesapp.repository

import com.pepperoni.android.moviesapp.model.Movie

data class MoviesResponse (
    val results: List<Movie>
)