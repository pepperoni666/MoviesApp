package com.pepperoni.android.moviesapp.viewmodel

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.ViewModelContext
import com.pepperoni.android.moviesapp.MoviesApp
import com.pepperoni.android.moviesapp.model.Movie
import com.pepperoni.android.moviesapp.model.MoviesState
import com.pepperoni.android.moviesapp.repository.MoviesRepository
import kotlinx.coroutines.launch

class MoviesViewModel(
    state: MoviesState,
    private val moviesRepository: MoviesRepository
) : MvRxViewModel<MoviesState>(state) {

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
        moviesRepository.launch {
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

    fun refresh() {
//        setState {
//            copy(
//                movies = Loading(),
//                favorites = Loading()
//            )
//        }
//        quizRepository.launch {
//            quizRepository.refreshLoadedCounter()
//            quizRepository.dropAllLocal()
//            val list = quizRepository.getQuizzes()
//            setState {
//                copy(
//                    movies = Success(movies),
//                    favorites = Success(favorites)
//                )
//            }
//        }
    }

    fun loadMore() {
//        quizRepository.launch {
//            val list = quizRepository.getQuizzes()
//            setState {
//                val newQuizzes: ArrayList<Quiz> = ArrayList(quizzes()!!)
//                newQuizzes.addAll(list)
//                copy(quizzes = Success(newQuizzes))
//            }
//        }
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