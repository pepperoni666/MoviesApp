package com.pepperoni.android.moviesapp.fragment.search

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.pepperoni.android.moviesapp.MainActivity
import com.pepperoni.android.moviesapp.fragment.BaseMovieListFragment
import com.pepperoni.android.moviesapp.model.MoviesState
import com.pepperoni.android.moviesapp.viewmodel.MoviesViewModel
import com.pepperoni.android.moviesapp.views.filmRow
import kotlinx.android.synthetic.main.fragment_main.*

class SearchResultFragment : BaseMovieListFragment<MoviesState, MoviesViewModel>() {

    override val allowSwipeRefresh = false

    override val viewModel: MoviesViewModel by activityViewModel()

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
                        (activity as? MainActivity)?.openMovieDetailsActivity(movie)
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