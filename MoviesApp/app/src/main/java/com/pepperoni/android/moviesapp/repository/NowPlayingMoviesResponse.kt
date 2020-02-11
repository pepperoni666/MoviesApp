package com.pepperoni.android.moviesapp.repository

import com.pepperoni.android.moviesapp.model.Movie

data class NowPlayingMoviesResponse (
    val results: List<Movie>
)