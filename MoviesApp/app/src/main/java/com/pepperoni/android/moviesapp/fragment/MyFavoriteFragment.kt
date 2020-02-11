package com.pepperoni.android.moviesapp.fragment

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.withState
import com.pepperoni.android.moviesapp.model.MoviesState
import com.pepperoni.android.moviesapp.views.filmRow
import kotlinx.android.synthetic.main.fragment_main.*

class MyFavoriteFragment: NowPlayingFragment() {

    override fun invalidate() = withState(moviesViewModel) { state ->
        swipe_refresh.isRefreshing = state.favorites is Loading
        moviesRecyclerView.withModels {
            state.favorites()?.forEach { movie ->
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

    override fun makeLoadMoreButtonVisible(
        recyclerView: RecyclerView,
        state: MoviesState
    ): Boolean {
        return (recyclerView.layoutManager as LinearLayoutManager)
            .findLastCompletelyVisibleItemPosition() == state.favorites()?.count()?.minus(
            1
        ) && state.favorites !is Loading
    }

    companion object {
        @JvmStatic
        fun newInstance(): MyFavoriteFragment {
            return MyFavoriteFragment()
        }
    }
}