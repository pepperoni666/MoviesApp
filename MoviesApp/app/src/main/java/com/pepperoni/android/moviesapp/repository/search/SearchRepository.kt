package com.pepperoni.android.moviesapp.repository.search

import android.util.Log
import com.google.gson.GsonBuilder
import com.pepperoni.android.moviesapp.model.Movie
import com.pepperoni.android.moviesapp.repository.BaseRepository
import com.pepperoni.android.moviesapp.repository.MoviesResponse
import com.squareup.okhttp.Request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchRepository : BaseRepository() {
    suspend fun getSearch(searchQuery: String): List<Movie> = withContext(Dispatchers.IO) {
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
            return@withContext data.results
        }
        return@withContext listOf<Movie>()
    }
}