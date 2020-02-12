package com.pepperoni.android.moviesapp.viewmodel.search

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.ViewModelContext
import com.pepperoni.android.moviesapp.model.search.SearchState
import com.pepperoni.android.moviesapp.repository.search.SearchRepository
import com.pepperoni.android.moviesapp.viewmodel.MvRxViewModel
import kotlinx.coroutines.launch

class SearchViewModel(
    state: SearchState,
    private val searchRepository: SearchRepository
) : MvRxViewModel<SearchState>(state) {

    fun searchQueryUpdated(query: String) {
        setState {
            copy(searchSuggestedMovies = Loading())
        }
        searchRepository.launch {
            val movieList = searchRepository.getSearch(query)
            setState {
                copy(searchSuggestedMovies = Success(movieList))
            }
        }
    }

    companion object : MvRxViewModelFactory<SearchViewModel, SearchState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: SearchState
        ): SearchViewModel? {
            //val moviesRepository = viewModelContext.app<MoviesApplication>().moviesRepository
            return SearchViewModel(
                state,
                SearchRepository()
            )
        }
    }
}