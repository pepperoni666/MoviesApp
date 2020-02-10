package com.pepperoni.android.moviesapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.activityViewModel
import com.pepperoni.android.moviesapp.R
import com.pepperoni.android.moviesapp.viewmodel.MoviesViewModel

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : BaseMvRxFragment() {

    private val moviesViewModel: MoviesViewModel by activityViewModel()
    override fun invalidate() {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(): PlaceholderFragment {
            return PlaceholderFragment()
        }
    }
}