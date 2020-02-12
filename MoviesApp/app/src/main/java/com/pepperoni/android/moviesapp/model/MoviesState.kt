package com.pepperoni.android.moviesapp.model

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized

data class MoviesState(
    val nowPlaying: Async<List<Movie>> = Uninitialized,
    val favorites: Async<List<Movie>> = Uninitialized,
    val searchSuggestedMovies: Async<List<Movie>> = Uninitialized,
    var isSearching: Boolean = false
) : MvRxState {

    fun changeIsFavoriteFlagSearchResultItem(movieId: Int) {
        searchSuggestedMovies()?.firstOrNull { it.id == movieId }
            ?.let { it.isFavorite = !it.isFavorite }

    }

    fun changeIsFavoriteFlagNowPlayingItem(movieId: Int): List<Movie> {
        return nowPlaying()?.also { list ->
            list.firstOrNull { it.id == movieId }
                ?.let { it.isFavorite = !it.isFavorite }
        } ?: listOf()

    }
}