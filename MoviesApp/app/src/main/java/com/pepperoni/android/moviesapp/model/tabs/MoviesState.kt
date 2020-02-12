package com.pepperoni.android.moviesapp.model.tabs

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.pepperoni.android.moviesapp.model.Movie

data class MoviesState(
    val movies: Async<List<Movie>> = Uninitialized,
    val favorites: Async<List<Movie>> = Uninitialized
): MvRxState