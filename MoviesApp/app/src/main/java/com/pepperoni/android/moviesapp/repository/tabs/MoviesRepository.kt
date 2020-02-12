package com.pepperoni.android.moviesapp.repository.tabs

import com.google.gson.GsonBuilder
import com.pepperoni.android.moviesapp.database.MoviesDatabase
import com.pepperoni.android.moviesapp.model.Movie
import com.pepperoni.android.moviesapp.repository.BaseRepository
import com.pepperoni.android.moviesapp.repository.MoviesResponse
import com.squareup.okhttp.Request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MoviesRepository(val db: MoviesDatabase) : BaseRepository() {

    suspend fun getMoviesNowPlaying(): List<Movie> = withContext(Dispatchers.IO) {

        val favorites = db.moviesDao().getFavorites()

        val request = Request.Builder()
            .url(
                preparedUrlBuilder
                    .addPathSegment("movie")
                    .addPathSegment("now_playing")
                    .build()
            )
            .build()

        val response = httpClient.newCall(request).execute()
        if (response.isSuccessful) {
            val gson = GsonBuilder().create()
            val data = gson.fromJson(response.body().string(), MoviesResponse::class.java)
            favorites.forEach{ fromBd -> data.results.firstOrNull{ fromBd.id == it.id }?.isFavorite = true}
            return@withContext data.results
        }
        return@withContext listOf<Movie>()
    }

}