package com.pepperoni.android.moviesapp.fragment.tabs

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.withState
import com.pepperoni.android.moviesapp.MainActivity
import com.pepperoni.android.moviesapp.views.filmRow
import kotlinx.android.synthetic.main.fragment_main.*

class MyFavoriteFragment: NowPlayingFragment() {

    override fun invalidate() = withState(viewModel) { state ->
        swipe_refresh.isRefreshing = state.favorites is Loading
        moviesRecyclerView.withModels {
            state.favorites()?.forEach { movie ->
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

    override fun swipeRefresh(){
        viewModel.refreshFavorites()
    }

    override fun makeLoadMoreButtonVisible(
        recyclerView: RecyclerView,
        state: MvRxState
    ): Boolean {
        return false
    }

    companion object {
        @JvmStatic
        fun newInstance(): MyFavoriteFragment {
            return MyFavoriteFragment()
        }
    }
}