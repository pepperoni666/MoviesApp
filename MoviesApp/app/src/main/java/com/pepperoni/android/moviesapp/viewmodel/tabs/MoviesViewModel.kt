package com.pepperoni.android.moviesapp.viewmodel.tabs

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.ViewModelContext
import com.pepperoni.android.moviesapp.model.tabs.MoviesState
import com.pepperoni.android.moviesapp.repository.tabs.MoviesRepository
import com.pepperoni.android.moviesapp.viewmodel.MvRxViewModel
import kotlinx.coroutines.launch

class MoviesViewModel(
    state: MoviesState,
    private val moviesRepository: MoviesRepository
) : MvRxViewModel<MoviesState>(state) {

    init {
        setState {
            copy(
                movies = Loading(),
                favorites = Loading()
            )
        }
        moviesRepository.launch {
            val movieList = moviesRepository.getMoviesNowPlaying()
//            val list = if(moviesRepository.isDBEmpty())
//                quizRepository.getQuizzes()
//            else
//                quizRepository.getSavedQuizzes()
            setState {
                copy(
                    movies = Success(movieList),
                    favorites = Success(listOf())
                )
            }
        }
    }

    fun changeIsFavoriteFlag(movieId: Int) = moviesRepository.launch {
        setState {
            val movies = ArrayList(movies() ?: listOf())
            val selectedMovie = movies.firstOrNull { it.id == movieId }
            val favorites = ArrayList(favorites() ?: listOf())
            selectedMovie?.let {
                it.isFavorite = !it.isFavorite
                if (it.isFavorite)
                    favorites.add(it)
                else
                    favorites.remove(it)
            }
            copy(
                movies = Success(movies),
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
            //val moviesRepository = viewModelContext.app<MoviesApplication>().moviesRepository
            return MoviesViewModel(
                state,
                MoviesRepository()
            )
        }
    }
}