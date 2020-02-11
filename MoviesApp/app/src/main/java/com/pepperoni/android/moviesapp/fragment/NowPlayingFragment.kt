package com.pepperoni.android.moviesapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.pepperoni.android.moviesapp.R
import com.pepperoni.android.moviesapp.viewmodel.MoviesViewModel
import com.pepperoni.android.moviesapp.views.filmRow
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * A placeholder fragment containing a simple view.
 */
class NowPlayingFragment : BaseMvRxFragment() {

    private val moviesViewModel: MoviesViewModel by activityViewModel()

    override fun invalidate() = withState(moviesViewModel) { state ->
        swipe_refresh.isRefreshing = state.movies is Loading
        moviesRecyclerView.withModels {
            state.movies()?.forEach { movie ->
                filmRow {
                    id(movie.id)
                    movie(movie)
                    clickListener { _ ->
//                        findNavController().navigate(
//                            R.id.action_quizzesFragment_to_solvingFragment,
//                            SolvingFragment.arg(movie.id)
//                        )
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onStart() {
        super.onStart()
        swipe_refresh.setOnRefreshListener {
            //moviesViewModel.refresh()
        }
//        return_top_btn.setOnClickListener {
//            quizzesRecyclerView.smoothScrollToPosition(0)
//            return_top_btn.visibility = View.GONE
//        }
        load_more_btn.setOnClickListener {
            //moviesViewModel.loadMore()
            load_more_btn.visibility = View.GONE
        }
        moviesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //Show "return to top" if view scrolled down
//                if ((recyclerView.layoutManager as LinearLayoutManager)
//                        .findFirstVisibleItemPosition() < 2
//                ) {
//                    return_top_btn.visibility = View.GONE
//                } else {
//                    return_top_btn.visibility = View.VISIBLE
                //Show "load more" if view scrolled to its end
                withState(moviesViewModel) { state ->
                    if ((recyclerView.layoutManager as LinearLayoutManager)
                            .findLastCompletelyVisibleItemPosition() == state.movies()?.count()?.minus(1)
                    ) {
                        load_more_btn.visibility = View.VISIBLE
                    } else {
                        load_more_btn.visibility = View.GONE
                    }
                }
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(): NowPlayingFragment {
            return NowPlayingFragment()
        }
    }
}