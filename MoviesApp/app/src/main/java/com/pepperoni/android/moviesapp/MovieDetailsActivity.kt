package com.pepperoni.android.moviesapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pepperoni.android.moviesapp.model.Movie
import kotlinx.android.synthetic.main.activity_details.*

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var movie: Movie
    private var isFavoriteChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        movie = intent.getParcelableExtra(MOVIE_DETAILS_EXTRA)
        star_view.setOnClickListener {
            movie.isFavorite = !movie.isFavorite
            isFavoriteChanged = !isFavoriteChanged
            setIsFavorite(movie.isFavorite)
        }
        setIsFavorite(movie.isFavorite)
    }

    private fun setIsFavorite(isFavorite: Boolean) {
        star_view.setImageResource(if (isFavorite) android.R.drawable.star_big_on else android.R.drawable.star_big_off)
    }

    override fun onBackPressed() {
        val intent = Intent()
        if(isFavoriteChanged){
            intent.putExtra(MOVIE_DETAILS_EXTRA, movie)
            setResult(Activity.RESULT_OK, intent)
        }
        else{
            setResult(Activity.RESULT_CANCELED)
        }
        finish()
    }

    companion object {

        const val MOVIE_DETAILS_EXTRA = "MOVIE_DETAILS_EXTRA"

        @JvmStatic
        fun getIntent(context: Context, movie: Movie): Intent {
            val intent = Intent(context, MovieDetailsActivity::class.java)
            intent.putExtra(MOVIE_DETAILS_EXTRA, movie)
            return intent
        }
    }
}