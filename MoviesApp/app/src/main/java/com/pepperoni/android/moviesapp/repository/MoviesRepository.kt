package com.pepperoni.android.moviesapp.repository

import com.google.gson.GsonBuilder
import com.pepperoni.android.moviesapp.MoviesApp
import com.pepperoni.android.moviesapp.database.MoviesDatabase
import com.pepperoni.android.moviesapp.model.Movie
import com.squareup.okhttp.Request
import kotlinx.coroutines.*
import java.io.IOException

class MoviesRepository(db: MoviesDatabase) : BaseRepository(db) {

    suspend fun getMoviesNowPlaying(): List<Movie> = withContext(Dispatchers.IO) {
        val favoriteMoviesId = db.moviesDao().getFavorites()

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
            favoriteMoviesId.forEach{ fromBd -> data.results.firstOrNull{ fromBd.id == it.id }?.isFavorite = true}
            return@withContext data.results
        }
        return@withContext listOf<Movie>()
    }

    suspend fun getFavorites(): List<Movie> = withContext(Dispatchers.IO) {
        val favoriteMoviesId = db.moviesDao().getFavorites()

        val favorites = java.util.Collections.synchronizedList(mutableListOf<Movie>())

        try {
            //set timeout for API response to 10s
            withTimeout(10000) {
                //every movie request will run asynchronously
                val awaitItems = ArrayList<Deferred<Unit>>()

                for (i: Movie in favoriteMoviesId) {
                    awaitItems.add(async {
                        val request = Request.Builder()
                            .url(
                                preparedUrlBuilder
                                    .addPathSegment("movie")
                                    .addPathSegment(i.id.toString())
                                    .build()
                            )
                            .build()

                        val response = httpClient.newCall(request).execute()
                        if (response.isSuccessful) {
                            val gson = GsonBuilder().create()
                            val data =
                                gson.fromJson(response.body().string(), Movie::class.java)
                            data.isFavorite = true
                            favorites.add(data)
                        }
                    })
                }

                //waiting for every movie to load
                for (i: Deferred<Unit> in awaitItems) {
                    i.await()
                }
            }

        } catch (e: TimeoutCancellationException) {
            MoviesApp.showToast("Connection timeout", true)
        } catch (e: IOException) {
            e.message?.let {
                MoviesApp.showToast(it, true)
            }
        }
        return@withContext favorites
    }

    suspend fun getSearch(searchQuery: String): List<Movie> = withContext(Dispatchers.IO) {
        val favoriteMoviesId = db.moviesDao().getFavorites()

        val request = Request.Builder()
            .url(
                preparedUrlBuilder
                    .addPathSegment("search")
                    .addPathSegment("movie")
                    .addQueryParameter("query", searchQuery)
                    .build()
            )
            .build()

        val response = httpClient.newCall(request).execute()
        if (response.isSuccessful) {
            val gson = GsonBuilder().create()
            val data = gson.fromJson(response.body().string(), MoviesResponse::class.java)
            favoriteMoviesId.forEach{ fromBd -> data.results.firstOrNull{ fromBd.id == it.id }?.isFavorite = true}
            return@withContext data.results
        }
        return@withContext listOf<Movie>()
    }

}