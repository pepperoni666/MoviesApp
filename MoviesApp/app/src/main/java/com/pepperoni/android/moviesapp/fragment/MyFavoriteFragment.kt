package com.pepperoni.android.moviesapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.BaseMvRxFragment
import com.pepperoni.android.moviesapp.R

class MyFavoriteFragment: BaseMvRxFragment() {
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
        fun newInstance(): MyFavoriteFragment {
            return MyFavoriteFragment()
        }
    }
}