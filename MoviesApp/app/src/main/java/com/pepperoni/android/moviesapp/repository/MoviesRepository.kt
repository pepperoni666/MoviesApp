package com.pepperoni.android.moviesapp.repository

import com.google.gson.GsonBuilder
import com.pepperoni.android.moviesapp.MoviesApp
import com.pepperoni.android.moviesapp.database.MoviesDatabase
import com.pepperoni.android.moviesapp.model.Movie
import com.squareup.okhttp.HttpUrl
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlinx.coroutines.*
import java.io.IOException

class MoviesRepository(val db: MoviesDatabase) : CoroutineScope by CoroutineScope(Dispatchers.IO) {

    companion object {

        private const val imageBaseUrlWithoutSize = "https://image.tmdb.org/t/p/"
        //        private const val posterSize = "w45"
//        private const val posterSize = "w92"
//        private const val posterSize = "w154"
        private const val posterSize = "w185"
        //        private const val posterSize = "w300"
//        private const val posterSize = "w500"
//        private const val posterSize = "original"
//        private const val backdropSize = "w300"
//        private const val backdropSize = "w780"
        private const val backdropSize = "w1280"
//        private const val backdropSize = "original"

        val posterBaseUrl: String get() = "$imageBaseUrlWithoutSize$posterSize"
        val backdropBaseUrl: String get() = "$imageBaseUrlWithoutSize$backdropSize"
    }

    private val apiKey = "529549b0138460ff02c40a3d40099222"

    private val httpClient = OkHttpClient()

    private val preparedUrlBuilder: HttpUrl.Builder
        get() {
            return HttpUrl.Builder()
                .scheme("https")
                .host("api.themoviedb.org")
                .addPathSegment("3")
                .addQueryParameter("api_key", apiKey)
        }

    private var pagesLoaded = 0
    private var maxPages = 1 //random for start but should be more than 0

    suspend fun getMoviesNowPlaying(): List<Movie> = withContext(Dispatchers.IO) {
        pagesLoaded = 0
        maxPages = 1
        loadMoreForNowPlaying()
    }

    suspend fun loadMoreForNowPlaying(): List<Movie> = withContext(Dispatchers.IO) {

        if (pagesLoaded >= maxPages)
            return@withContext listOf<Movie>()
        else
            MoviesApp.showToast("No more to load", false)

        val favoriteMoviesId = db.moviesDao().getFavorites()

        val request = Request.Builder()
            .url(
                preparedUrlBuilder
                    .addPathSegment("movie")
                    .addPathSegment("now_playing")
                    .addQueryParameter("page", (pagesLoaded + 1).toString())
                    .build()
            )
            .build()

        val response = httpClient.newCall(request).execute()
        if (response.isSuccessful) {
            val gson = GsonBuilder().create()
            val data = gson.fromJson(response.body().string(), MoviesResponse::class.java)
            pagesLoaded = data.page
            maxPages = data.total_pages
            favoriteMoviesId.forEach { fromBd ->
                data.results.firstOrNull { fromBd.id == it.id }?.isFavorite = true
            }
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
            favoriteMoviesId.forEach { fromBd ->
                data.results.firstOrNull { fromBd.id == it.id }?.isFavorite = true
            }
            return@withContext data.results
        }
        return@withContext listOf<Movie>()
    }

}