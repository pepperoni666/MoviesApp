package com.pepperoni.android.moviesapp.fragment.search

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.pepperoni.android.moviesapp.fragment.BaseMovieListFragment
import com.pepperoni.android.moviesapp.model.search.SearchState
import com.pepperoni.android.moviesapp.viewmodel.search.SearchViewModel
import com.pepperoni.android.moviesapp.views.filmRow
import kotlinx.android.synthetic.main.fragment_main.*

class SearchResultFragment : BaseMovieListFragment<SearchState, SearchViewModel>() {

    override val allowSwipeRefresh = false

    override val viewModel: SearchViewModel by activityViewModel()

    override fun makeLoadMoreButtonVisible(recyclerView: RecyclerView, state: MvRxState): Boolean {
        return false
    }

    override fun swipeRefresh() {

    }

    override fun loadMore() {

    }

    override fun invalidate() = withState(viewModel) { state ->
        swipe_refresh.isRefreshing = state.searchSuggestedMovies is Loading
        moviesRecyclerView.withModels {
            state.searchSuggestedMovies()?.forEach { movie ->
                filmRow {
                    id(movie.id)
                    movie(movie)
                    starVisibility(View.INVISIBLE)
                    clickListener { _ ->

                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): SearchResultFragment {
            return SearchResultFragment()
        }
    }
}