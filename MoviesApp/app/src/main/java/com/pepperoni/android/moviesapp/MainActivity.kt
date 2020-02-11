package com.pepperoni.android.moviesapp

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.airbnb.mvrx.BaseMvRxActivity
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseMvRxActivity() {

    private var isSearching = false

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
        tabs.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                scrollUpListener?.let { it() }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {}

        })
        search_icon.setOnClickListener {
            if(isSearching){
                closeSearching()
            }
            else{
                openSearching()
            }
        }
    }

    private fun openSearching(){
        if(!isSearching) {
            search_text_box.visibility = View.VISIBLE
            app_bar_title.visibility = View.GONE
            tabs.visibility = View.GONE
            view_pager.visibility = View.INVISIBLE
            app_bar_layout.setBackgroundColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.design_default_color_background
                )
            )
            search_icon.setColorFilter(R.color.colorPrimary)
            search_text_box.requestFocus()
            isSearching = true
        }
    }

    private fun closeSearching(){
        if(isSearching) {
            search_text_box.visibility = View.GONE
            app_bar_title.visibility = View.VISIBLE
            tabs.visibility = View.VISIBLE
            view_pager.visibility = View.VISIBLE
            app_bar_layout.setBackgroundColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorPrimary
                )
            )
            search_icon.setColorFilter(R.color.design_default_color_background)
            search_text_box.clearFocus()
            isSearching = false
        }
    }
}