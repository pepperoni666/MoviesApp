package com.pepperoni.android.moviesapp.model.search

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.pepperoni.android.moviesapp.model.Movie

data class SearchState(
    val searchSuggestedMovies: Async<List<Movie>> = Uninitialized
) : MvRxState