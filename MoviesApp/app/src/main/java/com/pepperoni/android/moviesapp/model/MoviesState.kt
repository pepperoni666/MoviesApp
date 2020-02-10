package com.pepperoni.android.moviesapp.model

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized

data class MoviesState(
    val quizzes: Async<List<Movie>> = Uninitialized
): MvRxState
{}