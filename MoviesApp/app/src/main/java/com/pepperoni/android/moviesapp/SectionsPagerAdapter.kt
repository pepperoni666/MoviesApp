package com.pepperoni.android.moviesapp

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.pepperoni.android.moviesapp.fragment.tabs.MyFavoriteFragment
import com.pepperoni.android.moviesapp.fragment.tabs.NowPlayingFragment

private val TAB_TITLES = arrayOf(
    R.string.now_playing,
    R.string.favorites
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> NowPlayingFragment.newInstance()
            else -> MyFavoriteFragment.newInstance()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }
}