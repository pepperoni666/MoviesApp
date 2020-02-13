package com.pepperoni.android.moviesapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.airbnb.mvrx.BaseMvRxActivity
import com.airbnb.mvrx.viewModel
import com.airbnb.mvrx.withState
import com.google.android.material.tabs.TabLayout
import com.pepperoni.android.moviesapp.fragment.search.SearchResultFragment
import com.pepperoni.android.moviesapp.model.Movie
import com.pepperoni.android.moviesapp.model.MoviesState
import com.pepperoni.android.moviesapp.viewmodel.MoviesViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseMvRxActivity() {

    private val LAUNCH_DETAILS_ACTIVITY = 2233

    private val moviesViewModel: MoviesViewModel by viewModel()

    var scrollUpListener: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter =
            SectionsPagerAdapter(
                this,
                supportFragmentManager
            )
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                scrollUpListener?.let { it() }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {}

        })
        search_text_box.doOnTextChanged { text, start, count, after ->
            moviesViewModel.searchQueryUpdated(text.toString())
        }
        search_icon.setOnClickListener {
            withState(moviesViewModel) { state ->
                if (state.isSearching) {
                    closeSearching(state)
                } else {
                    openSearching(state)
                }
            }
        }
        withState(moviesViewModel) { state ->
            if (state.isSearching) {
                openSearching(state)
            }
        }
    }

    fun openMovieDetailsActivity(movie: Movie) {
        startActivityForResult(
            MovieDetailsActivity.getIntent(applicationContext, movie),
            LAUNCH_DETAILS_ACTIVITY
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == LAUNCH_DETAILS_ACTIVITY) {
            //RESULT_OK returned when isFavorite field changed
            //and RESULT_CANCELED otherwise
            if (resultCode == Activity.RESULT_OK) {
                val movie =
                    data?.getParcelableExtra<Movie>(MovieDetailsActivity.MOVIE_DETAILS_EXTRA)
                movie?.let {
                    moviesViewModel.changeIsFavoriteFlag(it)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        withState(moviesViewModel) { state ->
            if (state.isSearching)
                closeSearching(state)
            else
                super.onBackPressed()
        }
    }

    private fun openSearching(state: MoviesState) {
        search_text_box.visibility = View.VISIBLE
        app_bar_title.visibility = View.GONE
        tabs.visibility = View.GONE
        view_pager.visibility = View.GONE
        search_results_placeholder.visibility = View.VISIBLE
        val searchFragment = SearchResultFragment.newInstance()
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(search_results_placeholder.id, searchFragment).commit()
        app_bar_layout.setBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.design_default_color_background
            )
        )
        search_icon.setImageResource(R.drawable.ic_search_black_24dp)
        search_text_box.requestFocus()
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(search_text_box, InputMethodManager.SHOW_IMPLICIT)
        state.isSearching = true
    }

    private fun closeSearching(state: MoviesState) {
        search_text_box.visibility = View.GONE
        app_bar_title.visibility = View.VISIBLE
        tabs.visibility = View.VISIBLE
        view_pager.visibility = View.VISIBLE
        search_results_placeholder.visibility = View.GONE
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.remove(
            supportFragmentManager.findFragmentById(search_results_placeholder.id) ?: Fragment()
        ).commit()
        app_bar_layout?.setBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.colorPrimary
            )
        )
        search_icon.setImageResource(R.drawable.ic_search_white_24dp)
        search_text_box.clearFocus()
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(search_text_box.windowToken, 0)
        state.isSearching = false
    }
}