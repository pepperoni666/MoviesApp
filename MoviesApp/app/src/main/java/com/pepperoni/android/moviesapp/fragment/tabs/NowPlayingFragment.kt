package com.pepperoni.android.moviesapp.fragment.tabs

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.mvrx.*
import com.pepperoni.android.moviesapp.MainActivity
import com.pepperoni.android.moviesapp.fragment.BaseMovieListFragment
import com.pepperoni.android.moviesapp.model.MoviesState
import com.pepperoni.android.moviesapp.viewmodel.MoviesViewModel
import com.pepperoni.android.moviesapp.views.filmRow
import kotlinx.android.synthetic.main.fragment_main.*

open class NowPlayingFragment : BaseMovieListFragment<MoviesState, MoviesViewModel>() {

    override val viewModel: MoviesViewModel by activityViewModel()

    override fun invalidate() = withState(viewModel) { state ->
        swipe_refresh.isRefreshing = state.nowPlaying is Loading
        moviesRecyclerView.withModels {
            state.nowPlaying()?.forEach { movie ->
                filmRow {
                    id(movie.id)
                    movie(movie)
                    starVisibility(View.VISIBLE)
                    isFavorite(movie.isFavorite)
                    clickListener { _ ->
                        (activity as? MainActivity)?.openMovieDetailsActivity(movie)
                    }
                    starClickListener { _ ->
                        viewModel.changeIsFavoriteFlag(movie.copy(isFavorite = !movie.isFavorite))
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
            .findLastCompletelyVisibleItemPosition() == (state as MoviesState).nowPlaying()?.count()?.minus(
            1
        ) && state.nowPlaying !is Loading
    }

    override fun swipeRefresh() {
        viewModel.refreshNowPlaying()
    }

    override fun loadMore() {
        viewModel.loadMoreOfNowPlaying()
    }

    companion object {
        @JvmStatic
        fun newInstance(): NowPlayingFragment {
            return NowPlayingFragment()
        }
    }
}