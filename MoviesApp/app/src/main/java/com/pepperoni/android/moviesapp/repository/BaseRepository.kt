package com.pepperoni.android.moviesapp.repository

import com.pepperoni.android.moviesapp.database.MoviesDatabase
import com.squareup.okhttp.HttpUrl
import com.squareup.okhttp.OkHttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

open class BaseRepository(val db: MoviesDatabase) : CoroutineScope by CoroutineScope(Dispatchers.IO) {

    private val apiKey = "529549b0138460ff02c40a3d40099222"

    protected val httpClient = OkHttpClient()

    protected val preparedUrlBuilder: HttpUrl.Builder
        get() {
            return HttpUrl.Builder()
                .scheme("https")
                .host("api.themoviedb.org")
                .addPathSegment("3")
                .addQueryParameter("api_key", apiKey)
        }
}