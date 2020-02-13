package com.pepperoni.android.moviesapp.viewmodel

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.ViewModelContext
import com.pepperoni.android.moviesapp.MoviesApp
import com.pepperoni.android.moviesapp.model.Movie
import com.pepperoni.android.moviesapp.model.MoviesState
import com.pepperoni.android.moviesapp.repository.MoviesRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

class MoviesViewModel(
    state: MoviesState,
    private val moviesRepository: MoviesRepository
) : MvRxViewModel<MoviesState>(state) {

    private var searchingJob: Job? = null

    init {
        setState {
            copy(
                nowPlaying = Loading(),
                favorites = Loading()
            )
        }
        moviesRepository.launch {
            val movieList = moviesRepository.getMoviesNowPlaying()
            setState {
                copy(nowPlaying = Success(movieList))
            }
        }
        moviesRepository.launch {
            val favoriteList = moviesRepository.getFavorites()
            setState {
                copy(favorites = Success(favoriteList))
            }
        }
    }

    fun searchQueryUpdated(query: String) {
        setState {
            copy(searchSuggestedMovies = Loading())
        }
        searchingJob?.let {
            if (it.isActive) {
                it.cancel("newSearchQuery")
            }
        }
        searchingJob = moviesRepository.launch {
            val movieList = moviesRepository.getSearch(query)
            setState {
                copy(searchSuggestedMovies = Success(movieList))
            }
        }
    }

    fun changeIsFavoriteFlag(movie: Movie) = moviesRepository.launch {
        setState {
            changeIsFavoriteFlagSearchResultItem(movie.id)
            val favorites = ArrayList(favorites() ?: listOf())
            if (movie.isFavorite) {
                moviesRepository.db.moviesDao().insertFavorites(movie)
                favorites.add(movie)
            } else {
                moviesRepository.db.moviesDao().deleteFavorites(movie)
                favorites.remove(favorites.find { it.id == movie.id })
            }
            copy(
                nowPlaying = Success(changeIsFavoriteFlagNowPlayingItem(movie.id)),
                favorites = Success(favorites)
            )
        }
    }

    fun refreshFavorites() {
        setState {
            copy(favorites = Loading())
        }
        moviesRepository.launch {
            val list = moviesRepository.getFavorites()
            setState {
                copy(favorites = Success(list))
            }
        }
    }

    fun refreshNowPlaying() {
        setState {
            copy(nowPlaying = Loading())
        }
        moviesRepository.launch {
            val list = moviesRepository.getMoviesNowPlaying()
            setState {
                copy(nowPlaying = Success(list))
            }
        }
    }

    fun loadMoreOfNowPlaying() {
        moviesRepository.launch {
            val newMovies = moviesRepository.loadMoreForNowPlaying()
            setState {
                val allMovies: ArrayList<Movie> = ArrayList(nowPlaying() ?: listOf())
                allMovies.addAll(newMovies)
                copy(nowPlaying = Success(allMovies))
            }
        }
    }

    companion object : MvRxViewModelFactory<MoviesViewModel, MoviesState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: MoviesState
        ): MoviesViewModel? {
            val db = viewModelContext.app<MoviesApp>().db
            return MoviesViewModel(
                state,
                MoviesRepository(db)
            )
        }
    }
}