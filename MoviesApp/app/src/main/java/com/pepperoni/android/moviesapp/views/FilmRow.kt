package com.pepperoni.android.moviesapp.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.pepperoni.android.moviesapp.R
import com.pepperoni.android.moviesapp.model.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.film_raw.view.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class FilmRow @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.film_raw, this, true)
    }

    @ModelProp
    fun setMovie(movie: Movie){
        Picasso.with(context)
            .load("https://image.tmdb.org/t/p/w185" + movie.poster_path)
            .into(poster_view)
        movie_title.text = movie.title
        movie_release_date.text = movie.release_date
        movie_vote.text = movie.vote_average.toString()
    }

    @ModelProp
    fun setStarVisibility(viewVisibility: Int){
        star_view.visibility = when(viewVisibility){
            View.VISIBLE -> viewVisibility
            View.INVISIBLE -> viewVisibility
            else -> View.GONE
        }
    }

    @ModelProp
    fun setIsFavorite(isFavorite: Boolean){
        star_view.setImageResource(if(isFavorite) android.R.drawable.star_big_on else android.R.drawable.star_big_off)
    }

    @CallbackProp
    fun setStarClickListener(listener: OnClickListener?){
        star_view.setOnClickListener(listener)
    }

    @CallbackProp
    fun setClickListener(listener: OnClickListener?) {
        setOnClickListener(listener)
    }
}