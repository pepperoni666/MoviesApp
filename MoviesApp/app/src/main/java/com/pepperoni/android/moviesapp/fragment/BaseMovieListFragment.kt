package com.pepperoni.android.moviesapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.withState
import com.pepperoni.android.moviesapp.MainActivity
import com.pepperoni.android.moviesapp.R
import kotlinx.android.synthetic.main.fragment_main.*

abstract class BaseMovieListFragment<S : MvRxState, T : BaseMvRxViewModel<S>> : BaseMvRxFragment() {

    abstract val viewModel: T

    open val allowSwipeRefresh = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    abstract fun makeLoadMoreButtonVisible(
        recyclerView: RecyclerView,
        state: MvRxState
    ): Boolean

    abstract fun swipeRefresh()

    abstract fun loadMore()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipe_refresh.isEnabled = allowSwipeRefresh
        swipe_refresh.setOnRefreshListener {
            swipeRefresh()
        }
        (activity as? MainActivity)?.scrollUpListener = {
            //TODO: scroll to top doesnt work
            //moviesRecyclerView.smoothScrollToPosition(0)
        }
        load_more_btn.setOnClickListener {
            loadMore()
            load_more_btn.visibility = View.GONE
        }
        moviesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                //Show "load more" if view scrolled to its end
                withState(viewModel) { state ->
                    if (makeLoadMoreButtonVisible(recyclerView, state)) {
                        load_more_btn.visibility = View.VISIBLE
                    } else {
                        load_more_btn.visibility = View.GONE
                    }
                }
            }
        })
    }
}