package com.pepperoni.android.moviesapp.fragment.tabs

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.mvrx.*
import com.pepperoni.android.moviesapp.fragment.BaseMovieListFragment
import com.pepperoni.android.moviesapp.model.tabs.MoviesState
import com.pepperoni.android.moviesapp.viewmodel.tabs.MoviesViewModel
import com.pepperoni.android.moviesapp.views.filmRow
import kotlinx.android.synthetic.main.fragment_main.*

open class NowPlayingFragment : BaseMovieListFragment<MoviesState, MoviesViewModel>() {

    override val viewModel: MoviesViewModel by activityViewModel()

    override fun invalidate() = withState(viewModel) { state ->
        swipe_refresh.isRefreshing = state.movies is Loading
        moviesRecyclerView.withModels {
            state.movies()?.forEach { movie ->
                filmRow {
                    id(movie.id)
                    movie(movie)
                    starVisibility(View.VISIBLE)
                    isFavorite(movie.isFavorite)
                    clickListener { _ ->

                    }
                    starClickListener { _ ->
                        viewModel.changeIsFavoriteFlag(movie.id)
                    }
                }
            }
        }
    }

    /**
     * Check if recycleView is scrolled all the way down.
     * @return - true for View.VISIBLE, false for View.GONE
     */
    override fun makeLoadMoreButtonVisible(
        recyclerView: RecyclerView,
        state: MvRxState
    ): Boolean {
        return (recyclerView.layoutManager as LinearLayoutManager)
            .findLastCompletelyVisibleItemPosition() == (state as MoviesState).movies()?.count()?.minus(
            1
        ) && state.movies !is Loading
    }

    override fun swipeRefresh() {
        viewModel.refresh()
    }

    override fun loadMore() {
        viewModel.loadMore()
    }

    companion object {
        @JvmStatic
        fun newInstance(): NowPlayingFragment {
            return NowPlayingFragment()
        }
    }
}