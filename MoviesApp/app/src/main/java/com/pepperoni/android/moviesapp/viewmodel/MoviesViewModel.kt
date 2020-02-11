package com.pepperoni.android.moviesapp.viewmodel

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.ViewModelContext
import com.pepperoni.android.moviesapp.model.MoviesState
import com.pepperoni.android.moviesapp.repository.MoviesRepository
import kotlinx.coroutines.launch

class MoviesViewModel(
    state: MoviesState,
    private val moviesRepository: MoviesRepository
): MvRxViewModel<MoviesState>(state) {

    init {
        setState {
            copy(movies = Loading())
        }
        moviesRepository.launch {
            val list = moviesRepository.getMoviesNowPlaying()
//            val list = if(moviesRepository.isDBEmpty())
//                quizRepository.getQuizzes()
//            else
//                quizRepository.getSavedQuizzes()
            setState {
                copy(movies = Success(list))
            }
        }
    }

    companion object : MvRxViewModelFactory<MoviesViewModel, MoviesState> {
        override fun create(viewModelContext: ViewModelContext, state: MoviesState): MoviesViewModel? {
            //val moviesRepository = viewModelContext.app<MoviesApplication>().moviesRepository
            return MoviesViewModel(state, MoviesRepository())
        }
    }

}