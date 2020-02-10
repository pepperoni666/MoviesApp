package com.pepperoni.android.moviesapp.viewmodel

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.pepperoni.android.moviesapp.model.MoviesState
import com.pepperoni.android.moviesapp.repository.MoviesRepository

class MoviesViewModel(
    state: MoviesState,
    private val quizRepository: MoviesRepository
): MvRxViewModel<MoviesState>(state) {

    companion object : MvRxViewModelFactory<MoviesViewModel, MoviesState> {
        override fun create(viewModelContext: ViewModelContext, state: MoviesState): MoviesViewModel? {
            //val quizRepository = viewModelContext.app<QuizApplication>().quizRepository
            return MoviesViewModel(state, MoviesRepository())
        }
    }

}